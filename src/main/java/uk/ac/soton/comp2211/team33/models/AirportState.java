package uk.ac.soton.comp2211.team33.models;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class AirportState {

  private static final Logger logger = LogManager.getLogger(AirportState.class);

  private final String name;

  private final String city;

  private SimpleListProperty<Obstacle> obstaclesList = new SimpleListProperty<>(FXCollections.observableArrayList());

  private SimpleListProperty<Aircraft> aircraftList = new SimpleListProperty<>(FXCollections.observableArrayList());

  private SimpleListProperty<Runway> runwaysList = new SimpleListProperty<>(FXCollections.observableArrayList());

  public AirportState(String name, String city) {
    this.name = name;
    this.city = city;
  }

  public String getName() {
    return name;
  }

  public String getCity() {
    return city;
  }

  public void addObstacle(String name, double height, double length) {
    this.obstaclesList.add(new Obstacle(name, height, length));
  }

  public void loadPredefinedObstacles() {
    logger.info("Loading obstacles from CSV file...");

    try {
      InputStream stream = getClass().getResourceAsStream("/uk/ac/soton/comp2211/team33/data/obstacles.csv");
      BufferedReader br = new BufferedReader(new InputStreamReader(stream));
      String line;

      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");
        addObstacle(values[0], Double.parseDouble(values[1]), Double.parseDouble(values[2]));
      }
    }
    catch (IOException e) {
      logger.error("Error loading obstacles from CSV file.");
      e.printStackTrace();
    }
  }

  public void addAircraft(String id, double blastProtection) {
    this.aircraftList.add(new Aircraft(id, blastProtection));
  }

  public void addRunway(String designator, double tora, double toda, double asda, double lda,
                        double resa, double threshold) {
    this.runwaysList.add(new Runway(designator, tora, toda, asda, lda, resa, threshold));
  }

  public SimpleListProperty<Obstacle> obstaclesListProperty() {
    return obstaclesList;
  }

  public SimpleListProperty<Aircraft> aircraftListProperty() {
    return aircraftList;
  }

  public SimpleListProperty<Runway> runwaysListProperty() {
    return runwaysList;
  }
}
