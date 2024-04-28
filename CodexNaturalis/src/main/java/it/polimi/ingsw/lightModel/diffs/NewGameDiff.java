package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.*;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewGameDiff extends GameDiff{
    private final String gameName;
    private final List<String> playerNicknames;
    private final String firstToStart;
    private final Map<DrawableCard, LightDeck> decks;

    /**
     * @param gameName the name of the game
     * @param playerNicknames the nicknames of the players that will play the game
     * @param firstToStart the nickname of the player that will start to play
     * @param decks the decks that will be used in the game
     */
    public NewGameDiff(String gameName, List<String> playerNicknames, String firstToStart, Map<DrawableCard, LightDeck> decks) {
        this.gameName = gameName;
        this.playerNicknames = playerNicknames;
        this.firstToStart = firstToStart;
        this.decks = decks;

    }

    @Override
    public void apply(LightGame newGame) {
        newGame.setGameName(gameName);
        Map<String, LightCodex> codexMap = new HashMap<>();
        Map<String, LightHandOthers> handOthers = new HashMap<>();
        for(String player : playerNicknames){
            newGame.setInactivePlayer(player);
            codexMap.put(player, new LightCodex());
            handOthers.put(player, new LightHandOthers());
        }
        newGame.setCodexMap(codexMap);
        newGame.setCurrentPlayer(firstToStart);
        newGame.setOtherHand(handOthers);
        newGame.setDecks(decks);
    }
}
