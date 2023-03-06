package uk.ac.soton.comp2211.team33.controllers;

import javafx.fxml.FXML;
import uk.ac.soton.comp2211.team33.components.InputField;
import uk.ac.soton.comp2211.team33.entities.Aircraft;


public class NewAircraftController extends BaseController {

    @FXML
    private InputField id;
    @FXML
    private InputField blastProtection;

    private Aircraft aircraft;

    public void createAircraft() {
        String idValue = null;
        double blastProtectionValue = 0;

        try {
            idValue = id.getText();
            blastProtectionValue = Double.parseDouble(blastProtection.getText());
        } catch (NumberFormatException e) {
            // should be replaced with a proper error message
            System.err.println("Invalid input");
        }

        aircraft = new Aircraft(idValue, blastProtectionValue);

        System.out.println("Creating aircraft... \n" + aircraft.getAircraftID() + " " + aircraft.getBlastProtection());

    }

    public Aircraft getAircraft() {
        return aircraft;
    }
}
