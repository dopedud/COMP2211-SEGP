package uk.ac.soton.comp2211.team33.components;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import java.io.IOException;

public class RunwayTab extends Tab {

  public RunwayTab() {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RunwayTab.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
