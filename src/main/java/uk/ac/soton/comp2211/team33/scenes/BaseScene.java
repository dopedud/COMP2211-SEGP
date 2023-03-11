package uk.ac.soton.comp2211.team33.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import uk.ac.soton.comp2211.team33.models.Airport;

/**
 * An abstract class to create scenes.
 *
 * @author Brian (dal1g21@soton.ac.uk), Geeth (gv2g21@soton.ac.uk)
 */
abstract class BaseScene {

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
  BaseScene(Stage stage, Airport state) {
    this.stage = stage;
    this.state = state;

    build();
  }

  /**
   * A method to implement to build the scene.
   */
  protected abstract void build();

  /**
   * A utility method to render the FXML markup of this scene.
   *
   * @param fxmlUri The URI, relative to the calling class' classpath, of the FXML file
   */
  protected void renderFXML(String fxmlUri) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlUri));
    loader.setController(this);
    try {
      Parent root = loader.load();
      stage.setScene(new Scene(root));
      stage.show();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Create a modal stage that is owned by the stage of this scene.
   *
   * @return The created stage to render in
   */
  protected Stage createModalStage() {
    Stage modal = new Stage();
    modal.initOwner(stage);
    modal.initModality(Modality.WINDOW_MODAL);

    return modal;
  }

  protected Stage createNewStage() {
    Stage modal = new Stage();
    modal.initModality(Modality.NONE);

    return modal;
  }
}
