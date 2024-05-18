package it.polimi.ingsw.view.GUI.Components.CodexRelated;

import it.polimi.ingsw.view.GUI.GUIConfigs;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class ImageCounter {
    private final ImageView image;
    private final Text counterLabel;

    private final HBox container = new HBox();

    private int counter = 0;

    public ImageCounter(Image image) {
        this.image = new ImageView(image);
        // Preserve the image ratio
        this.image.setPreserveRatio(true);
        this.image.setFitWidth(GUIConfigs.collectablesWidth);


        this.counterLabel = new Text("x 0");
        counterLabel.setStyle("-fx-font-size: 50;");

        container.getChildren().addAll(this.image, counterLabel);

        container.setSpacing(10);
        container.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2);");
        container.setAlignment(Pos.CENTER);
    }

    public void setCounter(int counter) {
        this.counter = counter;
        counterLabel.setText("x " + counter);
    }

    public Node getContent() {
        return container;
    }

}
