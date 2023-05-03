package uk.ac.soton.comp2211.team33.controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.components.InputField;
import uk.ac.soton.comp2211.team33.models.Airport;

/**
 * The AircraftController controller class that manages edition or addition of the list of aircraft.
 * Corresponds to user story #3.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
public class AircraftController extends BaseController {

  private static final Logger logger = LogManager.getLogger(AircraftController.class);

  /**
   * Boolean to decide if this scene is to edit current aircraft or add a new aircraft.
   */
  private final boolean isEdit;

  /**
   * Multiple JavaFX UI elements to store different values of an aircraft.
   */
  @FXML
  private InputField id, blastProtection;

  public AircraftController(Stage stage, Airport state, boolean isEdit) {
    super(stage, state);
    this.isEdit = isEdit;
  }

  @Override
  protected void initialise() {
    logger.info("Building AircraftController...");

    stage.setResizable(false);

    if (isEdit) {
      stage.setTitle("AVIA - Edit Current Aircraft");
      buildScene("/views/EditAircraftView.fxml");
    } else {
      stage.setTitle("AVIA - New Aircraft");
      buildScene("/views/AddAircraftView.fxml");
    }
  }

  @FXML
  private void onSubmitNewAircraft() {
    try {
      String id = this.id.getText();
      double blastProtection = Double.parseDouble(this.blastProtection.getText());

      state.addAircraft(id, blastProtection);

      logger.info("New aircraft added - " + id);

      stage.close();
    } catch (NumberFormatException e) {
      e.printStackTrace();
      //TODO: create a modal stage informing the user that value entered is not in correct format
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      //TODO: create a modal stage informing the user that value entered is out of range
    }
  }
}
