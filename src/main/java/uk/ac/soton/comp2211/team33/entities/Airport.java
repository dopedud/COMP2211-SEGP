package uk.ac.soton.comp2211.team33.entities;


import java.util.ArrayList;

public class Airport {

  private String name;
  private String city;
  private ArrayList<Runway> runways = new ArrayList<>();

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

  public void addRunway(Runway runway) {
    runways.add(runway);
  }

  public Runway getRunway(String name) {
    //logger.info("Switching current obstacle to " + name);
    boolean found = false;
    Runway temp = null;
    int x = 0;
    while (x < runways.size() && !found) {
      if (runways.get(x).getRdesignator().matches(name)) {
        temp = runways.get(x);
        found = true;
      } else {
        x++;
      }
    }
    return temp;
  }

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
