package uk.ac.soton.comp2211.team33.models;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
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
  private final SimpleStringProperty designator;

  /**
   * Initial values of the runway.
   */
  private final double tora, toda, asda, lda, resa;

  /**
   * Currently used runway values (after calculation).
   */
  private final SimpleDoubleProperty ctora, ctoda, casda, clda, cresa;

  /**
   * Displaced threshold, clearway, and stopway.
   */
  private final SimpleDoubleProperty threshold, clearway, stopway;

  /**
   * TOCS constant of 50m, ALS constant of 50m, and strip end constant of 60m.
   */
  private final double tocs = 50, als = 50, stripEnd = 60;


  private SimpleObjectProperty<Aircraft> currentAircraft;

  private SimpleObjectProperty<Obstacle> currentObstacle;

  /**
   * The distance of the obstacle from the threshold of the runway.
   */
  private SimpleDoubleProperty obstacleDistance;

  public Runway(String designator, double tora, double toda, double asda, double lda,
                double resa, double threshold) {
    this.designator = new SimpleStringProperty(designator);

    this.tora = tora;
    this.toda = toda;
    this.asda = asda;
    this.lda = lda;

    if (resa < 240) {
      logger.info("RESA value below 240m. Setting it as 240m minimum value...");
      this.resa = 240;
      cresa = new SimpleDoubleProperty(240);
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
    obstacleDistance = new SimpleDoubleProperty();
  }

  // Below are getters and setters for some values that don't have to change but may be used in certain calculations.

  public String getDesignator() {
    return designator.get();
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

  public Aircraft getCurrentAircraft() {
    return currentAircraft.get();
  }

  public Obstacle getCurrentObstacle() {
    return currentObstacle.get();
  }

  public void setCurrentAircraft(Aircraft aircraft) {
    currentAircraft.set(aircraft);
  }

  public void setCurrentObstacle(Obstacle obstacle) {
    currentObstacle.set(obstacle);
  }

  public SimpleObjectProperty<Aircraft> aircraftProperty() {
    return currentAircraft;
  }

  public SimpleObjectProperty<Obstacle> obstacleProperty() {
    return currentObstacle;
  }

  public SimpleDoubleProperty obstacleDistanceProperty() {
    return obstacleDistance;
  }

  public double getObstacleDistance() {
      return obstacleDistance.get();
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
