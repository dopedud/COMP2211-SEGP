package uk.ac.soton.comp2211.team33.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class Aircraft models what attributes an aircraft has when runway is being re-declared.
 * Corresponds to user story #3.
 *
 * @author Jackson (jl14u21@soton.ac.uk)
 */
public class Aircraft {
  private static final Logger logger = LogManager.getLogger(Aircraft.class);

  /**
   * Name to identify the aircraft.
   */
  private String id;

  /**
   * Blast protection value which is dependent on the aircraft.
   */
  private double blastProtection;

  /**
   * Creates a new aircraft with the specified id and blast protection.
   *
   * @param id id of the aircraft
   * @param blastProtection blast protection of the aircraft
   */
  public Aircraft(String id, double blastProtection) throws IllegalArgumentException {
    if (blastProtection < 300 || blastProtection > 500) {
      throw new IllegalArgumentException("Blast protection out of range between 300m to 500m.");
    } else {
      this.id = id;
      this.blastProtection = blastProtection;
    }
  }

  // Below are the usual accessors and mutators for the instance variables of this class.

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public double getBlastProtection() {
    return blastProtection;
  }

  @Override
  public String toString() {
    return "Aircraft ID: " + id + "\nBlast Protection: " +  blastProtection;
  }
}


