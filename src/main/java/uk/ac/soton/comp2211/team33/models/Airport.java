package uk.ac.soton.comp2211.team33.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * The Airport class that acts as a container for the collection of runways, aircraft, and obstacles.
 *
 * @author Brian (dal1g21@soton.ac.uk), Geeth (gv2g21@soton.ac.uk)
 */
public class Airport {

  private static final Logger logger = LogManager.getLogger(Airport.class);

  /**
   * City in which this airport is situated at.
   */
  private final String city;

  /**
   * Name of the airport.
   */
  private final String name;

  /**
   * List of runways.
   */
  private final SimpleListProperty<Runway> runwayList;

  /**
   * List of aircraft.
   */
  private final SimpleListProperty<Aircraft> aircraftList;

  /**
   * List of obstacles.
   */
  private final SimpleListProperty<Obstacle> obstacleList;

  /**
   * Trigger to only load pre-defined obstacles once.
   */
  private final SimpleBooleanProperty obstaclesLoaded;

  /**
   * Class constructor.
   *
   * @param city city this airport is situated at
   * @param name name of the airport
   */
  public Airport(String city, String name) {
    this.city = city;
    this.name = name;

    runwayList = new SimpleListProperty<>(FXCollections.observableArrayList());
    aircraftList = new SimpleListProperty<>(FXCollections.observableArrayList());
    obstacleList = new SimpleListProperty<>(FXCollections.observableArrayList());

    obstaclesLoaded = new SimpleBooleanProperty(false);
  }

  public void addRunway(String designator, double tora, double toda, double asda, double lda,
                        double resa, double threshold) {
    runwayList.add(new Runway(designator, tora, toda, asda, lda, resa, threshold));
  }

  public void addAircraft(String id, double blastProtection) {
    aircraftList.add(new Aircraft(id, blastProtection));
  }

  public void addObstacle(String name, double height, double length, double centerline) {
    obstacleList.add(new Obstacle(name, height, length, centerline));
  }

  public void loadPredefinedObstacles() {
    logger.info("Loading obstacles from CSV file...");

    if (obstaclesLoaded.get()) return;

    try {
      InputStream stream = getClass().getResourceAsStream("/uk/ac/soton/comp2211/team33/data/obstacles.csv");
      BufferedReader br = new BufferedReader(new InputStreamReader(stream));
      String line;

      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");
        addObstacle(values[0], Double.parseDouble(values[1]),
            Double.parseDouble(values[2]), Double.parseDouble(values[3]));
      }

      obstaclesLoaded.set(true);
    }
    catch (IOException e) {
      logger.error("Error loading obstacles from CSV file.");
      e.printStackTrace();
    }
  }

  public String getCity() {
    return city;
  }

  public String getName() {
    return name;
  }

  public SimpleListProperty<Runway> runwayListProperty() {
    return runwayList;
  }

  public SimpleListProperty<Aircraft> aircraftListProperty() {
    return aircraftList;
  }

  public SimpleListProperty<Obstacle> obstacleListProperty() {
    return obstacleList;
  }

  public SimpleBooleanProperty obstaclesLoadedProperty() {
    return obstaclesLoaded;
  }
}
