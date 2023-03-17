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
  private Label label;

  private final String message = "";

  /**
   * Instantiate a scene.
   *
   * @param stage The application stage to render the scene in
   * @param state The global application state
   * @param message the message of the notification
   */
  public NotiController(Stage stage, Airport state, String message) {
    super(stage, state);
    label.setText(message);
  }

  @Override
  protected void initialise() {
    logger.info("Building NotiController...");

    stage.setResizable(false);
    stage.setTitle("Notification");

    buildScene("/views/NotiView.fxml");
  }
}
