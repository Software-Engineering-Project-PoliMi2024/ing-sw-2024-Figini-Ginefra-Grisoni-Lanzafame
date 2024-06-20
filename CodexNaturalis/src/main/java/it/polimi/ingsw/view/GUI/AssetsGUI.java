package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;
import javafx.scene.image.Image;

import java.util.Objects;

public class AssetsGUI {

    public static Image logoCenter = new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/Logo/Center.png")));
    public static Image logoCircle = new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/Logo/Circle.png")));
    public static Image logoBackground = new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/Logo/Background.png")));
    public static Image logo = new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/CODEX_Rulebook_IT/01.png")));

    public static Image plateau = new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/PLATEAU-SCORE-IMP/plateau.png")));
    public static Image pawnBleu = new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/Pion/CODEX_pion_bleu.png")));
    public static Image pawnJaune = new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/Pion/CODEX_pion_jaune.png")));
    public static Image pawnBlack = new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/Pion/CODEX_pion_noir.png")));
    public static Image pawnGreen = new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/Pion/CODEX_pion_vert.png")));
    public static Image pawnRed = new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images//Pion/CODEX_pion_rouge.png")));

    public static Image chatIcon = new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/Misc/chatIcon.png")));
    public static Image unreadChatIcon = new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/Misc/unreadChatIcon.png")));

    public static Image loadCardFront(int id){
        String idString = String.valueOf(id);
        idString = "000".substring(idString.length()) + idString;
        return new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/CODEX_cards_gold_front/"+idString+".png")));
    }
    
    public static Image loadCardBack(int id) {
        String idString = String.valueOf(id);
        idString = "000".substring(idString.length()) + idString;
        return new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/CODEX_cards_gold_back/" + idString + ".png")));
    }

    public static Image loadResource(Resource resource) {
        return new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/Collectables/" + resource + ".png")));
    }

    public static Image loadWritingMaterial(WritingMaterial writingMaterial) {
        return new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/Collectables/" + writingMaterial.toString() + ".png")));
    }

    public static Image loadResourceCircle(Resource resource) {
        return new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/ResourceCircles/" + resource + ".png")));
    }

    public static Image loadCharacter(Resource resource) {
        return new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/Characters/" + resource + ".png")));
    }

    public static Image eye = new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/Misc/eye.png")));
    public static Image closedEye = new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/Misc/closedEye.png")));

    public static Image loadCorner(CardCorner corner){
        return new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/Corners/" + corner + ".png")));
    }
}
