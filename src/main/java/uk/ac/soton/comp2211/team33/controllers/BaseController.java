package uk.ac.soton.comp2211.team33.controllers;

import javafx.scene.Scene;
import javafx.stage.Stage;

import uk.ac.soton.comp2211.team33.models.Airport;
import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;

/**
 * The BaseScene abstract class to create scenes.
 *
 * @author Brian (dal1g21@soton.ac.uk), Geeth (gv2g21@soton.ac.uk)
 */
abstract class BaseController {

  /**
   * The application window this scene is rendered in.
   */
  protected Stage stage;

  /**
   * Global application state.
   */
  protected Airport state;

  /**
   * Instantiate a scene.
   *
   * @param stage The application stage to render the scene in
   * @param state The global application state
   */
  BaseController(Stage stage, Airport state) {
    this.stage = stage;
    this.state = state;

    this.initialise();
  }

  /**
   * Abstract method to initialise the scene.
   */
  abstract protected void initialise();

  /**
   * Protected method to build the scene.
   *
   * @param filename name of the FXML file for this scene
   */
  protected void buildScene(String filename) {
    Scene scene = ProjectHelpers.renderScene(filename, this);
    stage.setScene(scene);
    stage.show();
  }
}
