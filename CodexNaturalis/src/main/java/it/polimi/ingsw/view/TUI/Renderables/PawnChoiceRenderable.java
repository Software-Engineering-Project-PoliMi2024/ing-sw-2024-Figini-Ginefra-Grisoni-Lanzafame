package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.utils.designPatterns.Observed;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Styles.CardTextStyle;
import it.polimi.ingsw.view.TUI.Styles.DecoratedString;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.util.*;

/**
 * This class is a Renderable that represents the choice of a pawn.
 */
public class PawnChoiceRenderable extends Renderable {
    /** The light game of the player. */
    private final LightGame lightGame;
    /**
     * Creates a new PawnChoiceRenderable.
     * @param name The name of the renderable.
     * @param commandPrompts The commands related to this renderable.
     * @param view The controller provider.
     * @param lightGame The light game of the player.
     */
    public PawnChoiceRenderable(String name, CommandPrompt[] commandPrompts, ControllerProvider view, LightGame lightGame) {
        super(name, commandPrompts, view);
        this.lightGame = lightGame;
    }

    /**
     * Renders the pawn options.
     */
    @Override
    public void render() {
        List<String> pawnOptions = new ArrayList<>();
        for (PawnColors pawn : lightGame.getLightGameParty().getFreePawns()) {
            pawnOptions.add(new DecoratedString(pawn.toString(), CardTextStyle.convertPawnBgColor(pawn)).toString());
        }
        PromptStyle.printListInABox("Pawn options", pawnOptions, 50, 1);
    }

    /**
     * Updates the renderable based on the command prompt result.
     * @param answer The command prompt result.
     */
    @Override
    public void updateCommand(CommandPromptResult answer) {
        if (answer.getCommand() == CommandPrompt.CHOOSE_PAWN) {
            try {
                String chosenPawn = answer.getAnswer(0).toUpperCase();
                PawnColors chosenColor = PawnColors.valueOf(chosenPawn);
                view.getController().choosePawn(chosenColor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (answer.getCommand() == CommandPrompt.DISPLAY_PAWN_OPTIONS) {
            render();
        }
    }

    /**
     * Gets the part of the light model observed by this renderable.
     * @return The observed light model.
     */
    @Override
    public List<Observed> getObservedLightModel(){
        List<Observed> observed = new ArrayList<>();
        observed.add(lightGame.getLightGameParty());
        return observed;

    }
}