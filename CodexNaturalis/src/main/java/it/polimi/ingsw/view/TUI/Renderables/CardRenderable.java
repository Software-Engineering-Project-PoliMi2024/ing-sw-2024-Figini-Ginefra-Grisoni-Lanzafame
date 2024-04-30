package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.TUI.Renderables.drawables.Drawable;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.cardDrawing.TextCard;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

public class CardRenderable extends LightGameRenderable {
    private CardFace face;
    private final CardMuseum museum;

    public CardRenderable(String name, CardMuseum museum, LightGame game, CardFace face, CommandPrompt[] relatedCommands, ControllerInterface controller){
        super(name, relatedCommands, game, controller);
        this.face = face;
        this.museum = museum;
    }

    public void renderCard(LightCard card){
        TextCard textCard = museum.get(card.id());
        Drawable drawable = textCard.get(this.face);
        Printer.print(drawable.toString());
    }

    @Override
    public void render() {
        return;
    }

    public void updateInput(String input) {
        // Do nothing
    }

    public void setFace(CardFace face) {
        this.face = face;
    }

}
