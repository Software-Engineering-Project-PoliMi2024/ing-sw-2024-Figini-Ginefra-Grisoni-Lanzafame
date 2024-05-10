package it.polimi.ingsw.view.GUI;

import javafx.scene.image.Image;

import java.util.Objects;

public class CardMuseumGUI {
    public static Image loadCardFront(int id){
        String idString = String.valueOf(id);
        idString = "000".substring(idString.length()) + idString;
        return new Image(Objects.requireNonNull(GUI.class.getResourceAsStream("/GUI/images/CODEX_cards_gold_front/"+idString+".png")));
    }
}
