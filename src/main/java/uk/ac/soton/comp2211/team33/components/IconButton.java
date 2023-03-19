package uk.ac.soton.comp2211.team33.components;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;

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

    public void setIconSize(double iconSize) {
      this.iconSize = iconSize;
      imageView.setPreserveRatio(true);
      imageView.setFitHeight(iconSize);
    }

    public double getIconSize() {
      return this.iconSize;
    }


}
