package uk.ac.soton.comp2211.team33.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class Aircraft models what attributes an aircraft has when runway is being re-declared.
 *
 * Corresponds to user story #3.
 *
 * @author Jackson (jl14u21@soton.ac.uk)
 */
public class Aircraft {

  private static final Logger logger = LogManager.getLogger(Aircraft.class);

  /**
   * A name to identify the aircraft.
   */
  private String aircraftID;

  /**
   * The blast protection value which is dependent on the aircraft.
   */
  private double blastProtection;

  /**
   * Constructor to create a new Aircraft object.
   *
   * @param id                id of the aircraft
   * @param blastProtection   blast protection of the aircraft
   */
  public Aircraft(String id, double blastProtection) {
    this.aircraftID = id;

    if(blastProtection < 300 || blastProtection > 500) {
      logger.error("Blast protection value not within boundaries (300m-500m).");
    } else {
      this.blastProtection = blastProtection;
    }
  }

  /**
   * Below are the usual accessors and mutators for the instance variables of this class.
   */

  public String getAircraftID() {
    return aircraftID;
  }

  public void setAircraftID(String aircraftID) {
    this.aircraftID = aircraftID;
  }

  public double getBlastProtection() {
    return blastProtection;
  }

  public void setBlastProtection(double blastProtection) {
    this.blastProtection = blastProtection;
  }
}
