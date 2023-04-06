package uk.ac.soton.comp2211.team33.controllers;

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
import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;

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
    Document document;

    var fileChooser = new FileChooser();
    fileChooser.setTitle("Import Airport State");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
    File file = fileChooser.showOpenDialog(stage);

    if (!getFileExtension(file.getName()).equals("xml")) {
      logger.error("wrong file type"); //TODO: inform the user that the chosen file has incorrect file extension
      return;
    }

    try {
      document = new SAXReader().read(file);

      var airportElement = document.getRootElement();

      var newState = new Airport(airportElement.attributeValue("city"), airportElement.attributeValue("name"));
      newState.setObstaclesLoaded(Boolean.parseBoolean(airportElement.attributeValue("obstaclesLoaded")));

      var runwaysElement = airportElement.elements().get(0);
      var aircraftsElement = airportElement.elements().get(1);
      var obstaclesElement = airportElement.elements().get(2);

      for (Element runwayElement : runwaysElement.elements()) {
        String designator = runwayElement.attributeValue("designator");
        double tora = Double.parseDouble(runwayElement.elements().get(0).getText());
        double toda = Double.parseDouble(runwayElement.elements().get(1).getText());
        double asda = Double.parseDouble(runwayElement.elements().get(2).getText());
        double lda = Double.parseDouble(runwayElement.elements().get(3).getText());
        double resa = Double.parseDouble(runwayElement.elements().get(4).getText());
        double threshold = Double.parseDouble(runwayElement.elements().get(5).getText());

        newState.addRunway(designator, tora, toda, asda, lda, resa, threshold);
      }

      for (Element aircraftElement : aircraftsElement.elements()) {
        String id = aircraftElement.attributeValue("id");
        double blastProtection = Double.parseDouble(aircraftElement.elements().get(0).getText());

        newState.addAircraft(id, blastProtection);
      }

      for (Element obstacleElement : obstaclesElement.elements()) {
        String name = obstacleElement.attributeValue("name");
        double height = Double.parseDouble(obstacleElement.elements().get(0).getText());
        double length = Double.parseDouble(obstacleElement.elements().get(1).getText());
        double centerline = Double.parseDouble(obstacleElement.elements().get(2).getText());

        newState.addObstacle(name, height, length, centerline);
      }

      new MainController(stage, newState);
    } catch (DocumentException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onExport() {
    Document document = DocumentHelper.createDocument();

    var fileChooser = new FileChooser();
    fileChooser.setTitle("Export Airport State");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
    File file = fileChooser.showSaveDialog(stage);

    if (!getFileExtension(file.getName()).equals("xml")) {
      logger.error("wrong file type"); //TODO: inform the user that the chosen file has incorrect file extension
      return;
    }

    try {
      Element airportElement = document.addElement("airport").
          addAttribute("city", state.getCity()).
          addAttribute("name", state.getName()).
          addAttribute("obstaclesLoaded", String.valueOf(state.getObstaclesLoaded()));

      Element runwaysElement = airportElement.addElement("runways");
      Element aircraftsElement = airportElement.addElement("aircrafts");
      Element obstaclesElement = airportElement.addElement("obstacles");

      for (Runway runway : state.runwayListProperty()) {
        Element runwayElement = runwaysElement.addElement("runway").addAttribute("designator", runway.getDesignator());

        runwayElement.addElement("tora").addText(String.valueOf(runway.getTora()));
        runwayElement.addElement("toda").addText(String.valueOf(runway.getToda()));
        runwayElement.addElement("asda").addText(String.valueOf(runway.getAsda()));
        runwayElement.addElement("lda").addText(String.valueOf(runway.getLda()));
        runwayElement.addElement("resa").addText(String.valueOf(runway.getResa()));
        runwayElement.addElement("threshold").addText(String.valueOf(runway.getThreshold()));
      }

      for (Aircraft aircraft : state.aircraftListProperty()) {
        Element aircraftElement = aircraftsElement.addElement("aircraft").addAttribute("id", aircraft.getId());

        aircraftElement.addElement("blastProtection").addText(String.valueOf(aircraft.getBlastProtection()));
      }

      for (Obstacle obstacle : state.obstacleListProperty()) {
        Element obstacleElement = obstaclesElement.addElement("obstacle").addAttribute("name", obstacle.getName());

        obstacleElement.addElement("height").addText(String.valueOf(obstacle.getHeight()));
        obstacleElement.addElement("length").addText(String.valueOf(obstacle.getLength()));
        obstacleElement.addElement("centerline").addText(String.valueOf(obstacle.getCenterline()));
      }

      var xmlWriter = new XMLWriter(new FileWriter(file), OutputFormat.createPrettyPrint());
      xmlWriter.write(document);
      xmlWriter.close();
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
}
