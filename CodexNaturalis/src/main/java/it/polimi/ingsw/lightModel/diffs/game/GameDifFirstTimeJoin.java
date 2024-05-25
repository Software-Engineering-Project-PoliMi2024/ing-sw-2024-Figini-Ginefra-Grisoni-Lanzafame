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

    public GameDifFirstTimeJoin(GameDiffInitialization initializeCodexMap, GameDiffPlayerActivity playerActivity, List<DeckDiff> decks, List<CodexDiff> codex, List<HandDiff> handYours, List<HandOtherDiff> handOther) {
        this.initialization = initializeCodexMap;
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
