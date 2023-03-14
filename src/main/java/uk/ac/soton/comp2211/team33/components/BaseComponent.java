package uk.ac.soton.comp2211.team33.components;

import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public interface BaseComponent {
  default void renderFXML(String filename) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  default Stage createModalStage(Stage owner) {
    Stage modal = new Stage();
    modal.initOwner(owner);
    modal.initModality(Modality.WINDOW_MODAL);

    return modal;
  }
}
