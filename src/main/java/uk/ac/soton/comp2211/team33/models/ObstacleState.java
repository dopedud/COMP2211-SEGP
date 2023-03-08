package uk.ac.soton.comp2211.team33.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ObstacleState {

  private static final Logger logger = LogManager.getLogger(ObstacleState.class);

  private final SimpleStringProperty name;

  private final SimpleDoubleProperty height, distanceThreshold;

  public ObstacleState(String name, double height, double distanceThreshold) {
    this.name = new SimpleStringProperty(name);
    this.height = new SimpleDoubleProperty(height);
    this.distanceThreshold = new SimpleDoubleProperty(distanceThreshold);
  }

  public SimpleStringProperty getNameProperty() {
    return name;
  }

  public SimpleDoubleProperty getHeightProperty() {
    return height;
  }

  public SimpleDoubleProperty getDistThreshProperty() {
    return distanceThreshold;
  }
}
