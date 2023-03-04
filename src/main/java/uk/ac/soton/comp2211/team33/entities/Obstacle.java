package uk.ac.soton.comp2211.team33.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class Obstacle specifies what attributes should an obstacle have in a runway re-declaration.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
public class Obstacle {

  private static final Logger logger = LogManager.getLogger(Obstacle.class);

  /**
   * Name of the obstacle.
   */
  private  String name;

  /**
   * Height of the obstacle.
   */
  private  double height;

  /**
   * Distance between the obstacle and the threshold of the runway.
   */
  private double distanceThreshold;

  /**
   * Creates a new obstacle with the specified name, height, and length.
   *
   * @param name                name of the obstacle
   * @param height              height of the obstacle
   * @param distanceThreshold   the distance from the threshold of the obstacle
   */
  public Obstacle(String name, double height, double distanceThreshold) {
    this.name = name;
    this.height = height;
    this.distanceThreshold = distanceThreshold;
  }

  /**
   * Below are the usual accessors and mutators for the instance variables of this class.
   */
  public String getName() {
    return name;
  }

  public double getHeight() {
    return height;
  }

  public double getDistThresh() {
    return distanceThreshold;
  }

  public String toString() {
     return ("Name: " + name + "\nHeight: " + height + "\nDistance: " + distanceThreshold);
  }
}
