package uk.ac.soton.comp2211.team33.scenes2;

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
  private InputField designator;

  @FXML
  private InputField tora;

  @FXML
  private InputField toda;

  @FXML
  private InputField asda;

  @FXML
  private InputField lda;

  @FXML
  private InputField resa;

  @FXML
  private InputField threshold;

  public NewRunwayScene(Stage stage, Airport state) {
    super(stage, state);
  }

  @Override
  protected void build() {
    logger.info("Building NewRunwayScene...");

    stage.setTitle("New Runway");
    stage.setResizable(false);

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

    state.getRunwayList().add(new Runway(designator.getText(), toraDouble, todaDouble, asdaDouble, ldaDouble,
        resaDouble, thresholdDouble));

    stage.close();
  }
}
