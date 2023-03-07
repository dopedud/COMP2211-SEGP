package uk.ac.soton.comp2211.team33.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class Runway models a runway and its values for re-declaration in an airport.
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
  private double resa;

  /**
   * Currently used runway values (after calculation).
   */
  private double ctora, ctoda, casda, clda;

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
  private double clearway, stopway, threshold;

  /**
   * Strip end constant of 60m.
   */
  private final double stripEnd = 60;

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
    } else {
      this.resa = resa;
    }

    this.ctora = tora;
    this.ctoda = toda;
    this.casda = asda;
    this.clda = lda;

    clearway = toda - tora;
    stopway = asda - tora;
    this.threshold = threshold;
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

  public void setResa(double resa) {
    this.resa = resa;
  }

  public double getThreshold() {
    return threshold;
  }

  public double getClearway() {
    return clearway;
  }

  public double getStopway() {
    return stopway;
  }

  public double getStripEnd() {
    return stripEnd;
  }

  /**
   *  Getters and setter for all current values that can be changed by a re-declaration.
   */
  public double getCtora() {
    return ctora;
  }

  public void setCtora(double ctora) {
    this.ctora = ctora;
  }

  public double getCtoda() {
    return ctoda;
  }

  public void setCtoda(double ctoda) {
    this.ctoda = ctoda;
  }

  public double getCasda() {
    return casda;
  }

  public void setCasda(double casda) {
    this.casda = casda;
  }

  public double getClda() {
    return clda;
  }

  public void setClda(double clda) {
    this.clda = clda;
  }

  public double getAls() {
    return als;
  }

  public double getTocs() {
    return tocs;
  }

  public String toString() {
    return "Runway: " + designator + "\n" +
            "TORA: " + tora + "\n" +
            "TODA: " + toda + "\n" +
            "ASDA: " + asda + "\n" +
            "LDA: " + lda + "\n" +
            "RESA: " + resa + "\n" +
            "Threshold: " + threshold + "\n" +
            "Clearway: " + clearway + "\n" +
            "Stopway: " + stopway + "\n" +
            "Strip End: " + stripEnd + "\n";
  }
}
