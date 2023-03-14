package uk.ac.soton.comp2211.team33.utilities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public final class ProjectHelpers {
  private static String BASE_PATH = "/uk/ac/soton/comp2211/team33";

  public static Object renderRoot(String path, Object controller, Object root) {
    FXMLLoader loader = new FXMLLoader(ProjectHelpers.getResource(path));

    loader.setController(controller);

    if (root != null) {
      loader.setRoot(root);
    }

    try {
      return loader.load();
    }
    catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  public static Scene renderScene(String path, Object controller) {
    return new Scene((Parent) renderRoot(path, controller, null));
  }

  public static Stage createModalStage(Stage ownerStage) {
    Stage modal = new Stage();
    modal.initOwner(ownerStage);
    modal.initModality(Modality.WINDOW_MODAL);

    return modal;
  }

  public static InputStream getResourceAsStream(String path) {
    return ProjectHelpers.class.getResourceAsStream(BASE_PATH + path);
  }

  public static URL getResource(String path) {
    return ProjectHelpers.class.getResource(BASE_PATH + path);
  }
}
