package uk.ac.soton.comp2211.team33.components;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * A component to display a summary of calculations.
 */
public class CalcSummary extends VBox {

    @FXML private TextArea calcSummary;

    public CalcSummary() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("calcSummary.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    public void bindCalcText(StringProperty text) {
        calcSummary.textProperty().bind(text);
    }

    public void removeCalcTextBinding() {
        calcSummary.textProperty().unbind();
    }

    public void setCalcText(String text) {
        calcSummary.setText(text);
    }


}
