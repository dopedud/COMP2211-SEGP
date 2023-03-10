package uk.ac.soton.comp2211.team33.scenes2;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.models.Airport;

public class MainScene extends BaseScene {

  private static final Logger logger = LogManager.getLogger(MainScene.class);

  @FXML
  private TabPane runwayTabs;

  public MainScene(Stage stage, Airport state) {
    super(stage, state);
  }

  @Override
  protected void build() {
    logger.info("Building MainScene...");

    stage.setTitle("Runway Re-decleration Tool - " + state.getName());
    stage.setMinHeight(600);
    stage.setMinWidth(1000);

    renderFXML("MainScene.fxml");
  }

//  @FXML
//  private void onNewRunway() {
//    new NewRunwayScene(createModalStage(), state);
//  }
}
