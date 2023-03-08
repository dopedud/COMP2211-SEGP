package uk.ac.soton.comp2211.team33.entities;

import javafx.beans.property.SimpleStringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * The class Airport that serves as a collection of runways and aircraft.
 *
 * Corresponds to user story #1.
 *
 * @author Jackson (jl14u21@soton.ac.uk)
 */
public class Airport {

  private SimpleStringProperty name;
  private SimpleStringProperty city;

  private static final Logger logger = LogManager.getLogger(Airport.class);

  /**
   * Airport constructor for an airport project.
   *
   * @param name The name of the aircraft
   * @param city The city the airport is in
   */
  public Airport(String name, String city) {
    this.name.set(name);
    this.city.set(city);
  }

  /**
   * Getters and setters for the airport name and city.
   */

  public String getName() {
    return name.get();
  }

  public SimpleStringProperty nameProperty() {
    return name;
  }

  public void setName(String name) {
    this.name.set(name);
  }

  public String getCity() {
    return city.get();
  }

  public SimpleStringProperty cityProperty() {
    return city;
  }

  public void setCity(String city) {
    this.city.set(city);
  }
}
