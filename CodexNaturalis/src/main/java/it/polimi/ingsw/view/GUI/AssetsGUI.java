package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;
import javafx.scene.image.Image;

import java.util.Objects;

public class AssetsGUI {
    //Animation assets
    public static Image logoCenter = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Logo/Center.png"), "Logo Center is null"));
    public static Image logoCircle = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Logo/Circle.png"), "Logo Circle is null"));
    public static Image logoBackground = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Logo/Background.png"), "Logo Background is null"));
    public static Image logo = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/CODEX_Rulebook_IT/01.png"), "Logo is null"));
    //Pawn assets
    public static Image plateau = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/PLATEAU-SCORE-IMP/plateau.png"), "Plateau is null"), 1575, 3150, true, true);
    public static Image pawnBleu = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Pion/CODEX_pion_bleu.png"), "Pawn Bleu is null"));
    public static Image pawnJaune = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Pion/CODEX_pion_jaune.png"), "Pawn Jaune is null"));
    public static Image pawnBlack = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Pion/CODEX_pion_noir.png"), "Pawn Black is null"));
    public static Image pawnGreen = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Pion/CODEX_pion_vert.png"), "Pawn Green is null"));
    public static Image pawnRed = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Pion/CODEX_pion_rouge.png"), "Pawn Red is null"));
    //Misc assets
    public static Image eye = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Misc/eye.png"), "Eye is null"), 100, 100, true, true);
    public static Image closedEye = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Misc/closedEye.png"), "Closed Eye is null"), 100, 100, true, true);
    public static Image bgTile = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/bgTile.png"), "bgTile is null"));
    public static Image book = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Misc/book.png"), "Book is null"), 100, 100, true, true);
    public static Image flower = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Misc/flower.png"), "Flower is null"), 100, 100, true, true);
    public static Image plateauIcon = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/PLATEAU-SCORE-IMP/plateauIcon.png"), "Plateau Icon is null"), 100, 100, true, true);
    public static Image loadCardFront(int id){
        String idString = String.valueOf(id);
        idString = "000".substring(idString.length()) + idString;
        return new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/CODEX_cards_gold_front/"+idString+".png"), "Card Front of id "+id+" is null"));
    }
    
    public static Image loadCardBack(int id) {
        String idString = String.valueOf(id);
        idString = "000".substring(idString.length()) + idString;
        return new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/CODEX_cards_gold_back/" + idString + ".png"), "Card Back of id " + id + " is null"));
    }

    public static Image loadResource(Resource resource) {
        return new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Collectables/" + resource + ".png"), "Resource " + resource + " is null"),
                100, 100, true, true);
    }

    public static Image loadWritingMaterial(WritingMaterial writingMaterial) {
        return new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Collectables/" + writingMaterial.toString() + ".png"), "Writing Material " + writingMaterial + " is null"),
                100, 100, true, true);
    }

    public static Image loadResourceCircle(Resource resource) {
        return new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/ResourceCircles/" + resource + ".png"), "Resource Circle " + resource + " is null"));
    }

    public static Image loadCharacter(Resource resource) {
        return new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Characters/" + resource + ".png"), "Character " + resource + " is null"));
    }

    public static Image loadCorner(CardCorner corner){
        return new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Corners/" + corner + ".png"), "Corner " + corner + " is null"), 500, 500, true, true);
    }
}
