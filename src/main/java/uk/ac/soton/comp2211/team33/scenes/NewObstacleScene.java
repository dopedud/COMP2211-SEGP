package uk.ac.soton.comp2211.team33.scenes;

import javafx.fxml.FXML;
import javafx.stage.Stage;

import uk.ac.soton.comp2211.team33.components.InputField;
import uk.ac.soton.comp2211.team33.models.AppState;

/**
 * @author Geeth (gv2g21@soton.ac.uk)
 */
public class NewObstacleScene extends BaseScene {
  @FXML
  private InputField name;

  @FXML
  private InputField height;

  @FXML
  private InputField length;

  public NewObstacleScene(Stage stage, AppState state) {
    super(stage, state);
  }

  @FXML
  private void handleSubmitObstacle() {

    // TODO: Validation

    String obstacleName;
    double heightValue = 0;
    double lengthValue = 0;

    try {
      obstacleName = name.getText();
      heightValue = Double.parseDouble(height.getText());
      lengthValue = Double.parseDouble(length.getText());
    }
    catch (NumberFormatException e) {
      System.err.println("Invalid input");
      return;
    }

    state.getActiveAirportState().addObstacle(obstacleName, heightValue, lengthValue);
    stage.close();
  }

  protected void build() {
    stage.setTitle("New obstacle");
    renderFXML("newObstacleScene.fxml");
  }
}
