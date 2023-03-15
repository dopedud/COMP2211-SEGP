package uk.ac.soton.comp2211.team33.controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.components.InputField;
import uk.ac.soton.comp2211.team33.models.Airport;

/**
 * The ObstacleScene class that manages deletion or edition of the list of obstacles.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
public class ObstacleController extends BaseController {

  private static final Logger logger = LogManager.getLogger(ObstacleController.class);

  private final boolean isEdit;

  @FXML
  private InputField name, height, length, centerline;

  public ObstacleController(Stage stage, Airport state, boolean isEdit) {
    super(stage, state);
    this.isEdit = isEdit;
  }

  @Override
  protected void initialise() {
    logger.info("Building ObstacleController...");

    stage.setResizable(false);

    if (isEdit) {
      stage.setTitle("Edit Current Obstacle");
      buildScene("/views/EditObstacleView.fxml");
    } else {
      stage.setTitle("New Obstacle");
      buildScene("/views/AddObstacleView.fxml");
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