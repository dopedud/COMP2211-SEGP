package uk.ac.soton.comp2211.team33.scenes;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainScene {
  public MainScene(Stage stage) {

    GridPane root = new GridPane();

    VBox rightPanel = new VBox();
    rightPanel.setStyle("-fx-background-color: red");

    HBox bottomPanel = new HBox();
    bottomPanel.setStyle("-fx-background-color: green");

    Canvas visualisationPanel = new Canvas();
    visualisationPanel.setStyle("-fx-background-color: blue");

    root.setMinSize(1280, 960);

    root.add(visualisationPanel, 0, 0);
    root.add(rightPanel, 1, 0, 1, 2);
    root.add(bottomPanel, 0, 1);

    ColumnConstraints col1Constraints = new ColumnConstraints();
    col1Constraints.setPercentWidth(66.6666);
    ColumnConstraints col2Constraints = new ColumnConstraints();
    col2Constraints.setPercentWidth(33.3333);

    root.getColumnConstraints().addAll(col1Constraints, col2Constraints);

    RowConstraints row1Constraints = new RowConstraints();
    row1Constraints.setPercentHeight(66.6666);
    RowConstraints row2Constraints = new RowConstraints();
    row2Constraints.setPercentHeight(33.3333);

    root.getRowConstraints().addAll(row1Constraints, row2Constraints);

    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setTitle("Runway Redeclaration");
  }
}
