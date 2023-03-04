package uk.ac.soton.comp2211.team33.controllers;

import javafx.fxml.FXML;
import uk.ac.soton.comp2211.team33.components.InputField;
import uk.ac.soton.comp2211.team33.entities.Runway;

/**
 * Controller for the interfacce to configure a new runway.
 */
public class NewRunwayController extends BaseController {

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

    private Runway newRunway;

    /**
     * Creates a new runway based on the input values in the text fields.
     */
    public void createRunway() {
        // TODO: Validate input, display error message if invalid
        String designator = null;
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
      } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        }

        newRunway = new Runway(designator, toraValue, todaValue, asdaValue, ldaValue, resaValue,
                thresholdValue);

        System.out.println("Creating runway... \n" + newRunway);

    }
}
