package uk.ac.soton.comp2211.team33.utilities;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * The Styling class to store and modify the visual preferences set by the user.
 */
public class StylePrefs {
  public static SimpleStringProperty colourScheme = new SimpleStringProperty("Dark");

  public static SimpleStringProperty fontSize = new SimpleStringProperty("Medium");

  public static ObservableList<String> styleSheets = FXCollections.observableArrayList();

  /**
   * Private constructor to prevent instantiation.
   */
  private StylePrefs() {
  }

  public static void setColourScheme(String colourScheme) {
    StylePrefs.colourScheme.set(colourScheme);
  }

  public static String getColourScheme() {
    return colourScheme.get();
  }

  public static void setFontSize(String fontSize) {
    StylePrefs.fontSize.set(fontSize);
  }

  public static String getFontSize() {
    return fontSize.get();
  }

  public static String[] getColourSchemes() {
    return new String[] {"Light", "Dark", "High Contrast"};
  }

  public static String[] getFontSizes() {
    return new String[] {"Small", "Medium", "Large"};
  }

  public static ObservableList<String> getStylesSheetsProperty() {
    return StylePrefs.styleSheets;
  }

  public static String[] getStyleSheets() {
    return StylePrefs.styleSheets.toArray(new String[0]);
  }

  /**
   * Updates the style sheets list to reflect the current colour scheme and font size.
   */
  public static void updateStyleSheets() {
    var sheets = new ArrayList<String>();
    sheets.add("/style/" + getColourScheme().toLowerCase().replace(" ", "_") + ".css");
    sheets.add("/style/" + getFontSize().toLowerCase().replace(" ", "_") + ".css");
    styleSheets.setAll(sheets);
  }

}
