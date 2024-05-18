package it.polimi.ingsw.view.GUI.Components.CardRelated;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import javafx.scene.input.MouseEvent;

import java.util.function.Consumer;

public class DraggableCard extends CardGUI{
    private double offsetX = 0;
    private double offsetY = 0;

    private Consumer<MouseEvent> onDrag = null;

    public DraggableCard(LightCard target, CardFace face) {
        super(target, face);

        this.getImageView().setOnMouseDragged(e -> {
            if(onDrag != null) {
                onDrag.accept(e);
            }
        });
    }

    public DraggableCard(CardGUI card) {
        this(card.getTarget(), card.getFace());
    }

    public void setOnDrag(Consumer<MouseEvent> onDrag) {
        this.onDrag = onDrag;
    }
}
