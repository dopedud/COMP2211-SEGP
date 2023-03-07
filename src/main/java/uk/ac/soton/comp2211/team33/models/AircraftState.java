package uk.ac.soton.comp2211.team33.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AircraftState {

  private static final Logger logger = LogManager.getLogger(AircraftState.class);

  private final SimpleStringProperty aircraftID;

  private final SimpleDoubleProperty blastProtection;

  public AircraftState(String aircraftID, double blastProtection) {
    this.aircraftID = new SimpleStringProperty(aircraftID);
    this.blastProtection = new SimpleDoubleProperty(blastProtection);
  }

  public SimpleStringProperty getAircraftIDProperty() {
    return aircraftID;
  }

  public SimpleDoubleProperty getBlastProtectionProperty() {
    return blastProtection;
  }
}
