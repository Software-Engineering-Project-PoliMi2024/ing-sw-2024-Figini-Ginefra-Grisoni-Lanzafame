package it.polimi.ingsw.lightModel;


import it.polimi.ingsw.lightModel.diffPublishers.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffs.game.*;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyDiffEdit;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyDiffEditLogin;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyListDiff;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyListDiffEdit;
import it.polimi.ingsw.lightModel.lightPlayerRelated.*;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.Hand;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Deck;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     * Generates a diff that updates the lightGame with the name of the user joined
     * @param nickname the nickname of the user joined
     * @return the diff that updates the lightGame with the name of the user joined
     */
    public static GameDiffPlayerActivity diffAddUserToGame(String nickname){
        return new GameDiffPlayerActivity(List.of(nickname), new ArrayList<>());
    }

    /**
     * Generates a diff that updates the lightGame with the current state of the game
     * for users that join the game for the first time
     * @param game the game to get the current state from and that the user is joining
     * @param nickname the nickname of the user joining the game
     * @param activePlayers a list containing the nickname of the active players
     * @return the diff that updates the lightGame with the current state of the game
     */
    public static GameDifFirstTimeJoin diffFirstTimeJoin(Game game, String nickname, List<String> activePlayers){
        return new GameDifFirstTimeJoin(
                getInitialization(game, nickname),
                getPlayerActivity(activePlayers),
                getDeckCurrentState(game),
                getCodexCurrentState(game),
                getHandYourCurrentState(game, nickname),
                getHandOtherCurrentState(game, nickname)
        );
    }

    /**
     * Generates a diff that updates the lightGame with the current state of the game
     * for users that rejoin the game after a disconnection
     * @param game the game to get the current state from and that the user is rejoining
     * @param nickname the nickname of the user rejoining the game
     * @param activePlayers a list containing the nickname of the active players
     * @return the diff that updates the lightGame with the current state of the game
     */
    public static GameDiffRejoinAfterDisconnection diffRejoin(Game game, String nickname, List<String> activePlayers){
        return new GameDiffRejoinAfterDisconnection(
                getInitialization(game, nickname),
                getPlayerActivity(activePlayers),
                getDeckCurrentState(game),
                getCodexCurrentState(game),
                getHandYourCurrentState(game, nickname),
                getHandOtherCurrentState(game, nickname),
                getPublicObjectiveCurrentState(game)
        );
    }

    private static GameDiffInitialization getInitialization(Game game, String nickname){
        return new GameDiffInitialization(
                game.getUsersList().stream().map(User::getNickname).toList(),
                game.getName(),
                nickname,
                new GameDiffCurrentPlayer(game.getCurrentPlayer().getNickname())
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

        deckDiff.add(new DeckDiffDeckDraw(DrawableCard.RESOURCECARD, Lightifier.lightifyToBack(resourceCardDeck.getActualDeck().stream().toList().getFirst())));
        deckDiff.add(new DeckDiffDeckDraw(DrawableCard.GOLDCARD, Lightifier.lightifyToBack(goldCardDeck.getActualDeck().stream().toList().getFirst())));
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
    private static List<CodexDiff> getCodexCurrentState(Game game){
        List<CodexDiff> codexDiff = new ArrayList<>();
        for(String nickname : game.getGameParty().getUsersList().stream().map(User::getNickname).toList()){
            LightCodex codex = Lightifier.lightify(game.getGameParty().getUsersList().stream().filter(user -> user.getNickname().equals(nickname)).findFirst().get().getUserCodex());
            codexDiff.add(new CodexDiff(
                    nickname,
                    codex.getPoints(),
                    codex.getEarnedCollectables(),
                    codex.getPlacementHistory().values().stream().toList(),
                    codex.getFrontier().frontier()
            ));
        }
        return codexDiff;
    }

    /**
     * @param game the subscriber to get the hand from
     * @return a list of diffs containing the current state of the hand of the subscriber
     */
    private static List<HandDiff> getHandYourCurrentState(Game game, String nickname) {
        List<HandDiff> handDiffAdd = new ArrayList<>();
        User user = game.getUserFromNick(nickname);

        LightHand subscriberHand = Lightifier.lightifyYour(user.getUserHand(), user);
        for (LightCard card : subscriberHand.getCards()) {
            if (card != null)
                handDiffAdd.add(new HandDiffAdd(card, subscriberHand.isPlayble(card)));
        }

        if (subscriberHand.getSecretObjective() != null)
            handDiffAdd.add(new HandDiffSetObj(subscriberHand.getSecretObjective()));

        List<ObjectiveCard> objectiveOptions = user.getUserHand().getSecretObjectiveChoices();
        if(objectiveOptions != null){
            handDiffAdd.add(new HandDiffAddOneSecretObjectiveOption(Lightifier.lightifyToCard(objectiveOptions.getFirst())));
            handDiffAdd.add(new HandDiffAddOneSecretObjectiveOption(Lightifier.lightifyToCard(objectiveOptions.getLast())));
        }
        return handDiffAdd;
    }

    /**
     * @param game the subscriber to which sends the hand of the other players
     * @return a list of diffs containing the current state of the hand of the other players
     */
    private static List<HandOtherDiff> getHandOtherCurrentState(Game game, String nickname){
        List<HandOtherDiff> handOtherDiff = new ArrayList<>();
        for(String nick : game.getGameParty().getUsersList().stream().map(User::getNickname).toList()){
            if(!nick.equals(nickname)){
                Hand handOther = game.getUserFromNick(nick).getUserHand();
                LightHandOthers otherHand = Lightifier.lightifyOthers(handOther);
                for(LightBack card : otherHand.getCards()){
                    handOtherDiff.add(new HandOtherDiffAdd(card, nick));
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
