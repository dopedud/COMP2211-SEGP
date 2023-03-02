package uk.ac.soton.comp2211.team33.controllers;

import javafx.scene.control.TextField;

public class NewRunwayController extends BaseController {
    public TextField runwayName;
    public TextField runwayLength;

    public void createRunway() {
        String name = runwayName.getText();
        String length = runwayLength.getText();
        System.out.println("Creating runway " + name + " with length " + length);
    }
}
