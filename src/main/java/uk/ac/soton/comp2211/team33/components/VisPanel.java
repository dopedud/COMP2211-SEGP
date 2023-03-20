package uk.ac.soton.comp2211.team33.components;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
   * The current transform of the visualisation.
   */
  private SimpleObjectProperty<Affine> transform = new SimpleObjectProperty<>(new Affine());

  public VisPanel(Airport state, Runway runway) {
    this.runway = runway;
    this.state = state;

    ProjectHelpers.renderRoot("/components/VisPanel.fxml", this, this);

    canvas.widthProperty().addListener(ignored -> draw());
    canvas.heightProperty().addListener(ignored -> draw());

    canvas.setManaged(false);
    canvas.widthProperty().bind(widthProperty());
    canvas.heightProperty().bind(heightProperty());

    runway.currentObstacleProperty().addListener(ignored -> draw());
    runway.currentAircraftProperty().addListener(ignored -> draw());
    runway.obsDistFromThreshProperty().addListener(ignored -> draw());
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

    // Draw toda

    double toda = runway.getCtoda();

    drawDistanceLegend(gc, "Toda: " + toda + " m", Color.BLACK, runwayStartX, runwayEndY - 500, runwayLengthPx, 500);


    // Print designator

    String[] designators = formatDesignators(runway.getDesignator());
    String designator = designators[0];
    String oppositeDesignator = designators[1];
    double designatorStartY = runwayStartY + layerDepth / 2;

    gc.setFill(Color.BLACK);
    gc.fillText(designator, runwayStartX - 60, designatorStartY);
    gc.fillText(oppositeDesignator, runwayEndX + 60, designatorStartY);

    // Draw tora

    double tora = runway.getCtora();
    double toraLengthPx = (tora / toda) * runwayLengthPx;
    double runwayBodyEndX = runwayStartX + toraLengthPx;

    gc.setFill(Color.rgb(50, 50, 50));
    gc.fillRect(runwayStartX, runwayStartY, toraLengthPx, layerDepth);

    drawDistanceLegend(gc, "Tora: " + tora + " m", Color.BLACK, runwayStartX, runwayEndY - 200, toraLengthPx, 200);

    // Draw clearway

    double clearway = runway.getClearway();
    double clearwayLengthPx = (clearway / toda) * runwayLengthPx;
    Color clearwayColor = Color.RED;

    gc.setFill(clearwayColor);
    gc.fillRect(runwayBodyEndX, runwayStartY, clearwayLengthPx, layerDepth);

    drawDistanceLegend(gc, "Clearway: " + clearway + " m", clearwayColor, runwayBodyEndX, runwayEndY - 300, clearwayLengthPx, 300);

    // Draw stopway

    double stopway = runway.getStopway();
    double stopwayLengthPx = (stopway / toda) * runwayLengthPx;
    Color stopwayColor = Color.rgb(240, 173, 58);

    gc.setFill(stopwayColor);
    gc.fillRect(runwayBodyEndX, runwayStartY, stopwayLengthPx, layerDepth);

    drawDistanceLegend(gc, "Stopway: " + stopway + " m", stopwayColor, runwayBodyEndX, runwayEndY - 200, stopwayLengthPx, 200);

    // Draw asda

    double asda = runway.getCasda();
    double asdaLengthPx = toraLengthPx + stopwayLengthPx;

    drawDistanceLegend(gc, "Asda: " + asda + " m", Color.BLACK, runwayStartX, runwayEndY - 400, asdaLengthPx, 400);

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
    gc.fillText("Thres: " + threshold + " m", thresholdStartX, thresholdEndY + 20);

    // Draw lda

    double lda = runway.getClda();

    drawDistanceLegend(gc, "LDA: " + lda + " m", Color.BLACK, thresholdStartX, runwayEndY - 300, runwayBodyEndX - thresholdStartX, 300);

    // Draw obstacle

    Obstacle obstacle = runway.getCurrentObstacle();
    if (obstacle == null) {
      return;
    }

    double obstacleDistance = runway.getObsDistFromThresh();
    double obstacleLengthPx = (obstacle.getLength() / toda) * runwayLengthPx;
    double obstacleHeightPx = (obstacle.getHeight() / obstacle.getLength()) * obstacleLengthPx;

    double obstacleStartX = (obstacleDistance / toda) * runwayLengthPx + thresholdStartX;

    gc.setFill(Color.ORANGE);
    gc.fillRect(obstacleStartX, runwayStartY - obstacleHeightPx, obstacleLengthPx, obstacleHeightPx);
  }

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

    //This is a boolean that determines if the threshold should be switched
    boolean leftT = checkThresh(otherDesignator);

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

    //Some given TORA to be passed in, dummy value
    double toraL = 3550;




    // Clears the canvas, clearing a large area so that it is displayed correctly when zoomed out
    gc.clearRect(-5000, -5000, 10000, 10000);


    //Surrounding area
    gc.setFill(Color.valueOf("#7CB342"));
    gc.fillRect(-5000, -5000, 10000, 10000);

    double[] yCoord = {ch * 0.3, ch * 0.3, ch * 0.225, ch * 0.225, ch * 0.3, ch * 0.3, ch * 0.7, ch * 0.7, ch * 0.775, ch * 0.775, ch * 0.7, ch * 0.7};
    double[] xCoord = {0.0, cw * 0.18, cw * 0.27, cw * 0.727, cw * 0.818, cw, cw, cw * 0.818, cw * 0.727, cw * 0.27, cw * 0.18, 0.0};

    //The polygon around the runway
    gc.setFill(Color.valueOf("#0072C6"));
    gc.fillPolygon(xCoord, yCoord, 12);
    gc.setStroke(Color.BLACK);
    gc.strokePolygon(xCoord, yCoord, 12);

    //Runway body
    gc.setFill(Color.valueOf("#242424"));
    gc.fillRect(cw * 0.1, ch * 0.43, cw * 0.77, ch * 0.1);
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(0.25);
    gc.strokeRect(cw * 0.1, ch * 0.43, cw * 0.77, ch * 0.1);

    //Dashed centre line, lower dash number to get more dashes in the line.
    gc.setStroke(Color.valueOf("#ffffff"));
    gc.setLineWidth(1);
    int dashes = 9;
    if (toraL >= 3500) {
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

    var tempE = cw * 0.81;
    var tempF = cw * 0.86;
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
      gc.fillText(formattedDes[0], cw * 0.77, ch * 0.48);
    } else {
      gc.fillText(formattedDes[0], cw * 0.17, ch * 0.48);
      gc.fillText(formattedDes[1], cw * 0.77, ch * 0.48);
    }

    //Add a threshold if it exists
    if (threshold != 0) {
      gc.setLineWidth(1.5);
      gc.setLineDashes(6);
      gc.setStroke(Color.valueOf("#FF5733"));
      gc.setFill(Color.valueOf("#FF5733"));
      if (leftT){
        gc.strokeLine(cw * 0.8, ch * 0.41, cw * 0.8, ch * 0.57);
        gc.fillText(threshold + "m", cw * 0.18, ch * 0.6);
      } else {
        gc.strokeLine(cw * 0.20, ch * 0.41, cw * 0.20, ch * 0.57);
        //Write metrics next to threshold
        gc.fillText(threshold + "m", cw * 0.18, ch * 0.6);
      }
      gc.setLineDashes(0);
    }

    gc.setFont(new Font(16));
    gc.setLineWidth(1);
    double stopwayPar = 0.87 + 0.08;

    //Stopway on runway end
    // TODO: 17/03/2023 Abeed apply switching here
    if (stopway != 0) {
      gc.setLineDashes(0);
      gc.setStroke(Color.valueOf("#f7ff00"));
      gc.setFill(Color.valueOf("#f7ff00"));
      gc.strokeRect(cw * 0.87, ch * 0.43, cw * 0.08, ch * 0.1);
      //Write metrics over threshold
      gc.setLineDashes(0);
      gc.fillText("Stopway" + "\n" + stopway + "m", cw * 0.88, ch * 0.38);
    }

    //Clearway on runway end
    double clearwayPar = 0.87;
    // TODO: 17/03/2023 Abeed change this
    if (clearway != 0) {
      gc.setLineDashes(0);
      gc.setStroke(Color.valueOf("#ff8b00"));
      gc.setFill(Color.valueOf("#ff8b00"));
      if (clearway < stopway) {
        gc.strokeRect(cw * 0.87, ch * 0.425, cw * 0.05, ch * 0.11);
        clearwayPar += 0.05;
      } else if (clearway > stopway) {
        gc.strokeRect(cw * 0.87, ch * 0.425, cw * 0.1, ch * 0.11);
        clearwayPar += 0.1;
      } else {
        gc.strokeRect(cw * 0.87, ch * 0.425, cw * 0.08, ch * 0.11);
        clearwayPar += 0.08;
      }
      //Write metrics over threshold
      gc.setLineDashes(0);
      gc.fillText("Clearway" + "\n" + clearway + "m", cw * 0.88, ch * 0.58);
    }
    gc.setFont(new Font(20));

    //Add a marking about the Cleared and Graded area
    gc.setLineWidth(1);
    gc.setFill(Color.valueOf("#FFD700"));
    gc.setStroke(Color.valueOf("#FFD700"));
    gc.fillText("Cleared and Graded Area", cw * 0.40, ch * 0.75);
    gc.strokeText("Cleared and Graded Area", cw * 0.40, ch * 0.75);

    //Draw the direction arrow
    // TODO: 17/03/2023 Abeed change this to make the arrow point to the other way
    drawDirectionArrow(gc, cw * 0.1, ch * 0.1, cw * 0.3, ch * 0.1, 10.0, 0);
    gc.fillText("Take-Off/Landing", cw * 0.1, ch * 0.1 - 5, 160);

    //Picked an object
    if (runway.getCurrentObstacle() != null) {
      gc.setFill(Color.RED);
      gc.setFont(new Font(30));
      gc.fillText(runway.getCurrentObstacle().getName(), cw * (0.1 + ((runway.getObsDistFromThresh() + runway.getThreshold()) / runway.getTora())), ch / 2);
    }
    gc.setFont(new Font(20));

    //Runway distances
    // TODO: 17/03/2023 Abeed make the like start from the other end of the rectangle instead
    gc.setLineWidth(1.5);
    gc.setLineDashes(5);
    gc.setStroke(Color.valueOf("#000000"));
    gc.setFill(Color.valueOf("#000000"));
    gc.strokeLine(cw * 0.1, ch * 0.3, cw * 0.1, ch * 0.43);
    gc.setFont(new Font(15));
    gc.setLineDashes(0);

    //Displays LDA value and adjusts length to current LDA. The start of the line also depends on the threshold of the runway.
    // TODO: 17/03/2023 Abeed change this. I will do this one - Jackson
    if (runway.getClda() < 0) {
      logger.error("Negative value, not drawing LDA");
    } else {
      //Main height
      var heightA = ch * 0.41;

      //Displaced upwards parameter
      var heightB = ch * 0.4;

      //Displaced downwards parameter
      var heightC = ch * 0.42;

      //ratio to multiply with to adjust length
      var ratio = runway.getLda() / runway.getClda();

      //ratio flipped around for the end of the distance line
      var revratio = runway.getClda() / runway.getLda();

      if (runway.getClda() != runway.getLda() && threshold == 0) {
        //Changes LDA start point based on where the obstacle is located
        if (runway.getObsDistFromThresh() < runway.getTora() / 2) {
          gc.strokeLine(cw * 0.1 * ratio, heightA, cw * 0.87, heightA);
          gc.strokeLine(cw * 0.1 * ratio, heightB, cw * 0.1 * ratio, heightC);
          gc.strokeLine(cw * 0.87, heightB, cw * 0.87, heightC);
        } else {
          gc.strokeLine(cw * 0.1, heightA, cw * 0.87 * revratio, heightA);
          gc.strokeLine(cw * 0.1, heightB, cw * 0.1, heightC);
          gc.strokeLine(cw * 0.87 * revratio, heightB, cw * 0.87 * revratio, heightC);
        }
      } else if (runway.getClda() != runway.getLda() && threshold != 0) {
        if (runway.getObsDistFromThresh() < runway.getTora() / 2) {
          gc.strokeLine(cw * 0.2 * ratio, heightA, cw * 0.87, heightA);
          gc.strokeLine(cw * 0.2 * ratio, heightB, cw * 0.2 * ratio, heightC);
          gc.strokeLine(cw * 0.87, heightB, cw * 0.87, heightC);
        } else {
          gc.strokeLine(cw * 0.2, heightA, cw * 0.87 * revratio, heightA);
          gc.strokeLine(cw * 0.2, heightB, cw * 0.2, heightC);
          gc.strokeLine(cw * 0.87 * revratio, heightB, cw * 0.87 * revratio, heightC);
        }
      } else if (threshold != 0) {
        gc.strokeLine(cw * 0.2, heightA, cw * 0.87, heightA);
        gc.strokeLine(cw * 0.2, heightB, cw * 0.2, heightC);
        gc.strokeLine(cw * 0.87, heightB, cw * 0.87, heightC);
      } else {
        gc.strokeLine(cw * 0.1, heightA, cw * 0.87, heightA);
        gc.strokeLine(cw * 0.1, heightB, cw * 0.1, heightC);
        gc.strokeLine(cw * 0.87, heightB, cw * 0.87, heightC);
      }
      gc.fillText("LDA= " + runway.getClda() + "m", cw * 0.3, heightB);
    }

    //Displays TORA value and adjusts length to current TORA
    // TODO: 17/03/2023 Abeed change this
    if (runway.getCtora() < 0) {
      logger.error("Negative value, not drawing TORA");
    } else {
      if (runway.getCtora() != runway.getTora()) {
        gc.strokeLine(cw * 0.1, ch * 0.38, cw * 0.87 * (runway.getCtora() / runway.getTora()), ch * 0.38);
        gc.strokeLine(cw * 0.87 * (runway.getCtora() / runway.getTora()), ch * 0.37,
          cw * 0.87 * (runway.getCtora() / runway.getTora()), ch * 0.39);
      } else {
        gc.strokeLine(cw * 0.1, ch * 0.38, cw * 0.87, ch * 0.38);
        gc.strokeLine(cw * 0.87, ch * 0.37, cw * 0.87, ch * 0.39);
      }
      gc.fillText("TORA= " + runway.getCtora() + "m", cw * 0.3, ch * 0.37);
    }

    //Displays ASDA value and adjusts length to current ASDA
    // TODO: 17/03/2023 Abeed change this
    if (runway.getCasda() < 0) {
      logger.error("Negative value, not drawing ASDA");
    } else {
      var heightU = ch * 0.34;
      var heightM = ch * 0.35;
      var heightD = ch * 0.36;
      if (runway.getCasda() != runway.getAsda()) {
        gc.strokeLine(cw * 0.1, heightM, cw * stopwayPar * (runway.getCasda() / runway.getAsda()), heightM);
        gc.strokeLine(cw * stopwayPar * (runway.getCasda() / runway.getAsda()), heightU,
          cw * stopwayPar * (runway.getCasda() / runway.getAsda()), heightD);
      } else {
        gc.strokeLine(cw * 0.1, heightM, cw * stopwayPar, heightM);
        gc.strokeLine(cw * stopwayPar, heightU, cw * stopwayPar, heightD);
      }
      gc.fillText("ASDA= " + runway.getCasda() + "m", cw * 0.3, heightU);
    }

    //Displays TODA value and adjusts length to current TODA
    // TODO: 17/03/2023 Abeed change this
    if (runway.getCtoda() < 0) {
      logger.error("Negative value, not drawing TODA");
    } else {
      var heightU = ch * 0.31;
      var heightM = ch * 0.32;
      var heightD = ch * 0.33;
      if (runway.getCtoda() != runway.getToda()) {
        gc.strokeLine(cw * 0.1, heightM, cw * clearwayPar * (runway.getCtora() / runway.getTora()), heightM);
        gc.strokeLine(cw * clearwayPar * (runway.getCtora() / runway.getTora()),
          heightU, cw * clearwayPar * (runway.getCtora() / runway.getTora()), heightD);
      } else {
        gc.strokeLine(cw * 0.1, heightM, cw * clearwayPar, heightM);
        gc.strokeLine(cw * clearwayPar, heightU, cw * clearwayPar, heightD);
      }
      gc.fillText("TODA= " + runway.getCtoda() + "m", cw * 0.3, heightU);
    }

    //gc.setLineDashes(0);
    gc.setFont(new Font(20));
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
    if (!found) {
      logger.info("No matching designator found, keeping the selected runway to the left");
    }
    return leftT;
  }


  /**
   * Draws a direction arrown on the canvas
   *
   * @param gc        the graphics context
   * @param x1        the x coordinate of the start of the arrow
   * @param y1        the y coordinate of the start of the arrow
   * @param x2        the x coordinate of the end of the arrow
   * @param y2        the y coordinate of the end of the arrow
   * @param arrowSize the size of the arrow head
   */
  private void drawDirectionArrow(GraphicsContext gc, double x1, double y1, double x2, double y2, double arrowSize,
                                  double rotation) {

    var arrowColour = Color.valueOf("#000000");
    gc.setStroke(arrowColour);
    gc.setFill(arrowColour);

    double dx = x2 - x1, dy = y2 - y1;
    double angle = Math.atan2(dy, dx);
    int len = (int) Math.sqrt(dx * dx + dy * dy);

//    Transform transform = Transform.translate(x1, y1);
//    transform = transform.createConcatenation(Transform.rotate(rotation, canvas.getWidth() / 2.5, canvas.getHeight() / 2.5));
//    transform = transform.createConcatenation(Transform.rotate(Math.toDegrees(angle), 0, 0));
//    gc.setTransform(new Affine(transform));


    gc.strokeLine(0, 0, len, 0);
    gc.fillPolygon(new double[]{dx, dx - arrowSize, dx - arrowSize, dx}, new double[]{0, arrowSize, -arrowSize, 0}, 4);

    gc.restore();
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
