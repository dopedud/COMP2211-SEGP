package uk.ac.soton.comp2211.team33.scenes;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uk.ac.soton.comp2211.team33.models.AppState;

public class JacksonScene extends BaseScene {
  @FXML
  private Button buttonOne;

  public JacksonScene(Stage stage, AppState state) {
    super(stage, state);
  }

  @FXML
  private void handleButtonOneClick() {
    System.out.println("Clicked");
  }

  @Override
  protected void build() {
    // JavaFX

    /*BorderPane root = new BorderPane();

    Button button1 = new Button("1");
    Button button2 = new Button("2");
    Button button3 = new Button("3");

    root.setCenter(button2);
    root.setLeft(button1);
    root.setRight(button3);

    Scene scene = new Scene(root);

    stage.setScene(scene);
    stage.show();*/

    renderFXML("jacksonScene.fxml");
  }
}
