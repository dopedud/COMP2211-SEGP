package uk.ac.soton.comp2211.team33.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.team33.components.DropdownField;
import uk.ac.soton.comp2211.team33.models.Airport;
import uk.ac.soton.comp2211.team33.utilities.StylePrefs;

public class OptionsController extends BaseController {

  private static final Logger logger = LogManager.getLogger(OptionsController.class);

  @FXML
  private DropdownField colourMode;

  @FXML
  private DropdownField fontSize;

  @FXML
  private DropdownField visPanelTheme;

  public OptionsController(Stage stage, Airport state) {
    super(stage, state);
  }

  @Override
  protected void initialise() {
    logger.info("Building Options View...");

    stage.setResizable(false);
    stage.setTitle("AVIA - Options");

    buildScene("/views/OptionsView.fxml");


    ObservableList<String> colourModes = FXCollections.observableArrayList();
    colourModes.addAll(StylePrefs.getColourSchemes());
    colourMode.setItems(colourModes);
    colourMode.setValue(StylePrefs.getColourScheme());


    ObservableList<String> fontSizes = FXCollections.observableArrayList();
    fontSizes.addAll(StylePrefs.getFontSizes());
    fontSize.setItems(fontSizes);
    fontSize.setValue(StylePrefs.getFontSize());

    ObservableList<String> visPanelStyles = FXCollections.observableArrayList();
    visPanelStyles.addAll(StylePrefs.getVisPanelThemes());
    visPanelTheme.setItems(visPanelStyles);
    visPanelTheme.setValue(StylePrefs.getVisPanelTheme());

  }

  @FXML
  private void cancel() {
    stage.close();
  }

  @FXML
  private void apply() {
    StylePrefs.setColourScheme(colourMode.getValue());
    StylePrefs.setFontSize(fontSize.getValue());
    StylePrefs.setVisPanelTheme(visPanelTheme.getValue());
    StylePrefs.updateStyleSheets();
    StylePrefs.updateVisPanel();
  }

  @FXML
  private void ok() {
    apply();
    cancel();
  }


}
