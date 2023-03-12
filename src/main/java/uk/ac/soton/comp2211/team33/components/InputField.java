package uk.ac.soton.comp2211.team33.components;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * The InputField class is a custom component that contains a label and a text field.
 *
 * @author Geeth (gv2g21@soton.ac.uk)
 */
public class InputField extends AnchorPane {

  @FXML
  private Label label;

  @FXML
  private TextField textField;

  public InputField() {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("InputField.fxml"));
    loader.setRoot(this);
    loader.setController(this);

    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
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