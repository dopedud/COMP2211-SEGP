package uk.ac.soton.comp2211.team33.scenes;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import uk.ac.soton.comp2211.team33.components.CalcSummary;
import uk.ac.soton.comp2211.team33.models.*;
import uk.ac.soton.comp2211.team33.utilities.Calculator;

/**
 * Main scene of the application. This scene should only be fully functional when there is at least a runway configured.
 * Contains visualisation and computation of the selected runway.
 *
 * @author Brian (dal1g21@soton.ac.uk)
 */
public class MainScene extends BaseScene {

  /**
   * The TabPane node used to switch between different airports
   */
  @FXML
  private TabPane tabPanel;

  /**
   * The Canvas panel node that contains the canvas node
   */
  @FXML
  private VBox canvasPanel;

  /**
   * The Canvas node to draw visualisations
   */
  @FXML
  private Canvas canvas;

  /**
   * The "Add new runway" button used to add new runways
   */
  @FXML
  private Button addNewRunwayButton;

  /**
   * A ListView to display all added obstacles
   */
  @FXML
  private ListView<Obstacle> obstaclesList;

  /**
   * A ListView to display all added aircraft
   */
  @FXML
  private ListView<Aircraft> aircraftList;

  @FXML
  private ListView<Runway> runwaysList;

  private SimpleStringProperty calcMode;            // Local state, no need to be in app state


  /**
   * A CalcSummary component to display the calculation steps
   */
  @FXML
  private CalcSummary calcSummary;

  public MainScene(Stage stage, AppState state) {
    super(stage, state);
  }

  /**
   * An event handler that is fired when the "Add obstacle" button is clicked
   */
  @FXML
  private void handleAddObstacle() {
    new NewObstacleScene(createModalStage(), state);
  }

  @FXML
  private void handleLoadPredefinedObstacles() {
    state.getActiveAirportState().loadPredefinedObstacles();
  }

  /**
   * An event handler that is fired when the "Add aircraft" button is clicked
   */
  @FXML
  private void handleAddAircraft() {
    new NewAircraftScene(createModalStage(), state);
  }

  /**
   * An event handler that is fired when the "Add new runway" button is clicked
   */
  @FXML
  private void handleAddRunway() {
    new NewRunwayScene(createModalStage(), state);
  }

  /**
   * An event handler that is fired when the "Calculate TORA away" button is clicked:
   * Calculates the TORA away from obstacle, and updates the currently selected runway object accordingly
   */
  @FXML
  private void handleCalculateToraAway() {
    calcMode.set("TORA_AWAY");

  }

  /**
   * An event handler that is fired when the "Calculate TORA towards" button is clicked:
   * Calculates the TORA towards the obstacle, and updates the currently selected runway object accordingly
   */
  @FXML
  private void handleCalculateToraTowards() {
    calcMode.set("TORA_TOWARDS");
  }

  /**
   * An event handler that is fired when the "Calculate LDA towards" button is clicked:
   * Calculates the LDA towards the obstacle, and updates the currently selected runway object accordingly
   */
  @FXML
  private void handleCalculateLdaTowards() {
    calcMode.set("LDA_TOWARDS");
  }

  /**
   * An event handler that is fired when the "Calculate LDA over" button is clicked:
   * Calculates the LDA over the obstacle, and updates the currently selected runway object accordingly
   */
  @FXML
  private void handleCalculateLdaOver() {
    calcMode.set("LDA_OVER");
  }

  /**
   * Builds the Main scene
   */
  protected void build() {
    // Initialise local states

    calcMode = new SimpleStringProperty();

    // Main part

    stage.setTitle("Runway Redeclaration");

    // Render skeleton layout

    renderFXML("mainScene.fxml");

    // Render tabs

    renderTabs();
    state.airportCodesProperty().addListener((ov, oldCodes, newCodes) -> renderTabs());

    // On tab change, we set the active airport, and then re-render tab view

    tabPanel.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
      state.setActiveAirportCode(newTab.getText());
      renderTabView();
    });

    // Change handlers for calculation summary

    calcMode.addListener((ov, oldMode, newMode) -> renderCalculations());
    obstaclesList.getSelectionModel().selectedItemProperty().addListener((ov, oldObs, newObs) -> renderCalculations());
    aircraftList.getSelectionModel().selectedItemProperty().addListener((ov, oldAc, newAc) -> renderCalculations());
    runwaysList.getSelectionModel().selectedItemProperty().addListener((ov, oldRw, newRw) -> renderCalculations());

    // Render tab view

    renderTabView();
  }

  /**
   * Renders airport tabs
   */
  private void renderTabs() {
    SimpleListProperty<String> airportCodesProperty = state.airportCodesProperty();

    for (String airportCode: airportCodesProperty) {
      Tab tab = new Tab(airportCode);
      tabPanel.getTabs().add(tab);
    }
  }

  /**
   * Renders tab content
   */
  private void renderTabView() {
    AirportState airportState = state.getActiveAirportState();

    // Render obstacles and aircraft list

    obstaclesList.setItems(airportState.obstaclesListProperty().get());
    aircraftList.setItems(airportState.aircraftListProperty().get());
    runwaysList.setItems(airportState.runwaysListProperty().get());

    // Render calculation summary

    renderCalculations();
  }

  private void renderCalculations() {
    String calcMode = this.calcMode.get();
    Runway currentRunway = runwaysList.getSelectionModel().getSelectedItem();
    Obstacle currentObstacle = obstaclesList.getSelectionModel().getSelectedItem();
    Aircraft currentAircraft = aircraftList.getSelectionModel().getSelectedItem();

    if (calcMode == null || currentRunway == null || currentObstacle == null) {
      calcSummary.setCalcText("No calculation has been performed yet");
      return;
    }

    if (calcMode == "TORA_TOWARDS") {
      String calculation = Calculator.toraTowardsObsPP(currentRunway, currentObstacle);
      calcSummary.setCalcText(calculation);
      return;
    }

    if (calcMode == "LDA_TOWARDS") {
      String calculation = Calculator.ldaTowardsObsPP(currentRunway, currentObstacle);
      calcSummary.setCalcText(calculation);
      return;
    }

    if (currentAircraft == null) {
      calcSummary.setCalcText("Please pick an aircraft to complete the calculation");
      return;
    }

    if (calcMode == "TORA_AWAY") {
      String calculation = Calculator.toraAwayObsPP(currentRunway, currentObstacle, currentAircraft);
      calcSummary.setCalcText(calculation);
      return;
    }

    if (calcMode == "LDA_OVER") {
      String calculation = Calculator.ldaOverObsPP(currentRunway, currentObstacle, currentAircraft);
      calcSummary.setCalcText(calculation);
    }
  }
}
