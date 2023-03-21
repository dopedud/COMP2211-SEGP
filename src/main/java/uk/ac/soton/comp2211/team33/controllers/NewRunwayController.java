package uk.ac.soton.comp2211.team33.controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.components.InputField;
import uk.ac.soton.comp2211.team33.models.Airport;

/**
 * The NewRunwayController class that creates a new runway upon user request.
 * Corresponds to user story #3, #13.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
public class NewRunwayController extends BaseController {

  private static final Logger logger = LogManager.getLogger(NewRunwayController.class);

  /**
   * Multiple JavaFX UI elements to store different values of a runway.
   */
  @FXML
  private InputField designator, tora, toda, asda, lda, resa, threshold;

  NewRunwayController(Stage stage, Airport state) {
    super(stage, state);
  }

  @Override
  protected void initialise() {
    logger.info("Building NewRunwayController...");

    stage.setResizable(false);
    stage.setTitle("New Runway");

    buildScene("/views/NewRunwayView.fxml");
  }

  @FXML
  private void onSubmitRunway() {
    double toraDouble = Double.parseDouble(tora.getText());
    double todaDouble = Double.parseDouble(toda.getText());
    double asdaDouble = Double.parseDouble(asda.getText());
    double ldaDouble = Double.parseDouble(lda.getText());
    double resaDouble = Double.parseDouble(resa.getText());
    double thresholdDouble = Double.parseDouble(threshold.getText());

    state.addRunway(designator.getText(), toraDouble, todaDouble, asdaDouble, ldaDouble, resaDouble, thresholdDouble);

    stage.close();
  }
}
