package it.polimi.ingsw.view.TUI.Renderables.drawables;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.model.cardReleted.cards.CardWithCorners;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.TUI.Styles.CardTextStyle;
import it.polimi.ingsw.view.TUI.cardDrawing.TextCard;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

import java.util.Set;

public class CardRenderable extends Renderable {

    private final TextCard textCard;
    private final CardFace face;

    public CardRenderable(LightCard target, CardFace face, CommandPrompt[] relatedCommands){
        super(relatedCommands);
        this.face = face;
        this.textCard = null;
    }

    @Override
    public void render() {
        Drawable card = textCard.get(this.face);
        System.out.print(card.toString());
    }

    @Override
    public void update() {

    }


    public void updateInput(String input) {
        // Do nothing
    }
}
