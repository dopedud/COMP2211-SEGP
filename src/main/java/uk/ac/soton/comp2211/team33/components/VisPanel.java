package uk.ac.soton.comp2211.team33.components;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.models.Airport;
import uk.ac.soton.comp2211.team33.models.Obstacle;
import uk.ac.soton.comp2211.team33.models.Runway;
import uk.ac.soton.comp2211.team33.utilities.Pair;
import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;
import uk.ac.soton.comp2211.team33.utilities.StylePrefs;

import java.io.*;
import java.util.ArrayList;

/**
 * The VisPanel class is a custom component that renders the 2D top-down and side-on view.
 * Corresponds to user story #2, #8a, #8b, #8c, #9, #10, #11, #12, #16.
 *
 * @author Brian (dal1g21@soton.ac.uk), Jackson (jl14u21@soton.ac.uk), Geeth (gv2g21@soton.ac.uk)
 */
public class VisPanel extends StackPane {
  private static final Logger logger = LogManager.getLogger(VisPanel.class);

  private final Airport state;

  private final Runway runway;

  @FXML
  private Canvas canvas;

  @FXML
  private ImageView directionIndicator;

  private boolean isTopDownView = true;


  /**
   * The scale factor of the visualisation.
   */
  private double scale = 1;

  /**
   * The translation of the visualisation, expressed as a proportion of the canvas width / height.
   * For example, a value of 0.5 would translate the visualisation by half the width of the canvas.
   * On a 1000x1000 canvas, this would translate the visualisation by 500 pixels.
   */
  private double translateX = 0;

  /**
   * The translation of the visualisation, expressed as a proportion of the canvas width / height.
   * For example, a value of 0.5 would translate the visualisation by half the height of the canvas.
   * On a 1000x1000 canvas, this would translate the visualisation by 500 pixels.
   */
  private double translateY = 0;

  /**
   * The rotation of the vis in degrees.
   */
  private double rotation = 0;

  /**
   * The offset of the rotation in degrees - used to correctly rotate the vis to match compass heading depending on
   * which side the threshold is.
   * If the threshold is on the left, the offset is 0 degrees, if it is on the right, the offset is 180 degrees.
   */
  private int compassOffset = 0;

  private String pathToColours;

  private String[] colours = new String[0];

  /**
   * The current transform of the visualisation.
   */
  private final SimpleObjectProperty<Affine> transform = new SimpleObjectProperty<>(new Affine());

  public VisPanel(Airport state, Runway runway) {
    this.runway = runway;
    this.state = state;

    ProjectHelpers.renderRoot("/components/VisPanel.fxml", this, this);

    StylePrefs.updateVisPanel();
    pathToColours = StylePrefs.getVisPanelThemePathProperty().get();
    loadColours();

    StylePrefs.getVisPanelThemePathProperty().addListener((observable, oldValue, newValue) -> {
      pathToColours = newValue;
      loadColours();
      draw();
    });

    directionIndicator.setImage(new Image(ProjectHelpers.getResource("/pictures/direction_indicator.png").toExternalForm()));
    directionIndicator.setPreserveRatio(true);
    directionIndicator.setFitHeight(50);

    canvas.widthProperty().addListener(ignored -> draw());
    canvas.heightProperty().addListener(ignored -> draw());

    canvas.setManaged(false);
    canvas.widthProperty().bind(widthProperty());
    canvas.heightProperty().bind(heightProperty());

    runway.currentObstacleProperty().addListener(ignored -> draw());
    runway.currentAircraftProperty().addListener(ignored -> draw());
    runway.obsDistFromThreshProperty().addListener(ignored -> draw());
    state.runwayListProperty().addListener((ListChangeListener<? super Runway>) ignored -> draw());
    runway.ctoraProperty().addListener(ignored -> draw());
    runway.ctodaProperty().addListener(ignored -> draw());
    runway.casdaProperty().addListener(ignored -> draw());
    runway.cldaProperty().addListener(ignored -> draw());
    runway.cresaProperty().addListener(ignored -> draw());

    transform.addListener(ignored -> draw());

    draw();
  }

  private void loadColours() {
    InputStream stream = ProjectHelpers.getResourceAsStream(pathToColours);
    BufferedReader br = new BufferedReader(new InputStreamReader(stream));
    ArrayList<String> coloursList = new ArrayList<>();
    String line;
    while (true) {
      try {
        if ((line = br.readLine()) == null) break;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      coloursList.add(line.split(",")[0]);
    }

    colours = coloursList.toArray(new String[0]);
  }

  /**
   * Switches between the top-down and side-on view.
   */
  @FXML
  private void onSwitchView() {
    isTopDownView = !isTopDownView;
    resetTransform();
    draw();
  }

  /**
   * Resets the transform to the default values.
   */
  @FXML
  private void resetTransform() {
    scale = 1;
    translateX = 0;
    translateY = 0;
    rotation = 0;
    updateTransform();
  }

  /**
   * Updates the transform property based on the current scale, translation and rotation values.
   */
  private void updateTransform() {
    var t = new Affine();
    t.appendScale(scale, scale, getCurrentCenter());
    t.appendTranslation(translateX * canvas.getWidth(), translateY * canvas.getHeight());
    t.appendRotation(rotation, canvas.getWidth() / 2, canvas.getHeight() / 2);
    transform.set(t);
  }

  /**
   * Get the centre of the canvas as a coordinate.
   *
   * @return The current center of the visualisation, in canvas coordinates.
   */
  private Point2D getCurrentCenter() {
    var x = (0.5 + translateX) * canvas.getWidth();
    var y = (0.5 + translateY) * canvas.getHeight();
    return new Point2D(x, y);
  }

  /**
   * Zooms in by 10%.
   */
  @FXML
  private void zoomIn() {
    scale *= 1.1;
    updateTransform();
  }

  /**
   * Zooms out by 10%.
   */
  @FXML
  private void zoomOut() {
    scale /= 1.1;
    updateTransform();
  }

  /**
   * Rotates the visualisation by 5 degrees to the left.
   */
  @FXML
  private void rotateLeft() {
    rotation += 5;
    updateTransform();
  }

  /**
   * Rotates the visualisation by 5 degrees to the right.
   */
  @FXML
  private void rotateRight() {
    rotation -= 5;
    updateTransform();
  }

  /**
   * Rotates the visualisation to the runway heading. Only works in top-down view.
   */
  @FXML
  private void rotateToRunwayHeading() {
    if (!isTopDownView) {
      return;
    }
    rotation = (runway.getCompassHeading() + 270 + compassOffset) % 360;
    updateTransform();
  }

  private void rotateIndicator() {
    directionIndicator.setRotate((rotation - runway.getCompassHeading() - 270 - compassOffset) % 360);
  }

  /**
   * Translates the visualisation to the left by 10% of the canvas width.
   */
  @FXML
  private void translateLeft() {
    // TODO: Trigger on mouse drag
    translateX += 0.1;
    updateTransform();
  }

  /**
   * Translates the visualisation to the right by 10% of the canvas width.
   */
  @FXML
  private void translateRight() {
    // TODO: Trigger on mouse drag
    translateX -= 0.1;
    updateTransform();
  }

  /**
   * Translates the visualisation up by 10% of the canvas height.
   */
  @FXML
  private void translateUp() {
    translateY += 0.1;
    updateTransform();
  }

  /**
   * Translates the visualisation down by 10% of the canvas height.
   */
  @FXML
  private void translateDown() {
    translateY -= 0.1;
    updateTransform();
  }

  /**
   * Function for drawing the 2D views.
   */
  private void draw() {
    if (isTopDownView) {
      drawTopDown();
      return;
    }

    drawSideways();
  }

  private void drawSideways() {
    GraphicsContext gc = canvas.getGraphicsContext2D();

    var grass = Color.valueOf(colours[0]);
    var runwayBody = Color.valueOf(colours[2]);
    var thresholdColour = Color.valueOf(colours[4]);
    var stopwayColour = Color.valueOf(colours[5]);
    var clearwayColour = Color.valueOf(colours[6]);
    var obstacleColour = Color.valueOf(colours[8]);
    var strokeColour = Color.valueOf(colours[9]);
    var sky = Color.valueOf(colours[10]);
    var dust = Color.valueOf(colours[11]);
    var alsTocsColour = Color.valueOf(colours[12]);
    var resaColour = Color.valueOf(colours[13]);
    var safetyDistColour = Color.valueOf(colours[14]);
    var legendColour = Color.valueOf(colours[15]);

    // TODO: disable rotating

    gc.setTransform(transform.get());

    gc.setFont(new Font(20));
    gc.setTextBaseline(VPos.CENTER);
    gc.setTextAlign(TextAlignment.CENTER);

    double cw = this.getWidth();
    double ch = this.getHeight();
    double layerDepth = ch / 40;
    double grassStartY = (double) 2 / 3 * ch;

    // Clears the canvas, clearing a large area so that it is displayed correctly when zoomed out
    gc.clearRect(-5000, -5000, 10000, 10000);

    // Sky

    gc.setFill(sky);
    gc.fillRect(-5000, -5000, 10000, 10000);

    // Grass

    gc.setFill(grass);
    gc.fillRect(-5000, grassStartY, 10000, layerDepth * 10);

    // Dust

    gc.setFill(dust);
    gc.fillRect(-5000, grassStartY + layerDepth, cw * 100, ch * 100);

    // Overall runway properties

    Pair<Runway, Runway> runwayPair = getRunwayPair();
    Runway leftRunway = runwayPair.fst();
    Runway rightRunway = runwayPair.snd();
    boolean drawLegendFromLeft = runway == leftRunway;

    double runwayLengthPx = cw / 1.5;
    double runwayStartX = cw / 2 - runwayLengthPx / 2;
    double runwayStartY = grassStartY - layerDepth;
    double runwayEndX = runwayStartX + runwayLengthPx;
    double runwayEndY = grassStartY;
    double toda = leftRunway.getToda();

    // Print designator

    double designatorStartY = runwayStartY + layerDepth / 2;

    gc.setFill(strokeColour);
    gc.fillText(leftRunway.getDesignator(), runwayStartX - 60, designatorStartY - 10);

    if (rightRunway != null) {
      gc.fillText(rightRunway.getDesignator(), runwayEndX + 60, designatorStartY - 10);
    }

    // Draw runway body

    double tora = leftRunway.getTora();
    double toraLengthPx = (tora / toda) * runwayLengthPx;
    double runwayBodyEndX = runwayStartX + toraLengthPx;

    gc.setFill(runwayBody);
    gc.fillRect(runwayStartX, runwayStartY, toraLengthPx, layerDepth);

    // Draw primary clearway

    double clearway = leftRunway.getClearway();
    double clearwayLengthPx = (clearway / toda) * runwayLengthPx;

    gc.setFill(clearwayColour);
    gc.fillRect(runwayBodyEndX, runwayStartY, clearwayLengthPx, layerDepth);

    // Draw primary stopway

    double stopway = leftRunway.getStopway();
    double stopwayLengthPx = (stopway / toda) * runwayLengthPx;

    gc.setFill(stopwayColour);
    gc.fillRect(runwayBodyEndX, runwayStartY, stopwayLengthPx, layerDepth);

    // Draw right clearway

    double rightClearway = rightRunway == null ? 0 : rightRunway.getClearway();
    double rightClearwayLengthPx = (rightClearway / toda) * runwayLengthPx;
    double rightClearwayStartX = runwayStartX - rightClearwayLengthPx;

    gc.setFill(clearwayColour);
    gc.fillRect(rightClearwayStartX, runwayStartY, rightClearwayLengthPx, layerDepth);

    // Draw right stopway

    double rightStopway = rightRunway == null ? 0 : rightRunway.getStopway();
    double rightStopwayLengthPx = (rightStopway / toda) * runwayLengthPx;
    double rightStopwayStartX = runwayStartX - rightStopwayLengthPx;

    gc.setFill(stopwayColour);
    gc.fillRect(rightStopwayStartX, runwayStartY, rightStopwayLengthPx, layerDepth);

    // Draw threshold

    double threshold = runway.getThreshold();
    double thresholdLengthPx = (threshold / toda) * runwayLengthPx;
    double thresholdStartX = drawLegendFromLeft ? thresholdLengthPx + runwayStartX : runwayBodyEndX - thresholdLengthPx;

    double thresholdLineLengthPx = 100;
    double thresholdEndY = runwayStartY + thresholdLineLengthPx;

    gc.beginPath();
    gc.setStroke(thresholdColour);
    gc.setLineDashes(5);
    gc.moveTo(thresholdStartX, runwayStartY);
    gc.lineTo(thresholdStartX, thresholdEndY);
    gc.stroke();

    gc.setFill(thresholdColour);
    gc.fillText("Threshold= " + threshold + " m", thresholdStartX, thresholdEndY + 20);

    boolean isTowards = runway.isCalcTowards();
    boolean isNegativeGradient = drawLegendFromLeft ^ isTowards;

    // Draw obstacle

    Obstacle obstacle = runway.getCurrentObstacle();
    if (obstacle != null) {
      double mHeightToPx = 4;
      double obstacleDistance = runway.getObsDistFromThresh();
      double obstacleLengthPx = (obstacle.getLength() / toda) * runwayLengthPx;
      double scaledObstacleHeightPx = obstacle.getHeight() * mHeightToPx;
      double heightTimes50Px = (obstacle.getHeight() * 50 / toda) * runwayLengthPx;

      double obstacleDistancePx = (obstacleDistance / toda) * runwayLengthPx;
      double obstacleStartX = drawLegendFromLeft ? obstacleDistancePx + thresholdStartX : thresholdStartX - obstacleDistancePx - obstacleLengthPx;
      double obstacleStartY = runwayStartY - scaledObstacleHeightPx;

      gc.setFill(obstacleColour);
      gc.fillRect(obstacleStartX, obstacleStartY, obstacleLengthPx, scaledObstacleHeightPx);

      // Draw landing slope

      double slopeStartX = isNegativeGradient ? obstacleStartX : obstacleStartX + obstacleLengthPx;

      gc.beginPath();
      gc.setStroke(obstacleColour);
      gc.setLineDashes(5);
      gc.moveTo(slopeStartX, runwayStartY - scaledObstacleHeightPx);
      gc.lineTo(isNegativeGradient ? slopeStartX + heightTimes50Px : slopeStartX - heightTimes50Px, runwayStartY);
      gc.stroke();

      drawDistanceLegend(gc, "ALS/TOCS= " + obstacle.getHeight() * 50 + " m", alsTocsColour, slopeStartX, runwayStartY - 200, (isNegativeGradient ? 1 : -1) * heightTimes50Px, 200);

      // Draw RESA legend

      double obstacleEndX = isNegativeGradient ? obstacleStartX + obstacleLengthPx : obstacleStartX;
      double resa = runway.getResa();
      double resaLengthPx = (resa / toda) * runwayLengthPx;

      drawDistanceLegend(gc, "RESA= " + resa + " m", resaColour, obstacleEndX, runwayEndY - 150, (isNegativeGradient ? 1 : -1) * resaLengthPx, 150);

      // Draw safety distance

      double safetyDistancePx = 60 / toda * runwayLengthPx;
      double safetyDistanceStartX = isNegativeGradient
              ? Math.max(obstacleEndX + resaLengthPx, obstacleStartX + heightTimes50Px)
              : Math.min(obstacleStartX - resaLengthPx, obstacleEndX - heightTimes50Px);
      drawDistanceLegend(gc, "Safety distance", safetyDistColour, safetyDistanceStartX, runwayEndY - 150, (isNegativeGradient ? 1 : -1) * safetyDistancePx, 150);
    }

    // Draw ctoda legend

    double ctoda = runway.getCtoda();
    double conceptualRunwayStartX = drawLegendFromLeft ? runwayStartX : runwayBodyEndX;

    if (ctoda > 0) {
      double ctodaLengthPx = (ctoda / toda) * runwayLengthPx;
      double startX = isTowards ? conceptualRunwayStartX : (drawLegendFromLeft ? runwayEndX : rightClearwayStartX);
      drawDistanceLegend(gc, "TODA= " + ctoda + " m", legendColour,
        startX, runwayEndY - 500, (isNegativeGradient ? -1 : 1) * ctodaLengthPx, 500);
    }

    // Draw casda

    double casda = runway.getCasda();

    if (casda > 0) {
      double casdaLengthPx = (casda / toda) * runwayLengthPx;
      double startX = isTowards ? conceptualRunwayStartX : (drawLegendFromLeft ? (runwayBodyEndX + stopwayLengthPx) : rightStopwayStartX);
      drawDistanceLegend(gc, "ASDA= " + casda + " m", legendColour, startX,
        runwayEndY - 400, (isNegativeGradient ? -1 : 1) * casdaLengthPx, 400);
    }

    // Draw ctora legend

    double ctora = runway.getCtora();

    if (ctora > 0) {
      double ctoraLengthPx = (ctora / toda) * runwayLengthPx;
      double startX = isTowards ? conceptualRunwayStartX : (drawLegendFromLeft ? runwayBodyEndX : runwayStartX);
      drawDistanceLegend(gc, "TORA= " + ctora + " m", legendColour,
              startX, runwayEndY - 300, (isNegativeGradient ? -1 : 1) * ctoraLengthPx, 300);
    }

    // Draw clda legend

    double clda = runway.getClda();

    if (clda > 0) {
      double cldaLengthPx = (clda / toda) * runwayLengthPx;
      double startX = isTowards ? thresholdStartX : (drawLegendFromLeft ? runwayBodyEndX : runwayStartX);
      drawDistanceLegend(gc, "LDA= " + clda + " m", legendColour,
              startX, runwayEndY - 200, (isNegativeGradient ? -1 : 1) * cldaLengthPx, 200);
    }

    // Draw clearway legend

    if (clearway > 0) {
      drawDistanceLegend(gc, "Clearway= " + clearway + " m", clearwayColour, runwayBodyEndX, runwayEndY - 250, clearwayLengthPx, 250);
    }

    // Draw stopway legend

    if (stopway > 0) {
      drawDistanceLegend(gc, "Stopway= " + stopway + " m", stopwayColour, runwayBodyEndX, runwayEndY - 200, stopwayLengthPx, 200);
    }

    if (rightClearway > 0) {
      drawDistanceLegend(gc, "Clearway= " + rightClearway + " m", clearwayColour, runwayStartX, runwayEndY - 250, -rightClearwayLengthPx, 250);
    }

    if (rightStopway > 0) {
      drawDistanceLegend(gc, "Stopway= " + rightStopway + " m", stopwayColour, runwayStartX, runwayEndY - 250, -rightStopwayLengthPx, 250);
    }
  }

  /**
   * Draws the distance indicators for the runway when called.
   *
   * @param gc     graphics context
   * @param legend distance label
   * @param color  colour
   * @param startX
   * @param startY
   * @param width
   * @param height
   */
  private void drawDistanceLegend(GraphicsContext gc, String legend, Color color, double startX, double startY, double width, double height) {
    gc.beginPath();

    gc.setStroke(color);
    gc.setLineDashes(5);

    // First endpoint

    gc.moveTo(startX, startY);
    gc.lineTo(startX, startY + height);

    // Second endpoint

    gc.moveTo(startX + width, startY);
    gc.lineTo(startX + width, startY + height);

    // Top line

    gc.moveTo(startX, startY);
    gc.lineTo(startX + width, startY);

    // Stroke everything

    gc.stroke();

    // Add legend

    gc.setFill(color);
    gc.fillText(legend, startX + width / 2, startY - 20);
  }

  /**
   * Returns a pair of matching runways. The runway with a shorter threshold will be the first item.
   *
   * @return Pair of runways
   */
  private Pair<Runway, Runway> getRunwayPair() {
    // TODO: Move this method to Runway class, and remove the other 2 methods

    String oppositeDesignator = formatDesignators(runway.getDesignator())[1];
    Runway oppositeRunway = fetchRunway(oppositeDesignator.replaceAll("[\r\n ]+", ""));

    System.out.println(oppositeRunway);

    if (oppositeRunway == null) {
      return new Pair(runway, null);
    }

    if (runway.getThreshold() <= oppositeRunway.getThreshold()) {
      return new Pair(runway, oppositeRunway);
    }

    return new Pair(oppositeRunway, runway);
  }

  /**
   * This method is called when a top-down view is requested by the controller.
   * Draws a top-down view of the selected runway.
   */
  private void drawTopDown() {
    var gc = canvas.getGraphicsContext2D();

    // Direction Indicator
    rotateIndicator();

    // Setting the transform of the canvas to the current one
    gc.setTransform(transform.get());

    gc.setFont(new Font(20));
    gc.setTextAlign(TextAlignment.LEFT);
    gc.setTextBaseline(VPos.BASELINE);

    //Width and height of the canvas
    double cw = canvas.getWidth();
    double ch = canvas.getHeight();

    var designator = runway.getDesignator();

    var formattedDes = formatDesignators(designator);

    //The opposing side's designator formatted into a string of no spaces, ready to use for searching
    var otherDesignator = formattedDes[1].replaceAll("[\r\n]+", "").replaceAll(" ", "");

    var otherRunway = fetchRunway(otherDesignator);

    //This is a boolean that determines if the threshold should be switched.
    boolean leftT = checkThresh(otherDesignator);

    if (leftT) {
      this.compassOffset = 180;
    }

    double threshold = runway.getThreshold();

    double stopway = runway.getStopway();

    double clearway = runway.getClearway();

    gc.save();

    // Clears the canvas, clearing a large area so that it is displayed correctly when zoomed out
    gc.clearRect(-5000, -5000, 10000, 10000);

    //Colours that can be changed
    var grass = Color.valueOf(colours[0]);
    var clearedAndgraded = Color.valueOf(colours[1]);
    var runwayBody = Color.valueOf(colours[2]);
    var runwayLines = Color.valueOf(colours[3]);
    var thresholdColour = Color.valueOf(colours[4]);
    var stopwayColour = Color.valueOf(colours[5]);
    var clearwayColour = Color.valueOf(colours[6]);
    var accent1 = Color.valueOf(colours[7]);
    var obstacleColour = Color.valueOf(colours[8]);
    var strokeColour = Color.valueOf(colours[9]);

    //Surrounding area
    gc.setFill(grass);
    gc.fillRect(-5000, -5000, 10000, 10000);

    //X and Y coordinates
    double[] xCoord = {0.0, cw * 0.2, cw * 0.3, cw * 0.7, cw * 0.8, cw, cw, cw * 0.8, cw * 0.7, cw * 0.3, cw * 0.2, 0.0};
    double[] yCoord = {ch * 0.3, ch * 0.3, ch * 0.2, ch * 0.2, ch * 0.3, ch * 0.3, ch * 0.7, ch * 0.7, ch * 0.8, ch * 0.8, ch * 0.7, ch * 0.7};

    //The polygon around the runway
    gc.setFill(clearedAndgraded);
    gc.fillPolygon(xCoord, yCoord, 12);
    gc.setStroke(strokeColour);
    gc.strokePolygon(xCoord, yCoord, 12);

    //Runway body
    gc.setFill(runwayBody);
    gc.fillRect(cw * 0.1, ch * 0.43, cw * 0.8, ch * 0.1);
    gc.setStroke(strokeColour);
    gc.setLineWidth(0.25);
    gc.strokeRect(cw * 0.1, ch * 0.43, cw * 0.8, ch * 0.1);

    //Dashed centre line, lower dash number to get more dashes in the line.
    gc.setStroke(runwayLines);
    gc.setLineWidth(1);
    int dashes = 9;
    if (runway.getTora() >= 3250) {
      dashes = 5;
    }
    gc.setLineDashes(dashes);
    gc.strokeLine(cw * 0.25, ch * 0.48, cw * 0.75, ch * 0.48);
    gc.setLineDashes(0);

    //Starting lines at each end of the runway
    gc.setStroke(runwayLines);
    gc.setLineWidth(2);
    var tempA = cw * 0.11;
    var tempB = ch * 0.44;
    var tempC = cw * 0.16;
    var tempD = ch * 0.44;
    while (tempB < ch * 0.52) {
      gc.strokeLine(tempA, tempB, tempC, tempD);
      tempB += 5;
      tempD += 5;
    }

    var tempE = cw * 0.84;
    var tempF = cw * 0.89;
    tempB = ch * 0.44;
    tempD = ch * 0.44;
    while (tempB < ch * 0.52) {
      gc.strokeLine(tempE, tempB, tempF, tempD);
      tempB += 5;
      tempD += 5;
    }

    //Show the runway designators in the 2D view
    gc.setLineWidth(1);
    gc.setFill(runwayLines);
    if (leftT) {
      gc.fillText(formattedDes[1], cw * 0.17, ch * 0.48);
      gc.fillText(formattedDes[0], cw * 0.8, ch * 0.48);
    } else {
      gc.fillText(formattedDes[0], cw * 0.17, ch * 0.48);
      gc.fillText(formattedDes[1], cw * 0.8, ch * 0.48);
    }

    //A variable threshold parameter
    var thresh = 0.1 + 0.8 * (runway.getThreshold() / runway.getLda());
    var opthresh = 0.9 - 0.8 * (runway.getThreshold() / runway.getLda());
    var forLDA = thresh;

    if (leftT) {
      forLDA = opthresh;
    }

    //Add a threshold if it exists
    if (threshold != 0) {
      gc.setLineWidth(1.5);
      gc.setLineDashes(6);
      gc.setStroke(thresholdColour);
      gc.setFill(thresholdColour);
      if (leftT) {
        gc.strokeLine(cw * opthresh, ch * 0.41, cw * opthresh, ch * 0.57);
        gc.fillText(threshold + "m", cw * (opthresh - 0.03), ch * 0.6);
      } else {
        gc.strokeLine(cw * thresh, ch * 0.41, cw * thresh, ch * 0.57);
        //Write metrics next to threshold
        gc.fillText(threshold + "m", cw * (thresh - 0.03), ch * 0.6);
      }
      gc.setLineDashes(0);
    }

    gc.setFont(new Font(16));
    gc.setLineWidth(1);
    double stopwayS = 0.9;
    double stopwayW = 0.07;
    double boxHeight = 0.1;
    double stopwayPar = 0.9;
    if (leftT) {
      stopwayPar = 0.1;
    }

    //Stopway on runway end
    gc.setStroke(stopwayColour);
    gc.setFill(stopwayColour);
    //Write metrics over threshold and draw rectangles
    if (leftT) {

      if (otherRunway != null && otherRunway.getStopway() != 0) {
        gc.strokeRect(cw * stopwayS, ch * 0.43, cw * stopwayW, ch * boxHeight);
      }
      if (otherRunway != null) {
        gc.fillText("Stopway" + "\n" + otherRunway.getStopway() + "m", cw * (stopwayS + 0.01), ch * 0.38);
      }

      if (stopway != 0) {
        stopwayPar -= stopwayW;
        gc.strokeRect(cw * 0.03, ch * 0.43, cw * stopwayW, ch * boxHeight);
      }
      gc.fillText("Stopway" + "\n" + stopway + "m", cw * 0.03, ch * 0.38);

    } else {
      if (stopway != 0) {
        stopwayPar += stopwayW;
        gc.strokeRect(cw * stopwayS, ch * 0.43, cw * stopwayW, ch * boxHeight);
      }
      gc.fillText("Stopway" + "\n" + stopway + "m", cw * (stopwayS + 0.01), ch * 0.38);

      if (otherRunway != null && otherRunway.getStopway() != 0) {
        gc.strokeRect(cw * 0.02, ch * 0.43, cw * stopwayW, ch * boxHeight);
      }
      if (otherRunway != null) {
        gc.fillText("Stopway" + "\n" + otherRunway.getStopway() + "m", cw * 0.03, ch * 0.38);
      }

    }

    //Clearway on runway end
    double clearwayPar = 0.9;
    double rectS = 0.9;
    double rectH = 0.425;
    double rectHeight = 0.11;
    double txtHeight = 0.58;
    gc.setStroke(clearwayColour);
    gc.setFill(clearwayColour);
    //Includes setting the clearway for the other runway
    if (leftT) {
      clearwayPar = 0.1;
      if (otherRunway != null && otherRunway.getClearway() != 0) {
        if (otherRunway.getClearway() < otherRunway.getStopway()) {
          gc.strokeRect(cw * rectS, ch * rectH, cw * 0.05, ch * rectHeight);
        } else if (otherRunway.getClearway() > otherRunway.getStopway()) {
          gc.strokeRect(cw * rectS, ch * rectH, cw * 0.09, ch * rectHeight);
        } else {
          gc.strokeRect(cw * rectS, ch * rectH, cw * 0.07, ch * rectHeight);
        }
      }
      if (otherRunway != null) {
        gc.fillText("Clearway" + "\n" + otherRunway.getClearway() + "m", cw * 0.91, ch * txtHeight);
      }
    } else {
      double temp = 0.01;
      if (otherRunway != null && otherRunway.getClearway() != 0) {
        if (otherRunway.getClearway() < otherRunway.getStopway()) {
          gc.strokeRect(cw * 0.04, ch * rectH, cw * 0.06, ch * rectHeight);
          temp = 0.04;
        } else if (otherRunway.getClearway() > otherRunway.getStopway()) {
          gc.strokeRect(cw * 0.01, ch * rectH, cw * 0.09, ch * rectHeight);
          temp = 0.01;
        } else {
          gc.strokeRect(cw * 0.02, ch * rectH, cw * 0.07, ch * rectHeight);
          temp = 0.02;
        }
      }
      if (otherRunway != null) {
        gc.fillText("Clearway" + "\n" + otherRunway.getClearway() + "m", cw * temp, ch * txtHeight);
      }
    }

    //For this runway, if the clearway exists
    if (clearway != 0) {
      if (leftT) {
        if (clearway < stopway) {
          gc.strokeRect(cw * 0.04, ch * rectH, cw * 0.06, ch * rectHeight);
          clearwayPar -= 0.06;
        } else if (clearway > stopway) {
          gc.strokeRect(cw * 0.01, ch * rectH, cw * 0.09, ch * rectHeight);
          clearwayPar -= 0.09;
        } else {
          gc.strokeRect(cw * 0.02, ch * rectH, cw * 0.07, ch * rectHeight);
          clearwayPar -= 0.07;
        }
        //Write metrics over threshold
        gc.fillText("Clearway" + "\n" + clearway + "m", cw * 0.02, ch * txtHeight);
      } else {
        if (clearway < stopway) {
          gc.strokeRect(cw * rectS, ch * rectH, cw * 0.05, ch * rectHeight);
          clearwayPar += 0.05;
        } else if (clearway > stopway) {
          gc.strokeRect(cw * rectS, ch * rectH, cw * 0.09, ch * rectHeight);
          clearwayPar += 0.09;
        } else {
          gc.strokeRect(cw * rectS, ch * rectH, cw * 0.07, ch * rectHeight);
          clearwayPar += 0.07;
        }
        //Write metrics over threshold
        gc.fillText("Clearway" + "\n" + clearway + "m", cw * 0.91, ch * txtHeight);
      }
    } else {
      if (leftT) {
        gc.fillText("Clearway" + "\n" + clearway + "m", cw * 0.02, ch * txtHeight);
      } else {
        gc.fillText("Clearway" + "\n" + clearway + "m", cw * 0.91, ch * txtHeight);
      }
    }


    gc.setFont(new Font(20));

    //Add a marking about the Cleared and Graded area
    gc.setLineWidth(1);
    gc.setFill(accent1);
    gc.setStroke(accent1);
    gc.fillText("Cleared and Graded Area", cw * 0.40, ch * 0.75);
    gc.strokeText("Cleared and Graded Area", cw * 0.40, ch * 0.75);

    //Draw the direction arrow
    //Height of the arrow
    double arrowH = 0.15;
    if (leftT) {
      drawDirectionArrow(gc, cw * 0.7, ch * arrowH, cw * 0.9, 10.0, false);
      gc.fillText("Take-Off/Landing", cw * 0.75, ch * arrowH - 5, 160);
    } else {
      drawDirectionArrow(gc, cw * 0.1, ch * arrowH, cw * 0.3, 10.0, true);
      gc.fillText("Take-Off/Landing", cw * 0.1, ch * arrowH - 5, 160);
    }

    //Runway distances
    gc.setLineWidth(1.5);
    gc.setStroke(strokeColour);
    gc.setFill(strokeColour);
    gc.setFont(new Font(15));
    gc.setLineDashes(0);

    //The boolean that will determine the distance line's location
    var towards = runway.isCalcTowards();

    //Location of the obstacle as a ratio
    double obsLocation;
    if (leftT) {
      var m = 0.9 - (runway.getObsDistFromThresh() + runway.getThreshold()) / runway.getTora();

      while (m < 0.1) {
        m += 0.1;
      }

      obsLocation = cw * m;

    } else {

      var n = 0.1 + (runway.getObsDistFromThresh() + runway.getThreshold()) / runway.getTora();

      while (n > 0.9) {
        n -= 0.1;
      }

      obsLocation = cw * n;

    }

    boolean isRedeclared = runway.getAsda() != runway.getCasda() || runway.getTora() != runway.getCtora() || runway.getToda()
      != runway.getCtoda() || runway.getLda() != runway.getClda();

    //Displays LDA value and adjusts length to current LDA. The start of the line also depends on the threshold of the runway.
    if (runway.getClda() < 0) {
      logger.error("Negative value, not drawing LDA");
    } else {

      //Main height
      var height = 0.41;

      //Text name
      var label = "LDA= " + runway.getClda() + "m";

      if (runway.getCurrentObstacle() == null || !isRedeclared) {

        if (leftT) {
          drawTDistance(gc, label, cw * 0.1, height, cw * forLDA, ch);
        } else {
          drawTDistance(gc, label, cw * forLDA, height, cw * 0.9, ch);
        }

      } else {

        if (leftT) {

          if (towards) {
            drawTDistance(gc, label, obsLocation + 20, height, cw * forLDA, ch);
          } else {
            drawTDistance(gc, label, cw * 0.1, height, obsLocation - 20, ch);
          }

        } else {

          if (towards) {
            drawTDistance(gc, label, cw * forLDA, height, obsLocation - 20, ch);
          } else {
            drawTDistance(gc, label, obsLocation + 20, height, cw * 0.9, ch);
          }

        }
      }
    }

    //Displays TORA value and adjusts length to current TORA
    if (runway.getCtora() < 0) {
      logger.error("Negative value, not drawing TORA");
    } else {

      // Height of the line
      var height = 0.38;

      //Label for the distance line
      var label = "TORA= " + runway.getCtora() + "m";

      if (runway.getCurrentObstacle() == null || !isRedeclared) {

        drawTDistance(gc, label, cw * 0.1, height, cw * 0.9, ch);

      } else {

        if (leftT) {
          if (towards) {
            drawTDistance(gc, label, obsLocation + 20, height, cw * 0.9, ch);
          } else {
            drawTDistance(gc, label, cw * 0.1, height, obsLocation - 20, ch);
          }
        } else {
          if (towards) {
            drawTDistance(gc, label, cw * 0.1, height, obsLocation - 20, ch);
          } else {
            drawTDistance(gc, label, obsLocation + 20, height, cw * 0.9, ch);
          }
        }

      }
    }

    //Displays ASDA value and adjusts length to current ASDA
    if (runway.getCasda() < 0) {
      logger.error("Negative value, not drawing ASDA");
    } else {

      //Height of the line
      var height = 0.35;

      //Label to go above the line
      var label = "ASDA= " + runway.getCasda() + "m";

      if (runway.getCurrentObstacle() == null || !isRedeclared) {

        if (leftT) {
          drawTDistance(gc, label, cw * stopwayPar, height, cw * 0.9, ch);
        } else {
          drawTDistance(gc, label, cw * stopwayPar, height, cw * 0.1, ch);
        }

      } else {

        if (leftT) {
          if (towards) {
            drawTDistance(gc, label, obsLocation + 20, height, cw * 0.9, ch);
          } else {
            drawTDistance(gc, label, cw * stopwayPar, height, obsLocation - 20, ch);
          }
        } else {
          if (towards) {
            drawTDistance(gc, label, cw * 0.1, height, obsLocation - 20, ch);
          } else {
            drawTDistance(gc, label, obsLocation + 20, height, cw * stopwayPar, ch);
          }
        }

      }
    }

    //Displays TODA value and adjusts length to current TODA
    if (runway.getCtoda() < 0) {
      logger.error("Negative value, not drawing TODA");
    } else {

      //The height of the line
      var height = 0.32;

      //Label above the line
      var label = "TODA= " + runway.getCtoda() + "m";

      if (runway.getCurrentObstacle() == null || !isRedeclared) {

        if (leftT) {
          drawTDistance(gc, label, cw * clearwayPar, height, cw * 0.9, ch);
        } else {
          drawTDistance(gc, label, cw * clearwayPar, height, cw * 0.1, ch);
        }

      } else {

        if (leftT) {
          if (towards) {
            drawTDistance(gc, label, obsLocation + 20, height, cw * 0.9, ch);
          } else {
            drawTDistance(gc, label, cw * clearwayPar, height, obsLocation - 20, ch);
          }
        } else {
          if (towards) {
            drawTDistance(gc, label, cw * 0.1, height, obsLocation - 20, ch);
          } else {
            drawTDistance(gc, label, obsLocation + 20, height, cw * clearwayPar, ch);
          }
        }

      }
    }

    gc.setFont(new Font(20));

    //Picked an obstacle
    if (runway.getCurrentObstacle() != null) {

      if (runway.getObsDistFromThresh() < -60 || Math.abs(runway.getCurrentObstacle().getCenterline()) > 75) {

        logger.info("Obstacle does not affect runway, not displaying...");

      } else {

        logger.info("Obstacle detected, showing on view.");

        gc.setFill(obstacleColour);
        gc.setStroke(obstacleColour);

        double offsetY = -runway.getCurrentObstacle().getCenterline() / 1000;

        //Draw obstacle as a dot to pinpoint location
        if (leftT) {
          gc.strokeOval(obsLocation, ch * (0.475 + offsetY), 10, 10);
          gc.fillOval(obsLocation, ch * (0.475 + offsetY), 10, 10);
        } else {
          gc.strokeOval(obsLocation, ch * (0.475 + offsetY), 10, 10);
          gc.fillOval(obsLocation, ch * (0.475 + offsetY), 10, 10);
        }
      }
    }
  }

  /**
   * Draws distance line for top-down view
   *
   * @param gc    Graphics Context
   * @param label The text displayed on top of the line
   * @param x1    The start point of the line
   * @param y1    The height of the line
   * @param x2    The end point of the line
   * @param ch    The height of the canvas
   */
  private void drawTDistance(GraphicsContext gc, String label, double x1, double y1, double x2, double ch) {

    //Error handling
    if (x2 < x1) {
      var temp = x1;
      x1 = x2;
      x2 = temp;
    }

    var heightM = ch * y1;

    var heightU = ch * (y1 - 0.01);

    var heightD = ch * (y1 + 0.01);

    var length = x2 - x1;

    gc.beginPath();

    //Line vert1
    gc.moveTo(x1, heightU);
    gc.lineTo(x1, heightD);

    //Line vert2
    gc.moveTo(x2, heightU);
    gc.lineTo(x2, heightD);

    //Distance Line
    gc.moveTo(x1, heightM);
    gc.lineTo(x2, heightM);

    // Stroke everything
    gc.stroke();

    // Add header/distance name
    gc.fillText(label, x1 + length / 2, heightU);
  }


  /**
   * Fetches the other runway object if it exists.
   *
   * @param des the designator of the runway to be searched
   * @return
   */
  private Runway fetchRunway(String des) {
    Runway otherRunway = null;
    var found = false;
    var i = 0;
    while (!found && i < state.runwayListProperty().size()) {
      var runway = state.runwayListProperty().get(i);
      if (runway.getDesignator().equals(des)) {
        otherRunway = runway;
        found = true;
      }
      i++;
    }
    return otherRunway;
  }

  /**
   * Checks if the threshold of the matching runway is smaller than the selected runway.
   * If yes, returns true. Otherwise, it returns false.
   *
   * @param otherDesignator the runway designator of the other side
   * @return a boolean that determines if the designator should be switched or not
   */
  private boolean checkThresh(String otherDesignator) {
    boolean leftT = false;
    var found = false;
    var i = 0;
    while (!found && i < state.runwayListProperty().size()) {
      var runway = state.runwayListProperty().get(i);
      if (runway.getDesignator().equals(otherDesignator)) {
        if (runway.getThreshold() < this.runway.getThreshold()) {
          leftT = true;
        } else {
          leftT = false;
        }
        found = true;
      }
      i++;
    }
    return leftT;
  }


  /**
   * Draws a parallel direction arrow on the canvas
   *
   * @param gc        the graphics context
   * @param x1        the x coordinate of the start of the arrow
   * @param y1        the height of the arrow as a y coordinate
   * @param x2        the x coordinate of the end of the arrow
   * @param arrowSize the size of the arrow head
   * @param right     determines whether the arrow is pointing left (false) or right (true)
   */
  private void drawDirectionArrow(GraphicsContext gc, double x1, double y1, double x2, double arrowSize, boolean right) {

    var arrowColour = Color.valueOf(colours[9]);
    gc.setStroke(arrowColour);
    gc.setFill(arrowColour);

    gc.strokeLine(x1, y1, x2, y1);

    if (right) {
      var xs = new double[]{x2, x2 + arrowSize, x2};
      var ys = new double[]{y1 + arrowSize, y1, y1 - arrowSize};
      gc.fillPolygon(xs, ys, 3);
    } else {
      var xs = new double[]{x1, x1 - arrowSize, x1};
      var ys = new double[]{y1 + arrowSize, y1, y1 - arrowSize};
      gc.fillPolygon(xs, ys, 3);
    }

  }

  /**
   * Give a format to designators
   *
   * @param designator
   * @return
   */
  private String[] formatDesignators(String designator) {

    //Throw error if designator is not of the correct format (e.g. correct formats are 09L, 26)
    if (designator.length() != 3 && designator.length() != 2) {
      logger.error("Bad format for designator.");
    }

    //The runway designator of the other side
    String oppositeDes;

    //Designator number
    int numDes = 0;
    try {
      if (designator.length() == 3) {
        numDes = Integer.parseInt(designator.substring(0, 2));
      } else {
        numDes = Integer.parseInt(designator);
      }
    } catch (NumberFormatException e) {
      logger.error("Bad number format for designator.");
    }

    //Get the other sides designator by adding 180 and modding by 360
    int newNumDes = ((180 + numDes * 10) % 360) / 10;
    String desSide = "";
    if (designator.length() == 3) {
      //Change character for other designator
      if (designator.charAt(2) == 'L') {
        desSide = "R";
      } else if (designator.charAt(2) == 'R') {
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
