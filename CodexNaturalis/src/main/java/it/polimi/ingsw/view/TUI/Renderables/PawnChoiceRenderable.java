package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.TUI.Styles.CardTextStyle;
import it.polimi.ingsw.view.TUI.Styles.DecoratedString;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.time.format.TextStyle;
import java.util.*;

public class PawnChoiceRenderable extends Renderable {
    private final LightGame lightGame;
    public PawnChoiceRenderable(String name, CommandPrompt[] commandPrompts, ControllerProvider view, LightGame lightGame) {
        super(name, commandPrompts, view);
        this.lightGame = lightGame;
    }

    @Override
    public void render() {
        List<String> pawnOptions = new ArrayList<>();
        for (PawnColors pawn : lightGame.getLightGameParty().getFreePawns()) {
            pawnOptions.add(new DecoratedString(pawn.toString(), CardTextStyle.convertPawnBgColor(pawn)).toString());
        }
        PromptStyle.printListInABox("Pawn options", pawnOptions, 50, 1);
    }

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
}