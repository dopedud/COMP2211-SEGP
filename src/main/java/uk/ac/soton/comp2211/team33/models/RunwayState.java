package uk.ac.soton.comp2211.team33.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RunwayState {

  private static final Logger logger = LogManager.getLogger(RunwayState.class);

  private final SimpleStringProperty designator;

  private double tora, toda, asda, lda;

  private final SimpleDoubleProperty ctora, ctoda, casda, clda, resa;

  private final SimpleDoubleProperty clearway, stopway, threshold;

  private final double tocs = 50, als = 50, stripEnd = 60;

  public RunwayState() {
    designator = new SimpleStringProperty();
    ctora = new SimpleDoubleProperty();
    ctoda = new SimpleDoubleProperty();
    casda = new SimpleDoubleProperty();
    clda = new SimpleDoubleProperty();
    resa = new SimpleDoubleProperty();
    clearway = new SimpleDoubleProperty();
    stopway = new SimpleDoubleProperty();
    threshold = new SimpleDoubleProperty();
  }

  public void createRunway(String designator, double tora, double toda, double asda, double lda,
                           double resa, double threshold) {
    this.designator.set(designator);

    this.tora = tora;
    this.toda = toda;
    this.asda = asda;
    this.lda = lda;

    ctora.set(tora);
    ctoda.set(toda);
    casda.set(asda);
    clda.set(lda);

    if (resa < 240) {
      logger.info("RESA value below 240m. Setting it as 240m minimum value...");
      this.resa.set(240);
    } else this.resa.set(resa);

    clearway.set(toda - tora);
    stopway.set(asda - tora);
    this.threshold.set(threshold);
  }

  public SimpleStringProperty getDesignator() {
    return designator;
  }

  public SimpleDoubleProperty getCtoraProperty() {
    return ctora;
  }

  public SimpleDoubleProperty getCtodaProperty() {
    return ctoda;
  }

  public SimpleDoubleProperty getCasdaProperty() {
    return casda;
  }

  public SimpleDoubleProperty getCldaProperty() {
    return clda;
  }

  public SimpleDoubleProperty getResaProperty() {
    return resa;
  }

  public SimpleDoubleProperty getClearwayProperty() {
    return clearway;
  }

  public SimpleDoubleProperty getStopwayProperty() {
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
