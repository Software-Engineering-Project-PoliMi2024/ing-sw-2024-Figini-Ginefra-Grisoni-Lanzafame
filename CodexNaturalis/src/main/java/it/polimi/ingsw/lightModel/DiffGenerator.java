package it.polimi.ingsw.lightModel;


import it.polimi.ingsw.Configs;
import it.polimi.ingsw.lightModel.diffs.game.*;
import it.polimi.ingsw.lightModel.diffs.game.codexDiffs.CodexDiffPlacement;
import it.polimi.ingsw.lightModel.diffs.game.deckDiffs.DeckDiff;
import it.polimi.ingsw.lightModel.diffs.game.deckDiffs.DeckDiffBufferDraw;
import it.polimi.ingsw.lightModel.diffs.game.deckDiffs.DeckDiffDeckDraw;
import it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs.GameDiffCurrentPlayer;
import it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs.GameDiffFirstPlayer;
import it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs.GameDiffPlayerActivity;
import it.polimi.ingsw.lightModel.diffs.game.handDiffOther.HandOtherDiff;
import it.polimi.ingsw.lightModel.diffs.game.handDiffOther.HandOtherDiffAdd;
import it.polimi.ingsw.lightModel.diffs.game.handDiffs.HandDiff;
import it.polimi.ingsw.lightModel.diffs.game.handDiffs.HandDiffAdd;
import it.polimi.ingsw.lightModel.diffs.game.handDiffs.HandDiffAddOneSecretObjectiveOption;
import it.polimi.ingsw.lightModel.diffs.game.handDiffs.HandDiffSetObj;
import it.polimi.ingsw.lightModel.diffs.game.joinDiffs.*;
import it.polimi.ingsw.lightModel.diffs.game.pawnChoiceDiffs.GameDiffSetPawns;
import it.polimi.ingsw.lightModel.diffs.game.pawnChoiceDiffs.GameDiffSetPlayerColor;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyDiffEdit;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyDiffEditLogin;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyListDiff;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyListDiffEdit;
import it.polimi.ingsw.lightModel.lightPlayerRelated.*;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Hand;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.model.playerReleted.Player;
import it.polimi.ingsw.model.tableReleted.Deck;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DiffGenerator {
    /**
     * Generates a diff that adds a lobby to the lobbyList
     * @param addedLobby the lobby to add
     * @return the diff that adds the lobby
     */
    public static LobbyListDiff addLobbyDiff(Lobby addedLobby){
        return new LobbyListDiffEdit(List.of(Lightifier.lightify(addedLobby)), new ArrayList<LightLobby>());
    }

    /**
     * Generates a diff that removes a lobby from the lobbyList
     * @param removedLobby the lobby to remove
     * @return the diff that removes the lobby
     */
    public static LobbyListDiff removeLobbyDiff(Lobby removedLobby){
        return new LobbyListDiffEdit(new ArrayList<LightLobby>(), List.of(Lightifier.lightify(removedLobby)));
    }

    /**
     * Generates a diff that updates the lobbyList with the history of the lobbies
     * @param lobbyHistory the history of the lobbies
     * @return the diff that updates the lobbyList with the history of the lobbies
     */
    public static LobbyListDiff lobbyListHistory(List<Lobby> lobbyHistory){
        return new LobbyListDiffEdit(new ArrayList<LightLobby>(Lightifier.lightify(lobbyHistory)), new ArrayList<LightLobby>());
    }

    /**
     * Generates a diff that updates the lobby with the attributes of the lobby joined
     * @param lobbyJoined the lobby joined by the subscriber
     * @return the diff that updates the lobby with the attributes of the lobby joined
     */
    public static LobbyDiffEditLogin diffJoinLobby(Lobby lobbyJoined){
        return new LobbyDiffEditLogin(lobbyJoined.getLobbyPlayerList(), new ArrayList<>(), lobbyJoined.getLobbyName(), lobbyJoined.getNumberOfMaxPlayer());
    }

    /**
     * Generates a diff that adds a user to the lobby
     * @param nickname the nickname of the user to add
     * @return the diff that adds the user to the lobby
     */
    public static LobbyDiffEdit diffAddUserToLobby(String nickname){
        return new LobbyDiffEdit(List.of(nickname), new ArrayList<>());
    }

    /**
     * Generates a diff that removes a user from the lobby
     * @param nickname the nickname of the user to remove
     * @return the diff that removes the user from the lobby
     */
    public static LobbyDiffEdit diffRemoveUserFromLobby(String nickname){
        return new LobbyDiffEdit(new ArrayList<>(), List.of(nickname));
    }

    /**
     * Generates a diff that updates the deck with a new card after one is drawn
     * @param deckType the type of the deck
     * @param pos the position of the card (2 = deck; 0,1 = buffer)
     * @return the diff that replace the buffer or the deck card drawn on the lightModel
     */
    public static List<DeckDiff> draw(DrawableCard deckType, int pos, Game game){
        List<DeckDiff> diff = new ArrayList<>();
        CardInHand replacement;
        if(pos == Configs.actualDeckPos) {
            if (deckType == DrawableCard.RESOURCECARD)
                replacement = game.getResourceCardDeck().showTopCardOfDeck();
            else
                replacement = game.getGoldCardDeck().showTopCardOfDeck();
            diff.add(new DeckDiffDeckDraw(deckType, Lightifier.lightifyToBack(replacement)));
        }else {
            CardInHand deckReplacement;
            if(deckType == DrawableCard.RESOURCECARD) {
                replacement = game.getResourceCardDeck().showCardFromBuffer(pos);
                deckReplacement = game.getResourceCardDeck().showTopCardOfDeck();
            }else{
                replacement = game.getGoldCardDeck().showCardFromBuffer(pos);
                deckReplacement = game.getGoldCardDeck().showTopCardOfDeck();
            }
            diff.add(new DeckDiffBufferDraw(Lightifier.lightifyToCard(replacement), pos, deckType));
            diff.add(new DeckDiffDeckDraw(deckType, Lightifier.lightifyToBack(deckReplacement)));
        }
        return diff;
    }

    public static CodexDiffPlacement placeCodexDiff(String placer, LightPlacement placement, Codex codex){
        return new CodexDiffPlacement(placer, codex.getPoints(),
                codex.getEarnedCollectables(), List.of(placement), codex.getFrontier().getFrontier());
    }

    public static GameDiffJoin updateJoinStartCard(Game game, List<String> activePlayers, String nickname){
        GameDiffJoin joinDiff = new GameDiffJoin(getInitialization(game, nickname));
        joinDiff.put(getPlayerActivity(activePlayers));
        joinDiff.put(new ArrayList<>(getDeckCurrentState(game)));
        joinDiff.put(new ArrayList<>(getCodexCurrentState(game)));
        joinDiff.put(new ArrayList<>(getHandYourCurrentState(game.getPlayerFromNick(nickname))));

        return joinDiff;
    }

    public static GameDiffJoin updateChosePawn(Game game, List<String> activePlayers, String nickname){
        GameDiffJoin joinDiff = updateJoinStartCard(game, activePlayers, nickname);
        joinDiff.put(new ArrayList<>(getHandOtherCurrentState(game, nickname)));
        joinDiff.put(new ArrayList<>(getPawnCurrentState(game)));

        return joinDiff;
    }

    public static GameDiffJoin updateJoinSecretObj(Game game, List<String> activePlayers, String nickname){
        GameDiffJoin joinDiff = updateChosePawn(game, activePlayers, nickname);

        return joinDiff;
    }

    public static GameDiffJoin updateJoinActualGame(Game game, List<String> activePlayers, String nickname){
        GameDiffJoin joinDiff = updateJoinSecretObj(game, activePlayers, nickname);
        joinDiff.put(getPublicObjectiveCurrentState(game));

        return joinDiff;
    }

    public static List<GameDiff> getPawnCurrentState(Game game){
        List<GameDiff> pawnsDiff = new ArrayList<>();
        List<PawnColors> pawnChoices = game.getPawnChoices();
        if(pawnChoices != null)
            pawnsDiff.add(new GameDiffSetPawns(pawnChoices));
        for(Player player : game.getPlayersList()) {
            PawnColors playerColor = player.getPawnColor();
            if(playerColor != null)
                pawnsDiff.add(new GameDiffSetPlayerColor(player.getNickname(), playerColor));
        }

        return pawnsDiff;
    }

    private static GameDiffInitialization getInitialization(Game game, String nickname){
        return new GameDiffInitialization(
                game.getPlayersList().stream().map(Player::getNickname).toList(),
                game.getName(),
                nickname,
                new GameDiffCurrentPlayer(game.getCurrentPlayer().getNickname()),
                new GameDiffFirstPlayer(game.getPlayersList().getFirst().getNickname())
        );
    }

    private static List<DeckDiff> getDeckCurrentState(Game game){
        List<DeckDiff> deckDiff = new ArrayList<>();
        Deck<ResourceCard> resourceCardDeck = game.getResourceCardDeck();
        Deck<GoldCard> goldCardDeck = game.getGoldCardDeck();

        for(int i=0; i < resourceCardDeck.getBuffer().toArray().length; i++){
            deckDiff.add(new DeckDiffBufferDraw(Lightifier.lightifyToCard(resourceCardDeck.getBuffer().stream().toList().get(i)), i, DrawableCard.RESOURCECARD));
        }
        for(int i=0; i < goldCardDeck.getBuffer().toArray().length; i++){
            deckDiff.add(new DeckDiffBufferDraw(Lightifier.lightifyToCard(goldCardDeck.getBuffer().stream().toList().get(i)), i, DrawableCard.GOLDCARD));
        }

        if(resourceCardDeck.getActualDeck().peek()!=null)
            deckDiff.add(new DeckDiffDeckDraw(DrawableCard.RESOURCECARD, Lightifier.lightifyToBack(Objects.requireNonNull(resourceCardDeck.showTopCardOfDeck()))));
        if(goldCardDeck.getActualDeck().peek() != null)
            deckDiff.add(new DeckDiffDeckDraw(DrawableCard.GOLDCARD, Lightifier.lightifyToBack(Objects.requireNonNull(goldCardDeck.showTopCardOfDeck()))));
        return deckDiff;
    }

    /**
     * @param activePlayers the list of the active players in the game
     * @return a diff containing the current state of the active players in the game
     */
    private static GameDiffPlayerActivity getPlayerActivity(List<String> activePlayers){
        return new GameDiffPlayerActivity(activePlayers, new ArrayList<>());
    }

    /**
     * @return a list of diffs containing the current state of the codexes in the game
     */
    private static List<CodexDiffPlacement> getCodexCurrentState(Game game){
        List<CodexDiffPlacement> codexDiff = new ArrayList<>();
        List<String> nicknamesInGameParty = game.getGameParty().getPlayersList().stream().map(Player::getNickname).toList();
        for(String nickname : nicknamesInGameParty){
            LightCodex codex = Lightifier.lightify(game.getGameParty().getPlayersList().stream().filter(user -> user.getNickname().equals(nickname)).findFirst().get().getUserCodex());
            codexDiff.add(new CodexDiffPlacement(
                    nickname,
                    codex.getPoints(),
                    codex.getEarnedCollectables(),
                    codex.getPlacementHistory().stream().toList(),
                    codex.getFrontier().frontier()
            ));
        }
        return codexDiff;
    }

    /**
     * @param player the user to which sends the hand diffs
     * @return a list of diffs containing the current state of the hand of the subscriber
     */
    public static List<HandDiff> getHandYourCurrentState(Player player) {
        List<HandDiff> handDiffAdd = new ArrayList<>();
        //User user = game.getUserFromNick(nickname);
        Hand hand = player.getUserHand();

        LightHand subscriberHand = Lightifier.lightifyYour(player.getUserHand(), player.getUserCodex());
        for (LightCard card : subscriberHand.getCards()) {
            if (card != null)
                handDiffAdd.add(new HandDiffAdd(card, subscriberHand.isPlayble(card)));
        }

        if (subscriberHand.getSecretObjective() != null)
            handDiffAdd.add(new HandDiffSetObj(subscriberHand.getSecretObjective()));

        List<ObjectiveCard> objectiveOptions = player.getUserHand().getSecretObjectiveChoices();
        if(objectiveOptions != null){
            handDiffAdd.add(new HandDiffAddOneSecretObjectiveOption(Lightifier.lightifyToCard(objectiveOptions.getFirst())));
            handDiffAdd.add(new HandDiffAddOneSecretObjectiveOption(Lightifier.lightifyToCard(objectiveOptions.getLast())));
        }

        StartCard startCard = player.getUserHand().getStartCard();
        if(startCard != null)
            handDiffAdd.add(new HandDiffAdd(Lightifier.lightifyToCard(startCard), true));

        return handDiffAdd;
    }

    /**
     * @param game the subscriber to which sends the hand of the other players
     * @return a list of diffs containing the current state of the hand of the other players
     */
    public static List<HandOtherDiff> getHandOtherCurrentState(Game game, String nickname){
        List<HandOtherDiff> handOtherDiff = new ArrayList<>();
        for(Player player : game.getGameParty().getPlayersList()){
            if(!player.getNickname().equals(nickname)){
                Hand handOther = player.getUserHand();
                LightHandOthers otherHand = Lightifier.lightifyOthers(handOther);
                for(LightBack card : otherHand.getCards()){
                    handOtherDiff.add(new HandOtherDiffAdd(card, player.getNickname()));
                }
            }

        }

        return handOtherDiff;
    }

    /**
     * @return a diff containing the current state of the public objectives in the game
     */
    private static GameDiffPublicObj getPublicObjectiveCurrentState(Game game){
        LightCard[] publicObj = game.getCommonObjective().stream().map(Lightifier::lightifyToCard).toArray(LightCard[]::new);
        if(publicObj.length == 2)
            return new GameDiffPublicObj(publicObj);
        return new GameDiffPublicObj();
    }
}
