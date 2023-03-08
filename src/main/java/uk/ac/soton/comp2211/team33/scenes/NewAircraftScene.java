package uk.ac.soton.comp2211.team33.scenes;

import javafx.fxml.FXML;
import javafx.stage.Stage;

import uk.ac.soton.comp2211.team33.components.InputField;
import uk.ac.soton.comp2211.team33.models.AirportState;


/**
 * @author Geeth (gv2g21@soton.ac.uk)
 */
public class NewAircraftScene extends BaseScene {
  @FXML
  private InputField id;

  @FXML
  private InputField blastProtection;

  public NewAircraftScene(Stage stage, AirportState state) {
    super(stage, state, "newAircraftScene.fxml");
  }

  @FXML
  private void handleSubmitAircraft() {

    // TODO: Validation

    String aircraftId;
    double blastProtectionValue = 0;

    try {
      aircraftId = id.getText();
      blastProtectionValue = Double.parseDouble(blastProtection.getText());
    }
    catch (NumberFormatException e) {
      System.err.println("Invalid input");
    }

    // Call state
  }

  @Override
  protected void build() {
    stage.setTitle("New aircraft");
    renderMarkup();
  }
}
