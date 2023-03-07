package uk.ac.soton.comp2211.team33.models;

import javafx.beans.property.SimpleListProperty;

/**
 * The application state container.
 *
 * @author Brian (dal1g21@soton.ac.uk)
 */
public class AirportState {
  private RunwayState runwayState;

  private final SimpleListProperty<ObstacleState> obstacleListState;

  private AircraftState aicraftState;

  public AirportState() {
    runwayState = new RunwayState();
    obstacleListState = new SimpleListProperty<>();
  }

  public RunwayState getRunwayState() {
    return runwayState;
  }

  public SimpleListProperty<ObstacleState> getObstacleListState() {
    return obstacleListState;
  }
}
