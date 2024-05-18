package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import javafx.scene.image.Image;

import java.util.Objects;

public class AssetsGUI {

    public static Image logoCenter = new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/Logo/Center.png")));
    public static Image logoCircle = new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/Logo/Circle.png")));
    public static Image logoBackground = new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/Logo/Background.png")));
    public static Image logo = new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/CODEX_Rulebook_IT/01.png")));
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
        System.out.println("Resource: " + resource.toString());
        return new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/Collectables/" + resource.toString() + ".png")));
    }
}
