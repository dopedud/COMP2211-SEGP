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
import java.util.HashMap;

import uk.ac.soton.comp2211.team33.components.DropdownField;
import uk.ac.soton.comp2211.team33.models.Airport;
import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;
import uk.ac.soton.comp2211.team33.utilities.XMLHelpers;

/**
 * The NewAirportController controller class that creates a new airport upon user request.
 * Corresponds to user story #1.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
public class PresetAirportController extends BaseController {

  private static final Logger logger = LogManager.getLogger(PresetAirportController.class);


  /**
   * A JavaFX UI element to select a name for the airport.
   */
  @FXML
  private DropdownField name;

  private HashMap<String, String> nameToPath;

  public PresetAirportController(Stage stage, Airport state) {
    super(stage, state);
  }


  @Override
  protected void initialise() {
    logger.info("Building NewAirportController...");

    stage.setResizable(false);
    stage.setTitle("AVIA - Load Preset Airport");

    buildScene("/views/PresetAirportView.fxml");

    nameToPath = new HashMap<String, String>();
    loadPredefinedAirports();
  }

  @FXML
  private void onSubmitAirport() {
    if (name.getValue().equals("-")) {
      return;
    }

    String airportPath = ProjectHelpers.getResource("/data/presets/" + nameToPath.get(name.getValue())).toExternalForm();
    Airport airport = XMLHelpers.importAirport(airportPath);
    new MainController(new Stage(), airport);
    stage.close();
  }

  private void loadPredefinedAirports() {
    logger.info("Loading airports from CSV file: " + ProjectHelpers.getResource("/data/airports.csv").toExternalForm() + "...");

    try {
      InputStream stream = ProjectHelpers.getResourceAsStream("/data/airports.csv");
      BufferedReader br = new BufferedReader(new InputStreamReader(stream));
      String line;

      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");

        nameToPath.put(values[0], values[1]);
      }
    }
    catch (IOException e) {
      logger.error("Error loading airports from CSV file.");
      e.printStackTrace();
    }

    name.setItems(FXCollections.observableArrayList(nameToPath.keySet()));
    name.getItems().add("-");
    name.setValue("-");

  }
}
