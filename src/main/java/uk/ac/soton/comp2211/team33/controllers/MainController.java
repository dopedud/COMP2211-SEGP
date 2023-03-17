package uk.ac.soton.comp2211.team33.controllers;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TabPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.components.RunwayTab;
import uk.ac.soton.comp2211.team33.models.Airport;
import uk.ac.soton.comp2211.team33.models.Runway;
import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;

/**
 * The MainScene class that acts as the main workspace for the runway re-declaration.
 *
 * @author Geeth (gv2g21@soton.ac.uk), Abeed (mabs1u21@soton.ac.uk)
 */
public class MainController extends BaseController {

  private static final Logger logger = LogManager.getLogger(MainController.class);

  @FXML
  private TabPane runwayTabs;

  MainController(Stage stage, Airport state) {
    super(stage, state);
  }

  @Override
  protected void initialise() {
    logger.info("Building MainController...");

    // Set stage properties

    Screen screen = Screen.getPrimary();
    Rectangle2D bounds = screen.getVisualBounds();

    stage.setX(bounds.getMinX());
    stage.setY(bounds.getMinY());
    stage.setWidth(bounds.getWidth());
    stage.setHeight(bounds.getHeight());
    stage.setResizable(true);

    stage.setTitle("Runway Re-decleration Tool - " + state.getName() + ", " + state.getCity());

    buildScene("/views/MainView.fxml");

    renderTabs();

    addInitialRunways();
  }

  @FXML
  private void onChangeAirport() {
    new NewAirportController(new Stage(), null);
  }

  @FXML
  private void onNewRunway() {
    new NewRunwayController(ProjectHelpers.createModalStage(stage), state);
  }

  private void renderTabs() {
    state.runwayListProperty().addListener((ListChangeListener<? super Runway>) list -> {
      list.next();

      if (list.wasAdded()) {
        Runway runway = list.getAddedSubList().get(0);
        runwayTabs.getTabs().add(new RunwayTab(stage, state, runway));
      } else if (list.wasRemoved()) {
        //TODO: remove runways when tab is closed
      }
    });
  }

  private void addInitialRunways() {
    state.addRunway("09L", 3902, 3902, 3902, 3595, 240, 306);
    state.addRunway("27R", 3884, 3962, 3884, 3884, 240, 0);
    state.addRunway("09R", 3660, 3660, 3660, 3353, 240, 307);
    state.addRunway("27L", 3660, 3660, 3660, 3660, 240, 0);
  }
}
