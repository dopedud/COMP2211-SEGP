package uk.ac.soton.comp2211.team33.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.models.Airport;

/**
 * The NotiController controller class to notify the user if runway values have changed, or reset to its original values.
 * Corresponds to user story #14.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
public class NotiController extends BaseController {

  private static final Logger logger = LogManager.getLogger(NotiController.class);

  /**
   * A JavaFX UI element to display notification message.
   */
  @FXML
  private Label label;

  /**
   * String containing the message to notify.
   */
  private final String message = "";

  public NotiController(Stage stage, Airport state, String message) {
    super(stage, state);
    label.setText(message);
  }

  @Override
  protected void initialise() {
    logger.info("Building NotiController...");

    stage.setResizable(false);
    stage.setTitle("AVIA - Notification");

    buildScene("/views/NotiView.fxml");
  }
}
