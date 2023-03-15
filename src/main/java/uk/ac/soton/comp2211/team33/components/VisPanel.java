package uk.ac.soton.comp2211.team33.components;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uk.ac.soton.comp2211.team33.models.Runway;
import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;

/**
 * The Visualisation class is a custom component that renders the 2D top-down and side-on view.
 *
 * @author Brian (dal1g21@soton.ac.uk), Jackson (jl14u21@soton.ac.uk)
 */
public class VisPanel extends StackPane {

  private Runway runway;

  @FXML
  private Canvas canvas;

  private boolean isTopDownView = true;

  public VisPanel(Runway runway) {
    this.runway = runway;

    ProjectHelpers.renderRoot("/components/VisPanel.fxml", this, this);

    canvas.widthProperty().bind(widthProperty());
    canvas.heightProperty().bind(heightProperty());

    canvas.widthProperty().addListener(((observableValue, oldWidth, newWidth) -> draw()));
    canvas.heightProperty().addListener(((observableValue, oldWidth, newWidth) -> draw()));

    runway.currentObstacleProperty().addListener((obVal, oldVal, newVal) -> draw());
    runway.currentAircraftProperty().addListener((obVal, oldVal, newVal) -> draw());
    runway.ctoraProperty().addListener((obVal, oldVal, newVal) -> draw());
    runway.ctodaProperty().addListener((obVal, oldVal, newVal) -> draw());
    runway.casdaProperty().addListener((obVal, oldVal, newVal) -> draw());
    runway.cldaProperty().addListener((obVal, oldVal, newVal) -> draw());
    runway.cresaProperty().addListener((obVal, oldVal, newVal) -> draw());

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

    gc.clearRect(0, 0, cw, ch);

    gc.setFill(Color.RED);
    gc.setFont(new Font(30));
    gc.fillText("Sideways view", cw / 2, ch / 2);
  }

  private void drawTopDown() {
    GraphicsContext gc = canvas.getGraphicsContext2D();

    double cw = this.getWidth();
    double ch = this.getHeight();

    gc.clearRect(0, 0, cw, ch);

    // double toraL = otora; //Some given TORA to be passed in, dummy value

    //Surrounding area
    gc.setFill(Color.valueOf("#2d8000"));
    gc.fillRect(0, 0, this.getWidth(), this.getHeight());

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
    if (runway.getTora() >= 3500) {
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
    // gc.strokeText(formattedDes[0], cw * 0.18, ch * 0.5);
    // gc.strokeText(formattedDes[1], cw * 0.80, ch * 0.5);

    if (runway.getCurrentObstacle() != null) {
      gc.setFill(Color.RED);
      gc.setFont(new Font(30));
      gc.fillText(runway.getCurrentObstacle().getName(), cw / 2, ch / 2);
    }
  }
}
