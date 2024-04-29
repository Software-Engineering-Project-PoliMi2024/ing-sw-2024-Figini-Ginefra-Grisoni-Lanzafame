package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterfaceClient;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.model.cardReleted.cards.CardWithCorners;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.TUI.Renderables.drawables.Drawable;
import it.polimi.ingsw.view.TUI.Styles.CardTextStyle;
import it.polimi.ingsw.view.TUI.cardDrawing.TextCard;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

import java.util.Set;

public class CardRenderable extends Renderable {

    private final TextCard textCard;
    private final CardFace face;

    public CardRenderable(String name, LightCard target, CardFace face, CommandPrompt[] relatedCommands, ControllerInterfaceClient controller){
        super(name, relatedCommands, controller);
        this.face = face;
        this.textCard = null;
    }

    @Override
    public void render() {
        Drawable card = textCard.get(this.face);
        System.out.print(card.toString());
    }

    public void updateInput(String input) {
        // Do nothing
    }
}
