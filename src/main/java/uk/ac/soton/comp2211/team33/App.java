package uk.ac.soton.comp2211.team33;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main class to execute when running the application.
 */
public class App extends Application {
  private static Logger logger = LogManager.getLogger(App.class);

  /**
   * The main method to run when starting the application.
   * @param args  arguments given when running the application
   */
  public static void main(String[] args) {
    logger.info("Starting application...");
    launch();
  }

  /**
   * The method to invoke when the application starts.
   * @param stage       initial stage of the JavaFX application
   * @throws Exception  exception thrown if application fails to start
   */
  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("views/main.fxml"));
    Scene scene = new Scene(root);

    stage.setTitle("New application");
    stage.setScene(scene);
    stage.show();
  }
}
