package uk.ac.soton.comp2211.team33;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.stage.Stage;

import uk.ac.soton.comp2211.team33.controllers.SplashController;

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
   * @param stage the initial window of the JavaFX application
   */
  @Override
  public void start(Stage stage) {
    //Settings.setStylesheetPath("/css/high_contrast.css");
    new SplashController(stage, null);
  }
}
