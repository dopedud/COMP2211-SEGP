package uk.ac.soton.comp2211.team33.models;

public class Obstacle {
  private String name;
  private double height;
  private double length;

  private double distanceThresh;

  public Obstacle(String name, int height, int length) {
    this.name = name;
    this.height = height;
    this.length = length;
  }

  public int getHeight() {
    return height;
  }

  public int getLength() {
    return length;
  }
}
