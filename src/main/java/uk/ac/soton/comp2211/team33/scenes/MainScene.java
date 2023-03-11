package uk.ac.soton.comp2211.team33.scenes;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import uk.ac.soton.comp2211.team33.components.RawInformationDisplay;
import javafx.util.converter.NumberStringConverter;
import uk.ac.soton.comp2211.team33.components.InputField;
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
  private RawInformationDisplay calcSummary;


  @FXML
  private RawInformationDisplay oldRunwayValues;

  @FXML
  private RawInformationDisplay newRunwayValues;

  @FXML
  private InputField obstacleDistance;

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
  @Override
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
      calcMode.set(null);
      renderTabView();
    });

    // Change handlers for calculation summary

    calcMode.addListener((ov, oldMode, newMode) -> renderTabView());
    obstaclesList.getSelectionModel().selectedItemProperty().addListener((ov, oldObs, newObs) -> renderTabView());
    aircraftList.getSelectionModel().selectedItemProperty().addListener((ov, oldAc, newAc) -> renderTabView());
    runwaysList.getSelectionModel().selectedItemProperty().addListener((ov, oldRw, newRw) -> renderTabView());

    // Render tab view

    renderTabView();
  }

  /**
   * Renders airport tabs
   */
  private void renderTabs() {
    SimpleListProperty<String> airportCodesProperty = state.airportCodesProperty();

    for (String airportCode : airportCodesProperty) {
      Tab tab = new Tab(airportCode);
      tabPanel.getTabs().add(tab);
    }
  }

  private ChangeListener<Number> obstacleDistanceListener;

  /**
   * Renders tab content
   */
  private void renderTabView() {
    StringConverter<Number> converter = new NumberStringConverter();

    AirportState airportState = state.getActiveAirportState();

    // Render obstacles and aircraft list

    obstaclesList.setItems(airportState.obstaclesListProperty().get());
    aircraftList.setItems(airportState.aircraftListProperty().get());
    runwaysList.setItems(airportState.runwaysListProperty().get());

    Runway currentRunway = runwaysList.getSelectionModel().getSelectedItem();

    for (Runway runway : airportState.runwaysListProperty()) {
      Bindings.unbindBidirectional(runway.obstacleDistanceProperty(), obstacleDistance.inputTextProperty());
    }

    if (currentRunway != null) {
      if (obstacleDistanceListener == null) {
        obstacleDistanceListener = (ov, oldD, newD) -> renderTabView();
      }

      currentRunway.obstacleDistanceProperty().removeListener(obstacleDistanceListener);
      currentRunway.obstacleDistanceProperty().addListener(obstacleDistanceListener);

      // obstacleDistance.removeTextBinding(currentRunway.obstacleDistanceProperty());
      //obstacleDistance.inputTextProperty().bindBidirectional(runwaysList.getSelectionModel().getSelectedItem().obstacleDistanceProperty(), converter);

      obstacleDistance.setTextProperty(String.valueOf(currentRunway.getObstacleDistance()));

      Bindings.bindBidirectional(obstacleDistance.inputTextProperty(), currentRunway.obstacleDistanceProperty(), converter);

      obstacleDistance.setDisable(false);
    } else {
      obstacleDistance.setDisable(true);
      obstacleDistance.setText(null);
    }

    renderCalculations();
    renderRunwayValues();
  }

  private void renderRunwayValues() {
    Runway currentRunway = runwaysList.getSelectionModel().getSelectedItem();

    if (currentRunway == null) {
      newRunwayValues.setInformation("No calculation has been performed yet");
      return;
    }

    oldRunwayValues.setInformation(currentRunway.getInformationString());

    String calcMode = this.calcMode.get();
    Obstacle currentObstacle = obstaclesList.getSelectionModel().getSelectedItem();
    Aircraft currentAircraft = aircraftList.getSelectionModel().getSelectedItem();

    if (calcMode == null || currentObstacle == null) {
      newRunwayValues.setInformation("No calculation has been performed yet");
      return;
    }

    if ((calcMode.equals("TORA_AWAY") || calcMode.equals("LDA_OVER")) && currentAircraft == null) {
      newRunwayValues.setInformation("No calculation has been performed yet");
      return;
    }

    newRunwayValues.setInformation(currentRunway.getCInformationString());
  }

  private void renderCalculations() {
    String calcMode = this.calcMode.get();
    Runway currentRunway = runwaysList.getSelectionModel().getSelectedItem();
    Obstacle currentObstacle = obstaclesList.getSelectionModel().getSelectedItem();
    Aircraft currentAircraft = aircraftList.getSelectionModel().getSelectedItem();

    if (calcMode == null || currentRunway == null || currentObstacle == null) {
      calcSummary.setInformation("No calculation has been performed yet");
      return;
    }

    if (calcMode.equals("TORA_TOWARDS")) {
      String calculation = Calculator.toraTowardsObsPP(currentRunway, currentObstacle);
      calcSummary.setInformation(calculation);
      return;
    }

    if (calcMode.equals("LDA_TOWARDS")) {
      String calculation = Calculator.ldaTowardsObsPP(currentRunway, currentObstacle);
      calcSummary.setInformation(calculation);
      return;
    }

    if (currentAircraft == null) {
      calcSummary.setInformation("Please pick an aircraft to complete the calculation");
      return;
    }

    if (calcMode.equals("TORA_AWAY")) {
      String calculation = Calculator.toraAwayObsPP(currentRunway, currentObstacle, currentAircraft);
      calcSummary.setInformation(calculation);
      return;
    }

    if (calcMode.equals("LDA_OVER")) {
      String calculation = Calculator.ldaOverObsPP(currentRunway, currentObstacle, currentAircraft);
      calcSummary.setInformation(calculation);
    }
  }

  /**
   * Creates a top-down view of the runway using a canvas
   *
   * @param primaryStage
   */
  // TODO: 11/03/2023 Brian I left most of the variables as you see them below
  /* Those variables can be replaced by given parameters to make the canvas adapt
   We can take care of the colours later.
   Variables that can be passed in instead are Height (currently 400), Width (currently 550)
   The drawing is all parameterised, so make sure to slightly maintain the aspect ratio of height-width.
  */
  public void makeTopDown(Stage primaryStage, String rdesignator, double otora) {

    var canvas = new Canvas();
    canvas.setHeight(400);
    canvas.setWidth(550);

    //The designator of the selected runway
    var designator = rdesignator;

    var formattedDes = formatDesignators(designator);

    var gc = canvas.getGraphicsContext2D();

    var cw = canvas.getWidth();
    var ch = canvas.getHeight();

    double toraL = otora; //Some given TORA to be passed in, dummy value

    //Surrounding area
    gc.setFill(Color.valueOf("#2d8000"));
    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

    double[] yCoord = {ch * 0.3, ch * 0.3, ch * 0.225, ch * 0.225, ch * 0.3, ch * 0.3, ch * 0.7, ch * 0.7, ch * 0.775, ch * 0.775, ch * 0.7, ch * 0.7};
    double[] xCoord = {0.0, cw * 0.18, cw * 0.27, cw * 0.727, cw * 0.818, cw, cw, cw * 0.818, cw * 0.727, cw * 0.27, cw * 0.18, 0.0};

    //The polygon around the runway
    gc.setFill(Color.valueOf("#4542ff"));
    gc.fillPolygon(xCoord, yCoord, 12);
    gc.setStroke(Color.BLACK);
    gc.strokePolygon(xCoord, yCoord, 12);

    //Runway body
    gc.setFill(Color.valueOf("#242424"));
    gc.fillRect(cw * 0.1, ch * 0.44, cw * 0.80, ch * 0.13);
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(0.25);
    gc.strokeRect(cw * 0.1, ch * 0.44, cw * 0.80, ch * 0.13);

    //Cleared and graded area lines
    // TODO: 11/03/2023 In development
    gc.setStroke(Color.valueOf("#ffffff"));
    gc.setLineWidth(1);
//  gc.strokeLine(101, 120, 160, 120);

    //Dashed centre line, lower dash number to get more dashes in the line.
    int dashes = 9;
    if (toraL >= 3500) {
      dashes = 5;
    }
    gc.setLineDashes(dashes);
    gc.strokeLine(cw * 0.25, ch * 0.5, cw * 0.75, ch * 0.5);
    gc.setLineDashes(0);

    //Starting lines at each end of the runway
    gc.setStroke(Color.valueOf("#ffffff"));
    gc.setLineWidth(2);
    var tempA = cw * 0.12;
    var tempB = ch * 0.46;
    var tempC = cw * 0.16;
    var tempD = ch * 0.46;
    while (tempB < ch * 0.5625) {
      gc.strokeLine(tempA, tempB, tempC, tempD);
      tempB += 5;
      tempD += 5;
    }

    var tempX = cw * 0.88;
    var tempY = cw * 0.84;
    tempB = ch * 0.46;
    tempD = ch * 0.46;
    while (tempB < ch * 0.5625) {
      gc.strokeLine(tempX, tempB, tempY, tempD);
      tempB += 5;
      tempD += 5;
    }

    //Write the runway designators in the 2D view
    gc.setLineWidth(1);
    gc.strokeText(formattedDes[0], cw * 0.18, ch * 0.5);
    gc.strokeText(formattedDes[1], cw * 0.80, ch * 0.5);

    VBox vbox = new VBox(canvas);
    Scene scene = new Scene(vbox);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * Takes in a designator string and gives back a list with 2 elements,
   * first is the original, and second is the other side's designator, both are formatted in a way for the canvas
   * @param designator the runway designator
   * @return a list with 2 opposite designators
   */
  private String[] formatDesignators(String designator) {

    //Throw error if designator is not of the correct format (e.g. 09L, 26)
    if (designator.length() != 3 && designator.length() != 2) {
      System.err.println("Bad format for designator.");
    }

    //The runway designator of the other side
    String oppositeDes = null;

    //Designator number
    int numDes = 0;
    try {
      if (designator.length() == 3) {
        numDes = Integer.parseInt(designator.substring(0, 2));
      } else {
        numDes = Integer.parseInt(designator);
      }
    } catch (NumberFormatException e) {
      System.err.println("Bad number format for designator.");
    }

    //Minus 36
    int newNumDes = 36 - numDes;
    String desSide = "";
    if (designator.length() == 3) {
      //Change character for other designator
      if (designator.charAt(2) == 'L') {
        desSide = "R";
      } else {
        desSide = "L";
      }
    }

    //Check for the number designator
    if (newNumDes < 10) {
      oppositeDes = "0" + newNumDes + "\n " + desSide;
    } else {
      oppositeDes = newNumDes + "\n " + desSide;
    }

    if (designator.length() == 3) {
      designator = designator.substring(0, 2) + "\n " + designator.substring(2);
    }

    return new String[]{designator, oppositeDes};
  }
}
