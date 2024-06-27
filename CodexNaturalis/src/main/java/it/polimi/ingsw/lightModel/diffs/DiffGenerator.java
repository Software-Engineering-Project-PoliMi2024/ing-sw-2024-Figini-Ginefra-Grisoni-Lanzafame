package it.polimi.ingsw.lightModel.diffs;


import it.polimi.ingsw.Configs;
import it.polimi.ingsw.lightModel.Lightifier;
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

/**
 * This class is used to generate the diffs that are sent to the client to update the lightModel
 */
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

    /**
     * Generates a diff that updates the deck with a new card after one is place
     * @param placer the nickname of the player that placed the card
     * @param placement the light placement of the card
     * @param codex the codex of the player that placed the card
     * @return a CodexDiffPlacement containing all the updated info for the player that placed the card
     */
    public static CodexDiffPlacement placeCodexDiff(String placer, LightPlacement placement, Codex codex){
        return new CodexDiffPlacement(placer, codex.getPoints(),
                codex.getEarnedCollectables(), List.of(placement), codex.getFrontier().getFrontier());
    }

    /**
     * Generate a diff for a player who is joining during the "Start Card" phase
     * @param game the game to which the player is joining
     * @param activePlayers the list of players that are active in that game
     * @param nickname the nickname of the player that is joining
     * @return a GameDiffJoin containing all the diffs needed to update the lightModel of the player that is joining
     */
    public static GameDiffJoin updateJoinStartCard(Game game, List<String> activePlayers, String nickname){
        GameDiffJoin joinDiff = new GameDiffJoin(getInitialization(game, nickname));
        ArrayList<String> allPlayers = new ArrayList<>(game.getGameParty().getPlayersList().stream().map(Player::getNickname).toList());
        allPlayers.removeAll(activePlayers);
        ArrayList<String> inactivePlayers = new ArrayList<>(allPlayers);
        joinDiff.put(new GameDiffPlayerActivity(activePlayers, inactivePlayers));
        joinDiff.put(new ArrayList<>(getDeckCurrentState(game)));
        joinDiff.put(new ArrayList<>(getCodexCurrentState(game)));
        joinDiff.put(new ArrayList<>(getHandYourCurrentState(game.getPlayerFromNick(nickname))));

        return joinDiff;
    }

    /**
     * Generate a diff for a player who is joining during the "Chose Pawn" phase
     * @param game the game to which the player is joining
     * @param activePlayers the list of players that are active in that game
     * @param nickname the nickname of the player that is joining
     * @return a GameDiffJoin containing all the diffs needed to update the lightModel of the player that is joining
     */
    public static GameDiffJoin updateChosePawn(Game game, List<String> activePlayers, String nickname){
        GameDiffJoin joinDiff = updateJoinStartCard(game, activePlayers, nickname);
        joinDiff.put(new ArrayList<>(getHandOtherCurrentState(game, nickname)));
        joinDiff.put(new ArrayList<>(getPawnCurrentState(game)));

        return joinDiff;
    }

    /**
     * Generate a diff for a player who is joining during the "Select Secret Objective" phase
     * @param game the game to which the player is joining
     * @param activePlayers the list of players that are active in that game
     * @param nickname the nickname of the player that is joining
     * @return a GameDiffJoin containing all the diffs needed to update the lightModel of the player that is joining
     */
    public static GameDiffJoin updateJoinSecretObj(Game game, List<String> activePlayers, String nickname){
        GameDiffJoin joinDiff = updateChosePawn(game, activePlayers, nickname);

        return joinDiff;
    }

    /**
     * Generate a diff for a player who is joining during the "Actual Game" phase
     * @param game the game to which the player is joining
     * @param activePlayers the list of players that are active in that game
     * @param nickname the nickname of the player that is joining
     * @return a GameDiffJoin containing all the diffs needed to update the lightModel of the player that is joining
     */
    public static GameDiffJoin updateJoinActualGame(Game game, List<String> activePlayers, String nickname){
        GameDiffJoin joinDiff = updateJoinSecretObj(game, activePlayers, nickname);
        joinDiff.put(getPublicObjectiveCurrentState(game));

        return joinDiff;
    }

    /**
     * Generate a diff for updating the pawn in the lightModel. If no pawn is chosen, the diff will be created with the default constructor
     * @param game the game from which the pawn state is taken
     * @return a list of GameDiff containing the diffs of the pawn. Each diff will either be empty or contain the pawn chosen by a player
     */
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

    /**
     * Generate a gameDiffInitialization
     * @param game the game from which the initialization is taken
     * @param nickname the nickname of the player that is joining
     * @return a GameDiffInitialization containing the initialization of the game
     */
    private static GameDiffInitialization getInitialization(Game game, String nickname){
        return new GameDiffInitialization(
                game.getPlayersList().stream().map(Player::getNickname).toList(),
                game.getName(),
                nickname,
                new GameDiffCurrentPlayer(game.getCurrentPlayer().getNickname()),
                new GameDiffFirstPlayer(game.getPlayersList().getFirst().getNickname())
        );
    }

    /**
     * Generate a list of diffs containing the current state of the deck in the game for updating the lightModel
     * @param game the game from which the deck state is taken from
     * @return a list of DeckDiff containing the current state of the deck
     */
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
                handDiffAdd.add(new HandDiffAdd(card, subscriberHand.isPlayable(card)));
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
