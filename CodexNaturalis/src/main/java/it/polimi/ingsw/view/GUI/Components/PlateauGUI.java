package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.view.GUI.AssetsGUI;
import it.polimi.ingsw.view.GUI.Components.Utils.PopUp;
import javafx.beans.binding.Bindings;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;

public class PlateauGUI {
    private final ImageView plateauView;
    private final AnchorPane root;
    private final PopUp popUp;

    public PlateauGUI(AnchorPane parent) {
        this.plateauView = new ImageView(AssetsGUI.plateau);
        this.root = new AnchorPane(plateauView);
        this.root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75);");

        // Bind plateauView's size to height while maintaining aspect ratio
        plateauView.fitHeightProperty().bind(Bindings.createDoubleBinding(
                () -> root.getHeight() * 0.9,
                root.heightProperty()
        ));
        plateauView.fitWidthProperty().bind(Bindings.createDoubleBinding(
                () -> plateauView.getFitHeight() * (AssetsGUI.plateau.getWidth() / AssetsGUI.plateau.getHeight()),
                plateauView.fitHeightProperty()
        ));

        AnchorPane.setTopAnchor(plateauView, 10.0);
        AnchorPane.setBottomAnchor(plateauView, 10.0);
        AnchorPane.setLeftAnchor(plateauView, 10.0);
        AnchorPane.setRightAnchor(plateauView, 10.0);

        // Plateau is displayed above other components
        this.root.toFront();

        this.popUp = new PopUp(parent);
        popUp.getContent().getChildren().add(root);
    }

    public void show() {
        popUp.open();
    }

    public void hide() {
        popUp.close();
    }

    public void updatePawnPosition(int score, ImageView pawnView) {
        Pair<Integer, Integer> coordinates = PlateauMapping.getPositionCoordinates(score);
        if (coordinates != null) {
            pawnView.setLayoutX(coordinates.getKey());
            pawnView.setLayoutY(coordinates.getValue());
            if (!root.getChildren().contains(pawnView)) {
                root.getChildren().add(pawnView);
            }
        }
    }
}