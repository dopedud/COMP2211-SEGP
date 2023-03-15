package uk.ac.soton.comp2211.team33.components;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;

/**
 * The DropdownField class is a custom component that contains a choice box and a text field.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
public class DropdownField extends AnchorPane {

  /**
   * The label of the dropdown field
   */
  @FXML
  private Label label;

  /**
   * The choice box (dropdown input) of the dropdown field
   */
  @FXML
  private ChoiceBox<String> dropdown;

  public DropdownField() {
    ProjectHelpers.renderRoot("/components/DropdownField.fxml", this, this);
  }

  /**
   * @return The text of the label
   */
  public String getLabelText() {
    return label.getText();
  }

  /**
   * Set the text of the label
   *
   * @param label The text the label is being set to
   */
  public void setLabelText(String label) {
    this.label.setText(label);
  }

  /**
   * @return The current selected value of the dropdown field
   */
  public String getDropdownValue() {
    return dropdown.getValue();
  }

  /**
   * @return The list of values the dropdown field is displaying
   */
  public ObservableList<String> getDropdownList() {
    return dropdown.getItems();
  }

  /**
   * Set the selected value of the dropdown field
   *
   * @param value The value the selected value is being set to
   */
  public void setDropdownValue(String value) {
    dropdown.setValue(value);
  }

  /**
   * Set the list of values of the dropdown field
   *
   * @param list The list of values the dropdown field is having
   */
  public void setDropdownList(ObservableList<String> list) {
    dropdown.setItems(list);
  }

  /**
   * @return The choice box (dropdown input) of the dropdown field
   */
  public ChoiceBox<String> getDropdown() {
    return dropdown;
  }
}
