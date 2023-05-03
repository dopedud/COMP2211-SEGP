package uk.ac.soton.comp2211.team33.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.models.Airport;
import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;
import uk.ac.soton.comp2211.team33.utilities.XMLHelpers;

public class SplashController extends BaseController {

  private static final Logger logger = LogManager.getLogger(MainController.class);




  /**
   * A class to control the initial splash screen displayed when the application is started.
   * @param stage The stage to use to display the splash screen.
   * @param state The current state of the application (likely null).
   */
  public SplashController(Stage stage, Airport state) {
    super(stage, state);
  }


  @Override
  protected void initialise() {
    logger.info("Building Initial View...");

    stage.setResizable(false);
    stage.setTitle("AVIA - Runway Management Tool");

    buildScene("/views/SplashView.fxml");
  }


  @FXML
  private void configureNew() {
    new NewAirportController(stage, state);
  }

  @FXML
  private void selectAirportFromList() {
    new PresetAirportController(stage, state);
  }

  @FXML
  private void importAirportFromFile() {
    var filepath = ProjectHelpers.getPathWithDialog(stage);
    if (filepath != null) {
      state = XMLHelpers.importAirport(filepath);
      new MainController(new Stage(), state = XMLHelpers.importAirport(filepath));
      stage.close();
    }
  }




}
