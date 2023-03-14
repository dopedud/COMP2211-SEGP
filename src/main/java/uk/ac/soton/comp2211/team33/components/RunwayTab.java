package uk.ac.soton.comp2211.team33.components;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.models.Aircraft;
import uk.ac.soton.comp2211.team33.models.Airport;
import uk.ac.soton.comp2211.team33.models.Obstacle;
import uk.ac.soton.comp2211.team33.models.Runway;
import uk.ac.soton.comp2211.team33.controllers.AircraftScene;
import uk.ac.soton.comp2211.team33.controllers.ObstacleScene;
import uk.ac.soton.comp2211.team33.utilities.Calculator;

import java.util.ArrayList;

/**
 * The RunwayTab class is a custom component that holds a runway in a tab in an airport. An airport can contain many
 * runways.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
public class RunwayTab extends Tab implements BaseComponent {

  private static final Logger logger = LogManager.getLogger(RunwayTab.class);

  private Stage stage;

  private Airport state;

  private Runway runway;

  @FXML
  private AnchorPane listPanel, viewPanel, calcPanel;

  @FXML
  private Label tora, toda, asda, lda, resa;

  @FXML
  private Label ctora, ctoda, casda, clda, cresa;

  @FXML
  private Label thresholdOriginal, thresholdCalculated;

  @FXML
  private TextArea calcBreakdown;

  @FXML
  private ChoiceBox<String> aircraftList, obstacleList;

  @FXML
  private Label blastProtection, height, length, centerline;

  @FXML
  private DropdownField calcMode;
  private boolean calcTowards;

  @FXML
  private InputField obsDistFromThresh;

  @FXML
  private Button loadPredefinedObs;

  public RunwayTab(Stage stage, Airport state, Runway runway) {
    logger.info("Creating new runway tab named " + runway.getDesignator());

    renderFXML("RunwayTab.fxml");

    // Set the stage this tab belongs to, the state of the airport and the runway this tab possesses
    this.stage = stage;
    this.state = state;
    this.runway = runway;

    // Set tab name
    setText(runway.getDesignator());

    // Resize input field UI
    resizeUI();

    // Enable or disable load pre-defined obstacles button, and add listener from state
    state.obstaclesLoadedProperty().addListener((obVal, oldVal, newVal) -> loadPredefinedObs.setDisable(newVal));

    // Set initial values of runway
    tora.setText(String.valueOf(runway.getTora()));
    toda.setText(String.valueOf(runway.getToda()));
    asda.setText(String.valueOf(runway.getAsda()));
    lda.setText(String.valueOf(runway.getLda()));
    resa.setText(String.valueOf(runway.getResa()));

    thresholdOriginal.setText(String.valueOf(runway.getThreshold()));
    thresholdCalculated.setText(String.valueOf(runway.getThreshold()));

    // Set calculated values
    ctora.setText(String.valueOf(runway.getTora()));
    ctoda.setText(String.valueOf(runway.getToda()));
    casda.setText(String.valueOf(runway.getAsda()));
    clda.setText(String.valueOf(runway.getLda()));
    cresa.setText(String.valueOf(runway.getResa()));

    // Add listeners from calculated values UI to calculated values model
    runway.ctoraProperty().addListener(((obVal, oldVal, newVal) -> ctora.setText(String.valueOf(newVal))));
    runway.ctodaProperty().addListener(((obVal, oldVal, newVal) -> ctoda.setText(String.valueOf(newVal))));
    runway.casdaProperty().addListener(((obVal, oldVal, newVal) -> casda.setText(String.valueOf(newVal))));
    runway.cldaProperty().addListener(((obVal, oldVal, newVal) -> clda.setText(String.valueOf(newVal))));
    runway.cresaProperty().addListener(((obVal, oldVal, newVal) -> cresa.setText(String.valueOf(newVal))));

    // Initialise list UI to list models
    initialiseAircraftList();
    initialiseObstacleList();

    // Live update when obstacle distance from threshold value is changed
    obsDistFromThresh.getTextField().textProperty().
        bindBidirectional(runway.obstacleDistanceProperty(), new NumberStringConverter());
    runway.obstacleDistanceProperty().addListener((obVal, oldVal, newVal) -> recalculateRunwayValues());

    // Set calculation mode to 2 modes, calculation towards obstacle and away from/over obstacle
    // Also adds a listener to re-calculate values based on which modes selected
    calcMode.getDropdown().valueProperty().addListener((obVal, oldVal, newVal) -> {
      calcTowards = newVal.equals("Calculations Towards Obstacle");

      recalculateRunwayValues();
    });
    calcMode.getDropdownList().add("Calculations Towards Obstacle");
    calcMode.getDropdownList().add("Calculations Away From/Over Obstacle");
    calcMode.setDropdownValue("Calculations Towards Obstacle");

    Visualisation visualisation = new Visualisation(runway);
    // visualisation.heightProperty().bind(viewPanel.heightProperty());
    // visualisation.widthProperty().bind(viewPanel.widthProperty());
    viewPanel.getChildren().add(visualisation);
    visualisation.renderTopDown();
  }

  private void resizeUI() {
    // Set minimum size for panels
    listPanel.setMinWidth(300);
    calcPanel.setMinWidth(300);

    // Set wrap text for obsDistFromThreshold label
    obsDistFromThresh.getLabel().setPrefHeight(40);
    obsDistFromThresh.getLabel().setPrefWidth(100);

    // Set alignment for obsDistFromThreshold text field
    obsDistFromThresh.getTextField().setLayoutY(8);
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
        if (isSelected()) aircraftList.setValue(id);
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

      recalculateRunwayValues();
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
        if (isSelected()) obstacleList.setValue(name);
      } else if (list.wasRemoved()) {
        String name = list.getRemoved().get(0).getName();

        obstacleList.getItems().remove(name);
        if (obstacleList.getValue().equals(name)) obstacleList.setValue("None");
      }
    });

    // Add listeners when current value from ChoiceBox change in obstacleList
    obstacleList.valueProperty().addListener((obVal, oldVal, newVal) -> {
      if (newVal == null) return;

      logger.info("Obstacle selected named " + obstacleList.getValue());
      if (obstacleList.getSelectionModel().getSelectedItem().equals("None")) {
        runway.setCurrentObstacle(null);

        height.setText("");
        length.setText("");
        centerline.setText("");
      } else {
        runway.setCurrentObstacle(state.obstacleListProperty().stream().
            filter(obstacle -> obstacle.getName().equals(obstacleList.getValue())).
            findAny().get());

        height.setText(String.valueOf(runway.getCurrentObstacle().getHeight()));
        length.setText(String.valueOf(runway.getCurrentObstacle().getLength()));
        centerline.setText(String.valueOf(runway.getCurrentObstacle().getCenterline()));
      }

      recalculateRunwayValues();
    });
  }

  private void recalculateRunwayValues() {
    if (calcTowards) {
      if (runway.getCurrentObstacle() == null) {
        calcBreakdown.setText(Calculator.resetCalculationsPP(runway));
      } else {
        calcBreakdown.setText(Calculator.toraTowardsObsPP(runway, runway.getCurrentObstacle()) + "\n" +
            Calculator.ldaTowardsObsPP(runway, runway.getCurrentObstacle()));
      }
    } else {
      if (runway.getCurrentObstacle() == null || runway.getCurrentAircraft() == null) {
        calcBreakdown.setText(Calculator.resetCalculationsPP(runway));
      } else {
        calcBreakdown.setText(Calculator.
            toraAwayObsPP(runway, runway.getCurrentObstacle(), runway.getCurrentAircraft()) + "\n" +
            Calculator.ldaOverObsPP(runway, runway.getCurrentObstacle(), runway.getCurrentAircraft()));
      }
    }
  }

  @FXML
  private void onEditAircraft() {

  }

  @FXML
  private void onAddAircraft() {
    new AircraftScene(createModalStage(stage), state, false);
  }

  @FXML
  private void onDeleteAircraft() {

  }

  @FXML
  private void loadPredefinedObstacles() {
    String oldValue = obstacleList.getValue();

    state.loadPredefinedObstacles();

    obstacleList.setValue(oldValue);
  }

  @FXML
  private void onEditObstacle() {

  }

  @FXML
  private void onAddObstacle() {
    new ObstacleScene(createModalStage(stage), state, false);
  }

  @FXML
  private void onDeleteObstacle() {

  }

  @FXML
  private void onCloseRunway() {

  }
}