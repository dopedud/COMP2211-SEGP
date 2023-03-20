package uk.ac.soton.comp2211.team33.components;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;

/**
 * The IconButton class is a custom component that contains a button with an icon. The icon can be set using the setIcon method
 */
public class IconButton extends Button {

  /**
   * The path to the icon.
   */
    private String iconPath;

  /**
   * The image view that displays the icon.
   */
    private ImageView imageView;

  /**
   * The size of the icon. (Height in pixels)
   */
    private double iconSize = 20;

    public IconButton() {
        super();
    }

  /**
   * Sets the icon of a button, using a path to the image.
   * @param iconPath The path to the icon
   */
    public void setIcon(String iconPath) {
      this.iconPath = iconPath;
      imageView = new ImageView(new Image(ProjectHelpers.getResource(iconPath).toExternalForm()));
      imageView.setPreserveRatio(true);
      imageView.setFitHeight(iconSize);
      this.setGraphic(imageView);
    }

    public String getIcon() {
      return this.iconPath;
    }

  /**
   * Sets the size of the icon, only required if a value other than the default (20px) is desired. Given a height, the width is automatically determined using the image aspect ratio.
   * @param iconSize Height of the icon in pixels.
   */
    public void setIconSize(double iconSize) {
      this.iconSize = iconSize;
      imageView.setPreserveRatio(true);
      imageView.setFitHeight(iconSize);
    }

    public double getIconSize() {
      return this.iconSize;
    }


}
