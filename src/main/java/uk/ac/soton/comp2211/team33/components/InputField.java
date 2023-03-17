package uk.ac.soton.comp2211.team33.components;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;

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

  @FXML
  private Label unitLabel;

  public InputField() {
    ProjectHelpers.renderRoot("/components/InputField.fxml", this, this);
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

  public String getUnit() {
    return unitLabel.getText();
  }
  public void setUnit(String unit) {
      unitLabel.setText(unit);
  }

  public void setTooltip(String tooltip) {
      Tooltip t = new Tooltip(tooltip);
      Tooltip.install(this.label, t);
  }

  public String getTooltip() {
      return this.label.getTooltip().getText();
  }
  public void removeTooltip() {
      this.label.setTooltip(null);
  }

}