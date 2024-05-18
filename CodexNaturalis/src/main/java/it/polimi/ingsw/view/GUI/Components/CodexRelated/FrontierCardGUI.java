package it.polimi.ingsw.view.GUI.Components.CodexRelated;

import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;


public class FrontierCardGUI {
    private final Position gridPosition;
    private final Rectangle rectangle = new Rectangle(0, 0, (int)GUIConfigs.cardWidth, (int)GUIConfigs.cardHeight);

    public FrontierCardGUI(Position gridPosition, double x, double y) {
        this.gridPosition = gridPosition;
        this.rectangle.setStyle(
                String.format(
                        "-fx-fill: transparent; -fx-stroke: gray; -fx-stroke-width: 2; -fx-border-radius: %d; -fx-arc-width: %d; -fx-arc-height: %d;",
                        GUIConfigs.cardBorderRadius, GUIConfigs.cardBorderRadius, GUIConfigs.cardBorderRadius));
        this.rectangle.setTranslateX(x);
        this.rectangle.setTranslateY(y);
    }

    public Node getCard() {
        return rectangle;
    }

    public void setVisibility(boolean visible) {
        rectangle.setVisible(visible);
    }

    public void setScale(double scale, Point2D center) {
        rectangle.setTranslateX((rectangle.getTranslateX() - center.getX()) * scale / rectangle.getScaleX() + center.getX());
        rectangle.setTranslateY((rectangle.getTranslateY() - center.getY()) * scale / rectangle.getScaleY() + center.getY());

        rectangle.setScaleX(scale);
        rectangle.setScaleY(scale);
    }

    public Position getGridPosition() {
        return gridPosition;
    }
}
