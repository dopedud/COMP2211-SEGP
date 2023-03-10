package uk.ac.soton.comp2211.team33.scenes2;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import uk.ac.soton.comp2211.team33.models.Airport;

public class NewRunwayScene extends BaseScene {

  public NewRunwayScene(Stage stage, Airport state) {
    super(stage, state);
  }

  @Override
  protected void build() {
    stage.setTitle("New Runway");

    renderFXML("NewRunwayScene.fxml");
  }

  @FXML
  private void onSubmitRunway() {

  }
}
