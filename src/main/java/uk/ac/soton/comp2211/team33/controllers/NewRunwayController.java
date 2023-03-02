package uk.ac.soton.comp2211.team33.controllers;

import javafx.scene.control.TextField;
import uk.ac.soton.comp2211.team33.entities.Runway;

public class NewRunwayController extends BaseController {
    public TextField runwayDesignator;
    public TextField tora;
    public TextField toda;
    public TextField asda;
    public TextField lda;
    public TextField resa;
    public TextField threshold;
    public TextField clearway;
    public TextField stopway;
    public TextField stripEnd;
    public TextField blastProtection;

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
        double clearwayValue = 0;
        double stopwayValue = 0;
        double stripEndValue = 0;
        double blastProtectionValue = 0;

        try {
            designator = runwayDesignator.getText();
            toraValue = Double.parseDouble(tora.getText());
            todaValue = Double.parseDouble(toda.getText());
            asdaValue = Double.parseDouble(asda.getText());
            ldaValue = Double.parseDouble(lda.getText());
            resaValue = Double.parseDouble(resa.getText());
            thresholdValue = Double.parseDouble(threshold.getText());
            clearwayValue = Double.parseDouble(clearway.getText());
            stopwayValue = Double.parseDouble(stopway.getText());
            stripEndValue = Double.parseDouble(stripEnd.getText());
            blastProtectionValue = Double.parseDouble(blastProtection.getText());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
        }

        newRunway = new Runway(designator, toraValue, todaValue, asdaValue, ldaValue, resaValue, thresholdValue,
                clearwayValue, stopwayValue, stripEndValue, blastProtectionValue);

        System.out.println("Creating runway... \n" + newRunway);

    }
}
