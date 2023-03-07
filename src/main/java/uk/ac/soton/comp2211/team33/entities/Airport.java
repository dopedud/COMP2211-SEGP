package uk.ac.soton.comp2211.team33.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Class that serves to meet user story #1 requirement
 *
 * @author Jackson (jl14u21@soton.ac.uk)
 */
public class Airport {

  private String name;
  private String city;

  private static final Logger logger = LogManager.getLogger(Airport.class);

  /**
   * Airport constructor for an airport project
   *
   * @param name The name of the aircraft
   * @param city The city the airport is in
   */
  public Airport(String name, String city) {
    this.name = name;
    this.city = city;
  }

  /**
   * Getters and setters for the airport name and city
   */

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }
}
