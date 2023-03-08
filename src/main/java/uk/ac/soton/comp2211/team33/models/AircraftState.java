package uk.ac.soton.comp2211.team33.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AircraftState {

  /**
   * Logger for this class
   */
  private static final Logger logger = LogManager.getLogger(AircraftState.class);

  /**
   * Any name that identifies the aircraft
   */
  private final SimpleStringProperty aircraftID;

  /**
   * Blast protection value of the aircraft
   */
  private final SimpleDoubleProperty blastProtection;

  /**
   * Create a new AircraftState object
   * @param aircraftID
   * @param blastProtection
   */
  public AircraftState(String aircraftID, double blastProtection) {
    this.aircraftID = new SimpleStringProperty(aircraftID);
    this.blastProtection = new SimpleDoubleProperty(blastProtection);
  }

  /**
   * Getter for the aircraftID property
   * @return
   */
  public SimpleStringProperty getAircraftIDProperty() {
    return aircraftID;
  }

  /**
   * Getter for the blast protection value property
   * @return
   */
  public SimpleDoubleProperty getBlastProtectionProperty() {
    return blastProtection;
  }
}
