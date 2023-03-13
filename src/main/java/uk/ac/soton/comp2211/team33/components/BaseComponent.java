package uk.ac.soton.comp2211.team33.components;

import javafx.fxml.FXMLLoader;

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
}
