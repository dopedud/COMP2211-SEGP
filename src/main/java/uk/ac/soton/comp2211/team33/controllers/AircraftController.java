package uk.ac.soton.comp2211.team33.controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.components.InputField;
import uk.ac.soton.comp2211.team33.exceptions.OutOfRangeException;
import uk.ac.soton.comp2211.team33.models.Airport;

/**
 * The AircraftScene class that manages deletion or edition of the list of aircraft.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
public class AircraftController extends BaseController {

  private static final Logger logger = LogManager.getLogger(AircraftController.class);

  private final boolean isEdit;

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
      stage.setTitle("Edit Current Aircraft");
      buildScene("/views/EditAircraftView.fxml");
    } else {
      stage.setTitle("New Aircraft");
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
    } catch (OutOfRangeException e) {
      e.printStackTrace();
      //TODO: create a modal stage informing the user that value entered is out of range
    }
  }

  @FXML
  private void onEditCurrentAircraft() {

  }
}
