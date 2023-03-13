package uk.ac.soton.comp2211.team33.components;

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
public class InputField extends AnchorPane implements BaseComponent {

  @FXML
  private Label label;

  @FXML
  private TextField textField;

  public InputField() {
    renderFXML("InputField.fxml");
  }

  public String getLabelText() {
    return label.getText();
  }

  public void setLabelText(String label) {
    this.label.setText(label);
  }

  public String getText() {
    return textField.getText();
  }

  public void setText(String text) {
    textField.setText(text);
  }

  public Label getLabel() {
    return label;
  }

  public TextField getTextField() {
    return textField;
  }
}