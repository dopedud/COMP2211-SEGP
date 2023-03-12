package uk.ac.soton.comp2211.team33.components;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * The DropdownField class is a custom component that contains a choice box and a text field.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
public class DropdownField extends AnchorPane {

  @FXML
  private Label label;

  @FXML
  private ChoiceBox<String> dropdown;

  public DropdownField() {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("DropdownField.fxml"));
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

  public ChoiceBox<String> dropdownProperty() {
    return dropdown;
  }
}
