package uk.ac.soton.comp2211.team33.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
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
  private final SimpleStringProperty city;

  /**
   * Name of the airport.
   */
  private final SimpleStringProperty name;

  /**
   * List of runways.
   */
  private final ObservableList<Runway> runwayList;

  /**
   * List of aircraft.
   */
  private final ObservableList<Aircraft> aircraftList;

  /**
   * List of obstacles.
   */
  private final ObservableList<Obstacle> obstacleList;
  private SimpleBooleanProperty obstaclesLoaded;

  /**
   * Class constructor.
   *
   * @param city city this airport is situated at
   * @param name name of the airport
   */
  public Airport(String city, String name) {
    this.city = new SimpleStringProperty(city);
    this.name = new SimpleStringProperty(name);

    obstacleList = new SimpleListProperty<>(FXCollections.observableArrayList());
    aircraftList = new SimpleListProperty<>(FXCollections.observableArrayList());
    runwayList = new SimpleListProperty<>(FXCollections.observableArrayList());
    obstaclesLoaded = new SimpleBooleanProperty();
  }

  public void addRunway(String designator, double tora, double toda, double asda, double lda,
                        double resa, double threshold) {
    runwayList.add(new Runway(designator, tora, toda, asda, lda, resa, threshold));
  }

  public void addAircraft(String id, double blastProtection) {
    aircraftList.add(new Aircraft(id, blastProtection));
  }

  public void addObstacle(String name, double height, double length) {
    obstacleList.add(new Obstacle(name, height, length));
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
        addObstacle(values[0], Double.parseDouble(values[1]), Double.parseDouble(values[2]));
      }

      obstaclesLoaded.set(true);
    }
    catch (IOException e) {
      logger.error("Error loading obstacles from CSV file.");
      e.printStackTrace();
    }
  }


  public String getCity() {
    return city.get();
  }

  public String getName() {
    return name.get();
  }

  public ObservableList<Runway> getRunwayList() {
    return runwayList;
  }

  public ObservableList<Aircraft> getAircraftList() {
    return aircraftList;
  }

  public ObservableList<Obstacle> getObstacleList() {
    return obstacleList;
  }

  public boolean getObstaclesLoaded() {
    return obstaclesLoaded.get();
  }

  public SimpleStringProperty getCityProperty() {
    return city;
  }

  public SimpleStringProperty getNameProperty() {
    return name;
  }

  public SimpleBooleanProperty getObstaclesLoadedProperty() {
    return obstaclesLoaded;
  }
}
