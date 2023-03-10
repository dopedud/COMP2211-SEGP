package uk.ac.soton.comp2211.team33;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.stage.Stage;

import uk.ac.soton.comp2211.team33.models.Airport;
import uk.ac.soton.comp2211.team33.scenes2.MainScene;

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
   * @param args
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
    new MainScene(stage, new Airport("London", "London Heathrow Airport"));
  }
}
