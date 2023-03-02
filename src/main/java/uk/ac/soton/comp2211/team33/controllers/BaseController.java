package uk.ac.soton.comp2211.team33.controllers;

import uk.ac.soton.comp2211.team33.models.AppState;

public class BaseController {
  protected AppState state;

  public void setState(AppState state) {
    this.state = state;
  }
}
