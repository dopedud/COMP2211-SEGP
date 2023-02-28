package uk.ac.soton.comp2211.team33;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
  private static Logger logger = LogManager.getLogger(App.class);

  public static void main(String[] args) {
    logger.info("Starting application...");
    launch();
  }

  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("views/main.fxml"));
    Scene scene = new Scene(root);

    stage.setTitle("New application");
    stage.setScene(scene);
    stage.show();
  }
}
