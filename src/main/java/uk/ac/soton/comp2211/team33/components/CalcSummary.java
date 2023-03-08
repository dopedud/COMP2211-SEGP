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

    /**
     * Binds the text displayed in the text area to the given property.
     * @param text The property to bind the text to.
     */
    public void bindCalcText(StringProperty text) {
        calcSummary.textProperty().bind(text);
    }

    /**
     * Removes binding from the text area.
     */
    public void removeCalcTextBinding() {
        calcSummary.textProperty().unbind();
    }

    /**
     * Manually set the text in the text area, without binding to a property.
     * @param text The text to set in the text area.
     */
    public void setCalcText(String text) {
        calcSummary.setText(text);
    }


}
