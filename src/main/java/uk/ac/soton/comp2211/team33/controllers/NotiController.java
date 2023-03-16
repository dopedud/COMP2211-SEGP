package uk.ac.soton.comp2211.team33.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.models.Airport;

public class NotiController extends BaseController {

  private static final Logger logger = LogManager.getLogger(NotiController.class);

  @FXML
  private Label message;

  /**
   * Instantiate a scene.
   *
   * @param stage The application stage to render the scene in
   * @param state The global application state
   */
  public NotiController(Stage stage, Airport state, String message) {
    super(stage, state);
  }

  @Override
  protected void initialise() {
    logger.info("Building NotiController...");

    stage.setResizable(false);

    buildScene("/views/NotiView.fxml");
  }

  @FXML
  private void onSubmit() {
    stage.close();
  }
}
