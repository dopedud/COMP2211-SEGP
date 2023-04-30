package uk.ac.soton.comp2211.team33.components;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.ac.soton.comp2211.team33.models.Airport;
import uk.ac.soton.comp2211.team33.models.Runway;
import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;

/**
 * The RunwayTab class is a custom component that holds a runway in a tab in an airport. An airport can contain many
 * runways.
 * Corresponds to user story #13.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
public class RunwayTab extends Tab {
  private static final Logger logger = LogManager.getLogger(RunwayTab.class);

  private final Stage stage;

  private final Airport state;

  private final Runway runway;

  /**
   * A JavaFX UI element that contains 3 panels; configuration panel, visualisation panel, and calculation panel.
   */
  @FXML
  private SplitPane splitPane;

  public RunwayTab(Stage stage, Airport state, Runway runway) {
    logger.info("Creating new runway tab named " + runway.getDesignator());

    // Set the stage this tab belongs to, the state of the airport, and the runway this tab possesses
    this.stage = stage;
    this.state = state;
    this.runway = runway;

    ProjectHelpers.renderRoot("/components/RunwayTab.fxml", this, this);

    // Set tab name
    setText(runway.getDesignator());

    splitPane.getItems().addAll(
        new ConfigPanel(this, stage, state, runway),
        new VisPanel(state, runway),
        new CalcPanel(stage, state, runway)
    );
  }

  @FXML
  private void onCloseRunway() {
    state.removeRunway(runway);
  }
}