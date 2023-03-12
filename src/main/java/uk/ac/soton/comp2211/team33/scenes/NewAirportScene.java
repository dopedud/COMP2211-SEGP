package uk.ac.soton.comp2211.team33.scenes;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.components.InputField;
import uk.ac.soton.comp2211.team33.models.Airport;

public class NewAirportScene extends BaseScene {

  private static final Logger logger = LogManager.getLogger(NewAirportScene.class);

  @FXML
  private InputField city;

  @FXML
  private InputField name;

  public NewAirportScene(Stage stage, Airport state) {
    super(stage, state);
  }

  @Override
  protected void build() {
    logger.info("Building NewAirportScene...");

    stage.setResizable(false);
    stage.setTitle("New Airport");

    renderFXML("NewAirportScene.fxml");
  }

  @FXML
  private void onSubmitAirport() {
    new MainScene(stage, new Airport(city.getText(), name.getText()));
  }
}
