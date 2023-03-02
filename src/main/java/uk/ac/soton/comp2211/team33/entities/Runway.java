package uk.ac.soton.comp2211.team33.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * The class Runway models a runway and its values for re-declaration in an airport.
 * (Jackson if you want you can add more description)
 *
 * @author Jackson (jl14u21@soton.ac.uk)
 */
public class Runway {

  private static Logger logger = LogManager.getLogger(Runway.class);

  /**
   * List of obstacles on the runway.
   */
  private ArrayList<Obstacle> obstacles = new ArrayList<>();

  /**
   * Current obstacle on the runway.
   */
  private Obstacle currentObs = null;

  /**
   * The designator for the runway. Usually 2 characters with L/R at the end.
   */
  private String runwayDesignator;

  /**
   * Initial values of the runway.
   */
  private final double tora, toda, asda, lda, resa;

  /**
   * Currently used runway values (after calculation).
   */
  private double ctora, ctoda, casda, clda, cresa, cals, ctocs;

  /**
   * Displaced threshold, clearway, stopway, Strip end and blast protection (300m-500m).
   */
  private double threshold, clearway, stopway, stripEnd, blastProtection;


  public Runway(String runwayDesignator, double tora, double toda, double asda, double lda,
                double resa, double threshold, double clearway, double stopway,
                double stripEnd, double blastProtection) {
    this.runwayDesignator = runwayDesignator;
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

    if (blastProtection < 300 || blastProtection > 500) {
      logger.error("Blast protection is not within required range");
    } else {
      this.blastProtection = blastProtection;
    }
  }

  /**
   * Adds an obstacle to the runway.
   *
   * @param obstacle the obstacle to add
   */
  public void addObstacle(Obstacle obstacle) {
      obstacles.add(obstacle);
      if (currentObs == null) {
        currentObs = obstacle;
      }
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
   * Lists all the obstacles on the runway.
   */
  public void listObstacles() {
    logger.info("Listing all obstacles on runway:");
    var iterator = obstacles.iterator();
    while (iterator.hasNext()) {
      logger.info(iterator.next());
    }
  }

  /**
   * Switch the current obstacle for some other one.
   *
   * @param name name of the obstacle
   */
  public void selectObstacle(String name) {
    logger.info("Switching current obstacle to " + name);
    boolean found = false;
    int x = 0;

    while( x < obstacles.size() && !found) {
      if(obstacles.get(x).getName().matches(name)) {
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

  /**
   * Below are getters for some values that don't have to change but may be used in certain calculations.
   */
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

  public double getBlastProtection() {
    return blastProtection;
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
