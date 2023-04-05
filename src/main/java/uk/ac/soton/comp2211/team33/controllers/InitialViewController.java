package uk.ac.soton.comp2211.team33.controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.models.Airport;

public class InitialViewController extends BaseController {

  private static final Logger logger = LogManager.getLogger(MainController.class);

  public InitialViewController(Stage stage, Airport state) {
    super(stage, state);
  }


  @Override
  protected void initialise() {
    logger.info("Building Initial View...");

    stage.setResizable(false);
    stage.setTitle("Runway Re-Declaration Tool");

    buildScene("/views/InitialView.fxml");
  }


  @FXML
  private void configureNew() {
    logger.info("New Airport");
  }

  @FXML
  private void importExisting(){
    logger.info("Open Existing Airport");
  }


}
