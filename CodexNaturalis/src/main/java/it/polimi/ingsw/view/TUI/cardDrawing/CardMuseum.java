package it.polimi.ingsw.view.TUI.cardDrawing;

import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.view.TUI.Renderables.drawables.Drawable;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class CardMuseum implements Serializable {
    private final Map<Integer, TextCard> textCards;

    private final Map<Resource, Drawable> resourceBacks = new LinkedHashMap<>();

    public CardMuseum() {
        textCards = new LinkedHashMap<>();
    }

    public void set(int id, TextCard card){
        textCards.put(id, card);
    }

    public TextCard get(int id){
        return textCards.get(id);
    }

    public Map<Integer, TextCard> getCards(){
        return textCards;
    }

    public int getSize(){
        return textCards.size();
    }

    public Drawable getResourceBack(Resource resource){
        return resourceBacks.get(resource);
    }

    public void setResourceBack(Resource resource, Drawable drawable){
        resourceBacks.put(resource, drawable);
    }
}
