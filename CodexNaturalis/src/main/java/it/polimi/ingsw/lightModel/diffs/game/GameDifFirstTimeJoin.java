package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.List;

public class GameDifFirstTimeJoin extends GameDiff{
    private final GameDiffInitialization initialization;
    private final GameDiffPlayerActivity playerActivity;
    private final List<DeckDiff> decks;
    private final List<CodexDiff> codex;
    private final List<HandDiff> handYours;
    private final List<HandOtherDiff> handOther;

    /**
     * Creates a diff to update the lightGame when it's the first time a player joins
     * @param initialization the diff to initialize the game with the number of players,
     * the game name, the current player and the player's name
     * @param playerActivity the diff to update the player activity to set
     * which players are active
     * @param decks the diffs to set the current state of the decks
     * @param codex the diffs to set the current state of the codexes
     * @param handYours the diffs to get the current state of your hand
     * @param handOther the diffs to get the current state of the other players' hands
     */
    public GameDifFirstTimeJoin(GameDiffInitialization initialization, GameDiffPlayerActivity playerActivity, List<DeckDiff> decks, List<CodexDiff> codex, List<HandDiff> handYours, List<HandOtherDiff> handOther) {
        this.initialization = initialization;
        this.playerActivity = playerActivity;
        this.decks = decks;
        this.codex = codex;
        this.handYours = handYours;
        this.handOther = handOther;
    }
    @Override
    public void apply(LightGame game) {
        initialization.apply(game);
        playerActivity.apply(game);
        for (DeckDiff d : decks) {
            d.apply(game);
        }
        for (CodexDiff c : codex) {
            c.apply(game);
        }
        for (HandDiff h : handYours) {
            h.apply(game);
        }
        for (HandOtherDiff h : handOther) {
            h.apply(game);
        }
    }
}
