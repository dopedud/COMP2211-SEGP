package uk.ac.soton.comp2211.team33.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class VisualisationState {
  private SimpleIntegerProperty viewMode = new SimpleIntegerProperty(0);

  private SimpleDoubleProperty zoomLevel = new SimpleDoubleProperty(0);

  public void increaseZoom(int step) {
    this.zoomLevel.add(step);
  }
}
