package uk.ac.soton.comp2211.team33.entities;

import javafx.beans.property.SimpleStringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class Obstacle specifies what attributes should an obstacle have in a runway re-declaration.
 *
 * Corresponds to user story #3, #4.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
public class Obstacle {

  private static final Logger logger = LogManager.getLogger(Obstacle.class);

  /**
   * Name of the obstacle.
   */
  private SimpleStringProperty name;

  /**
   * Height of the obstacle.
   */
  private final double height;

  /**
   * Distance between the obstacle and the threshold of the runway.
   */
  private final double distanceThreshold;

  /**
   * Centerline displacement, North : positive number, South : negative number
   */
  private double centerline = 0;

  /**
   * Creates a new obstacle with the specified name, height, and length.
   * @param name
   * @param height
   * @param distanceThreshold
   */
  public Obstacle(String name, double height, double distanceThreshold) {
    this.name.set(name);
    this.height = height;
    this.distanceThreshold = distanceThreshold;
  }

  /**
   * Creates a new obstacle with the specified name, height, length, and distance from centerline.
   *
   * @param name              name of the obstacle
   * @param height            height of the obstacle
   * @param distanceThreshold the distance from the threshold of the obstacle
   * @param centerline        Distance from centerline (+ if North, - if South)
   */
  public Obstacle(String name, double height, double distanceThreshold, double centerline) {
    this.name.set(name);
    this.height = height;
    this.distanceThreshold = distanceThreshold;
    this.centerline= centerline;
  }

  public String getName() {
    return name.get();
  }

  public SimpleStringProperty nameProperty() {
    return name;
  }

  /**
   * Below are the usual accessors and mutators for the instance variables of this class.
   */



  public double getHeight() {
    return height;
  }

  public double getDistThresh() {
    return distanceThreshold;
  }

  public double getCenterline() {
    return centerline;
  }

  @Override
  public String toString() {
     return "Name: " + name + "\nHeight: " + height + "\nDistance: " + distanceThreshold;
  }
}
