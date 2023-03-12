package uk.ac.soton.comp2211.team33.scenes;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.components.InputField;
import uk.ac.soton.comp2211.team33.models.Airport;
import uk.ac.soton.comp2211.team33.models.Runway;

public class NewRunwayScene extends BaseScene {

  private static final Logger logger = LogManager.getLogger(NewRunwayScene.class);

  @FXML
  private InputField designator, tora, toda, asda, lda, resa, threshold;

  NewRunwayScene(Stage stage, Airport state) {
    super(stage, state);
  }

  @Override
  protected void build() {
    logger.info("Building NewRunwayScene...");

    stage.setResizable(false);
    stage.setTitle("New Runway");

    renderFXML("NewRunwayScene.fxml");
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
