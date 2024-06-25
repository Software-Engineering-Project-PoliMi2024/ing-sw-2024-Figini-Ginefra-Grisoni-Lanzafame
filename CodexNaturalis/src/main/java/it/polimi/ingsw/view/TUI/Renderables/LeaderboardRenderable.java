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

import java.time.format.TextStyle;
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
        PromptStyle.printListInABox("Player Scores", scores.keySet().stream().map(nickname -> {

            String decoratedNickname = new DecoratedString(nickname, StringStyle.BOLD).toString();

            String pawnString = "  ";

            PawnColors nickColor = lightGame.getLightGameParty().getPlayerColor(nickname);
            if(nickColor != null) {
                StringStyle color = CardTextStyle.convertPawnBgColor(nickColor);
                pawnString = new DecoratedString(pawnString, color).toString();
            }

            String content = decoratedNickname + " - " + scores.get(nickname)  + " " + pawnString;

            Boolean isActive = lightGame.getLightGameParty().getPlayerActiveList().get(nickname);
            if(isActive != null && !isActive)
                content = new DecoratedString(content, StringStyle.STRIKETHROUGH).toString();

            boolean isCurrentPlayer = lightGame.getLightGameParty().getCurrentPlayer().equals(nickname);
            if(isCurrentPlayer)
                content = new DecoratedString(content, StringStyle.UNDERLINE).toString();

            return content;
        }).toList(), 70, 1);
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
