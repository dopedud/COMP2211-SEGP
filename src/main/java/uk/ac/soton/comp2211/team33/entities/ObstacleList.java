package uk.ac.soton.comp2211.team33.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;


/**
 * The class ObstacleList represents a list of obstacles, and can be used for importing / exporting a list of obstacles.
 *
 * @author Geeth (gv2g21@soton.ac.uk), Abeed (mabs1u21@soton.ac.uk)
 */
public class ObstacleList {

  private static final Logger logger = LogManager.getLogger(ObstacleList.class);

  /**
   * List of obstacles.
   */
  private final ArrayList<Obstacle> obstacles;

  /**
   * Class constructor that initialises the list of obstacles to be of type ArrayList.
   */
  public ObstacleList() {
    logger.info("Creating new obstacle list...");
    obstacles = new ArrayList<>();
  }

  /**
   * Get an ArrayList of all the obstacles in the list.
   *
   * @return the list of obstacles
   */
  public ArrayList<Obstacle> getObstacles() {
    return obstacles;
  }

  /**
   * Updates the obstacle list with the obstacles from the CSV file.
   *
   * @param filepath the file path to the CSV file.
   */
  public void loadObstaclesFromCSV(String filepath) {
    logger.info("Loading obstacles from CSV file...");

    try {
      BufferedReader br = new BufferedReader(new FileReader(filepath));
      String line;

      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");
        Obstacle obstacle = new Obstacle(values[0], Double.parseDouble(values[1]), Double.parseDouble(values[2]));
        addObstacle(obstacle);
      }
    } catch (IOException e) {
      logger.error("Error loading obstacles from CSV file.");
      e.printStackTrace();
    }
  }

  /**
   * Calls loadObstaclesFromCSV with the default file path.
   */
  public void loadObstaclesFromCSV() {
    String CSV_FILE_PATH = "src/main/resources/uk/ac/soton/comp2211/team33/data/obstacles.csv";
    loadObstaclesFromCSV(CSV_FILE_PATH);
  }

  /**
   * Adds an obstacle to the list.
   *
   * @param obstacle the obstacle to be added to the list
   */
  public void addObstacle(Obstacle obstacle) {
    if (!obstacles.contains(obstacle)) obstacles.add(obstacle);
  }

  /**
   * Finds an obstacle from the given name.
   *
   * @param name name of the obstacle to find
   */
  public Obstacle findObstacle(String name) {

    for (Obstacle obstacle : obstacles) {
      if (obstacle.getName().equals(name)) return obstacle;
    }

    return null;
  }
}
