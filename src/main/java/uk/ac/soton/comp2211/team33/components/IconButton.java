package uk.ac.soton.comp2211.team33.components;

import javafx.scene.control.Button;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import uk.ac.soton.comp2211.team33.utilities.ProjectHelpers;

/**
 * The IconButton class is a custom component that contains a button with an icon.
 * Corresponds to user story #16.
 *
 * @author Geeth (gv2g21@soton.ac.uk)
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

    private boolean autoResize = true;

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

      this.textFillProperty().addListener((observable, oldValue, newValue) -> {
        Color newColor = (Color) newValue;
        setIconColor(newColor);
      });

      this.fontProperty().addListener((observable, oldValue, newValue) -> {
        if (autoResize) {
          double size = newValue.getSize();
          this.resize(size);
        }
        setIconColor((Color) this.getTextFill()); // recolour so the whole icon is coloured properly
      });

      this.setGraphic(imageView);
    }

    private Paint colorToPaint(Color color) {
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), 1);
    }

    public void setIconColor(Color color) {
      Blend blend2 = new Blend();
      blend2.setMode(BlendMode.SRC_ATOP);
      var height = imageView.getImage().getHeight();
      var width = imageView.getImage().getWidth();
      var ratio = width / height;
      System.out.println("Height: " + height + " Width: " + width);
      blend2.setTopInput(new ColorInput(0, 0, iconSize*ratio, iconSize, colorToPaint(color)));
      imageView.setEffect(blend2);
    }

    public String getIcon() {
      return this.iconPath;
    }

    private void resize(double size) {
      this.iconSize = size;
      imageView.setPreserveRatio(true);
      imageView.setFitHeight(iconSize);
    }

  /**
   * Sets the size of the icon, only required if a value other than the default is desired.
   * Given a height, the width is automatically determined using the image aspect ratio.
   * Setting this value disables automatic resizing of the icon.
   * @param iconSize Height of the icon in pixels.
   */
    public void setIconSize(double iconSize) {
      autoResize = false;
      resize(iconSize);
    }

    public double getIconSize() {
      return this.iconSize;
    }


}
