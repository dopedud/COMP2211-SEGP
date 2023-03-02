package uk.ac.soton.comp2211.team33.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class Obstacle specifies what attributes should an obstacle have. Obstacles in a runway re-declaration is
 * treated like a cuboid object, thus they only really have 2 significant measurements, which is its height and length.
 * The width is not taken into account, as in a runway re-declaration it is assumed that the obstacle will be centred
 * on the runway, leaving no room for the incoming or outgoing aircraft to maneuver besides it.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
public class Obstacle {

  private static Logger logger = LogManager.getLogger(Obstacle.class);

  /**
   * Name of the obstacle.
   */
  private final String name;

  /**
   * Height of the obstacle.
   */
  private final double height;

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
   * Below are the usual accessors and mutators for the class variables.
   */
  public String getName() {
    return name;
  }

  public double getHeight() {
    return height;
  }

  public double getDistanceThreshold() {
    return distanceThreshold;
  }

  public String toString() {
     return ("Name: " + name + "\nHeight: " + height + "\nDistance: " + distanceThreshold);
  }
}
