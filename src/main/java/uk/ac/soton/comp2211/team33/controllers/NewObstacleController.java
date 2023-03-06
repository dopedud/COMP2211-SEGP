package uk.ac.soton.comp2211.team33.controllers;

import javafx.fxml.FXML;
import uk.ac.soton.comp2211.team33.components.InputField;
import uk.ac.soton.comp2211.team33.entities.Obstacle;

public class NewObstacleController extends BaseController {

    @FXML
    private InputField name;
    @FXML
    private InputField height;
    @FXML
    private InputField distanceThreshold;

    private Obstacle obstacle;

    public void createObstacle() {
        String nameValue = null;
        double heightValue = 0;
        double distanceThresholdValue = 0;

        try {
            nameValue = name.getText();
            heightValue = Double.parseDouble(height.getText());
            distanceThresholdValue = Double.parseDouble(distanceThreshold.getText());
        } catch (NumberFormatException e) {
            // should be replaced with a proper error message
            System.err.println("Invalid input");
        }

        obstacle = new Obstacle(nameValue, heightValue, distanceThresholdValue);

        System.out.println("Creating obstacle... \n" + obstacle);

    }

    public Obstacle getObstacle() {
        return obstacle;
    }

}
