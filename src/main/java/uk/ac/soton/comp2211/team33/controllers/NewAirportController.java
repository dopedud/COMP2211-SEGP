package uk.ac.soton.comp2211.team33.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import uk.ac.soton.comp2211.team33.components.DropdownField;
import uk.ac.soton.comp2211.team33.models.Airport;
import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;

/**
 * The NewAirportController controller class that creates a new airport upon user request.
 * Corresponds to user story #1.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
public class NewAirportController extends BaseController {

  private static final Logger logger = LogManager.getLogger(NewAirportController.class);

  /**
   * A JavaFX UI element to select a city for the airport.
   */
  @FXML
  private DropdownField city;

  /**
   * A JavaFX UI element to select a name for the airport.
   */
  @FXML
  private DropdownField name;

  /**
   * Multiple key-value pairs to store different commercial airports throughout the UK.
   */
  private MultiValuedMap<String, String> cityNamePairs;

  public NewAirportController(Stage stage, Airport state) {
    super(stage, state);
  }

  @Override
  protected void initialise() {
    logger.info("Building NewAirportController...");

    stage.setResizable(false);
    stage.setTitle("New Airport");

    buildScene("/views/NewAirportView.fxml");
    loadPredefinedAirports();
  }

  @FXML
  private void onSubmitAirport() {
    new MainController(stage, new Airport(city.getValue(), name.getValue()));
  }

  private void loadPredefinedAirports() {
    cityNamePairs = new ArrayListValuedHashMap<>();

    try {
      InputStream stream = ProjectHelpers.getResourceAsStream("/data/airports.csv");
      BufferedReader br = new BufferedReader(new InputStreamReader(stream));
      String line;

      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");

        cityNamePairs.put(values[0], values[1]);
      }
    }
    catch (IOException e) {
      logger.error("Error loading airports from CSV file.");
      e.printStackTrace();
    }

    city.setItems(FXCollections.observableArrayList(cityNamePairs.keySet()));
    city.getItems().add("-");
    city.setValue("-");

    name.getItems().add("-");
    name.setValue("-");

    city.valueProperty().addListener((obVal, oldVal, newVal) -> {
      if (newVal == null) return;

      name.setItems(FXCollections.observableArrayList(cityNamePairs.get(newVal)));
      name.getItems().add("-");
      name.setValue("-");
    });
  }
}
