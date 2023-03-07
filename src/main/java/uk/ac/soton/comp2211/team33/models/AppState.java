package uk.ac.soton.comp2211.team33.models;

/**
 * The application state container.
 *
 * @author Brian (dal1g21@soton.ac.uk)
 */
public class AppState {
  private final RunwayState runwayState = new RunwayState();

  private final ObstacleState obstacleState = new ObstacleState();

  public RunwayState getRunwayState() {
    return runwayState;
  }

  public ObstacleState getObstacleState() {
    return obstacleState;
  }
}
