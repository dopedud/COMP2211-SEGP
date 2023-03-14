package uk.ac.soton.comp2211.team33.controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.components.InputField;
import uk.ac.soton.comp2211.team33.models.Airport;

public class AircraftScene extends BaseScene {

  private static final Logger logger = LogManager.getLogger(AircraftScene.class);

  private boolean isEdit;

  @FXML
  private InputField id, blastProtection;

  /**
   * Instantiate a scene.
   *
   * @param stage The application stage to render the scene in
   * @param state The global application state
   */
  public AircraftScene(Stage stage, Airport state, boolean isEdit) {
    super(stage, state);
    this.isEdit = isEdit;
  }

  @Override
  protected void build() {
    logger.info("Building AircraftScene...");

    stage.setResizable(false);

    if (isEdit) {
      stage.setTitle("Edit Current Aircraft");
      renderFXML("EditAircraftScene.fxml");
    } else {
      stage.setTitle("New Aircraft");
      renderFXML("AddAircraftScene.fxml");
    }
  }

  @FXML
  private void onSubmitNewAircraft() {
    String id = this.id.getText();
    double blastProtection = Double.parseDouble(this.blastProtection.getText());

    state.addAircraft(id, blastProtection);

    logger.info("New aircraft added - " + id);

    stage.close();
  }

  @FXML
  private void onEditCurrentAircraft() {

  }
}
