package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.util.Map;
import java.util.stream.Collectors;

public class LeaderboardRenderable extends Renderable {
    private final LightGame lightGame;

    public LeaderboardRenderable(String name, LightGame lightGame, CommandPrompt[] relatedCommands, ControllerProvider view) {
        super(name, relatedCommands, view);
        this.lightGame = lightGame;
    }

    @Override
    public void render() {
        Map<String, Integer> scores = lightGame.getCodexMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getPoints()));

        String leaderboard = scores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining("\n"));

        PromptStyle.printInABox("Leaderboard", 70);
        PromptStyle.printListInABox("Player Scores", scores.keySet().stream().map(k -> k + " - " + scores.get(k)).toList(), 70, 1);
    }


    @Override
    public void updateCommand(CommandPromptResult answer) {
        switch (answer.getCommand()) {
            case DISPLAY_LEADERBOARD:
                this.render();
                break;
            default:
                break;
        }
    }
}


/*} public void render() {
    this.scores = getLightGame().getCodexMap().values().stream().mapToInt(LightCodex::getPoints);
    List<String> scoreDisplay = scores.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .map(e -> e.getKey() + ": " + e.getValue())
            .collect(Collectors.toList());

    System.out.println(PromptStyle.Title);  // Optional: Adds a title or header if you have any defined for sections
    PromptStyle.printListInABox("Leaderboard", scoreDisplay, 50, 1);
}
 */