package uk.ac.soton.comp2211.team33.components;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;

/**
 * The class InputField is a custom component that contains a label and a text field.
 *
 * @author Geeth (gv2g21@soton.ac.uk)
 */
public class InputField extends HBox {

  @FXML
  private Label label;

  @FXML
  private TextField textField;

  public InputField() {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("inputField.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  public String getLabel() {
    return label.getText();
  }

  public void setLabel(String label) {
    this.label.setText(label);
  }

  public String getText() {
    return textField.getText();
  }

  public void setText(String text) {
    textField.setText(text);
  }


}