package uk.ac.soton.comp2211.team33.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import uk.ac.soton.comp2211.team33.models.AirportState;

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
  protected AirportState state;

  private String markupUri;

  /**
   * Instantiate a scene.
   *
   * @param stage The application stage to render the scene in
   * @param state The global application state
   * @param markupUri The FXML markup URI of this scene
   */
  BaseScene(Stage stage, AirportState state, String markupUri) {
    this.stage = stage;
    this.state = state;
    this.markupUri = markupUri;

    this.build();
  }

  /**
   * A method to implement to render the scene.
   */
  protected abstract void build();

  /**
   * A utility method to render the FXML markup of this scene.
   */
  protected void renderMarkup() {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(markupUri));
    loader.setController(this);

    try {
      Parent root = loader.load();
      stage.setScene(new Scene(root));
      stage.show();
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  protected Stage createModalStage() {
    Stage modal = new Stage();
    modal.initOwner(stage);
    modal.initModality(Modality.WINDOW_MODAL);

    return modal;
  }
}
