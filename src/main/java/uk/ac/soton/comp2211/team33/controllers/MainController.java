package uk.ac.soton.comp2211.team33.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.*;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.components.RunwayTab;
import uk.ac.soton.comp2211.team33.models.Aircraft;
import uk.ac.soton.comp2211.team33.models.Airport;
import uk.ac.soton.comp2211.team33.models.Obstacle;
import uk.ac.soton.comp2211.team33.models.Runway;
import uk.ac.soton.comp2211.team33.utilities.Calculator;
import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;
import uk.ac.soton.comp2211.team33.utilities.XMLHelpers;

/**
 * The MainController controller class that serves as the main workspace for the runway re-declaration.
 * Corresponds to user story #7, #13.
 *
 * @author Geeth (gv2g21@soton.ac.uk), Abeed (mabs1u21@soton.ac.uk)
 */
public class MainController extends BaseController {

  private static final Logger logger = LogManager.getLogger(MainController.class);

  /**
   * A JavaFX UI element to store different tabs of runways.
   */
  @FXML
  private TabPane runwayTabs;

  MainController(Stage stage, Airport state) {
    super(stage, state);
  }

  @Override
  protected void initialise() {
    logger.info("Building MainController...");

    // Set stage properties
    Screen screen = Screen.getPrimary();
    Rectangle2D bounds = screen.getVisualBounds();

    stage.setX(bounds.getMinX());
    stage.setY(bounds.getMinY());
    stage.setMinWidth(800);
    stage.setWidth(bounds.getWidth());
    stage.setMinHeight(600);
    stage.setHeight(bounds.getHeight());
    stage.setResizable(true);

    stage.setTitle("Runway Re-decleration Tool - " + state.getName() + ", " + state.getCity());

    buildScene("/views/MainView.fxml");

    renderTabs();
  }

  @FXML
  private void onChangeAirport() {
    new SplashController(new Stage(), null);
  }

  @FXML
  private void onNewRunway() {
    new NewRunwayController(ProjectHelpers.createModalStage(stage), state);
  }

  @FXML
  private void onImport() {

    String path = ProjectHelpers.getPathWithDialog(stage);
    Airport newAirport = XMLHelpers.importAirport(path);
    new MainController(stage, newAirport);

  }

  @FXML
  private void onExport() {

    String path = ProjectHelpers.getSavePathWithDialog(stage);
    XMLHelpers.exportAirport(state, path);

  }

  @FXML
  private void onPrint() {
    var fileChooser = new FileChooser();
    fileChooser.setTitle("Print Airport State");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
    File file = fileChooser.showSaveDialog(stage);

    try {
      var fileWriter = new FileWriter(file);

      fileWriter.write("***AIRPORT***: " + state.getName() + ", " + state.getCity() + "\n");

      fileWriter.write("\n");

      fileWriter.write("***RUNWAYS***: \n");

      fileWriter.write("\n");

      for (Runway runway : state.runwayListProperty()) {
        fileWriter.write("**RUNWAY**: " + runway.getDesignator() + "\n");
        fileWriter.write("TORA: " + runway.getTora() + " m, ");
        fileWriter.write("Calculated TORA: " + runway.getCtora() + " m\n");
        fileWriter.write("TODA: " + runway.getToda() + " m, ");
        fileWriter.write("Calculated TODA: " + runway.getCtoda() + " m\n");
        fileWriter.write("ASDA: " + runway.getAsda() + " m, ");
        fileWriter.write("Calculated ASDA: " + runway.getCasda() + " m\n");
        fileWriter.write("LDA: " + runway.getLda() + " m, ");
        fileWriter.write("Calculated LDA: " + runway.getClda() + " m\n");

        fileWriter.write("\n");

        Obstacle currentObstacle = runway.getCurrentObstacle();
        fileWriter.write("CURRENTLY SELECTED OBSTACLE: " + currentObstacle.getName() + " m\n");
        fileWriter.write("Height: " + currentObstacle.getHeight() + " m\n");
        fileWriter.write("Length: " + currentObstacle.getLength() + " m\n");
        fileWriter.write("Distance from center-line: " + currentObstacle.getCenterline() + " m\n");

        fileWriter.write("\n");

        Aircraft currentAircraft = runway.getCurrentAircraft();
        fileWriter.write("CURRENTLY SELECTED OBSTACLE: " + currentAircraft.getId() + " m\n");
        fileWriter.write("Blast protection: " + currentAircraft.getBlastProtection() + " m\n");

        fileWriter.write("\n");

        fileWriter.write("CALCULATION FOR TAKE-OFF/LANDING TOWARDS OBSTACLE:\n");
        fileWriter.write(Calculator.takeOffTowardsObsPP(runway, runway.getCurrentObstacle()) + "\n" +
            Calculator.landingTowardsObsPP(runway, runway.getCurrentObstacle()) + "\n");

        fileWriter.write("\n");

        fileWriter.write("CALCULATION FOR TAKE-OFF AWAY/LANDING OVER OBSTACLE:\n");
        fileWriter.write(Calculator.takeOffAwayObsPP(runway, runway.getCurrentObstacle(), runway.getCurrentAircraft()) + "\n" +
            Calculator.landingOverObsPP(runway, runway.getCurrentObstacle(), runway.getCurrentAircraft()) + "\n");

        fileWriter.write("\n");
      }

      for (Obstacle obstacle : state.obstacleListProperty()) {

      }

      for (Aircraft aircraft : state.aircraftListProperty()) {

      }

      fileWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to get the file extension of a file.
   *
   * @param fileName name of file
   * @return file extension
   */
  public static String getFileExtension(String fileName) {
    int dotIndex = fileName.lastIndexOf('.');
    return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
  }

  /**
   * Method to update runway tabs when runway list has changed.
   */
  private void renderTabs() {
    for (Runway runway : state.runwayListProperty()) {
      runwayTabs.getTabs().add(new RunwayTab(stage, state, runway));
    }

    state.runwayListProperty().addListener((ListChangeListener<? super Runway>) list -> {
      list.next();

      if (list.wasAdded()) {
        Runway runway = list.getAddedSubList().get(0);
        runwayTabs.getTabs().add(new RunwayTab(stage, state, runway));
      } else if (list.wasRemoved()) {
        //TODO: remove runways when tab is closed
      }
    });
  }

  @FXML
  private void onPreferences() {
    new OptionsController(ProjectHelpers.createModalStage(stage), state);
  }


}
