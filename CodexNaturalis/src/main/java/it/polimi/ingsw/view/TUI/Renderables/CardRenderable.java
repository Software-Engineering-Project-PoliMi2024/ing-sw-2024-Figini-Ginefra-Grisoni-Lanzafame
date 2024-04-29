package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ConnectionLayerClient;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.TUI.Renderables.drawables.Drawable;
import it.polimi.ingsw.view.TUI.cardDrawing.TextCard;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

public class CardRenderable extends Renderable {

    private final TextCard textCard;
    private final CardFace face;

    public CardRenderable(String name, LightCard target, CardFace face, CommandPrompt[] relatedCommands, ConnectionLayerClient controller){
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
