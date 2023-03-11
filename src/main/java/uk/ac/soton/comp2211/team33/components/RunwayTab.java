package uk.ac.soton.comp2211.team33.components;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.models.Aircraft;
import uk.ac.soton.comp2211.team33.models.Airport;
import uk.ac.soton.comp2211.team33.models.Obstacle;
import uk.ac.soton.comp2211.team33.models.Runway;

import java.io.IOException;

public class RunwayTab extends Tab {

  private static final Logger logger = LogManager.getLogger(RunwayTab.class);

  @FXML
  private Label tora, toda, asda, lda, resa;

  @FXML
  private Label ctora, ctoda, casda, clda, cresa;

  @FXML
  private Label thresholdOriginal, thresholdCalculated;

  @FXML
  private ChoiceBox<String> aircraftList;

  @FXML
  private ChoiceBox<String> obstacleList;

  @FXML
  private Label blastProtection;

  public RunwayTab(Runway runway, Airport state) {
    logger.info("Creating new runway tab named " + runway.getDesignator() + "...");

    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RunwayTab.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Set tab name
    setText(runway.getDesignator());

    // Set initial values of runway
    tora.setText(String.valueOf(runway.getTora()));
    toda.setText(String.valueOf(runway.getToda()));
    asda.setText(String.valueOf(runway.getAsda()));
    lda.setText(String.valueOf(runway.getLda()));
    resa.setText(String.valueOf(runway.getResa()));

    thresholdOriginal.setText(String.valueOf(runway.getThreshold()));
    thresholdCalculated.setText(String.valueOf(runway.getThreshold()));

    // Set calculated values
    ctora.setText(String.valueOf(runway.getCtora()));
    ctoda.setText(String.valueOf(runway.getCtoda()));
    casda.setText(String.valueOf(runway.getCasda()));
    clda.setText(String.valueOf(runway.getClda()));
    cresa.setText(String.valueOf(runway.getCresa()));

    // Add listeners from calculated values to listeners
    runway.ctoraProperty().addListener(((obVal, oldVal, newVal) -> ctora.setText(String.valueOf(newVal))));
    runway.ctodaProperty().addListener(((obVal, oldVal, newVal) -> ctoda.setText(String.valueOf(newVal))));
    runway.casdaProperty().addListener(((obVal, oldVal, newVal) -> casda.setText(String.valueOf(newVal))));
    runway.cldaProperty().addListener(((obVal, oldVal, newVal) -> clda.setText(String.valueOf(newVal))));
    runway.cresaProperty().addListener(((obVal, oldVal, newVal) -> cresa.setText(String.valueOf(newVal))));

    state.getAircraftListProperty().addListener((obList, oldList, newList) ->
        aircraftList.getItems().add(newList.get(newList.size() - 1).getId()));
    state.getObstacleListProperty().addListener((obList, oldList, newList) ->
        obstacleList.getItems().add(newList.get(newList.size() - 1).getName()));

    aircraftList.getItems().add("None");
    obstacleList.getItems().add("None");
  }
}
