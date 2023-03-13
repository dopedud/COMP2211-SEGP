package uk.ac.soton.comp2211.team33.scenes;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.components.InputField;
import uk.ac.soton.comp2211.team33.models.Airport;

public class ObstacleScene extends BaseScene {

  private static final Logger logger = LogManager.getLogger(ObstacleScene.class);

  private boolean isEdit;

  @FXML
  private InputField name, height, length, centerline;

  /**
   * Instantiate a scene.
   *
   * @param stage The application stage to render the scene in
   * @param state The global application state
   */
  public ObstacleScene(Stage stage, Airport state, boolean isEdit) {
    super(stage, state);
    this.isEdit = isEdit;
  }

  @Override
  protected void build() {
    logger.info("Building ObstacleScene...");

    stage.setResizable(false);

    if (isEdit) {
      stage.setTitle("Edit Current Obstacle");
      renderFXML("EditObstacleScene.fxml");
    } else {
      stage.setTitle("New Obstacle");
      renderFXML("AddObstacleScene.fxml");
    }
  }

  @FXML
  private void onSubmitNewObstacle() {
    String name = this.name.getText();
    double height = Double.parseDouble(this.height.getText());
    double length = Double.parseDouble(this.length.getText());
    double centerline = Double.parseDouble(this.centerline.getText());

    state.addObstacle(name, height, length, centerline);

    logger.info("New obstacle added - " + name);

    stage.close();
  }

  @FXML
  private void onEditCurrentObstacle() {

  }
}
