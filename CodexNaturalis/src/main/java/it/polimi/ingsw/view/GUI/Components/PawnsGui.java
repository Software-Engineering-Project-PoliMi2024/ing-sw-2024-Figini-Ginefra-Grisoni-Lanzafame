package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.view.GUI.AssetsGUI;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public enum PawnsGui {
    BLUE(AssetsGUI.pawnBleu, PawnColors.BLUE),
    GREEN(AssetsGUI.pawnGreen, PawnColors.GREEN),
    YELLOW(AssetsGUI.pawnJaune, PawnColors.YELLOW),
    BLACK(AssetsGUI.pawnBlack, PawnColors.BLACK),
    RED(AssetsGUI.pawnRed, PawnColors.RED);

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
}