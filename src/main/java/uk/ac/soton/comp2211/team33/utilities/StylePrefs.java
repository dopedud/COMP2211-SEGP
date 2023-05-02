package uk.ac.soton.comp2211.team33.utilities;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * The Styling class to store and modify the visual preferences set by the user.
 */
public class StylePrefs {
  private static String colourScheme = new String("Light");

  private static String fontSize = new String("Medium");

  private static String visPanelTheme = new String("Default");

  private static ObservableList<String> styleSheets = FXCollections.observableArrayList();

  private static SimpleStringProperty visPanelThemePath = new SimpleStringProperty();

  /**
   * Private constructor to prevent instantiation.
   */
  private StylePrefs() {
  }

  public static void setColourScheme(String colourScheme) {
    StylePrefs.colourScheme = colourScheme;
  }

  public static String getColourScheme() {
    return colourScheme;
  }

  public static void setFontSize(String fontSize) {
    StylePrefs.fontSize = fontSize;
  }

  public static String getFontSize() {
    return fontSize;
  }

  public static void setVisPanelTheme(String visPanelTheme) {
    StylePrefs.visPanelTheme = visPanelTheme;
  }

  public static String getVisPanelTheme() {
    return visPanelTheme;
  }

  public static String[] getColourSchemes() {
    return new String[] {"Light", "Dark", "High Contrast"};
  }

  public static String[] getFontSizes() {
    return new String[] {"Small", "Medium", "Large"};
  }

  public static String[] getVisPanelThemes() {
    return new String[] {"Default", "High Contrast", "Colour Blind"};
  }

  public static ObservableList<String> getStylesSheetsProperty() {
    return StylePrefs.styleSheets;
  }

  public static String[] getStyleSheets() {
    return StylePrefs.styleSheets.toArray(new String[0]);
  }

  public static SimpleStringProperty getVisPanelThemePathProperty() {
    return StylePrefs.visPanelThemePath;
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

  public static void updateVisPanel() {
    visPanelThemePath.set("/style/vis/" + getVisPanelTheme().toLowerCase().replace(" ", "_") + ".txt");
  }

}
