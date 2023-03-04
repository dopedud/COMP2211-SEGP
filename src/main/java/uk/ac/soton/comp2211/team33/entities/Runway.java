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
   * Current obstacle.
   */
  private Obstacle currentObs = null;

  /**
   * The aircraft that is about to land on this runway.
   */
  private Aircraft aircraft = null;

  /**
   * The designator for the runway. Usually 2 characters with L/R at the end.
   */
  private final String rdesignator;

  /**
   * Initial values of the runway.
   */
  private final double tora, toda, asda, lda, resa;

  /**
   * ALS constant 50m.
   */
  private final double als = 50;

  /**
   * TOCS constant 50m.
   */
  private final double tocs = 50;

  /**
   * Currently used runway values (after calculation).
   */
  private double ctora, ctoda, casda, clda;

  /**
   * Displaced threshold, Clearway, Stopway, Strip end and blast protection (300m-500m).
   */
  private double threshold, clearway, stopway;

  /**
   * Strip end constant of 60m.
   */
  private final double stripEnd = 60;

  public Runway(String rdesignator, double tora, double toda, double asda, double lda,
                double resa, double threshold) {
    this.rdesignator = rdesignator;
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
    this.threshold = threshold;
    this.clearway = toda - tora;
    this.stopway = asda - tora;
  }

  public Runway(String rdesignator, double tora, double toda, double asda, double lda,
                double resa, double threshold, Aircraft aircraft) {
    this.rdesignator = rdesignator;
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
    this.threshold = threshold;
    this.clearway = toda - tora;
    this.stopway = asda - tora;
    this.aircraft = aircraft;
  }

  /**
   * Gets the current obstacle.
   *
   * @return an Obstacle object
   */
  public Obstacle getCurrentObs() {
    return currentObs;
  }

  /**
   * Set the current obstacle.
   *
   * @param currentObs new obstacle for the runway
   */
  public void setCurrentObs(Obstacle currentObs) {
    this.currentObs = currentObs;
  }

  /**
   * Returns the aircraft for this runway.
   *
   * @return an Aircraft object
   */
  public Aircraft getAircraft() {
    if (aircraft == null) {
      logger.error("No aircraft exists on runway " + rdesignator);
    }
    return aircraft;
  }

  /**
   * Set the aircraft for this runway.
   *
   * @param aircraft new aircraft for the runway
   */
  public void setAircraft(Aircraft aircraft) {
    this.aircraft = aircraft;
  }

  /**
   * Below are getters for some values that don't have to change but may be used in certain calculations.
   */
  public String getRdesignator() {
    return rdesignator;
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
    return "Runway: " + rdesignator + "\n" +
            "TORA: " + tora + "\n" +
            "TODA: " + toda + "\n" +
            "ASDA: " + asda + "\n" +
            "LDA: " + lda + "\n" +
            "RESA: " + resa + "\n" +
            "Threshold: " + threshold + "\n" +
            "Clearway: " + clearway + "\n" +
            "Stopway: " + stopway + "\n" +
            "Strip End: " + stripEnd + "\n" +
            "Blast Protection: " + aircraft.getBlastProtection() + "\n";
  }

  /*
  public void addObstacle(Obstacle obstacle) {
      obstacles.add(obstacle);
      if (currentObs == null) {
        currentObs = obstacle;
      }
  }

  public void listObjects() {
    logger.info("Listing all obstacles on runway:");
    var iterator = obstacles.iterator();
    while (iterator.hasNext()){
      logger.info(iterator.next());
    }
  }

  public void selectObs(String name){
    logger.info("Switching current obstacle to " + name);
    boolean found = false;
    int x = 0;
    while( x < obstacles.size() && !found){
      if(obstacles.get(x).getName().matches(name)){
        currentObs = obstacles.get(x);
        logger.info("Currently selected obstacle is: " + currentObs.getName());
        found = true;
      } else {
        x++;
      }
    }
    //In case the obstacle name is not found
    if (!found) {
      logger.info("Obstacle " + name + " does not exist.");
    }

  }
  */
}
