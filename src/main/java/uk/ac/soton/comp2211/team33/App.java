package uk.ac.soton.comp2211.team33;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import uk.ac.soton.comp2211.team33.controllers.BaseController;
import uk.ac.soton.comp2211.team33.models.AppState;

/**
 * The main class to execute when running the application.
 *
 * @author Brian (dal1g21@soton.ac.uk)
 * @author Geeth (gv2g21@soton.ac.uk)
 */
public class App extends Application {

  private static final Logger logger = LogManager.getLogger(App.class);

  /**
   * The main method to run when starting the application.
   *
   * @param args  arguments given when running the application
   */
  public static void main(String[] args) {
    logger.info("Starting application...");
    launch();
  }

  /**
   * The method to invoke when the application starts.
   *
   * @param stage       initial stage of the JavaFX application
   * @throws Exception  exception thrown if application fails to start
   */
  @Override
  public void start(Stage stage) throws Exception {
    AppState state = new AppState();

    FXMLLoader loader = new FXMLLoader(getClass().getResource("views/newRunwayConfig.fxml"));
    Parent root = loader.load();

    BaseController controller = loader.getController();
    controller.setState(state);

    Scene scene = new Scene(root);

    stage.setTitle("Runway Re-declaration tool");
    stage.setScene(scene);
    stage.show();
  }
}
