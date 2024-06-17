package it.polimi.ingsw.view.TUI.Renderables.Forms;

import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.util.HashSet;
import java.util.Set;

public class ChoosePawnForm extends FormRenderable {
    private final Set<PawnColors> availablePawns;

    public ChoosePawnForm(String name, CommandPrompt[] commandPrompts, ControllerProvider view) {
        super(name, commandPrompts, view);
        this.availablePawns = new HashSet<>();
        for (PawnColors color : PawnColors.values()) {
            if (color != PawnColors.BLACK) {
                availablePawns.add(color);
            }
        }
    }

    @Override
    public void updateCommand(CommandPromptResult answer) {
        if (answer.getCommand() == CommandPrompt.CHOOSE_PAWN) {
            try {
                String chosenPawn = answer.getAnswer(0).toUpperCase();
                PawnColors chosenColor = PawnColors.valueOf(chosenPawn);
                if (availablePawns.contains(chosenColor)) {
                    view.getController().choosePawn(chosenColor);
                    availablePawns.remove(chosenColor);
                } else {
                    System.out.println("Pawn already chosen or invalid choice. Please choose another.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}