package uk.ac.soton.comp2211.team33.components;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.ac.soton.comp2211.team33.controllers.AircraftController;
import uk.ac.soton.comp2211.team33.controllers.ObstacleController;
import uk.ac.soton.comp2211.team33.models.Aircraft;
import uk.ac.soton.comp2211.team33.models.Airport;
import uk.ac.soton.comp2211.team33.models.Obstacle;
import uk.ac.soton.comp2211.team33.models.Runway;
import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;

import java.util.ArrayList;

public class ConfigPanel extends AnchorPane {
  private static final Logger logger = LogManager.getLogger(ConfigPanel.class);

  private final RunwayTab runwayTab;

  private final Stage stage;

  private final Airport state;

  private final Runway runway;

  @FXML
  private ChoiceBox<String> aircraftList, obstacleList;

  @FXML
  private Label blastProtection, height, length, centerLine;

  @FXML
  private Button loadPredefinedObs;

  @FXML
  private InputField obsDistFromThresh;

  public ConfigPanel(RunwayTab runwayTab, Stage stage, Airport state, Runway runway) {
    this.runwayTab = runwayTab;
    this.state = state;
    this.stage = stage;
    this.runway = runway;

    ProjectHelpers.renderRoot("/components/ConfigPanel.fxml", this, this);

    // Enable or disable load pre-defined obstacles button, and add listener from state
    loadPredefinedObs.setDisable(state.getObstaclesLoaded());
    state.obstaclesLoadedProperty().addListener((obVal, oldVal, newVal) -> loadPredefinedObs.setDisable(newVal));

    initialiseObsDistFromThresh();
    initialiseAircraftList();
    initialiseObstacleList();
  }

  private void initialiseObsDistFromThresh() {
    // Set wrap text for obsDistFromThreshold label
    obsDistFromThresh.getLabel().setPrefHeight(40);
    obsDistFromThresh.getLabel().setPrefWidth(100);

    // Set alignment for obsDistFromThreshold text field
    obsDistFromThresh.getTextField().setLayoutY(8);

    // Live update when obstacle distance from threshold value is changed
    obsDistFromThresh.getTextField().textProperty().
        bindBidirectional(runway.obstacleDistanceProperty(), new NumberStringConverter());
  }

  private void initialiseAircraftList() {
    // Initialise list by setting items of aircraftList UI to aircraftList model
    ArrayList<String> ids = new ArrayList<>();
    ids.add("None");

    for (Aircraft aircraft : state.aircraftListProperty()) {
      ids.add(aircraft.getId());
    }

    aircraftList.setItems(FXCollections.observableArrayList(ids));
    aircraftList.setValue("None");

    // Add listeners from aircraftList UI to aircraftList model
    state.aircraftListProperty().addListener((ListChangeListener<? super Aircraft>) list -> {
      list.next();

      if (list.wasAdded()) {
        String id = list.getAddedSubList().get(0).getId();

        aircraftList.getItems().add(id);
        if (runwayTab.isSelected()) aircraftList.setValue(id);
      } else if (list.wasRemoved()) {
        String id = list.getRemoved().get(0).getId();

        aircraftList.getItems().remove(id);
        if (aircraftList.getValue().equals(id)) aircraftList.setValue("None");
      }
    });

    // Add listeners when current value from ChoiceBox change in aircraftList
    aircraftList.valueProperty().addListener((obVal, oldVal, newVal) -> {
      if (newVal == null) return;

      logger.info("Aircraft selected named " + newVal);

      if (newVal.equals("None")) {
        runway.setCurrentAircraft(null);

        blastProtection.setText("");
      } else {
        runway.setCurrentAircraft(state.aircraftListProperty().stream().
                filter(aircraft -> aircraft.getId().equals(aircraftList.getValue())).
                findAny().get());

        blastProtection.setText(String.valueOf(runway.getCurrentAircraft().getBlastProtection()));
      }
    });
  }

  private void initialiseObstacleList() {
    // Initialise list by setting aircraftList UI to aircraftList model
    ArrayList<String> names = new ArrayList<>();
    names.add("None");

    for (Obstacle obstacle : state.obstacleListProperty()) {
      names.add(obstacle.getName());
    }

    obstacleList.setItems(FXCollections.observableArrayList(names));
    obstacleList.setValue("None");

    // Add listeners from obstacleList UI to obstacleList model
    state.obstacleListProperty().addListener((ListChangeListener<? super Obstacle>) list -> {
      list.next();

      if (list.wasAdded()) {
        String name = list.getAddedSubList().get(0).getName();

        obstacleList.getItems().add(name);
        if (runwayTab.isSelected()) obstacleList.setValue(name);
      } else if (list.wasRemoved()) {
        String name = list.getRemoved().get(0).getName();

        obstacleList.getItems().remove(name);
        if (obstacleList.getValue().equals(name)) obstacleList.setValue("None");
      }
    });

    // Add listeners when current value from ChoiceBox change in obstacleList
    obstacleList.valueProperty().addListener((obVal, oldVal, newVal) -> {
      if (newVal == null) return;

      logger.info("Obstacle selected named " + newVal);
      if (newVal.equals("None")) {
        runway.setCurrentObstacle(null);

        height.setText("");
        length.setText("");
        centerLine.setText("");
      } else {
        runway.setCurrentObstacle(state.obstacleListProperty().stream().
                filter(obstacle -> obstacle.getName().equals(obstacleList.getValue())).
                findAny().get());

        height.setText(String.valueOf(runway.getCurrentObstacle().getHeight()));
        length.setText(String.valueOf(runway.getCurrentObstacle().getLength()));
        centerLine.setText(String.valueOf(runway.getCurrentObstacle().getCenterLine()));
      }
    });
  }

  @FXML
  private void onLoadPredefinedObstacles() {
    String oldValue = obstacleList.getValue();

    state.loadPredefinedObstacles();

    obstacleList.setValue(oldValue);
  }

  @FXML
  private void onEditAircraft() {

  }

  @FXML
  private void onAddAircraft() {
    new AircraftController(ProjectHelpers.createModalStage(stage), state, false);
  }

  @FXML
  private void onDeleteAircraft() {

  }

  @FXML
  private void onEditObstacle() {

  }

  @FXML
  private void onAddObstacle() {
    new ObstacleController(ProjectHelpers.createModalStage(stage), state, false);
  }

  @FXML
  private void onDeleteObstacle() {

  }
}
