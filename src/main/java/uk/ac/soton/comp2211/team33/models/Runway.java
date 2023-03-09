package uk.ac.soton.comp2211.team33.models;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class Runway models a runway and its values for re-declaration in an airport.
 *
 * Corresponds to user story #3.
 *
 * @author Jackson (jl14u21@soton.ac.uk)
 */
public class Runway {

  private static final Logger logger = LogManager.getLogger(Runway.class);

  /**
   * The designator for the runway. Usually 2 characters with L/R at the end.
   */
  private final String designator;

  /**
   * Initial values of the runway.
   */
  private final double tora, toda, asda, lda, resa;

  /**
   * Currently used runway values (after calculation).
   */
  private SimpleDoubleProperty ctora = new SimpleDoubleProperty();

  private SimpleDoubleProperty ctoda = new SimpleDoubleProperty();

  private SimpleDoubleProperty casda = new SimpleDoubleProperty();

  private SimpleDoubleProperty clda = new SimpleDoubleProperty();

  /**
   * RESA value of the runway, could be re-declared
   */
  private SimpleDoubleProperty cresa = new SimpleDoubleProperty();

  /**
   * TOCS constant 50m.
   */
  private final double tocs = 50;

  /**
   * ALS constant 50m.
   */
  private final double als = 50;

  /**
   * Displaced threshold, clearway, stopway, and strip end.
   */
  private SimpleDoubleProperty clearway = new SimpleDoubleProperty();

  private SimpleDoubleProperty stopway = new SimpleDoubleProperty();

  private SimpleDoubleProperty threshold = new SimpleDoubleProperty();

  /**
   * Strip end constant of 60m.
   */
  private final double stripEnd = 60;

  /**
   * The distance of the obstacle from the threshold of the runway.
   */
  private SimpleDoubleProperty obstacleDistance = new SimpleDoubleProperty(0);

  public Runway(String designator, double tora, double toda, double asda, double lda,
                double resa, double threshold) {
    this.designator = designator;

    this.tora = tora;
    this.toda = toda;
    this.asda = asda;
    this.lda = lda;

    if (resa < 240) {
      logger.info("RESA value below 240m. Setting it as 240m minimum value...");
      this.resa = 240;
      this.cresa.set(240);
    } else {
      this.resa = resa;
      this.cresa.set(resa);
    }

    this.ctora.set(tora);
    this.ctoda.set(toda);
    this.casda.set(asda);
    this.clda.set(lda);

    clearway.set(toda - tora);
    stopway.set(asda - tora);
    this.threshold.set(threshold);
  }


  /**
   * Below are getters and setters for some values that don't have to change but may be used in certain calculations.
   */
  public String getDesignator() {
    return designator;
  }

  public double getTora() {
    return tora;
  }

  public double getToda() {
    return toda;
  }

  public double getAsda() {
    return asda;
  }

  public double getLda() {
    return lda;
  }

  public double getResa() {
    return resa;
  }

  public SimpleDoubleProperty cresaProperty() {
    return cresa;
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

  public double getClearway() {
    return clearway.get();
  }

  public SimpleDoubleProperty clearwayProperty() {
    return clearway;
  }

  public double getStopway() {
    return stopway.get();
  }

  public SimpleDoubleProperty stopwayProperty() {
    return stopway;
  }

  public double getThreshold() {
    return threshold.get();
  }

  public SimpleDoubleProperty thresholdProperty() {
    return threshold;
  }

  public double getStripEnd() {
    return stripEnd;
  }

  public double getCresa() {
    return cresa.get();
  }

  public void setCresa(double resa) {
    this.cresa.set(resa);
  }

  public double getCtora() {
    return ctora.get();
  }

  public void setCtora(double ctora) {
    this.ctora.set(ctora);
  }

  public double getCtoda() {
    return ctoda.get();
  }

  public void setCtoda(double ctoda) {
    this.ctoda.set(ctoda);
  }

  public double getCasda() {
    return casda.get();
  }

  public void setCasda(double casda) {
    this.casda.set(casda);
  }

  public double getClda() {
    return clda.get();
  }

  public void setClda(double clda) {
    this.clda.set(clda);
  }

  /**
   *  Getters and setter for all current values that can be changed by a re-declaration.
   */

  public double getAls() {
    return als;
  }

  public double getTocs() {
    return tocs;
  }

  public SimpleDoubleProperty obstacleDistanceProperty() {
    return obstacleDistance;
  }

  public double getObstacleDistance() {
      return obstacleDistance.get();
  }

  public String toString() {
    return designator;
  }

  public String getInformationString() {
    return "TORA: " + tora + "\n" +
      "TODA: " + toda + "\n" +
      "ASDA: " + asda + "\n" +
      "LDA: " + lda + "\n" +
      "RESA: " + resa + "\n";
  }

  public String getCInformationString() {
    return "TORA: " + ctora.get() + "\n" +
      "TODA: " + ctoda.get() + "\n" +
      "ASDA: " + casda.get() + "\n" +
      "LDA: " + clda.get() + "\n" +
      "RESA: " + cresa.get() + "\n";
  }
}
