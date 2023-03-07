package uk.ac.soton.comp2211.team33.models;

import javafx.beans.property.SimpleObjectProperty;
import uk.ac.soton.comp2211.team33.entities.Runway;

public class RunwayState {
  private SimpleObjectProperty<Runway> runway = new SimpleObjectProperty();

  public SimpleObjectProperty<Runway> getRunway() {
    return runway;
  }

  public void createRunway(String designator, double tora, double toda, double asda, double lda, double resa, double threshold) {
    Runway runway = new Runway(designator, tora, toda, asda, lda, resa, threshold);
    this.runway.set(runway);

  }
}
