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

public class NewAirportScene extends BaseScene {

  private static final Logger logger = LogManager.getLogger(NewAirportScene.class);

  @FXML
  private DropdownField city;

  @FXML
  private DropdownField name;

  private MultiValuedMap<String, String> cityNamePairs;

  public NewAirportScene(Stage stage, Airport state) {
    super(stage, state);
  }

  @Override
  protected void build() {
    logger.info("Building NewAirportScene...");

    stage.setResizable(false);
    stage.setTitle("New Airport");

    renderFXML("NewAirportScene.fxml");

    loadPredefinedAirports();
  }

  @FXML
  private void onSubmitAirport() {
    new MainScene(stage, new Airport(city.getDropdownValue(), name.getDropdownValue()));
  }

  private void loadPredefinedAirports() {
    cityNamePairs = new ArrayListValuedHashMap<>();

    try {
      InputStream stream = getClass().getResourceAsStream("/uk/ac/soton/comp2211/team33/data/airports.csv");
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

    city.setDropdownList(FXCollections.observableArrayList(cityNamePairs.keySet()));
    city.getDropdownList().add("-");
    city.setDropdownValue("-");

    name.getDropdownList().add("-");
    name.setDropdownValue("-");

    city.getDropdown().valueProperty().addListener((obVal, oldVal, newVal) -> {
      if (newVal == null) return;

      name.setDropdownList(FXCollections.observableArrayList(cityNamePairs.get(newVal)));
      name.getDropdownList().add("-");
      name.setDropdownValue("-");
    });
  }
}
