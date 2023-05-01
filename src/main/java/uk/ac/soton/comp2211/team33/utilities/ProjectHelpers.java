package uk.ac.soton.comp2211.team33.utilities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import uk.ac.soton.comp2211.team33.controllers.FileImportController;
import uk.ac.soton.comp2211.team33.models.Airport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * The ProjectHelpers static class that is tasked to render scenes and stages and to fetch JavaFX resources.
 *
 * @author Brian (dal1g21@soton.ac.uk)
 */
public final class ProjectHelpers {

  /**
   * Base path to fetch all files associated with the project.
   */
  private static final String BASE_PATH = "/uk/ac/soton/comp2211/team33";

  /**
   * Private constructor to avoid instancing.
   */
  private ProjectHelpers() {}

  /**
   * Method to render an object in JavaFX.
   *
   * @param path file path to the JavaFX FXML file
   * @param controller controller class for this object
   * @param root parent for this object if one exists, null if this object is the root
   * @return an object to render
   */
  public static Object renderRoot(String path, Object controller, Object root) {
    FXMLLoader loader = new FXMLLoader(ProjectHelpers.getResource(path));

    loader.setController(controller);

    if (root != null) loader.setRoot(root);

    try {
      return loader.load();
    }
    catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Method to render a JavaFX scene.
   *
   * @param path file path to the JavaFX FXML file
   * @param controller controller class for this scene
   * @return a scene to render
   */
  public static Scene renderScene(String path, Object controller) {
    return new Scene((Parent) renderRoot(path, controller, null));
  }

  /**
   * Method to create a modal stage.
   *
   * @param owner owner of the modal stage
   * @return a modal stage to render
   */
  public static Stage createModalStage(Stage owner) {
    Stage modal = new Stage();
    modal.initOwner(owner);
    modal.initModality(Modality.WINDOW_MODAL);

    return modal;
  }

  /**
   * Method to read text files.
   *
   * @param path file path to the text file
   * @return an input stream of data from file
   */
  public static InputStream getResourceAsStream(String path) {
    return ProjectHelpers.class.getResourceAsStream(BASE_PATH + path);
  }

  /**
   * Method to get JavaFX resources.
   *
   * @param path file path to the JavaFX resource
   * @return a URL containing the JavaFX resource
   */
  public static URL getResource(String path) {
    return ProjectHelpers.class.getResource(BASE_PATH + path);
  }

  public static String getPathWithDialog(Stage stage) {
    // getting a filepath with a file import controller
    StringProperty filepath = new SimpleStringProperty();
    var fic = new FileImportController(ProjectHelpers.createModalStage(stage), null);
    filepath.bind(fic.getSelectedFileLocationProperty());
    fic.show();
    return filepath.get();
  }

  public static String getSavePathWithDialog(Stage stage) {
    var fileChooser = new FileChooser();
    fileChooser.setTitle("Save File");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
    File file = fileChooser.showSaveDialog(stage);
    return file.getAbsolutePath();
  }

}
