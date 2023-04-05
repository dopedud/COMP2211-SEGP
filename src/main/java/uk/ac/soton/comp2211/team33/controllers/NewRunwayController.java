package uk.ac.soton.comp2211.team33.controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.components.InputField;
import uk.ac.soton.comp2211.team33.models.Airport;
import uk.ac.soton.comp2211.team33.schemas.RunwaySchema;
import uk.ac.soton.comp2211.team33.utilities.validation.DoubleSchema;
import uk.ac.soton.comp2211.team33.utilities.validation.StringSchema;
import uk.ac.soton.comp2211.team33.utilities.validation.ValidationException;

/**
 * The NewRunwayScene class that creates a new runway upon user request.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
public class NewRunwayController extends BaseController {

  private static final Logger logger = LogManager.getLogger(NewRunwayController.class);

  @FXML
  private InputField designator, tora, toda, asda, lda, resa, threshold;

  @FXML
  private Text formError;

  private final RunwaySchema runwaySchema = new RunwaySchema(
    new StringSchema().required().regex("(0[1-9]|[12]\\d|3[0-6])[LCR]?").label("Designator"),
    new DoubleSchema().required().label("TORA"),
    new DoubleSchema().required().label("TODA"),
    new DoubleSchema().required().label("ASDA"),
    new DoubleSchema().required().label("LDA"),
    new DoubleSchema().required().label("RESA"),
    new DoubleSchema().required().label("Threshold")
  );

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
    try {
      String designator = runwaySchema.designator().validate(this.designator.getText());
      double tora = runwaySchema.tora().validate(this.tora.getText());
      double toda = runwaySchema.toda().validate(this.toda.getText());
      double asda = runwaySchema.asda().validate(this.asda.getText());
      double lda = runwaySchema.lda().validate(this.lda.getText());
      double resa = runwaySchema.resa().validate(this.resa.getText());
      double threshold = runwaySchema.threshold().validate(this.threshold.getText());

      state.addRunway(designator, tora, toda, asda, lda, resa, threshold);

      stage.close();
    } catch (ValidationException e) {
      formError.setText(e.getMessage());
    }
  }
}
