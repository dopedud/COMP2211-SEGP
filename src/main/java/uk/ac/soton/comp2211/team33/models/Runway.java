package uk.ac.soton.comp2211.team33.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Runway {

  private static final Logger logger = LogManager.getLogger(Runway.class);

  private final SimpleStringProperty designator;

  private double tora, toda, asda, lda;

  private final SimpleDoubleProperty ctora, ctoda, casda, clda, resa;

  private final SimpleDoubleProperty clearway, stopway, threshold;

  private final double tocs = 50, als = 50, stripEnd = 60;

  public Runway(String designator, double tora, double toda, double asda, double lda,
                double resa, double threshold) {
    this.tora = tora;
    this.toda = toda;
    this.asda = asda;
    this.lda = lda;

    this.designator = new SimpleStringProperty(designator);
    ctora = new SimpleDoubleProperty(tora);
    ctoda = new SimpleDoubleProperty(toda);
    casda = new SimpleDoubleProperty(asda);
    clda = new SimpleDoubleProperty(lda);
    this.resa = new SimpleDoubleProperty(resa);
    clearway = new SimpleDoubleProperty();
    stopway = new SimpleDoubleProperty();
    this.threshold = new SimpleDoubleProperty(threshold);

    if (resa < 240) {
      logger.info("RESA value below 240m. Setting it as 240m minimum value...");
      this.resa.set(240);
    }
    else {
      this.resa.set(resa);
    }

    clearway.set(toda - tora);
    stopway.set(asda - tora);
    this.threshold.set(threshold);
  }

  public SimpleStringProperty designatorProperty() {
    return designator;
  }

  public SimpleDoubleProperty ctoraProperty() {
    return ctora;
  }

  public SimpleDoubleProperty ctodaProperty() {
    return ctoda;
  }

  public SimpleDoubleProperty casdaProperty() {
    return casda;
  }

  public SimpleDoubleProperty cldaProperty() {
    return clda;
  }

  public SimpleDoubleProperty resaProperty() {
    return resa;
  }

  public SimpleDoubleProperty clearwayProperty() {
    return clearway;
  }

  public SimpleDoubleProperty stopwayProperty() {
    return stopway;
  }

  public double getTocs() {
    return tocs;
  }

  public double getAls() {
    return als;
  }

  public double getStripEnd() {
    return stripEnd;
  }
}
