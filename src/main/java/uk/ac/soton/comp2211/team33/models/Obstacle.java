package uk.ac.soton.comp2211.team33.models;

public class Obstacle {
  private String name;
  private double height;
  private double length;

  private double distanceThresh;

  public Obstacle(String name, int height, int length, double distanceThresh) {
    this.name = name;
    this.height = height;
    this.length = length;
    this.distanceThresh = distanceThresh;
  }

  public double getHeight() {
    return height;
  }

  public double getLength() {
    return length;
  }

  public double getDistanceThresh() {
    return distanceThresh;
  }
}
