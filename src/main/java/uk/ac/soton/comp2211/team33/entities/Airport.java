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
  private ArrayList<Runway> runways = new ArrayList<>();

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
   * Add a runway to the airport object
   * @param runway
   */
  public void addRunway(Runway runway) {
    runways.add(runway);
  }


  public Runway getRunway(String name) {
    //logger.info("Switching current obstacle to " + name);
    boolean found = false;
    Runway temp = null;
    int x = 0;
    while (x < runways.size() && !found) {
      if (runways.get(x).getRdesignator().equals(name)) {
        temp = runways.get(x);
        found = true;
      } else {
        x++;
      }
    }
    if (!found) {
      logger.error("No such runway exists");
    }
    return temp;
  }

  /**
   * Getters and setters for the airport name and city
   * @return
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
