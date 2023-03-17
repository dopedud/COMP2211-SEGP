package uk.ac.soton.comp2211.team33.components;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.models.Airport;
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
    runway.ctoraProperty().addListener(ignored -> draw());
    runway.ctodaProperty().addListener(ignored -> draw());
    runway.casdaProperty().addListener(ignored -> draw());
    runway.cldaProperty().addListener(ignored -> draw());
    runway.cresaProperty().addListener(ignored -> draw());

    draw();
  }

  @FXML
  private void onSwitchView() {
    isTopDownView = !isTopDownView;
    draw();
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
    double cw = this.getWidth();
    double ch = this.getHeight();

    double grassDepth = ch / 20;

    // Sky

    gc.setFill(Color.rgb(56, 179, 232));
    gc.fillRect(0, 0, cw, ch);

    // Grass

    gc.setFill(Color.rgb(73, 145, 99));
    gc.fillRect(0, (double) 2 / 3 * ch, cw, ch);

    // Dust

    gc.setFill(Color.rgb(50, 50 ,50));
    gc.fillRect(0, ((double) 2 / 3 * ch) + grassDepth, cw, ch);

    gc.setFill(Color.RED);
    gc.setFont(new Font(30));
    gc.fillText("Sideways view", cw / 2, ch / 2);
  }

  /**
   * This method is called when a top-down view is requested by the controller.
   * Draws a top-down view of the selected runway.
   */
  private void drawTopDown() {
    var gc = canvas.getGraphicsContext2D();
    gc.setFont(new Font(20));

    double cw = canvas.getWidth();
    double ch = canvas.getHeight();

    gc.clearRect(-300, -300, cw * 3, ch * 3);

    var designator = runway.getDesignator();

    var formattedDes = formatDesignators(designator);

    // TODO: 15/03/2023 Abeed can use this for looking up the threshold
    //The opposing side's designator formatted into a string of no spaces, ready to use for searching
    var otherDesignator = formattedDes[1].replaceAll("[\r\n]+", "").replaceAll(" ", "");


    double threshold = runway.getThreshold();

    double stopway = runway.getStopway();

    double clearway = runway.getClearway();

    double rotationAngle = 0;

    //Rotation
    Rotate r = new Rotate(rotationAngle, cw * 0.5, ch * 0.5);
    gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());

//    if(rotationAngle != 0 || rotationAngle != 90 || rotationAngle != 180 || rotationAngle != 270
//      || rotationAngle != 360){
//      gc.scale(0.8,0.8);
//    }

    gc.save();
    double toraL = 3550; //Some given TORA to be passed in, dummy value

    //Surrounding area
    gc.setFill(Color.valueOf("#7CB342"));
    gc.fillRect(-500, -500, canvas.getWidth() * 5, canvas.getHeight() * 5);

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
    while (tempB < ch * 0.53) {
      gc.strokeLine(tempA, tempB, tempC, tempD);
      tempB += 5;
      tempD += 5;
    }

    var tempE = cw * 0.81;
    var tempF = cw * 0.86;
    tempB = ch * 0.44;
    tempD = ch * 0.44;
    while (tempB < ch * 0.53) {
      gc.strokeLine(tempE, tempB, tempF, tempD);
      tempB += 5;
      tempD += 5;
    }

    //Write the runway designators in the 2D view
    gc.setLineWidth(1);
    gc.setFill(Color.valueOf("#ffffff"));
    gc.fillText(formattedDes[0], cw * 0.17, ch * 0.48);
    gc.fillText(formattedDes[1], cw * 0.77, ch * 0.48);

    //Add a threshold if it exists
    if (threshold != 0) {
      gc.setLineWidth(1.5);
      gc.setLineDashes(6);
      gc.setStroke(Color.valueOf("#FF5733"));
      gc.setFill(Color.valueOf("#FF5733"));
      gc.strokeLine(cw * 0.20, ch * 0.41, cw * 0.20, ch * 0.57);
      //Write metrics over threshold
      gc.setLineDashes(0);
      gc.fillText(threshold + "m", cw * 0.18, ch * 0.6);
    }

    gc.setFont(new Font(16));
    gc.setLineWidth(1);
    double stopwayPar = 0.87 + 0.08;
    //Stopway on runway end
    if (stopway != 0) {
      gc.setLineDashes(0);
      gc.setStroke(Color.valueOf("#f7ff00"));
      gc.setFill(Color.valueOf("#f7ff00"));
      gc.strokeRect(cw * 0.87, ch * 0.43, cw * 0.08, ch * 0.1);
      //Write metrics over threshold
      gc.setLineDashes(0);
      gc.fillText("Stopway" + "\n" + stopway + "m", cw * 0.88, ch * 0.38);
    }

    double clearwayPar = 0.87;
    //Clearway on runway end
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
    drawDirectionArrow(gc, cw * 0.1, ch * 0.1, cw * 0.3, ch * 0.1, 10.0, rotationAngle);
    gc.fillText("Take-off/Landing", cw * 0.1, ch * 0.1 - 5, 160);

    //Picked an object
    if (runway.getCurrentObstacle() != null) {
      gc.setFill(Color.RED);
      gc.setFont(new Font(30));
      gc.fillText(runway.getCurrentObstacle().getName(), cw * (0.1 + ((runway.getObsDistFromThresh() + runway.getThreshold()) / runway.getTora())), ch / 2);
    }
    gc.setFont(new Font(20));

    //Runway distances
    gc.setLineWidth(1.5);
    gc.setLineDashes(5);
    gc.setStroke(Color.valueOf("#000000"));
    gc.setFill(Color.valueOf("#000000"));
    gc.strokeLine(cw * 0.1, ch * 0.3, cw * 0.1, ch * 0.43);
    gc.setFont(new Font(15));
    gc.setLineDashes(0);

    //Displays LDA value and adjusts length to current LDA. The start of the line also depends on the threshold of the runway.
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

    Transform transform = Transform.translate(x1, y1);
    transform = transform.createConcatenation(Transform.rotate(rotation, canvas.getWidth() / 2.5, canvas.getHeight() / 2.5));
    transform = transform.createConcatenation(Transform.rotate(Math.toDegrees(angle), 0, 0));
    gc.setTransform(new Affine(transform));


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

    //Throw error if designator is not of the correct format (e.g. 09L, 26)
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
