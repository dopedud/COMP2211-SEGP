package uk.ac.soton.comp2211.team33.components;

import javafx.beans.property.SimpleObjectProperty;
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
import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;

/**
 * The Visualisation class is a custom component that renders the 2D top-down and side-on view.
 *
 * @author Brian (dal1g21@soton.ac.uk), Jackson (jl14u21@soton.ac.uk)
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
   * The offset of the rotation in degrees - used to correctly rotate the vis to match compass heading depending on which side the threshold is
   * If the threshold is on the left, the offset is 0 degrees, if it is on the right, the offset is 180 degrees.
   */
  private int compassOffset = 0;

  /**
   * The current transform of the visualisation.
   */
  private SimpleObjectProperty<Affine> transform = new SimpleObjectProperty<>(new Affine());

  public VisPanel(Airport state, Runway runway) {
    this.runway = runway;
    this.state = state;

    ProjectHelpers.renderRoot("/components/VisPanel.fxml", this, this);

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
    state.runwayListProperty().addListener((ov, oldV, newV) -> draw());
    runway.ctoraProperty().addListener(ignored -> draw());
    runway.ctodaProperty().addListener(ignored -> draw());
    runway.casdaProperty().addListener(ignored -> draw());
    runway.cldaProperty().addListener(ignored -> draw());
    runway.cresaProperty().addListener(ignored -> draw());

    transform.addListener(ignored -> draw());

    draw();
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
   * Get the centre of the canvas as a coordinate
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
   * Function for drawing the 2D views
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

    // TODO: disable rotating

    gc.setTransform(transform.get());

    gc.setFont(new Font(20));
    gc.setTextBaseline(VPos.CENTER);
    gc.setTextAlign(TextAlignment.CENTER);

    double cw = this.getWidth();
    double ch = this.getHeight();
    double layerDepth = ch / 40;
    double grassStartY = (double) 2 / 3 * ch;

    // Sky

    gc.setFill(Color.rgb(56, 179, 232));
    gc.fillRect(0, 0, cw, ch);

    // Grass

    gc.setFill(Color.rgb(73, 145, 99));
    gc.fillRect(0, grassStartY, cw, layerDepth);

    // Dust

    gc.setFill(Color.rgb(82, 50, 38));
    gc.fillRect(0, grassStartY + layerDepth, cw, ch);

    // Overall runway properties

    double runwayLengthPx = cw / 1.5;
    double runwayStartX = cw / 2 - runwayLengthPx / 2;
    double runwayStartY = grassStartY - layerDepth;
    double runwayEndX = runwayStartX + runwayLengthPx;
    double runwayEndY = grassStartY;
    double toda = runway.getToda();

    // Print designator

    String[] designators = formatDesignators(runway.getDesignator());
    String designator = designators[0];
    String oppositeDesignator = designators[1];
    double designatorStartY = runwayStartY + layerDepth / 2;

    //The opposing side's designator formatted into a string of no spaces, ready to use for searching
    var otherDesignator = designators[1].replaceAll("[\r\n]+", "").replaceAll(" ", "");

    var otherRunway = fetchRunway(otherDesignator);

    gc.setFill(Color.BLACK);
    gc.fillText(designator, runwayStartX - 60, designatorStartY - 10);
    gc.fillText(oppositeDesignator, runwayEndX + 60, designatorStartY - 10);

    // Draw runway body

    double tora = runway.getTora();
    double toraLengthPx = (tora / toda) * runwayLengthPx;
    double runwayBodyEndX = runwayStartX + toraLengthPx;

    gc.setFill(Color.rgb(50, 50, 50));
    gc.fillRect(runwayStartX, runwayStartY, toraLengthPx, layerDepth);

    // Draw clearway

    double clearway = runway.getClearway();
    double clearwayLengthPx = (clearway / toda) * runwayLengthPx;
    Color clearwayColor = Color.RED;

    gc.setFill(clearwayColor);
    gc.fillRect(runwayBodyEndX, runwayStartY, clearwayLengthPx, layerDepth);

    // Draw stopway

    double stopway = runway.getStopway();
    double stopwayLengthPx = (stopway / toda) * runwayLengthPx;
    Color stopwayColor = Color.rgb(240, 173, 58);

    gc.setFill(stopwayColor);
    gc.fillRect(runwayBodyEndX, runwayStartY, stopwayLengthPx, layerDepth);

    // Draw ctoda legend

    double ctoda = runway.getCtoda();
    double ctodaLengthPx = (ctoda / toda) * runwayLengthPx;

    if (0 < ctoda) {
      drawDistanceLegend(gc, "TODA= " + ctoda + " m", Color.BLACK,
        runwayEndX - ctodaLengthPx, runwayEndY - 500, ctodaLengthPx, 500);
    }

    // Draw ctora legend

    double ctora = runway.getCtora();
    double ctoraLengthPx = (ctora / toda) * runwayLengthPx;

    if (0 < ctora) {
      drawDistanceLegend(gc, "TORA= " + tora + " m", Color.BLACK,
        runwayBodyEndX - ctoraLengthPx, runwayEndY - 200, ctoraLengthPx, 200);
    }

    // Draw clda legend

    double clda = runway.getClda();
    double cldaLengthPx = (clda / toda) * runwayLengthPx;

    if (0 < clda) {
      drawDistanceLegend(gc, "LDA= " + clda + " m", Color.BLACK, runwayBodyEndX - cldaLengthPx, runwayEndY - 300, cldaLengthPx, 300);
    }

    // Draw casda

    double casda = runway.getCasda();
    double casdaLengthPx = (casda / toda) * runwayLengthPx;

    if (0 < casda) {
      drawDistanceLegend(gc, "ASDA= " + casda + " m", Color.BLACK, runwayBodyEndX + stopwayLengthPx - casdaLengthPx,
        runwayEndY - 400, casdaLengthPx, 400);
    }

    // Draw clearway legend

    drawDistanceLegend(gc, "Clearway= " + clearway + " m", clearwayColor, runwayBodyEndX, runwayEndY - 250, clearwayLengthPx, 250);

    // Draw stopway legend

    drawDistanceLegend(gc, "Stopway= " + stopway + " m", stopwayColor, runwayBodyEndX, runwayEndY - 200, stopwayLengthPx, 200);

    // Draw threshold

    double threshold = runway.getThreshold();
    double thresholdStartX = (threshold / toda) * runwayLengthPx + runwayStartX;
    double thresholdLineLengthPx = 100;
    double thresholdEndY = runwayStartY + thresholdLineLengthPx;

    gc.beginPath();
    gc.setStroke(Color.RED);
    gc.setLineDashes(5);
    gc.moveTo(thresholdStartX, runwayStartY);
    gc.lineTo(thresholdStartX, thresholdEndY);
    gc.stroke();

    gc.setFill(Color.RED);
    gc.fillText("Threshold= " + threshold + " m", thresholdStartX, thresholdEndY + 20);

    // Draw obstacle

    Obstacle obstacle = runway.getCurrentObstacle();
    if (obstacle != null) {
      double mHeightToPx = 4;
      double obstacleDistance = runway.getObsDistFromThresh();
      double obstacleLengthPx = (obstacle.getLength() / toda) * runwayLengthPx;
      double trueObstacleHeightPx = (obstacle.getHeight() / obstacle.getLength()) * obstacleLengthPx;
      double scaledObstacleHeightPx = obstacle.getHeight() * mHeightToPx;

      double obstacleStartX = (obstacleDistance / toda) * runwayLengthPx + thresholdStartX;
      double obstacleStartY = runwayStartY - scaledObstacleHeightPx;

      gc.setFill(Color.ORANGE);
      gc.fillRect(obstacleStartX, obstacleStartY, obstacleLengthPx, scaledObstacleHeightPx);

      // Draw landing slope

      gc.beginPath();
      gc.setStroke(Color.BLUE);
      gc.setLineDashes(5);
      gc.moveTo(obstacleStartX, obstacleStartY - (obstacleLengthPx * scaledObstacleHeightPx) / (50 * trueObstacleHeightPx - obstacleLengthPx));
      gc.lineTo(obstacleStartX + trueObstacleHeightPx * 50, runwayStartY);
      gc.stroke();

      drawDistanceLegend(gc, "Slope= " + obstacle.getHeight() * 50 + " m", Color.BLUE, obstacleStartX, runwayStartY - 200, trueObstacleHeightPx * 50, 200);

      // Draw RESA legend

      double obstacleEndX = obstacleStartX + obstacleLengthPx;
      double resa = runway.getResa();
      double resaLengthPx = (resa / toda) * runwayLengthPx;

      drawDistanceLegend(gc, "RESA= " + resa + " m", Color.RED, obstacleEndX, runwayEndY - 150, resaLengthPx, 150);

      // Draw safety distance

      double safetyDistanceStartX = Math.max(obstacleEndX + resaLengthPx, obstacleStartX + trueObstacleHeightPx * 50);
      double safetyDistancePx = 60 / toda * runwayLengthPx;
      drawDistanceLegend(gc, "Safety distance", Color.GREEN, safetyDistanceStartX, runwayEndY - 150, safetyDistancePx, 150);
    }
  }

  /**
   * Draws the distance indicators for the runway when called
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

//    double rotationAngle = 0;
//
//    //Rotation
//    Rotate r = new Rotate(rotationAngle, cw * 0.5, ch * 0.5);
//    gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());

//    if(rotationAngle != 0 || rotationAngle != 90 || rotationAngle != 180 || rotationAngle != 270
//      || rotationAngle != 360){
//      gc.scale(0.8,0.8);
//    }

    gc.save();

    // Clears the canvas, clearing a large area so that it is displayed correctly when zoomed out
    gc.clearRect(-5000, -5000, 10000, 10000);


    //Surrounding area
    gc.setFill(Color.valueOf("#7CB342"));
    gc.fillRect(-5000, -5000, 10000, 10000);

    //X and Y coordinates
    double[] xCoord = {0.0, cw * 0.18, cw * 0.27, cw * 0.727, cw * 0.818, cw, cw, cw * 0.818, cw * 0.727, cw * 0.27, cw * 0.18, 0.0};
    double[] yCoord = {ch * 0.3, ch * 0.3, ch * 0.225, ch * 0.225, ch * 0.3, ch * 0.3, ch * 0.7, ch * 0.7, ch * 0.775, ch * 0.775, ch * 0.7, ch * 0.7};

    //The polygon around the runway
    gc.setFill(Color.valueOf("#0072C6"));
    gc.fillPolygon(xCoord, yCoord, 12);
    gc.setStroke(Color.BLACK);
    gc.strokePolygon(xCoord, yCoord, 12);

    //Runway body
    gc.setFill(Color.valueOf("#242424"));
    gc.fillRect(cw * 0.1, ch * 0.43, cw * 0.8, ch * 0.1);
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(0.25);
    gc.strokeRect(cw * 0.1, ch * 0.43, cw * 0.8, ch * 0.1);

    //Dashed centre line, lower dash number to get more dashes in the line.
    gc.setStroke(Color.valueOf("#ffffff"));
    gc.setLineWidth(1);
    int dashes = 9;
    if (runway.getTora() >= 3250) {
      dashes = 5;
    }
    gc.setLineDashes(dashes);
    gc.strokeLine(cw * 0.25, ch * 0.48, cw * 0.75, ch * 0.48);
    gc.setLineDashes(0);

    //Starting lines at each end of the runway
    gc.setStroke(Color.valueOf("#ffffff"));
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
    gc.setFill(Color.valueOf("#ffffff"));
    if (leftT) {
      gc.fillText(formattedDes[1], cw * 0.17, ch * 0.48);
      gc.fillText(formattedDes[0], cw * 0.8, ch * 0.48);
    } else {
      gc.fillText(formattedDes[0], cw * 0.17, ch * 0.48);
      gc.fillText(formattedDes[1], cw * 0.8, ch * 0.48);
    }

    //A variable threshold parameter
    var thresh = 0.1 + 0.8 * (runway.getThreshold() / runway.getTora());
    var opthresh = 0.9 - 0.8 * (runway.getThreshold() / runway.getTora());
    var forLDA = thresh;
    if (leftT) {
      forLDA = opthresh;
    }
    //Add a threshold if it exists
    if (threshold != 0) {
      gc.setLineWidth(1.5);
      gc.setLineDashes(6);
      gc.setStroke(Color.valueOf("#FF5733"));
      gc.setFill(Color.valueOf("#FF5733"));
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
    gc.setStroke(Color.valueOf("#f7ff00"));
    gc.setFill(Color.valueOf("#f7ff00"));
    //Write metrics over threshold and draw rectangles
    if (leftT) {
      if (otherRunway != null && otherRunway.getStopway() != 0) {
        gc.strokeRect(cw * stopwayS, ch * 0.43, cw * stopwayW, ch * boxHeight);
        gc.fillText("Stopway" + "\n" + otherRunway.getStopway() + "m", cw * (stopwayS + 0.01), ch * 0.38);
      }
      if (stopway != 0) {
        stopwayPar -= stopwayW;
        gc.strokeRect(cw * 0.03, ch * 0.43, cw * stopwayW, ch * boxHeight);
        gc.fillText("Stopway" + "\n" + stopway + "m", cw * 0.03, ch * 0.38);
      }
    } else {
      if (stopway != 0) {
        stopwayPar += stopwayW;
        gc.strokeRect(cw * stopwayS, ch * 0.43, cw * stopwayW, ch * boxHeight);
        gc.fillText("Stopway" + "\n" + stopway + "m", cw * (stopwayS + 0.01), ch * 0.38);
      }
      if (otherRunway != null && otherRunway.getStopway() != 0) {
        gc.strokeRect(cw * 0.02, ch * 0.43, cw * stopwayW, ch * boxHeight);
        gc.fillText("Stopway" + "\n" + otherRunway.getStopway() + "m", cw * 0.03, ch * 0.38);

      }
    }

    //Clearway on runway end
    double clearwayPar = 0.9;
    double rectS = 0.9;
    double rectH = 0.425;
    double rectHeight = 0.11;
    //Includes setting the clearway for the other runway
    if (leftT) {
      clearwayPar = 0.1;
      if (otherRunway != null && otherRunway.getClearway() != 0) {
        gc.fillText("Clearway" + "\n" + otherRunway.getClearway() + "m", cw * 0.91, ch * 0.58);
        if (otherRunway.getClearway() < otherRunway.getStopway()) {
          gc.strokeRect(cw * rectS, ch * rectH, cw * 0.05, ch * rectHeight);
        } else if (otherRunway.getClearway() > otherRunway.getStopway()) {
          gc.strokeRect(cw * rectS, ch * rectH, cw * 0.09, ch * rectHeight);
        } else {
          gc.strokeRect(cw * rectS, ch * rectH, cw * 0.07, ch * rectHeight);
        }
      }
    } else {
      if (otherRunway != null && otherRunway.getClearway() != 0) {
        gc.fillText("Clearway" + "\n" + otherRunway.getClearway() + "m", cw * 0.07, ch * 0.57);
        if (otherRunway.getClearway() < otherRunway.getStopway()) {
          gc.strokeRect(cw * 0.04, ch * rectH, cw * 0.06, ch * rectHeight);
        } else if (otherRunway.getClearway() > otherRunway.getStopway()) {
          gc.strokeRect(cw * 0.01, ch * rectH, cw * 0.09, ch * rectHeight);
        } else {
          gc.strokeRect(cw * 0.02, ch * rectH, cw * 0.07, ch * rectHeight);
        }
      }
    }

    //For this runway, if the clearway exists
    if (clearway != 0) {
      gc.setStroke(Color.valueOf("#ff8b00"));
      gc.setFill(Color.valueOf("#ff8b00"));
      if (leftT) {
        //Write metrics over threshold
        gc.fillText("Clearway" + "\n" + clearway + "m", cw * 0.09, ch * 0.58);
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
        gc.fillText("Clearway" + "\n" + clearway + "m", cw * 0.91, ch * 0.58);
      }
    }
    gc.setFont(new Font(20));

    //Add a marking about the Cleared and Graded area
    gc.setLineWidth(1);
    gc.setFill(Color.valueOf("#FFD700"));
    gc.setStroke(Color.valueOf("#FFD700"));
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

    //Picked an object
//    if (runway.getCurrentObstacle() != null) {
//      gc.setFill(Color.RED);
//      gc.setFont(new Font(30));
//      gc.fillText(runway.getCurrentObstacle().getName(), cw * (0.1 + ((runway.getObsDistFromThresh() + runway.getThreshold()) / runway.getTora())), ch / 2);
//    }
    gc.setFont(new Font(20));

    //Runway distances
    gc.setLineWidth(1.5);
    gc.setLineDashes(5);
    gc.setStroke(Color.valueOf("#000000"));
    gc.setFill(Color.valueOf("#000000"));
    if (leftT) {
      gc.strokeLine(cw * 0.9, ch * 0.3, cw * 0.9, ch * 0.43);
    } else {
      gc.strokeLine(cw * 0.1, ch * 0.3, cw * 0.1, ch * 0.43);
    }
    gc.setFont(new Font(15));
    gc.setLineDashes(0);

    //Displays LDA value and adjusts length to current LDA. The start of the line also depends on the threshold of the runway.
    if (runway.getClda() < 0) {
      logger.error("Negative value, not drawing LDA");
    } else {
      //Main height
      var heightM = ch * 0.41;

      //Displaced upwards parameter
      var heightU = ch * 0.4;

      //Displaced downwards parameter
      var heightD = ch * 0.42;

      //ratio to multiply with to adjust length
      var ratio = runway.getLda() / runway.getClda();

      //ratio flipped around for the end of the distance line
      var revratio = runway.getClda() / runway.getLda();

      if (leftT) {
        if (runway.getObsDistFromThresh() > runway.getTora() / 2) {
          gc.strokeLine(cw * forLDA * revratio, heightM, cw * 0.1, heightM);
          gc.strokeLine(cw * 0.1, heightU, cw * 0.1, heightD);
          gc.strokeLine(cw * forLDA * revratio, heightU, cw * forLDA * revratio, heightD);
        } else {
          gc.strokeLine(cw * forLDA, heightM, cw * 0.1 * ratio, heightM);
          gc.strokeLine(cw * 0.1, heightU, cw * 0.1, heightD);
        }
      } else {
        if (runway.getObsDistFromThresh() < runway.getTora() / 2) {
          gc.strokeLine(cw * forLDA * ratio, heightM, cw * 0.9, heightM);
          gc.strokeLine(cw * 0.9, heightU, cw * 0.9, heightD);
          gc.strokeLine(cw * forLDA * ratio, heightU, cw * forLDA * ratio, heightD);
        } else {
          gc.strokeLine(cw * forLDA, heightM, cw * 0.9 * revratio, heightM);
          gc.strokeLine(cw * 0.9 * revratio, heightU, cw * 0.9 * revratio, heightD);
        }
      }
      gc.fillText("LDA= " + runway.getClda() + "m", cw * 0.45, heightU);
    }

    //Displays TORA value and adjusts length to current TORA
    if (runway.getCtora() < 0) {
      logger.error("Negative value, not drawing TORA");
    } else {
      double ratio = runway.getCtora() / runway.getTora();
      double start = 0.1;
      double end = 0.9;
      double heightU = 0.37;
      double heightM = 0.38;
      double heightD = 0.39;
      if (leftT) {
        gc.strokeLine(cw * end, ch * heightM, cw * start * (1 / ratio), ch * heightM);
        gc.strokeLine(cw * start * (1 / ratio), ch * heightU, cw * start * (1 / ratio), ch * heightD);
      } else {
        gc.strokeLine(cw * start, ch * heightM, cw * end * ratio, ch * heightM);
        gc.strokeLine(cw * end * ratio, ch * heightU, cw * end * ratio, ch * heightD);
      }
      gc.fillText("TORA= " + runway.getCtora() + "m", cw * 0.45, ch * heightU);
    }

    //Displays ASDA value and adjusts length to current ASDA
    if (runway.getCasda() < 0) {
      logger.error("Negative value, not drawing ASDA");
    } else {
      double heightU = ch * 0.34;
      double heightM = ch * 0.35;
      double heightD = ch * 0.36;
      double ratio = runway.getCasda() / runway.getAsda();
      if (leftT) {
        gc.strokeLine(cw * 0.9, heightM, cw * stopwayPar * (1 / ratio), heightM);
        gc.strokeLine(cw * stopwayPar * (1 / ratio), heightU, cw * stopwayPar * (1 / ratio), heightD);
      } else {
        gc.strokeLine(cw * 0.1, heightM, cw * stopwayPar * ratio, heightM);
        gc.strokeLine(cw * stopwayPar * ratio, heightU, cw * stopwayPar * ratio, heightD);
      }
      gc.fillText("ASDA= " + runway.getCasda() + "m", cw * 0.45, heightU);
    }

    //Displays TODA value and adjusts length to current TODA
    if (runway.getCtoda() < 0) {
      logger.error("Negative value, not drawing TODA");
    } else {
      double heightU = ch * 0.31;
      double heightM = ch * 0.32;
      double heightD = ch * 0.33;
      double ratio = runway.getCtora() / runway.getTora();
      if (leftT) {
        gc.strokeLine(cw * 0.9, heightM, cw * clearwayPar * (1 / ratio), heightM);
        gc.strokeLine(cw * clearwayPar * (1 / ratio), heightU, cw * clearwayPar * (1 / ratio), heightD);
      } else {
        gc.strokeLine(cw * 0.1, heightM, cw * clearwayPar * ratio, heightM);
        gc.strokeLine(cw * clearwayPar * ratio, heightU, cw * clearwayPar * ratio, heightD);
      }
      gc.fillText("TODA= " + runway.getCtoda() + "m", cw * 0.45, heightU);
    }

    gc.setFont(new Font(20));
  }


  /**
   * Fetches the other runway object if it exists
   *
   * @param des the designator of the runway to be searched.
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
   * @param otherDesignator the runway designator of the other side.
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
//    if (!found) {
//      logger.info("No matching designator found, keeping the selected runway to the left");
//    }
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

    var arrowColour = Color.valueOf("#000000");
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

//    double dx = x2 - x1, dy = y2 - y1;
//    double angle = Math.atan2(dy, dx);
//    int len = (int) Math.sqrt(dx * dx + dy * dy);
//
//    Transform transform = Transform.translate(x1, y1);
//    transform = transform.createConcatenation(Transform.rotate(rotation, canvas.getWidth() / 2.5, canvas.getHeight() / 2.5));
//    transform = transform.createConcatenation(Transform.rotate(Math.toDegrees(angle), 0, 0));
//    gc.setTransform(new Affine(transform));
//
//    gc.strokeLine(0, 0, len, 0);
//    gc.fillPolygon(new double[]{dx, dx - arrowSize, dx - arrowSize, dx}, new double[]{0, arrowSize, -arrowSize, 0}, 4);
//
//    gc.restore();
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
