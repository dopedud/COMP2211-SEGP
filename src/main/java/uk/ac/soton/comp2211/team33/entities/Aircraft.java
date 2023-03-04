package uk.ac.soton.comp2211.team33.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Aircraft {

  private static Logger logger = LogManager.getLogger(Aircraft.class);

  /**
   * The blast protection value which is dependent on the aircraft
   */
  private double blastProtection;

  /**
   * A name to identify the aircraft
   */
  private String aircraftID;

  /**
   * Constructor to create a new Aircraft object
   * @param id
   * @param blastProtection
   */
  public Aircraft(String id, double blastProtection){
    this.aircraftID = id;
    if(blastProtection < 300 || blastProtection > 500) {
      logger.error("Blast protection value not within boundaries (300m-500m)");
    } else {
      this.blastProtection = blastProtection;
    }
  }

  public double getBlastProtection() {
    return blastProtection;
  }

  public void setBlastProtection(double blastProtection) {
    this.blastProtection = blastProtection;
  }

  public String getAircraftID() {
    return aircraftID;
  }

  public void setAircraftID(String aircraftID) {
    this.aircraftID = aircraftID;
  }
}
