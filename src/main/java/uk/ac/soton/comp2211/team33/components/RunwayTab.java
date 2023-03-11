package uk.ac.soton.comp2211.team33.components;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.models.Aircraft;
import uk.ac.soton.comp2211.team33.models.Airport;
import uk.ac.soton.comp2211.team33.models.Obstacle;
import uk.ac.soton.comp2211.team33.models.Runway;
import uk.ac.soton.comp2211.team33.scenes.AircraftScene;
import uk.ac.soton.comp2211.team33.scenes.ObstacleScene;

import java.io.IOException;
import java.util.*;

public class RunwayTab extends Tab {

  private static final Logger logger = LogManager.getLogger(RunwayTab.class);

  private Stage stage;

  private Airport state;

  private Runway runway;

  @FXML
  private Label tora, toda, asda, lda, resa;

  @FXML
  private Label ctora, ctoda, casda, clda, cresa;

  @FXML
  private Label thresholdOriginal, thresholdCalculated;

  @FXML
  private ChoiceBox<String> aircraftList, obstacleList;

  @FXML
  private Label blastProtection, height, length, centerline;

  public RunwayTab(Runway runway, Stage stage, Airport state) {
    logger.info("Creating new runway tab named " + runway.getDesignator() + "...");

    FXMLLoader loader = new FXMLLoader(getClass().getResource("RunwayTab.fxml"));
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Set the stage this tab belongs to, the state of the airport and the runway this tab possesses
    this.stage = stage;
    this.state = state;
    this.runway = runway;

    // Set tab name
    setText(runway.getDesignator());

    // Set initial values of runway
    tora.setText(String.valueOf(runway.getTora()));
    toda.setText(String.valueOf(runway.getToda()));
    asda.setText(String.valueOf(runway.getAsda()));
    lda.setText(String.valueOf(runway.getLda()));
    resa.setText(String.valueOf(runway.getResa()));

    thresholdOriginal.setText(String.valueOf(runway.getThreshold()));
    thresholdCalculated.setText(String.valueOf(runway.getThreshold()));

    // Set calculated values
    ctora.setText(String.valueOf(runway.getCtora()));
    ctoda.setText(String.valueOf(runway.getCtoda()));
    casda.setText(String.valueOf(runway.getCasda()));
    clda.setText(String.valueOf(runway.getClda()));
    cresa.setText(String.valueOf(runway.getCresa()));

    // Add listeners from calculated values to listeners
    runway.ctoraProperty().addListener(((obVal, oldVal, newVal) -> ctora.setText(String.valueOf(newVal))));
    runway.ctodaProperty().addListener(((obVal, oldVal, newVal) -> ctoda.setText(String.valueOf(newVal))));
    runway.casdaProperty().addListener(((obVal, oldVal, newVal) -> casda.setText(String.valueOf(newVal))));
    runway.cldaProperty().addListener(((obVal, oldVal, newVal) -> clda.setText(String.valueOf(newVal))));
    runway.cresaProperty().addListener(((obVal, oldVal, newVal) -> cresa.setText(String.valueOf(newVal))));

    // Add listeners from aircraftList model to aircraftList UI
    state.getAircraftListProperty().addListener((obList, oldList, newList) -> {
      ArrayList<String> aircraftIDs = new ArrayList<>();
      aircraftIDs.add("None");

      for (Aircraft aircraft : newList) {
        aircraftIDs.add(aircraft.getId());
      }

      aircraftList.setItems(FXCollections.observableArrayList(aircraftIDs));
    });

    // Add listeners from obstacleList model to obstacleList UI
    state.getObstacleListProperty().addListener((obList, oldList, newList) -> {
      ArrayList<String> names = new ArrayList<>();
      names.add("None");

      for (Obstacle obstacle : newList) {
        names.add(obstacle.getName());
      }

      obstacleList.setItems(FXCollections.observableArrayList(names));
    });

    // Add default items in ChoiceBox
    aircraftList.getItems().add("None");
    obstacleList.getItems().add("None");
  }

  private Stage createModalStage() {
    Stage modal = new Stage();
    modal.initOwner(stage);
    modal.initModality(Modality.WINDOW_MODAL);

    return modal;
  }

  @FXML
  private void onEditAircraft() {

  }

  @FXML
  private void onAddAircraft() {
    new AircraftScene(createModalStage(), state, false);
  }

  @FXML
  private void onDeleteAircraft() {

  }

  @FXML
  private void onEditObstacle() {

  }

  @FXML
  private void onAddObstacle() {
    new ObstacleScene(createModalStage(), state, false);
  }

  @FXML
  private void onDeleteObstacle() {

  }
}
