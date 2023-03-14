package uk.ac.soton.comp2211.team33;

import javafx.scene.Scene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.stage.Stage;

import uk.ac.soton.comp2211.team33.controllers.NewAirportController;
import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;

/**
 * The main class to execute when running the application.
 *
 * @author Brian (dal1g21@soton.ac.uk), Geeth (gv2g21@soton.ac.uk)
 */
public class App extends Application {

  private static final Logger logger = LogManager.getLogger(App.class);

  /**
   * The main method to execute when starting the application.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    logger.info("Starting application...");
    launch();
  }

  /**
   * The method to invoke for JavaFX.
   *
   * @param stage The initial window of the JavaFX application
   */
  @Override
  public void start(Stage stage) {
    new NewAirportController(stage, null);
  }
}
