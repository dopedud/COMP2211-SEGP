package uk.ac.soton.comp2211.team33.models;

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
  private final double tora, toda, asda, lda;

  /**
   * RESA value of the runway, could be re-declared
   */
  private SimpleDoubleProperty resa = new SimpleDoubleProperty();

  /**
   * Currently used runway values (after calculation).
   */
  private SimpleDoubleProperty ctora = new SimpleDoubleProperty();

  private SimpleDoubleProperty ctoda = new SimpleDoubleProperty();

  private SimpleDoubleProperty casda = new SimpleDoubleProperty();

  private SimpleDoubleProperty clda = new SimpleDoubleProperty();

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
   * A summary of the last calculation performed on the runway.
   */
  private SimpleStringProperty summaryOfCalculation = new SimpleStringProperty();

  public Runway(String designator, double tora, double toda, double asda, double lda,
                double resa, double threshold) {
    this.designator = designator;

    this.tora = tora;
    this.toda = toda;
    this.asda = asda;
    this.lda = lda;

    if (resa < 240) {
      logger.info("RESA value below 240m. Setting it as 240m minimum value...");
      this.resa.set(240);
    } else {
      this.resa.set(resa);
    }

    this.ctora.set(tora);
    this.ctoda.set(toda);
    this.casda.set(asda);
    this.clda.set(lda);

    clearway.set(toda - tora);
    stopway.set(asda - tora);
    this.threshold.set(threshold);

    summaryOfCalculation.set("No calculation has been performed on Runway " + this.getDesignator() + " yet.");
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

  public SimpleDoubleProperty resaProperty() {
    return resa;
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

  public double getResa() {
    return resa.get();
  }

  public void setResa(double resa) {
    this.resa.set(resa);
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

  public SimpleStringProperty calculationSummaryProperty() {
    return summaryOfCalculation;
  }

  public String getCalculationSummary() {
      return summaryOfCalculation.get();
  }

  public void setCalculationSummary(String summary) {
      summaryOfCalculation.set(summary);
  }

  public String toString() {
    return designator;
  }

  public String toFullString() {
    return "Runway: " + designator + "\n" +
            "TORA: " + ctora.get() + "\n" +
            "TODA: " + ctoda.get() + "\n" +
            "ASDA: " + casda.get() + "\n" +
            "LDA: " + clda.get() + "\n" +
            "RESA: " + resa.get() + "\n" +
            "Threshold: " + threshold.get() + "\n" +
            "Clearway: " + clearway.get() + "\n" +
            "Stopway: " + stopway.get() + "\n" +
            "Strip End: " + stripEnd + "\n";
  }
}
