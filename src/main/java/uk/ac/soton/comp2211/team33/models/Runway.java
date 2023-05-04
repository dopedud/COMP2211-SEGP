package uk.ac.soton.comp2211.team33.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class Runway models a runway and its values for re-declaration in an airport.
 * Corresponds to user story #3.
 *
 * @author Jackson (jl14u21@soton.ac.uk)
 */
public class Runway {

  private static final Logger logger = LogManager.getLogger(Runway.class);

  /**
   * The designator for the runway. It can have the character L, R, C, or none appended at the end of the designator.
   */
  private final String designator;

  /**
   * Initial values of the runway.
   */
  private final double tora, toda, asda, lda, resa;

  /**
   * Currently used runway values (after calculation), would also be called calculated values.
   */
  private final SimpleDoubleProperty ctora, ctoda, casda, clda, cresa;

  /**
   * Displaced threshold, clearway, and stopway.
   */
  private final SimpleDoubleProperty threshold, clearway, stopway;

  /**
   * TOCS constant of 50m, ALS constant of 50m, and strip end constant of 60m.
   */
  private static final double tocs = 50, als = 50, stripEnd = 60;

  /**
   * Whether the calculation mode is towards obstacle
   */
  private final SimpleBooleanProperty calcTowards = new SimpleBooleanProperty(true);

  /**
   * The compass heading of the runway
   */
  private int compassHeading;

  /**
   * Currently selected aircraft for this runway.
   */
  private final SimpleObjectProperty<Aircraft> currentAircraft;

  /**
   * Currently selected obstacle for this runway.
   */
  private final SimpleObjectProperty<Obstacle> currentObstacle;

  /**
   * The distance of the obstacle from the runway threshold.
   */
  private final SimpleDoubleProperty obsDistFromThresh;

  /**
   * Class constructor.
   *
   * @param designator runway designator
   * @param tora take-off runway available
   * @param toda take-off distance available
   * @param asda acceleration-stop distance available
   * @param lda landing distance available
   * @param resa runway end safety area
   * @param threshold displaced threshold of the runway
   */
  public Runway(String designator, double tora, double toda, double asda, double lda, double resa, double threshold) {
    this.designator = designator;

    this.tora = tora;
    this.toda = toda;
    this.asda = asda;
    this.lda = lda;

    if (resa < 60) {
      logger.info("RESA value below 60m. Setting it as 60m minimum value...");
      this.resa = 60;
      cresa = new SimpleDoubleProperty(60);
    } else {
      this.resa = resa;
      cresa = new SimpleDoubleProperty(resa);
    }

    ctora = new SimpleDoubleProperty(tora);
    ctoda = new SimpleDoubleProperty(toda);
    casda = new SimpleDoubleProperty(asda);
    clda = new SimpleDoubleProperty(lda);

    this.threshold = new SimpleDoubleProperty(threshold);
    clearway = new SimpleDoubleProperty(toda - tora);
    stopway = new SimpleDoubleProperty(asda - tora);

    currentAircraft = new SimpleObjectProperty<>();
    currentObstacle = new SimpleObjectProperty<>();
    obsDistFromThresh = new SimpleDoubleProperty();

    determineCompassHeading();
  }

  private void determineCompassHeading() {
    var filter = new StringBuilder();
    for (int i = 0; i < this.designator.length(); i++) {
      char c = this.designator.charAt(i);
      if (Character.isDigit(c)) {
        filter.append(c);
      }
    }
    compassHeading = (Integer.parseInt(filter.toString()) * 10) % 360;
  }

  // Below are getters and setters for some values that don't have to change but may be used in certain calculations.

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

  // Below are getters and setter for values that would be re-declared.

  public double getCtora() {
    return ctora.get();
  }

  public double getCtoda() {
    return ctoda.get();
  }

  public double getCasda() {
    return casda.get();
  }

  public double getClda() {
    return clda.get();
  }

  public double getCresa() {
    return cresa.get();
  }

  public void setCtora(double ctora) {
    this.ctora.set(ctora);
  }

  public void setCtoda(double ctoda) {
    this.ctoda.set(ctoda);
  }

  public void setCasda(double casda) {
    this.casda.set(casda);
  }

  public void setClda(double clda) {
    this.clda.set(clda);
  }

  public void setCresa(double resa) {
    this.cresa.set(resa);
  }

  // Constants of a runway.

  public double getThreshold() {
    return threshold.get();
  }

  public double getClearway() {
    return clearway.get();
  }

  public double getStopway() {
    return stopway.get();
  }

  public double getAls() {
    return als;
  }

  public double getTocs() {
    return tocs;
  }

  public double getStripEnd() {
    return stripEnd;
  }

  public int getCompassHeading() {
    return compassHeading;
  }

  // Properties for re-declared values.

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

  public SimpleDoubleProperty cresaProperty() {
    return cresa;
  }

  public SimpleDoubleProperty thresholdProperty() {
    return threshold;
  }

  public SimpleDoubleProperty clearwayProperty() {
    return clearway;
  }

  public SimpleDoubleProperty stopwayProperty() {
    return stopway;
  }

  // Methods to get, set, and return properties that would be considered in a runway re-declaration.

  public Aircraft getCurrentAircraft() {
    return currentAircraft.get();
  }

  public Obstacle getCurrentObstacle() {
    return currentObstacle.get();
  }

  public double getObsDistFromThresh() {
    return obsDistFromThresh.get();
  }

  public void setCurrentAircraft(Aircraft aircraft) {
    currentAircraft.set(aircraft);
  }

  public void setCurrentObstacle(Obstacle obstacle) {
    currentObstacle.set(obstacle);
  }

  public void setObsDistFromThresh(double obsDistFromThresh) {
    this.obsDistFromThresh.set(obsDistFromThresh);
  }

  public SimpleObjectProperty<Aircraft> currentAircraftProperty() {
    return currentAircraft;
  }

  public SimpleObjectProperty<Obstacle> currentObstacleProperty() {
    return currentObstacle;
  }

  public SimpleDoubleProperty obsDistFromThreshProperty() {
    return obsDistFromThresh;
  }

  public boolean isCalcTowards() {
    return calcTowards.get();
  }

  public void setIsCalcTowards(boolean isTowards) {
    calcTowards.set(isTowards);
  }
}