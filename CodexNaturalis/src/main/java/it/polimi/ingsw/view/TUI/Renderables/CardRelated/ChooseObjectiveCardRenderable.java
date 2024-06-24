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
 * This class is a Renderable that can render a card and prompt the player to choose between two objective cards.
 */
public class ChooseObjectiveCardRenderable extends CanvasRenderable {
    private final LightGame lightGame;
    private final CardMuseum museum;


    /**
     * Creates a new ChooseObjectiveCardRenderable.
     * @param name The name of the renderable.
     * @param museum The card museum to use.
     * @param game The lightGame to render.
     * @param relatedCommands The commands related to this renderable.
     * @param view The controller provider.
     */
    public ChooseObjectiveCardRenderable(String name, CardMuseum museum, LightGame game, CommandPrompt[] relatedCommands, ControllerProvider view, LightGame lightGame) {
        super(name, CardTextStyle.getCardWidth() * 2 + 2, CardTextStyle.getCardHeight(), relatedCommands, view);
        this.museum = museum;
        this.lightGame = lightGame;
        this.canvas.fillContent(CardTextStyle.getBackgroundEmoji());
    }

    /**
     * Renders the two objective cards.
     */
    public void render(){
        Printable option1Label = new Printable("");
        String firstOptionNumber = new DecoratedString("[1]", StringStyle.BOLD).toString();
        PromptStyle.printInABox(option1Label, "Option " + firstOptionNumber, CardTextStyle.getCardWidth() * 2 - 2);

        LightCard[] options = lightGame.getHand().getSecretObjectiveOptions();

        for(int i = 0; i < options.length; i++) {
            LightCard card = options[i];
            Drawable drawableCard = museum.get(card.idFront()).get(CardFace.FRONT);
            this.canvas.draw( drawableCard, CardTextStyle.getCardWidth() /2 + i * (CardTextStyle.getCardWidth() + 1), CardTextStyle.getCardHeight() /2);
        }

        Printable option2Label = new Printable("");
        String secondOptionNumber = new DecoratedString("[2]", StringStyle.BOLD).toString();
        PromptStyle.printInABox(option2Label,"Option " + secondOptionNumber, CardTextStyle.getCardWidth() * 2 - 2);

        Printer.printStackedHorizontally(List.of(option1Label, option2Label), "  ");

        //Prints the canvas
        super.render();
    }

    /**
     * Updates the renderable based on the command prompt result.
     * @param command The command prompt result.
     */
    public void updateCommand(CommandPromptResult command){
        switch (command.getCommand()) {
            case CommandPrompt.CHOOSE_OBJECTIVE_CARD:
                try {
                    int cardIndex = Integer.parseInt(command.getAnswer(0)) - 1;
                    view.getController().chooseSecretObjective(lightGame.getHand().getSecretObjectiveOptions()[cardIndex]);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case CommandPrompt.DISPLAY_OBJECTIVE_OPTIONS:
                this.render();
                break;
            default:
                break;
        }
    }
}
