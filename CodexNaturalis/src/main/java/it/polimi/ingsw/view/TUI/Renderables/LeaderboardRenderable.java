package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Styles.CardTextStyle;
import it.polimi.ingsw.view.TUI.Styles.DecoratedString;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.Styles.StringStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class represents the Renderable that displays the leaderboard of players based on their scores.
 */
public class LeaderboardRenderable extends Renderable {
    /** The LightGame object representing the game's state */
    private final LightGame lightGame;

    /**
     * Constructs a LeaderboardRenderable object.
     *
     * @param name The name of the Renderable.
     * @param lightGame The LightGame object representing the game's state.
     * @param relatedCommands The commands related to this Renderable.
     * @param view The ControllerProvider object for controller access.
     */
    public LeaderboardRenderable(String name, LightGame lightGame, CommandPrompt[] relatedCommands, ControllerProvider view) {
        super(name, relatedCommands, view);
        this.lightGame = lightGame;
    }

    /**
     * Renders the leaderboard.
     * Displays player names, scores, and additional styling based on their state.
     */
    @Override
    public void render() {
        Map<String, Integer> scores = lightGame.getCodexMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getPoints()));

        String leaderboard = scores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining("\n"));

        PromptStyle.printInABox("Leaderboard", 70);
        PromptStyle.printListInABox("Player Scores", scores.keySet().stream().map(nickname -> {

            String decoratedNickname = new DecoratedString(nickname, StringStyle.BOLD).toString();

            String pawnString = "  ";

            PawnColors nickColor = lightGame.getLightGameParty().getPlayerColor(nickname);
            if(nickColor != null) {
                StringStyle color = CardTextStyle.convertPawnBgColor(nickColor);
                pawnString = new DecoratedString(pawnString, color).toString();
            }

            String content = decoratedNickname + " - " + scores.get(nickname)  + " " + pawnString;

            Boolean isActive = lightGame.getLightGameParty().getPlayerActiveMap().get(nickname);
            if(isActive != null && !isActive)
                content = new DecoratedString(content, StringStyle.STRIKETHROUGH).toString();

            boolean isCurrentPlayer = lightGame.getLightGameParty().getCurrentPlayer().equals(nickname);
            if(isCurrentPlayer)
                content = new DecoratedString(content, StringStyle.UNDERLINE).toString();

            return content;
        }).toList(), 70, 1);
    }

    /**
     * Updates the command based on the CommandPromptResult.
     *
     * @param answer The CommandPromptResult containing the user's command and input.
     */
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
