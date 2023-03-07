package uk.ac.soton.comp2211.team33.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import uk.ac.soton.comp2211.team33.entities.Runway;

public class RunwayState {

  private SimpleDoubleProperty tora = new SimpleDoubleProperty();

  private SimpleDoubleProperty toda = new SimpleDoubleProperty();

  private SimpleDoubleProperty asda = new SimpleDoubleProperty();

  private SimpleDoubleProperty lda = new SimpleDoubleProperty();

  private SimpleDoubleProperty resa = new SimpleDoubleProperty();

  private SimpleDoubleProperty threshold = new SimpleDoubleProperty();

  private SimpleObjectProperty<Runway> runway;


  public void createRunway(String designator, double tora, double toda, double asda, double lda, double resa, double threshold) {
    this.tora.set(tora);
    this.toda.set(toda);
    this.asda.set(asda);
    this.lda.set(lda);
    this.resa.set(resa);
    this.threshold.set(threshold);

    this.runway.set(new Runway(designator, tora, toda, asda, lda, resa, threshold));
  }

  public SimpleObjectProperty<Runway> getRunway() {
    return runway;
  }


}
