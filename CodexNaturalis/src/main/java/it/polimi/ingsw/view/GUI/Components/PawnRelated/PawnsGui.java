package it.polimi.ingsw.view.GUI.Components.PawnRelated;

import it.polimi.ingsw.view.GUI.AssetsGUI;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.view.GUI.Components.Utils.PopUp;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.security.PublicKey;
import java.util.Map;

public enum PawnsGui {
    BLUE(AssetsGUI.pawnBleu, PawnColors.BLUE),
    GREEN(AssetsGUI.pawnGreen, PawnColors.GREEN),
    YELLOW(AssetsGUI.pawnJaune, PawnColors.YELLOW),
    BLACK(AssetsGUI.pawnBlack, PawnColors.BLACK),
    RED(AssetsGUI.pawnRed, PawnColors.RED);

    private static final Map<PawnColors, Color> pawnColorsColorMap = Map.of(
        PawnColors.BLUE, Color.BLUE,
        PawnColors.GREEN, Color.GREEN,
        PawnColors.YELLOW, Color.GOLD,
        PawnColors.RED, Color.RED
    );

    public static PawnsGui getPawnGui(PawnColors pawnColor) {
        for (PawnsGui p : PawnsGui.values()) {
            if (p.getPawnColor().equals(pawnColor)) {
                return p;
            }
        }
        return null;
    }

    private final ImageView imageView;
    private final PawnColors pawnColor;

    PawnsGui(Image image, PawnColors pawnColor) {
        this.imageView = new ImageView(image);
        this.pawnColor = pawnColor;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public PawnColors getPawnColor() {
        return pawnColor;
    }

    public static Color getColor(PawnColors pawnColor) {
        return pawnColorsColorMap.get(pawnColor);
    }
}