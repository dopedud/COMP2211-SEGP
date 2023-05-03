package uk.ac.soton.comp2211.team33.controllers;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.*;
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

    stage.setTitle("AVIA - " + state.getName() + ", " + state.getCity());

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

      fileWriter.write("**RUNWAYS**: \n");

      fileWriter.write("\n");

      for (Runway runway : state.runwayListProperty()) {
        fileWriter.write("*RUNWAY*: " + runway.getDesignator() + "\n");
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
        if (currentObstacle != null) {
          fileWriter.write("CURRENTLY SELECTED OBSTACLE: " + currentObstacle.getName());
          fileWriter.write("Height: " + currentObstacle.getHeight() + " m\n");
          fileWriter.write("Length: " + currentObstacle.getLength() + " m\n");
          fileWriter.write("Distance from center-line: " + currentObstacle.getCenterline() + " m\n");

          fileWriter.write("\n");
        } else fileWriter.write("CURRENTLY SELECTED OBSTACLE: none\n\n");

        Aircraft currentAircraft = runway.getCurrentAircraft();
        if (currentAircraft != null) {
          fileWriter.write("CURRENTLY SELECTED AIRCRAFT: " + currentAircraft.getId());
          fileWriter.write("Blast protection: " + currentAircraft.getBlastProtection() + " m\n");

          fileWriter.write("\n");
        } else fileWriter.write("CURRENTLY SELECTED AIRCRAFT: none\n\n");

        fileWriter.write("CALCULATION FOR TAKE-OFF/LANDING TOWARDS OBSTACLE:\n");
        if (currentObstacle != null) {
          fileWriter.write(Calculator.takeOffTowardsObsPP(runway, currentObstacle) + "\n" +
              Calculator.landingTowardsObsPP(runway, currentObstacle) + "\n");
        } else fileWriter.write("""
            Runway values do not need to be re-declared.

            Select an obstacle and an aircraft to re-declare values.
            """);

        fileWriter.write("\n");

        fileWriter.write("CALCULATION FOR TAKE-OFF AWAY/LANDING OVER OBSTACLE:\n");
        if (currentAircraft != null && currentObstacle != null) {
          fileWriter.write(Calculator.takeOffAwayObsPP(runway, currentObstacle, currentAircraft) + "\n" +
              Calculator.landingOverObsPP(runway, currentObstacle, currentAircraft) + "\n");
        }
        else fileWriter.write("""
            Runway values do not need to be re-declared.

            Select an obstacle and an aircraft to re-declare values.
            """);

        fileWriter.write("\n");
      }

      fileWriter.write("**OBSTACLES**: \n");

      fileWriter.write("\n");


      for (Obstacle obstacle : state.obstacleListProperty()) {
        fileWriter.write("*OBSTACLE*: " + obstacle.getName() + "\n");
        fileWriter.write("Height: " + obstacle.getHeight() + "m\n");
        fileWriter.write("Length: " + obstacle.getLength() + "m\n");
        fileWriter.write("Distance from center-line: " + obstacle.getCenterline() + "m\n");

        fileWriter.write("\n");
      }

      fileWriter.write("**AIRCRAFT**: \n");

      fileWriter.write("\n");


      for (Aircraft aircraft : state.aircraftListProperty()) {
        fileWriter.write("*AIRCRAFT*: " + aircraft.getId() + "\n");
        fileWriter.write("Blast Protection: " + aircraft.getBlastProtection() + "m\n");

        fileWriter.write("\n");
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
        Runway runway = list.getRemoved().get(0);
        runwayTabs.getTabs().remove(new RunwayTab(stage, state, runway));
      }
    });
  }

  @FXML
  private void onPreferences() {
    new OptionsController(ProjectHelpers.createModalStage(stage), state);
  }


}
