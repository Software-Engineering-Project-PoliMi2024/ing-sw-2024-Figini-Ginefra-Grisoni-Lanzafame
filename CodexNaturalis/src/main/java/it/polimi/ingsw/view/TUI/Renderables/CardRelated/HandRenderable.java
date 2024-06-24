package it.polimi.ingsw.view.TUI.Renderables.CardRelated;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Printing.Printable;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.TUI.Renderables.CodexRelated.CanvasRenderable;
import it.polimi.ingsw.view.TUI.Renderables.drawables.Drawable;
import it.polimi.ingsw.view.TUI.Styles.CardTextStyle;
import it.polimi.ingsw.view.TUI.Styles.DecoratedString;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.Styles.StringStyle;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.cardDrawing.TextCard;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is a Renderable that can render the hand of the main player.
 */
public class HandRenderable extends CanvasRenderable {
    private final CardMuseum museum;
    private final LightGame lightGame;

    /**
     * Creates a new HandRenderable.
     * @param name The name of the renderable.
     * @param museum The card museum to use.
     * @param game The lightGame to render.
     * @param relatedCommands The commands related to this renderable.
     * @param view The controller provider.
     */
    public HandRenderable(String name, CardMuseum museum, LightGame game, CommandPrompt[] relatedCommands, ControllerProvider view) {
        super(name, CardTextStyle.getCardWidth() * 4 + 4, CardTextStyle.getCardHeight(), relatedCommands, view);
        this.museum = museum;
        this.lightGame = game;
    }

    private void renderCard(LightCard card){
        TextCard textCard = museum.get(card.idFront());
        Drawable drawable = textCard.get(CardFace.FRONT);
        Printer.print(drawable.toString());
    }

    /**
     * Renders the hand of the main player.
     */
    @Override
    public void render() {
        PromptStyle.printInABox("Hand", CardTextStyle.getCardWidth() * 8 + 6);
        this.canvas.fillContent(CardTextStyle.getBackgroundEmoji());
        List<Printable> infos = new LinkedList<>();

        for (int i = 0; i < 3; i++) {
            String cardNumber = new DecoratedString("[" + (i + 1) + "]", StringStyle.BOLD).toString();
            String text = "Card " + cardNumber;

            Printable info = new Printable("");
            PromptStyle.printInABox(info, text, CardTextStyle.getCardWidth() * 2 - 2);
            infos.add(info);


            LightCard card = lightGame.getHand().getCards()[i];
            if(card == null){
                continue;
            }
            this.canvas.draw(museum.get(card.idFront()).get(CardFace.FRONT),
                    CardTextStyle.getCardWidth() /2 + (i + 1) * (CardTextStyle.getCardWidth() + 1) + 1,
                    CardTextStyle.getCardHeight() /2);
        }

        LightCard secretObjective = lightGame.getHand().getSecretObjective();
        if(secretObjective != null){
            this.canvas.draw(museum.get(secretObjective.idFront()).get(CardFace.FRONT),
                    CardTextStyle.getCardWidth() /2,
                    CardTextStyle.getCardHeight() /2);
        }


        Printable labels = new Printable("");
        Printer.printStackedHorizontally(labels, infos, "  ");

        Printable secretObjectiveLabel = new Printable("");
        PromptStyle.printInABox(secretObjectiveLabel, "Secret Objective", CardTextStyle.getCardWidth() * 2 - 2);

        Printer.printStackedHorizontally(List.of(secretObjectiveLabel, labels), "    ");
        super.render();
    }

    /**
     * Updates the renderable based on the command prompt result.
     * @param answer The command prompt result.
     */
    public void updateCommand(CommandPromptResult answer){
        switch (answer.getCommand()) {
            case CommandPrompt.DISPLAY_HAND:
                this.render();
                break;
            default:
                break;
        }
    }
}
