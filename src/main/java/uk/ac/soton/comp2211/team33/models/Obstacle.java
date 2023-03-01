package uk.ac.soton.comp2211.team33.models;

/**
 * The class Obstacle specifies what attributes should an obstacle have. Obstacles in a runway re-declaration is
 * treated like a cuboid object, thus they only really have 2 significant measurements, which is its height and length.
 * The width is not taken into account, as in a runway re-declaration it is assumed that the obstacle will be centred
 * on the runway, leaving no room for the incoming or outgoing aircraft to maneuver besides it.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
public class Obstacle {

  private double distanceThresh;

  /**
   * Name of the obstacle.
   */
  private final String name;

  /**
   * Height of the obstacle.
   */
  private final double height;

  /**
   * Length of the obstacle.
   */
  private final double length;


  /**
   * Creates a new obstacle with the specified name, height, and length.
   *
   * @param name    name of the obstacle
   * @param height  height of the obstacle
   * @param length  length of the obstacle
   */
  public Obstacle(String name, int height, int length, double distanceThresh) {

    this.name = name;
    this.height = height;
    this.length = length;
    this.distanceThresh = distanceThresh;
  }

  public double getHeight() {
    return height;
  }

  public double getLength() {
    return length;
  }

  public double getDistanceThresh() {
    return distanceThresh;
  }

  public String getName() {
    return name;
  }
}
