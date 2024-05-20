package it.polimi.ingsw.view.GUI.Components.CodexRelated;

import it.polimi.ingsw.view.GUI.GUIConfigs;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;



public class ImageCounter {
    private final ImageView image;
    private final Text counterLabel;

    private final StackPane container = new StackPane();

    private int counter = 0;

    public ImageCounter(Image image) {
        this.image = new ImageView(image);
        // Preserve the image ratio
        this.image.setPreserveRatio(true);
        this.image.setFitWidth(GUIConfigs.collectablesWidth);


        this.counterLabel = new Text("0");
        //Remove space inter line
        this.counterLabel.setLineSpacing(0);

        Rectangle labelBg = new Rectangle(50, 50);
        labelBg.setArcWidth(10);
        labelBg.setArcHeight(10);
        labelBg.setStyle("-fx-fill: white;");


        //make the bg as big as the text
        counterLabel.boundsInLocalProperty().addListener((obs, oldBounds, newBounds) -> {
            labelBg.setWidth(newBounds.getWidth());
            labelBg.setHeight(newBounds.getHeight());
        });

        //make the bg follow the text
        counterLabel.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            labelBg.setLayoutX(newBounds.getCenterX());
            labelBg.setLayoutY(newBounds.getCenterY());
        });

        counterLabel.setStyle("-fx-font-size: 20; -fx-background-color: white;");

        container.getChildren().addAll(this.image, labelBg, counterLabel);

        container.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2);");

        StackPane.setAlignment(labelBg, Pos.BOTTOM_RIGHT);
        StackPane.setAlignment(counterLabel, Pos.BOTTOM_RIGHT);

    }

    public void setCounter(int counter) {
        this.counter = counter;
        counterLabel.setText(String.valueOf(counter));
    }

    public Node getContent() {
        return container;
    }

}
