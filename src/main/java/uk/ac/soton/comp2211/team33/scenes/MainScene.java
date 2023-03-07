package uk.ac.soton.comp2211.team33.scenes;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import uk.ac.soton.comp2211.team33.entities.Runway;
import uk.ac.soton.comp2211.team33.models.AppState;

/**
 * Main scene of the application. This scene should only be fully functional when there is at least a runway configured.
 * Contains visualisation and computation of the selected runway.
 *
 * @author Brian (dal1g21@soton.ac.uk)
 */
public class MainScene extends BaseScene {

  @FXML
  private Canvas canvas;

  public MainScene(Stage stage, AppState state) {
    super(stage, state, "mainScene.fxml");
  }

  @FXML
  private void handleAddObstacle() {
    new NewObstacleScene(this.createModalStage(), state);
  }

  @FXML
  private void handleAddAircraft() {
    new NewAircraftScene(this.createModalStage(), state);
  }

  protected void build() {
    stage.setTitle("Runway 1");
    renderMarkup();

    SimpleObjectProperty<Runway> runway = state.getRunwayState().getRunway();

    runway.addListener(((observableValue, oldRunway, newRunway) -> {
      GraphicsContext ctx = canvas.getGraphicsContext2D();

      ctx.setFill(Color.RED);
      ctx.setFont(new Font(20));
      ctx.fillText("Runway designator: " + newRunway.getDesignator(), 0, 20);
      ctx.fillText("TORA: " + newRunway.getTora(), 0, 40);
      ctx.fillText("TODA: " + newRunway.getToda(), 0, 60);
      ctx.fillText("ASDA: " + newRunway.getAsda(), 0, 80);
      ctx.fillText("LDA: " + newRunway.getLda(), 0, 100);
      ctx.fillText("RESA: " + newRunway.getResa(), 0, 120);
      ctx.fillText("Threshold: " + newRunway.getThreshold(), 0, 140);
    }));

    if (runway.get() == null) {
      new NewRunwayScene(this.createModalStage(), state);
    }
  }
}
