package uk.ac.soton.comp2211.team33.components;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * The DropdownField class is a custom component that contains a choice box and a text field.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
public class DropdownField extends AnchorPane implements BaseComponent {

  @FXML
  private Label label;

  @FXML
  private ChoiceBox<String> dropdown;

  public DropdownField() {
    renderFXML("DropdownField.fxml");
  }
  public String getLabelText() {
    return label.getText();
  }

  public void setLabelText(String label) {
    this.label.setText(label);
  }

  public String getDropdownValue() {
    return dropdown.getValue();
  }

  public ObservableList<String> getDropdownList() {
    return dropdown.getItems();
  }

  public void setDropdownValue(String value) {
    dropdown.setValue(value);
  }

  public void setDropdownList(ObservableList<String> list) {
    dropdown.setItems(list);
  }

  public ChoiceBox<String> getDropdown() {
    return dropdown;
  }
}
