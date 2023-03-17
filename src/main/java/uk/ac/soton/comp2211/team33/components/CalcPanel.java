package uk.ac.soton.comp2211.team33.components;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import uk.ac.soton.comp2211.team33.controllers.NotiController;
import uk.ac.soton.comp2211.team33.models.Airport;
import uk.ac.soton.comp2211.team33.models.Runway;
import uk.ac.soton.comp2211.team33.utilities.Calculator;
import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;

public class CalcPanel extends AnchorPane {

  private final Stage stage;

  private final Airport state;

  private final Runway runway;

  @FXML
  private Label tora, toda, asda, lda, resa;

  @FXML
  private Label ctora, ctoda, casda, clda, cresa;

  @FXML
  private Label threshold;

  @FXML
  private TextArea calcBreakdown;

  @FXML
  private DropdownField calcMode;

  private boolean calcTowards;

  @FXML
  private Button switchNotifications;

  private boolean switchNoti;

  public CalcPanel(Stage stage, Airport state, Runway runway) {
    this.stage = stage;
    this.state = state;
    this.runway = runway;

    ProjectHelpers.renderRoot("/components/CalcPanel.fxml", this, this);

    // Set initial values of runway
    tora.setText(String.valueOf(runway.getTora()));
    toda.setText(String.valueOf(runway.getToda()));
    asda.setText(String.valueOf(runway.getAsda()));
    lda.setText(String.valueOf(runway.getLda()));
    resa.setText(String.valueOf(runway.getResa()));
    threshold.setText(String.valueOf(runway.getThreshold()));

    // Set calculated values
    ctora.setText("-");
    ctoda.setText("-");
    casda.setText("-");
    clda.setText("-");
    cresa.setText("-");

    // Add listeners from calculated values UI to calculated values model
    runway.ctoraProperty().addListener((obVal, oldVal, newVal) -> {
      if (newVal.doubleValue() == runway.getTora()) ctora.setText("-");
      else ctora.setText(String.valueOf(newVal));
    });

    runway.ctodaProperty().addListener((obVal, oldVal, newVal) -> {
      if (newVal.doubleValue() == runway.getToda()) ctoda.setText("-");
      else ctoda.setText(String.valueOf(newVal));
    });

    runway.casdaProperty().addListener((obVal, oldVal, newVal) -> {
      if (newVal.doubleValue() == runway.getAsda()) casda.setText("-");
      else casda.setText(String.valueOf(newVal));
    });

    runway.cldaProperty().addListener((obVal, oldVal, newVal) -> {
      if (newVal.doubleValue() == runway.getLda()) clda.setText("-");
      else clda.setText(String.valueOf(newVal));
    });

    runway.cresaProperty().addListener((obVal, oldVal, newVal) -> {
      if (newVal.doubleValue() == runway.getResa()) cresa.setText("-");
      else cresa.setText(String.valueOf(newVal));
    });

    // Initialise list UI to list models
    runway.obsDistFromThreshProperty().addListener(ignored -> recalculateRunwayValues());
    runway.currentAircraftProperty().addListener(ignored -> {
      recalculateRunwayValues();
      notifyRecalculation();
    });
    runway.currentObstacleProperty().addListener(ignored -> {
      recalculateRunwayValues();
      notifyRecalculation();
    });

    // Set calculation mode to 2 modes, calculation towards obstacle and away from/over obstacle
    // Also adds a listener to re-calculate values based on which modes selected
    calcMode.getItems().add("Take-Off Towards/Landing Towards");
    calcMode.getItems().add("Take-Off Away/Landing Over");
    calcMode.setValue("Take-Off Towards/Landing Towards");

    calcMode.valueProperty().addListener((obVal, oldVal, newVal) -> {
      calcTowards = newVal.equals("Take-Off Towards/Landing Towards");
      recalculateRunwayValues();
      notifyRecalculation();
    });

    // Set enabling notifications default to false
    switchNotifications.setText("Enable Notifications");
  }

  private void recalculateRunwayValues() {
    if (calcTowards) {
      if (runway.getCurrentObstacle() == null) {
        calcBreakdown.setText(Calculator.resetCalculationsPP(runway));
      } else {
        calcBreakdown.setText(Calculator.takeOffTowardsObsPP(runway, runway.getCurrentObstacle()) + "\n" +
                Calculator.landingTowardsObsPP(runway, runway.getCurrentObstacle()));
      }
    } else {
      if (runway.getCurrentObstacle() == null || runway.getCurrentAircraft() == null) {
        calcBreakdown.setText(Calculator.resetCalculationsPP(runway));
      } else {
        calcBreakdown.setText(
            Calculator.takeOffAwayObsPP(runway, runway.getCurrentObstacle(), runway.getCurrentAircraft()) + "\n" +
                Calculator.landingOverObsPP(runway, runway.getCurrentObstacle(), runway.getCurrentAircraft())
        );
      }
    }
  }

  private void notifyRecalculation() {
    if (!switchNoti) return;

    if (calcTowards) {
      if (runway.getCurrentObstacle() == null) {
        new NotiController(ProjectHelpers.createModalStage(stage), state, "Runway values have been reset.");
      } else {
        new NotiController(ProjectHelpers.createModalStage(stage), state, "Runway values have been re-declared.");
      }
    } else {
      if (runway.getCurrentObstacle() == null || runway.getCurrentAircraft() == null) {
        new NotiController(ProjectHelpers.createModalStage(stage), state, "Runway values have been reset.");
      } else {
        new NotiController(ProjectHelpers.createModalStage(stage), state, "Runway values have been re-declared.");
      }
    }
  }

  @FXML
  private void onSwitchNoti() {
    switchNoti = !switchNoti;

    if (switchNoti) switchNotifications.setText("Disable Notifications");
    else switchNotifications.setText("Enable Notifications");
  }
}
