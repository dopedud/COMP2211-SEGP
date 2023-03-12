package uk.ac.soton.comp2211.team33.components;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import uk.ac.soton.comp2211.team33.utilities.Calculator;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The RunwayTab class is a custom component that holds a runway in a tab in an airport. An airport can contain many
 * runways.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
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
  private TextArea toraCalc, todaCalc, asdaCalc, ldaCalc;

  @FXML
  private ChoiceBox<String> aircraftList, obstacleList;

  @FXML
  private Label blastProtection, height, length, centerline;

  public RunwayTab(Stage stage, Airport state, Runway runway) {
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
    ctora.setText(String.valueOf(runway.getTora()));
    ctoda.setText(String.valueOf(runway.getToda()));
    casda.setText(String.valueOf(runway.getCasda()));
    clda.setText(String.valueOf(runway.getClda()));
    cresa.setText(String.valueOf(runway.getCresa()));

    // Add listeners from calculated values UI to calculated values model
    runway.ctoraProperty().addListener(((obVal, oldVal, newVal) -> ctora.setText(String.valueOf(newVal))));
    runway.ctodaProperty().addListener(((obVal, oldVal, newVal) -> ctoda.setText(String.valueOf(newVal))));
    runway.casdaProperty().addListener(((obVal, oldVal, newVal) -> casda.setText(String.valueOf(newVal))));
    runway.cldaProperty().addListener(((obVal, oldVal, newVal) -> clda.setText(String.valueOf(newVal))));
    runway.cresaProperty().addListener(((obVal, oldVal, newVal) -> cresa.setText(String.valueOf(newVal))));

    // Initialise list UI to list models
    InitialiseAircraftList();
    InitialiseObstacleList();
  }

  private void InitialiseAircraftList() {
    // Initialise list by setting items of aircraftList UI to aircraftList model
    ArrayList<String> ids = new ArrayList<>();
    ids.add("None");

    for (Aircraft aircraft : state.getAircraftList()) {
      ids.add(aircraft.getId());
    }

    aircraftList.setItems(FXCollections.observableArrayList(ids));
    aircraftList.setValue("None");

    // Add listeners from aircraftList UI to aircraftList model
    state.getAircraftList().addListener((ListChangeListener<? super Aircraft>) list -> {
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
        runway.setCurrentAircraft(state.getAircraftList().stream().
            filter(aircraft -> aircraft.getId().equals(aircraftList.getValue())).
            findAny().get());

        blastProtection.setText(String.valueOf(runway.getCurrentAircraft().getBlastProtection()));
      }

      calculateRunwayValues();
    });
  }

  private void InitialiseObstacleList() {
    // Initialise list by setting aircraftList UI to aircraftList model
    ArrayList<String> names = new ArrayList<>();
    names.add("None");

    for (Obstacle obstacle : state.getObstacleList()) {
      names.add(obstacle.getName());
    }

    obstacleList.setItems(FXCollections.observableArrayList(names));
    obstacleList.setValue("None");

    // Add listeners from obstacleList UI to obstacleList model
    state.getObstacleList().addListener((ListChangeListener<? super Obstacle>) list -> {
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
        runway.setCurrentObstacle(state.getObstacleList().stream().
            filter(obstacle -> obstacle.getName().equals(obstacleList.getValue())).
            findAny().get());

        height.setText(String.valueOf(runway.getCurrentObstacle().getHeight()));
        length.setText(String.valueOf(runway.getCurrentObstacle().getLength()));
        centerline.setText(String.valueOf(runway.getCurrentObstacle().getCenterline()));
      }

      calculateRunwayValues();
    });
  }

  private void calculateRunwayValues() {
    if (runway.getCurrentObstacle() == null) {
      Calculator.resetCalculations(runway);
    } else if (runway.getCurrentAircraft() == null) {
      Calculator.toraTowardsObs(runway, runway.getCurrentObstacle());
      Calculator.ldaTowardsObs(runway, runway.getCurrentObstacle());
    } else {
      Calculator.toraTowardsObs(runway, runway.getCurrentObstacle());
      Calculator.ldaTowardsObs(runway, runway.getCurrentObstacle());
      Calculator.toraAwayObs(runway, runway.getCurrentObstacle(), runway.getCurrentAircraft());
      Calculator.ldaOverObs(runway, runway.getCurrentObstacle(), runway.getCurrentAircraft());
    }
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
    new ObstacleScene(createModalStage(), state, false);
  }

  @FXML
  private void onDeleteObstacle() {

  }

  @FXML
  private void onCloseRunway() {

  }
}
