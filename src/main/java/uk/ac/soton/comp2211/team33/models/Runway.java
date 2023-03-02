package uk.ac.soton.comp2211.team33.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class Runway {

  private static Logger logger = LogManager.getLogger(Runway.class);

  /**
   * Current obstacle
   */
  private Obstacle currentObs = null;
  
  // TODO: 01/03/2023 Make sure to limit it to 1 obstacle

  /**
   * The designator for the runway. Usually 2 characters with L/R at the end
   */
  private final String rdesignator;

  /**
   * Initial values of the runway
   */
  private final double tora, toda, asda, lda, resa;

  /**
   * Currently used runway values (after calculation)
   */
  private double ctora, ctoda, casda, clda, cresa, cals, ctocs;

  /**
   * Displaced threshold, Clearway, Stopway, Strip end and blast protection (300m-500m)
   */
  private double threshold, clearway, stopway, stripEnd;

  /**
   * The aircraft that is about to land on this runway
   */
  private Aircraft aircraft = null;

  public Runway(String rdesignator, double tora, double toda, double asda, double lda,
                double resa, double threshold, double clearway, double stopway,
                double stripEnd) {
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
    this.cresa = this.resa;
    this.cals = 0;
    this.ctocs = 0;
    this.threshold = threshold;
    this.clearway = clearway;
    this.stopway = stopway;
    this.stripEnd = stripEnd;
  }

  public Runway(String rdesignator, double tora, double toda, double asda, double lda,
                double resa, double threshold, double clearway, double stopway,
                double stripEnd, Aircraft aircraft){
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
    this.cresa = this.resa;
    this.cals = 0;
    this.ctocs = 0;
    this.threshold = threshold;
    this.clearway = clearway;
    this.stopway = stopway;
    this.stripEnd = stripEnd;
    this.aircraft = aircraft;
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
  /**
   * Gets the current obstacle
   * @return an Obstacle object
   */
  public Obstacle getCurrentObs() {
    return currentObs;
  }

  /**
   * Set the current obstacle
   * @param currentObs
   */
  public void setCurrentObs(Obstacle currentObs) {
    this.currentObs = currentObs;
  }

  /**
   * Returns the aircraft for this runway
   * @return
   */
  public Aircraft getAircraft() {
    if (aircraft == null){
      logger.error("No aircraft exists on runway " + rdesignator);
    }
      return aircraft;
  }

  /**
   * Set the aircraft for this runway
   * @param aircraft
   */
  public void setAircraft(Aircraft aircraft) {
    this.aircraft = aircraft;
  }

  /**
   * Below are getters for some values that don't have to change but may be used in certain calculations
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
   *  Getters and setter for all current values that can be changed by a re-declaration
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

  public double getCresa() {
    return cresa;
  }

  public void setCresa(double cresa) {
    this.cresa = cresa;
  }

  public double getCals() {
    return cals;
  }

  public void setCals(double cals) {
    this.cals = cals;
  }

  public double getCtocs() {
    return ctocs;
  }

  public void setCtocs(double ctocs) {
    this.ctocs = ctocs;
  }
}
