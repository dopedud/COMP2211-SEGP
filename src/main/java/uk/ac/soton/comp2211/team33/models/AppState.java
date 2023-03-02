package uk.ac.soton.comp2211.team33.models;

public class AppState {
  private VisualisationState visualisationState = new VisualisationState();
  private RunwayState runwayState = new RunwayState();

  public VisualisationState getVisualisationState() {
    return visualisationState;
  }

  public RunwayState getRunwayState() {
    return runwayState;
  }
}
