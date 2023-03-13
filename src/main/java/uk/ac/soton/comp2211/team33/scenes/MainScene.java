package uk.ac.soton.comp2211.team33.scenes;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
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

    stage.setResizable(true);
    stage.setMinWidth(800);
    stage.setMinHeight(600);
    stage.setTitle("Runway Re-decleration Tool - " + state.getName());

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
    state.runwayListProperty().addListener((ListChangeListener<? super Runway>) (list) -> {
      list.next();

      if (list.wasAdded()) {
        Runway runway = list.getAddedSubList().get(0);
        runwayTabs.getTabs().add(new RunwayTab(stage, state, runway));
      } else if (list.wasRemoved()) {
        //TODO: remove runways when tab is closed
      }
    });
  }
}
