package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.List;

public class GameDiffJoinMidGame extends GameDiffJoinSecretObj {
    private final GameDiffPublicObj publicObjs;

    public GameDiffJoinMidGame(GameDiffInitialization initializeCodexMap, GameDiffPlayerActivity playerActivity, List<DeckDiff> decks, List<CodexDiff> codex, List<HandDiff> handYours, List<HandOtherDiff> handOther, GameDiffPublicObj publicObjs) {
        super(initializeCodexMap, playerActivity, decks, codex, handYours, handOther);
        this.publicObjs = publicObjs;

    }
    @Override
    public void apply(LightGame game) {
        super.apply(game);
        publicObjs.apply(game);
    }
}
