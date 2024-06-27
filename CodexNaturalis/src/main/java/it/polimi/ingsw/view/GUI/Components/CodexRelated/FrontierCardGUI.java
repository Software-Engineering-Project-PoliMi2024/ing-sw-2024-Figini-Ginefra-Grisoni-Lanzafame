package it.polimi.ingsw.view.GUI.Components.CodexRelated;

import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

/**
 * This class represents the GUI version of the frontier card of a codex.

 */
public class FrontierCardGUI {
    /** The position of the card in the grid of the codex. */
    private final Position gridPosition;
    /** The rectangle that represents the card. */
    private final Rectangle rectangle = new Rectangle(0, 0, GUIConfigs.cardWidth, GUIConfigs.cardHeight);

    /**
     * Creates a new FrontierCardGUI.
     * @param gridPosition the position of the card in the grid of the codex.
     * @param x the x coordinate of the card.
     * @param y the y coordinate of the card.
     */
    public FrontierCardGUI(Position gridPosition, double x, double y) {
        this.gridPosition = gridPosition;
        this.rectangle.setStyle(
                String.format(
                        "-fx-fill: transparent; -fx-stroke: gray; -fx-stroke-width: 2; -fx-border-radius: %d; -fx-arc-width: %d; -fx-arc-height: %d;",
                        GUIConfigs.cardBorderRadius, GUIConfigs.cardBorderRadius, GUIConfigs.cardBorderRadius));
        this.rectangle.setTranslateX(x);
        this.rectangle.setTranslateY(y);

        this.rectangle.setScaleX(1);
        this.rectangle.setScaleY(1);
    }

    /**
     * Returns the card.
     * @return the card.
     */
    public Node getCard() {
        return rectangle;
    }

    /**
     * Sets the visibility of the card.
     * @param visible true if the card has to be visible, false otherwise.
     */
    public void setVisibility(boolean visible) {
        rectangle.setVisible(visible);
    }

    /**
     * Sets the scale of the card.
     * @param scale the new scale of the card.
     */
    public void setScale(double scale) {
        rectangle.setScaleX(scale);
        rectangle.setScaleY(scale);
    }

    /**
     * Sets the scale of the card and updates the translation accordingly.
     * @param scale the new scale of the card.
     * @param center the center of the scaling.
     */
    public void setScaleAndUpdateTranslation(double scale, Point2D center) {
        rectangle.setTranslateX((rectangle.getTranslateX() - center.getX()) * scale / rectangle.getScaleX() + center.getX());
        rectangle.setTranslateY((rectangle.getTranslateY() - center.getY()) * scale / rectangle.getScaleY() + center.getY());

        rectangle.setScaleX(scale);
        rectangle.setScaleY(scale);
    }

    /**
     * Returns the position of the card in the grid of the codex.
     * @return the position of the card in the grid of the codex.
     */
    public Position getGridPosition() {
        return gridPosition;
    }
}
