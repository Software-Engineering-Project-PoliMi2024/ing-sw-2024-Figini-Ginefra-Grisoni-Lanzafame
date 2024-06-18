package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.view.GUI.AssetsGUI;
import it.polimi.ingsw.view.GUI.Components.Utils.PopUp;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlateauGUI {
    private final ImageView plateauView;
    private final PopUp popUp;

    private final StackPane container = new StackPane();

    private final Map<PawnColors, Integer> pawnScores = new HashMap<>();
    private final Map<PawnColors, ImageView> pawnViews = new HashMap<>();

    public PlateauGUI(AnchorPane parent) {
        this.plateauView = new ImageView(AssetsGUI.plateau);

        this.popUp = new PopUp(parent);

        // Bind plateauView's size to height while maintaining aspect ratio
        this.plateauView.fitWidthProperty().bind(this.plateauView.fitHeightProperty().multiply(plateauView.getImage().getWidth() / plateauView.getImage().getHeight()));
        this.plateauView.fitHeightProperty().bind(Bindings.min(popUp.getContent().heightProperty().multiply(0.9), popUp.getContent().widthProperty().multiply(0.9).multiply(plateauView.getImage().getHeight() / plateauView.getImage().getWidth())));

        container.getChildren().add(plateauView);

        AnchorPane.setBottomAnchor(container, 0.0);
        AnchorPane.setTopAnchor(container, 0.0);
        AnchorPane.setLeftAnchor(container, 0.0);
        AnchorPane.setRightAnchor(container, 0.0);

        popUp.getContent().getChildren().add(container);

        popUp.getContent().widthProperty().addListener((obs, oldWidth, newWidth) -> {
            this.updatePawnPosition();
        });

        popUp.getContent().heightProperty().addListener((obs, oldHeight, newHeight) -> {
            this.updatePawnPosition();
        });
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
            double y = (plateauView.getImage().getHeight() / 2 - coordinates.getValue()) * plateauView.getFitHeight() / plateauView.getImage().getHeight();
            System.out.println("Coordinates for score " + score + ": " + coordinates.getKey() + ", " + coordinates.getValue() + " width: " + plateauView.getFitWidth() + " height: " + plateauView.getFitHeight());
            return new Point2D(x, y);
        }
        return null;
    }

    public void setScore(PawnColors pawnColor, int score) {
        pawnScores.put(pawnColor, score);

        if(!pawnViews.containsKey(pawnColor)){
            ImageView pawnView = Objects.requireNonNull(PawnsGui.getPawnGui(pawnColor)).getImageView();
            pawnView.setPreserveRatio(true);
            pawnView.setFitHeight(100);
            pawnView.setFitWidth(100);
            pawnView.scaleXProperty().bindBidirectional(pawnView.scaleYProperty());
            pawnView.scaleXProperty().bind(plateauView.fitHeightProperty().divide(plateauView.getImage().getHeight()));
            pawnViews.put(pawnColor, pawnView);

            container.getChildren().add(pawnView);
        }

        updatePawnPosition(pawnColor);
    }

    public void updatePawnPosition(PawnColors pawnColor) {
        int score = pawnScores.get(pawnColor);
        Point2D coordinates = getCoordinates(score);
        if (coordinates != null) {
            System.out.println("Setting position of " + pawnColor + " score " + score + " to " + coordinates.getX() + ", " + coordinates.getY());
            ImageView pawnView = pawnViews.get(pawnColor);
            pawnView.setTranslateX(coordinates.getX());
            pawnView.setTranslateY(coordinates.getY());
        }
    }

    public void updatePawnPosition() {
        for (PawnColors pawnColor : pawnScores.keySet()) {
            updatePawnPosition(pawnColor);
        }
    }
}