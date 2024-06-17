package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.util.List;

public class PostGameStateRenderable extends Renderable {
    private final LightGame lightGame;

    public PostGameStateRenderable(String name, LightGame lightGame, CommandPrompt[] relatedCommands, ControllerProvider view) {
        super(name, relatedCommands, view);
        this.lightGame = lightGame;
    }

    @Override
    public void render() {
        List<String> winnersList = lightGame.getWinners();
        String winners = String.join(", ", winnersList);

        PromptStyle.printInABox("Game Over! Congratulations to the winners:", 70);
        PromptStyle.printInABox("Winners: " + winners, 70);
    }

    @Override
    public void updateCommand(CommandPromptResult answer) {
        switch (answer.getCommand()) {
            case DISPLAY_POSTGAME:
                this.render();
                break;
            default:
                break;
        }
    }
}
