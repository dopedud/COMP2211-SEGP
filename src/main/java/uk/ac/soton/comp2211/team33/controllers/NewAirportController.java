package uk.ac.soton.comp2211.team33.controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.components.InputField;
import uk.ac.soton.comp2211.team33.models.Airport;

public class NewAirportController extends BaseController {

  private static final Logger logger = LogManager.getLogger(NewAirportController.class);


  @FXML
  private InputField airportName;

  @FXML
  private InputField airportCity;

  public NewAirportController(Stage stage, Airport state) {
    super(stage, state);
  }

  // TODO: Make this look a bit nicer

  @Override
  protected void initialise() {
    logger.info("Building New Airport View...");

    stage.setResizable(false);
    stage.setTitle("Runway Re-Declaration Tool");

    buildScene("/views/NewAirportView.fxml");
  }

  @FXML
  private void onSubmitAirport() {
    try {
      String name = airportName.getText();
      String city = airportCity.getText();
      logger.info("Creating new airport with name: " + name + " and city: " + city);
      state = new Airport(name, city);
      new MainController(stage, state);
    } catch (Exception e) {
      logger.error("Error creating new airport", e);
    }
  }


}
