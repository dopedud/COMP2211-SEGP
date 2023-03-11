package uk.ac.soton.comp2211.team33.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.components.RunwayTab;
import uk.ac.soton.comp2211.team33.models.Airport;
import uk.ac.soton.comp2211.team33.models.Runway;

public class MainScene extends BaseScene {

  private static final Logger logger = LogManager.getLogger(MainScene.class);

  @FXML
  private TabPane runwayTabs;

  MainScene(Stage stage, Airport state) {
    super(stage, state);
  }

  @Override
  protected void build() {
    logger.info("Building MainScene...");

    stage.setTitle("Runway Re-decleration Tool - " + state.getName());
    stage.setResizable(true);

    renderFXML("MainScene.fxml");

    renderTabs();
  }

  @FXML
  private void onNewAirport() {
    new NewAirportScene(createNewStage(), null);
  }

  @FXML
  private void onNewRunway() {
    new NewRunwayScene(createModalStage(), state);
  }

  private void renderTabs() {
    state.getRunwayListProperty().addListener((obList, oldList, newList) -> {
      Runway runway = newList.get(newList.size() - 1);

      runwayTabs.getTabs().add(new RunwayTab(stage, state, runway));
    });
  }
}
