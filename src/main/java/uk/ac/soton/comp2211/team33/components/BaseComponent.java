package uk.ac.soton.comp2211.team33.components;

import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The BaseComponent interface to create UI components.
 *
 * @author Brian (dal1g21@soton.ac.uk)
 */
public interface BaseComponent {

  /**
   * Render the UI for this component given an FXML file.
   *
   * @param fxmlUri The URI, relative to the calling class' classpath, of the FXML file
   */
  default void renderFXML(String fxmlUri) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlUri));
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Render a new stage based on the current stage.
   *
   * @param owner the owner of the new stage
   * @return the new stage to render
   */
  default Stage createModalStage(Stage owner) {
    Stage modal = new Stage();
    modal.initOwner(owner);
    modal.initModality(Modality.WINDOW_MODAL);

    return modal;
  }
}
