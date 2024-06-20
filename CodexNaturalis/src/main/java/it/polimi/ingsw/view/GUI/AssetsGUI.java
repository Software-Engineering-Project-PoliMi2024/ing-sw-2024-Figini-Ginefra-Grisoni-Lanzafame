package it.polimi.ingsw.view.GUI;

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
    public static Image plateau = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/PLATEAU-SCORE-IMP/plateau.png"), "Plateau is null"));
    public static Image pawnBleu = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Pion/CODEX_pion_bleu.png"), "Pawn Bleu is null"));
    public static Image pawnJaune = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Pion/CODEX_pion_jaune.png"), "Pawn Jaune is null"));
    public static Image pawnBlack = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Pion/CODEX_pion_noir.png"), "Pawn Black is null"));
    public static Image pawnGreen = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Pion/CODEX_pion_vert.png"), "Pawn Green is null"));
    public static Image pawnRed = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Pion/CODEX_pion_rouge.png"), "Pawn Red is null"));
    //Misc assets
    public static Image chatIcon = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Misc/chatIcon.png"), "Chat Icon is null"));
    public static Image unreadChatIcon = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Misc/unreadChatIcon.png"), "Unread Chat Icon is null"));
    public static Image eye = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Misc/eye.png"), "Eye is null"));
    public static Image closedEye = new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Misc/closedEye.png"), "Closed Eye is null"));

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
        return new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Collectables/" + resource + ".png"), "Resource " + resource + " is null"));
    }

    public static Image loadWritingMaterial(WritingMaterial writingMaterial) {
        return new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Collectables/" + writingMaterial.toString() + ".png"), "Writing Material " + writingMaterial + " is null"));
    }

    public static Image loadResourceCircle(Resource resource) {
        return new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/ResourceCircles/" + resource + ".png")));
    }

    public static Image loadCharacter(Resource resource) {
        return new Image(Objects.requireNonNull(GUI.class.getClassLoader().getResourceAsStream("GUI/images/Characters/" + resource + ".png"), "Character " + resource + " is null"));
    }
}
