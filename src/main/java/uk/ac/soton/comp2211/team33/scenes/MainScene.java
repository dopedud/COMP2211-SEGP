package uk.ac.soton.comp2211.team33.scenes;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import uk.ac.soton.comp2211.team33.models.AirportState;
import uk.ac.soton.comp2211.team33.models.AppState;
import uk.ac.soton.comp2211.team33.models.Obstacle;
import uk.ac.soton.comp2211.team33.models.Runway;

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
  private ListView aircraftList;

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
   * Builds the Main scene
   */
  protected void build() {
    stage.setTitle("Runway Redeclaration");

    // Render skeleton layout

    renderFXML("mainScene.fxml");

    // Stretch canvas

    canvas.widthProperty().bind(canvasPanel.widthProperty());
    canvas.heightProperty().bind(canvasPanel.heightProperty());

    // Render tabs

    renderTabs();
    state.airportCodesProperty().addListener((ov, oldCodes, newCodes) -> renderTabs());

    // On tab change, we set the active airport, and then re-render tab view

    tabPanel.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
      state.setActiveAirportCode(newTab.getText());
      renderTabView();
    });

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

  private ChangeListener<Runway> runwayChangeListener;

  /**
   * Renders tab content
   */
  private void renderTabView() {
    AirportState airportState = state.getActiveAirportState();

    // Runway instance listener

    if (runwayChangeListener == null) {
      runwayChangeListener = (ov, oldRunwayState, newRunwayState) -> {
        renderAddRunwayButton();
        paintVisualisation();
      };
    }

    airportState.runwayProperty().removeListener(runwayChangeListener);      // Ensure that only one listener is active at one time
    airportState.runwayProperty().addListener(runwayChangeListener);

    // Change the disabled status of add runway button

    renderAddRunwayButton();

    // Render obstacles and aircraft list

    obstaclesList.setItems(airportState.obstaclesListProperty().get());
    aircraftList.setItems(airportState.aircraftListProperty().get());

    // Paint visualisations

    paintVisualisation();
  }

  /**
   * Renders "Add new runway" button
   */
  private void renderAddRunwayButton() {
    if (state.getActiveAirportState().getRunway() == null) {
      addNewRunwayButton.setDisable(false);
      return;
    }

    addNewRunwayButton.setDisable(true);
  }

  /**
   * Creates the background for displaying runway information
   */
  private void paintVisualisation() {
    GraphicsContext ctx = canvas.getGraphicsContext2D();
    ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    AirportState airportState = state.getActiveAirportState();

    if (airportState.getRunway() == null) {
      return;
    }

    Runway runway = airportState.getRunway();

    // Draw runway info

    ctx.setFill(Color.BLACK);
    ctx.fillRect(10, 10, 180, 200);

    ctx.setFill(Color.WHITE);
    ctx.setFont(new Font(15));
    ctx.fillText(runway.toString(), 26, 26);
  }
}
