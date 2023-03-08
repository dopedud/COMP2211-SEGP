package uk.ac.soton.comp2211.team33.models;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

import java.util.HashMap;
import java.util.Map;

/**
 * The application state container.
 *
 * @author Brian (dal1g21@soton.ac.uk)
 */
public class AppState {
  private Map<String, AirportState> airports = new HashMap<>();

  private SimpleListProperty<String> airportCodes = new SimpleListProperty<>(FXCollections.observableArrayList("LHR", "SOU"));

  private SimpleStringProperty activeAirportCode = new SimpleStringProperty("LHR");

  public AppState() {
    airports.put("LHR", new AirportState("London Heathrow"));
    airports.put("SOU", new AirportState("Southampton"));
  }

  public void setActiveAirportCode(String code) {
    activeAirportCode.set(code);
  }

  public AirportState getActiveAirportState() {
    return airports.get(activeAirportCode.get());
  }

  public SimpleListProperty<String> airportCodesProperty() {
    return airportCodes;
  }

  // ===== Useful method for future increments ======

  public void addAirport() {}

  public void removeAirport() {}
}
