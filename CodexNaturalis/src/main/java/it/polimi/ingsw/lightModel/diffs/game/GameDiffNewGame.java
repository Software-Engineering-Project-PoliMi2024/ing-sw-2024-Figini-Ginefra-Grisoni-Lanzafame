package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.List;

public class GameDiffNewGame extends GameDiff{
    private final GameDiffGameName gameName;
    private final GameDiffYourName yourName;
    private final GameDiffInitializeCodexMap initializeCodexMap;
    private final GameDiffPlayerActivity playerActivity;
    private final List<DeckDiff> decks;
    private final List<CodexDiff> codex;
    private final List<HandDiff> handYours;
    private final List<HandOtherDiff> handOther;
    private final GameDiffPublicObj publicObjs;

    public GameDiffNewGame(GameDiffGameName gameName, GameDiffYourName yourName, GameDiffInitializeCodexMap initializeCodexMap, GameDiffPlayerActivity playerActivity, List<DeckDiff> decks, List<CodexDiff> codex, List<HandDiff> handYours, List<HandOtherDiff> handOther, GameDiffPublicObj publicObjs) {
        this.gameName = gameName;
        this.yourName = yourName;
        this.initializeCodexMap = initializeCodexMap;
        this.playerActivity = playerActivity;
        this.decks = decks;
        this.codex = codex;
        this.handYours = handYours;
        this.handOther = handOther;
        this.publicObjs = publicObjs;

    }
    @Override
    public void apply(LightGame game) {
        gameName.apply(game);
        yourName.apply(game);
        initializeCodexMap.apply(game);
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
        publicObjs.apply(game);
    }
}