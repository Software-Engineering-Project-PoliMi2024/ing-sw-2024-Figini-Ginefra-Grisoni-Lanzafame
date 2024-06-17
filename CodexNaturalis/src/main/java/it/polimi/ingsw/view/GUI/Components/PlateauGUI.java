package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.view.GUI.AssetsGUI;
import it.polimi.ingsw.view.GUI.Components.Utils.PopUp;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Pair;

public class PlateauGUI {
    private final ImageView plateauView;
    private final PopUp popUp;

    private final StackPane container = new StackPane();

    public PlateauGUI(AnchorPane parent) {
        this.plateauView = new ImageView(AssetsGUI.plateau);

        this.popUp = new PopUp(parent);

        // Bind plateauView's size to height while maintaining aspect ratio
        this.plateauView.setPreserveRatio(true);
        plateauView.fitHeightProperty().bind(popUp.getContent().heightProperty().multiply(0.9));

        container.getChildren().add(plateauView);

        AnchorPane.setBottomAnchor(container, 0.0);
        AnchorPane.setTopAnchor(container, 0.0);
        AnchorPane.setLeftAnchor(container, 0.0);
        AnchorPane.setRightAnchor(container, 0.0);

        popUp.getContent().getChildren().add(container);
    }

    public void show() {
        popUp.open();
    }

    public void hide() {
        popUp.close();
    }

    private Point2D getCoordinates(int score) {
        Pair<Integer, Integer> coordinates = PlateauMapping.getPositionCoordinates(score);
        if (coordinates != null) {
            double x = (coordinates.getKey() - plateauView.getImage().getWidth() / 2) * plateauView.getFitWidth() / plateauView.getImage().getWidth();
            double y = (coordinates.getValue() - plateauView.getImage().getHeight() / 2) * plateauView.getFitHeight() / plateauView.getImage().getHeight();
            return new Point2D(x, y);
        }
        return null;
    }

    public void updatePawnPosition(int score, ImageView pawnView) {
//       Point2D coordinates = getCoordinates(score);
//        if (coordinates != null) {
//            pawnView.setTranslateX(coordinates.getX());
//            pawnView.setTranslateY(coordinates.getY());
//
//        }
        System.out.println("updatePawnPosition: " + score + " " + pawnView);
        if (!container.getChildren().contains(pawnView)) {
            container.getChildren().add(pawnView);
        }
    }
}