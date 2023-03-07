package uk.ac.soton.comp2211.team33.models;

import javafx.beans.property.SimpleListProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ObstacleListState {

  private static final Logger logger = LogManager.getLogger(ObstacleListState.class);

  private final SimpleListProperty<ObstacleState> obstacleList;

  public ObstacleListState() {
    obstacleList = new SimpleListProperty<>();
  }

  public SimpleListProperty<ObstacleState> getObstacleListProperty() {
    return obstacleList;
  }

  public void addNewObstacle(String name, double height, double distanceThreshold) {
    for (ObstacleState obstacle : obstacleList) {
      if (obstacle.getNameProperty().get().equals(name)) return;
    }

    obstacleList.add(new ObstacleState(name, height, distanceThreshold));
  }
}
