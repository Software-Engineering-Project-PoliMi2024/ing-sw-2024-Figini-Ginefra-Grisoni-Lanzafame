package it.polimi.ingsw.view.GUI.Components.PawnRelated;

import it.polimi.ingsw.view.GUI.AssetsGUI;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.view.GUI.Components.Utils.PopUp;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.security.PublicKey;
import java.util.Map;

/**
 * This enum represents the different pawns in the game with their associated colors and images from AssetsGui.
 */
public enum PawnsGui {
    BLUE(AssetsGUI.pawnBleu, PawnColors.BLUE),
    GREEN(AssetsGUI.pawnGreen, PawnColors.GREEN),
    YELLOW(AssetsGUI.pawnJaune, PawnColors.YELLOW),
    BLACK(AssetsGUI.pawnBlack, PawnColors.BLACK),
    RED(AssetsGUI.pawnRed, PawnColors.RED);

    /** A map that associates each pawn color with its corresponding JavaFX color */
    private static final Map<PawnColors, Color> pawnColorsColorMap = Map.of(
        PawnColors.BLUE, Color.BLUE,
        PawnColors.GREEN, Color.GREEN,
        PawnColors.YELLOW, Color.DARKGOLDENROD,
        PawnColors.RED, Color.RED
    );

    /**
     * Returns the PawnsGui enum constant associated with the specified pawn color.
     *
     * @param pawnColor the color of the pawn.
     * @return the PawnsGui enum constant associated with the specified pawn color, or null if no match is found.
     */
    public static PawnsGui getPawnGui(PawnColors pawnColor) {
        for (PawnsGui p : PawnsGui.values()) {
            if (p.getPawnColor().equals(pawnColor)) {
                return p;
            }
        }
        return null;
    }

    /** The ImageView representing the pawn */
    private final ImageView imageView;
    /** The color of the pawn */
    private final PawnColors pawnColor;

    /**
     * Constructs a PawnsGui enum constant with the specified image and pawn color.
     *
     * @param image the image representing the pawn.
     * @param pawnColor the color of the pawn.
     */
    PawnsGui(Image image, PawnColors pawnColor) {
        this.imageView = new ImageView(image);
        this.pawnColor = pawnColor;
    }

    /**
     * Returns the ImageView of the pawn.
     *
     * @return the ImageView of the pawn.
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * Returns the color of the pawn.
     *
     * @return the color of the pawn.
     */
    public PawnColors getPawnColor() {
        return pawnColor;
    }

    /**
     * Returns the JavaFX Color associated with the specified pawn color.
     *
     * @param pawnColor the color of the pawn
     * @return the JavaFX Color associated with the specified pawn color.
     */
    public static Color getColor(PawnColors pawnColor) {
        return pawnColorsColorMap.get(pawnColor);
    }
}