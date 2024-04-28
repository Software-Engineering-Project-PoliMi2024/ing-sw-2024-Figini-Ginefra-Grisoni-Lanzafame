package it.polimi.ingsw.view.TUI.cardDrawing;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class CardMuseum implements Serializable {
    private final Map<Integer, TextCard> textCards;

    public CardMuseum() {
        textCards = new LinkedHashMap<>();
    }

    public void set(int id, TextCard card){
        textCards.put(id, card);
    }

    public TextCard get(int id){
        return textCards.get(id);
    }

    public int getSize(){
        return textCards.size();
    }
}
