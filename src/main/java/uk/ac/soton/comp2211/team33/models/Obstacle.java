package uk.ac.soton.comp2211.team33.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class Obstacle specifies what attributes should an obstacle have in a runway re-declaration.
 * Corresponds to user story #3, #4.
 *
 * @author Abeed (mabs1u21@soton.ac.uk)
 */
public class Obstacle {

  private static final Logger logger = LogManager.getLogger(Obstacle.class);

  /**
   * Name of the obstacle.
   */
  private String name;

  /**
   * Height of the obstacle.
   */
  private double height;

  /**
   * Length of the obstacle.
   */
  private double length;

  /**
   * Center-line displacement. North : positive number, South : negative number.
   */
  private double centerline;

  /**
   * Creates a new obstacle with the specified name, height, and length.
   *
   * @param name A name to identify the obstacle
   * @param height Height of the obstacle
   * @param length Length of the obstacle
   */
  public Obstacle(String name, double height, double length) {
    this.name = name;
    this.height = height;
    this.length = length;
  }

  /**
   * Creates a new obstacle with the specified name, height, length, and distance from center-line.
   *
   * @param name name of the obstacle
   * @param height height of the obstacle
   * @param length length of the obstacle
   * @param centerline distance from center-line (+ if North, - if South)
   */
  public Obstacle(String name, double height, double length, double centerline) {
    this(name, height, length);
    this.centerline = centerline;
  }

  // Below are the usual accessors and mutators for the instance variables of this class.

  public String getName() {
    return name;
  }

  public double getHeight() {
    return height;
  }

  public double getLength() {
    return length;
  }

  public double getCenterline() {
    return centerline;
  }

  @Override
  public String toString() {
     return "Name: " + name + "\nHeight: " + height + "\nLength: " + length;
  }
}
