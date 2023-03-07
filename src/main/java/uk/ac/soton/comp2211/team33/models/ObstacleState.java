package uk.ac.soton.comp2211.team33.models;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import uk.ac.soton.comp2211.team33.entities.Obstacle;

public class ObstacleState {
  private SimpleListProperty<Obstacle> obstaclesList = new SimpleListProperty<>(FXCollections.observableArrayList());

  public SimpleListProperty<Obstacle> getObstaclesList() {
    return obstaclesList;
  }

  public void addNewObstacle(String name, double height, double distanceThreshold) {
    obstaclesList.add(new Obstacle(name, height,distanceThreshold));
  }
}
