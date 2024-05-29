package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.List;

public class GameDiffJoinSecretObj extends GameDiffJoinStartCard{
    private final List<HandOtherDiff> handOther;

    /**
     * Creates a diff to update the lightGame when it's the first time a player joins
     *
     * @param initialization the diff to initialize the game with the number of players,
     *                       the game name, the current player and the player's name
     * @param playerActivity the diff to update the player activity to set
     *                       which players are active
     * @param decks          the diffs to set the current state of the decks
     * @param codex          the diffs to set the current state of the codexes
     * @param handYours      the diffs to get the current state of your hand
     * @param handOther      the diffs to get the current state of the other players' hands
     */
    public GameDiffJoinSecretObj(GameDiffInitialization initialization, GameDiffPlayerActivity playerActivity, List<DeckDiff> decks, List<CodexDiffPlacement> codex, List<HandDiff> handYours, List<HandOtherDiff> handOther) {
        super(initialization, playerActivity, decks, codex, handYours);
        this.handOther = handOther;
    }

    @Override
    public void apply(LightGame game) {
        super.apply(game);
        for (HandOtherDiff h : handOther) {
            h.apply(game);
        }
    }
}
