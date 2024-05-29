package it.polimi.ingsw.lightModel.diffs.game.joinDiffs;

import it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs.GameDiffPlayerActivity;
import it.polimi.ingsw.lightModel.diffs.game.GameDiffPublicObj;
import it.polimi.ingsw.lightModel.diffs.game.handDiffs.HandDiff;
import it.polimi.ingsw.lightModel.diffs.game.handDiffOther.HandOtherDiff;
import it.polimi.ingsw.lightModel.diffs.game.codexDiffs.CodexDiffPlacement;
import it.polimi.ingsw.lightModel.diffs.game.deckDiffs.DeckDiff;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.List;

public class GameDiffJoinMidGame extends GameDiffJoinSecretObj {
    private final GameDiffPublicObj publicObjs;

    public GameDiffJoinMidGame(GameDiffInitialization initializeCodexMap, GameDiffPlayerActivity playerActivity, List<DeckDiff> decks, List<CodexDiffPlacement> codex, List<HandDiff> handYours, List<HandOtherDiff> handOther, GameDiffPublicObj publicObjs) {
        super(initializeCodexMap, playerActivity, decks, codex, handYours, handOther);
        this.publicObjs = publicObjs;

    }
    @Override
    public void apply(LightGame game) {
        super.apply(game);
        publicObjs.apply(game);
    }
}
