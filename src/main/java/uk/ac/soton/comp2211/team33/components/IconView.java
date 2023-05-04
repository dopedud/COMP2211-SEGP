package uk.ac.soton.comp2211.team33.components;


/**
 * The IconView class is a custom component that contains an icon.
 * Extremely hacky implementation, but it works.
 */
public class IconView extends IconButton {

  private final String STYLE = "-fx-background-color: transparent";

  public IconView() {
    super();
    this.setStyle(STYLE);
  }

}
