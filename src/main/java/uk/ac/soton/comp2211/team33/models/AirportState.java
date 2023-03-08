package uk.ac.soton.comp2211.team33.models;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

public class AirportState {
  private String name;

  private SimpleListProperty<Obstacle> obstaclesList = new SimpleListProperty<>(FXCollections.observableArrayList());

  private SimpleListProperty<Aircraft> aircraftList = new SimpleListProperty<>(FXCollections.observableArrayList());

  private SimpleObjectProperty<Runway> runway = new SimpleObjectProperty<>();

  public AirportState(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void addObstacle(String name, double height, double distanceThreshold) {
    this.obstaclesList.add(new Obstacle(name, height, distanceThreshold));
  }

  public void addAircraft(String id, double blastProtection) {
    this.aircraftList.add(new Aircraft(id, blastProtection));
  }

  public void addRunway(String designator, double tora, double toda, double asda, double lda,
                        double resa, double threshold) {
    this.runway.set(new Runway(designator, tora, toda, asda, lda, resa, threshold));
  }

  public SimpleListProperty<Obstacle> obstaclesListProperty() {
    return obstaclesList;
  }

  public SimpleListProperty<Aircraft> aircraftListProperty() {
    return aircraftList;
  }

  public SimpleObjectProperty<Runway> runwayProperty() {
    return runway;
  }

  public Runway getRunway() {
    return runway.get();
  }
}
