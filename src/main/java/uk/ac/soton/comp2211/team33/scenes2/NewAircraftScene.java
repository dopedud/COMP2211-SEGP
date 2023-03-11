package uk.ac.soton.comp2211.team33.scenes2;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.components.InputField;
import uk.ac.soton.comp2211.team33.models.Airport;

public class NewAircraftScene extends BaseScene {

  private static final Logger logger = LogManager.getLogger(NewAircraftScene.class);

  @FXML
  private InputField aircraftID;

  @FXML
  private InputField blastProtection;

  /**
   * Instantiate a scene.
   *
   * @param stage The application stage to render the scene in
   * @param state The global application state
   */
  NewAircraftScene(Stage stage, Airport state) {
    super(stage, state);
  }

  @Override
  protected void build() {
    logger.info("Building NewAircraftScene...");

    stage.setTitle("New Aircraft");
    stage.setResizable(false);

    renderFXML("NewAircraftScene.fxml");
  }

  @FXML
  private void onNewAircraft() {
    if (Double.parseDouble(blastProtection.getText()) < 300 || Double.parseDouble(blastProtection.getText()) > 500) {
      //reject adding new aircraft
    } else {

    }
  }
}
