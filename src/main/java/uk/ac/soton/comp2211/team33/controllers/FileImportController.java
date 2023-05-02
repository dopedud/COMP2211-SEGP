package uk.ac.soton.comp2211.team33.controllers;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.components.InputField;
import uk.ac.soton.comp2211.team33.models.Airport;
import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;

import java.io.File;

/**
 * A controller to handle file inputs. Allows the user to specify a file path using their OS file browser.
 * Usage:
 * 1. Instantiate a FileImportController
 * 2. Bind the file location property to a StringProperty, using getSelectedFileLocationProperty()
 * 3. Call show() to show the dialog. Will wait until the dialog is closed.
 * 4. Extract the file location from the StringProperty.
 *
 * Alternatively, use ProjectHelpers.getPathWithDialog()
 *
 */
public class FileImportController extends BaseController {

  private static final Logger logger = LogManager.getLogger(FileImportController.class);

  @FXML
  private TextField fileLocation;

  /**
   * Instantiate a FileImportController.
   *
   * @param stage The application stage to render the scene in. This should be a modal stage if the dialog is to appear over the application.
   * @param state The global application state
   */
  public FileImportController(Stage stage, Airport state) {
    super(stage, state);
  }

  /**
   * Builds the scene. Note that buildScene does not show the scene, so it must be done by calling show().
   * This is so that the fileName observable can be exposed before the scene is shown.
   * @param fxmlPath name of the FXML file for this scene
   */
  protected void buildScene(String fxmlPath) {
    Scene scene = ProjectHelpers.renderScene(fxmlPath, this);
    stage.setScene(scene);
  }

  @Override
  protected void initialise() {
    logger.info("Building FileImportController...");

    stage.setResizable(false);
    stage.setTitle("Select File");

    buildScene("/views/FileImportView.fxml");
    applyStyling();
  }

  /**
   * @return A StringProperty that is bound to the fileLocation InputField. This can be used to get the selected file location.
   */
  public StringProperty getSelectedFileLocationProperty() {
    return fileLocation.textProperty();
  }

  /**
   * Shows the scene, and suspends execution until the scene is closed.
   * Ensure that the file location property is bound before calling this method.
   */
  public void show() {
    stage.showAndWait();
  }

  @FXML
  private void browseFile() {
    var fileChooser = new FileChooser();
    fileChooser.setTitle("AVIA - Import File");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
    File file = fileChooser.showOpenDialog(stage);
    fileLocation.setText(file.getAbsolutePath());
  }

  @FXML
  private void confirm() {
    String filepath = fileLocation.getText();
    fileLocation.textProperty().unbind();
    stage.close();
  }

}
