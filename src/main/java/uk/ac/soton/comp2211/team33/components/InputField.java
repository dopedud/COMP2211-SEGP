package uk.ac.soton.comp2211.team33.components;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;

/**
 * The InputField class is a custom component that contains a label, a text field, and an optional suffix text.
 *
 * @author Geeth (gv2g21@soton.ac.uk)
 */
public class InputField extends AnchorPane {

  @FXML
  private Label labelNode;

  @FXML
  private TextField textFieldNode;

  @FXML
  private Label suffixNode;

  public InputField() {
    ProjectHelpers.renderRoot("/components/InputField.fxml", this, this);
  }

  public String getLabel() {
    return labelNode.getText();
  }

  public void setLabel(String label) {
    labelNode.setText(label);
  }

  public Label getLabelNode() {
    return labelNode;
  }

  public String getText() {
    return textFieldNode.getText();
  }

  public void setText(String text) {
    textFieldNode.setText(text);
  }

  public StringProperty textProperty() {
    return textFieldNode.textProperty();
  }

  public TextField getTextFieldNode() {
    return textFieldNode;
  }

  public String getSuffix() {
    return suffixNode.getText();
  }
  public void setSuffix(String suffix) {
      suffixNode.setText(suffix);
  }

  public Label getSuffixNode() {
    return suffixNode;
  }

  public void setTooltip(String tooltip) {
      Tooltip t = new Tooltip(tooltip);
      Tooltip.install(labelNode, t);
  }

  public String getTooltip() {
      return labelNode.getTooltip().getText();
  }
  public void removeTooltip() {
      labelNode.setTooltip(null);
  }

}