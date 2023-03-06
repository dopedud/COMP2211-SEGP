package uk.ac.soton.comp2211.team33.models;

import javafx.beans.property.SimpleObjectProperty;

class RunwayState {
  private SimpleObjectProperty runway = new SimpleObjectProperty();

  public SimpleObjectProperty getRunway() {
    return runway;
  }
}
