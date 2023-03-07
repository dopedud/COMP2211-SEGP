
package uk.ac.soton.comp2211.team33.scenes;

import javafx.fxml.FXML;
import javafx.stage.Stage;

import uk.ac.soton.comp2211.team33.components.InputField;
import uk.ac.soton.comp2211.team33.models.AppState;

/**
 * A scene which can be used to configure new runways.
 *
 * @author Geeth (gv2g21@soton.ac.uk)
 */
public class NewRunwayScene extends BaseScene {

  @FXML
  private InputField runwayDesignator;

  @FXML
  private InputField tora;

  @FXML
  private InputField toda;

  @FXML
  private InputField asda;

  @FXML
  private InputField lda;

  @FXML
  private InputField resa;

  @FXML
  private InputField threshold;

  public NewRunwayScene(Stage stage, AppState state) {
    super(stage, state, "newRunwayScene.fxml");
  }

  /**
   * A method invoked when the runway configuration form is submitted
   */
  @FXML
  private void handleSubmitConfigurations() {

    // TODO: Validate input, display error message if invalid

    String designator;
    double toraValue = 0;
    double todaValue = 0;
    double asdaValue = 0;
    double ldaValue = 0;
    double resaValue = 0;
    double thresholdValue = 0;

    try {
      designator = runwayDesignator.getText();
      toraValue = Double.parseDouble(tora.getText());
      todaValue = Double.parseDouble(toda.getText());
      asdaValue = Double.parseDouble(asda.getText());
      ldaValue = Double.parseDouble(lda.getText());
      resaValue = Double.parseDouble(resa.getText());
      thresholdValue = Double.parseDouble(threshold.getText());
    }
    catch (NumberFormatException e) {
      System.out.println("Invalid input");
      return;
    }

    this.state.getRunwayState().createRunway(designator, toraValue, todaValue, asdaValue, ldaValue, resaValue, thresholdValue);
    stage.close();
  }

  protected void build() {
    stage.setTitle("New runway configurator");
    renderMarkup();
  }
}
