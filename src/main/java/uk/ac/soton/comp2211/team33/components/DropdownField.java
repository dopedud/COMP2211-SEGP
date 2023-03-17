package uk.ac.soton.comp2211.team33.components;

import javafx.beans.property.ObjectProperty;
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
  private Label labelNode;

  /**
   * The choice box (dropdown input) of the dropdown field
   */
  @FXML
  private ChoiceBox<String> dropdownNode;

  public DropdownField() {
    ProjectHelpers.renderRoot("/components/DropdownField.fxml", this, this);
  }

  /**
   * @return The text of the label
   */
  public String getLabel() {
    return labelNode.getText();
  }

  /**
   * Set the text of the label
   *
   * @param label The text the label is being set to
   */
  public void setLabel(String label) {
    labelNode.setText(label);
  }

  /**
   * @return The current selected value of the dropdown field
   */
  public String getValue() {
    return dropdownNode.getValue();
  }

  /**
   * @return The list of values the dropdown field is displaying
   */
  public ObservableList<String> getItems() {
    return dropdownNode.getItems();
  }

  /**
   * Set the selected value of the dropdown field
   *
   * @param value The value the selected value is being set to
   */
  public void setValue(String value) {
    dropdownNode.setValue(value);
  }

  /**
   * Set the list of values of the dropdown field
   *
   * @param list The list of values the dropdown field is having
   */
  public void setItems(ObservableList<String> list) {
    dropdownNode.setItems(list);
  }

  /**
   * @return The choice box (dropdown input) of the dropdown field
   */
  public ObjectProperty<String> valueProperty() {
    return dropdownNode.valueProperty();
  }
}
