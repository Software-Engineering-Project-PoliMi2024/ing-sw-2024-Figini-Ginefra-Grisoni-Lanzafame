package it.polimi.ingsw.controller;


import it.polimi.ingsw.controller3.Controller3;
import it.polimi.ingsw.lightModel.Heavifier;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffs.*;
import it.polimi.ingsw.lightModel.diffs.game.*;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.lightModel.diffPublishers.DiffSubscriber;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.*;
import it.polimi.ingsw.model.tableReleted.Deck;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.util.ArrayList;
import java.util.List;

public class ServerModelController implements ControllerInterface, DiffSubscriber {
    private final MultiGame games;
    private final ViewInterface view;
    private final Controller3 controller3;
    private final Object freezeDisconnect = new Object();
    private String nickname;
    /**
     * The constructor of the class
     * @param games the reference to the Multi-game on the server
     * @param view The client's view interface associated with this controller.
     */
    public ServerModelController(MultiGame games, ViewInterface view) {
        this.games = games;
        this.view = view;
        controller3 = new Controller3(games, view);
    }

    @Override
    public void login(String nickname) {
        controller3.login(nickname);
    }

    @Override
    public void createLobby(String gameName, int maxPlayerCount) {
        controller3.createLobby(gameName, maxPlayerCount);
    }

    @Override
    public void joinLobby(String lobbyName){
        controller3.joinLobby(lobbyName);
    }


    @Override
    public void leaveLobby(){
        controller3.leaveLobby();
    }

    /**
     * Set the secretObjectiveCard chose by the user
     * @param card which represent the secret objective choose by the user
     */
    @Override
    public void choseSecretObjective(LightCard card){
        User userToEdit = games.getUserFromNick(this.nickname);
        Game userGame = games.getGameFromUserNick(nickname);
        userToEdit.setSecretObject(Heavifier.heavifyObjectCard(card, games));
        userToEdit.getUserHand().setSecretObjectiveChoice(null);
        this.updateGame(new HandDiffSetObj(card));
        userGame.getGameLoopController().secretObjectiveChose(this);
    }

    /**
     * Update the deck, Hand, Codex for the player and others
     * @param placement the LightPlacement created by placing the card
     */
    @Override
    public void place(LightPlacement placement) {
        User user = games.getUserFromNick(nickname);
        //if the card place is the startCard
        if(user.getUserCodex().getFrontier().isInFrontier(new Position(0,0))){
            Placement heavyPlacement = Heavifier.heavifyStartCardPlacement(placement, this.games);
            user.placeStartCard(heavyPlacement);

            Game userGame = games.getGameFromUserNick(this.nickname);
            this.updateGame(new HandDiffRemove(placement.card())); //remove the startCard from the Hand
            userGame.subscribe(new CodexDiff(this.nickname, user.getUserCodex().getPoints(),
                    user.getUserCodex().getEarnedCollectables(), getPlacementList(placement), user.getUserCodex().getFrontier().getFrontier()));
            userGame.getGameLoopController().startCardPlaced(this);
        }else {
            Placement heavyPlacement = Heavifier.heavify(placement, this.games);
            user.playCard(heavyPlacement); //place the card and remove it from the hand

            Game userGame = this.games.getGameFromUserNick(this.nickname);
            userGame.subscribe(this, new HandDiffRemove(placement.card()), new HandOtherDiffRemove(
                    new LightBack(heavyPlacement.card().getIdBack()), this.nickname));
            userGame.subscribe(new CodexDiff(this.nickname, user.getUserCodex().getPoints(),
                    user.getUserCodex().getEarnedCollectables(), getPlacementList(placement), user.getUserCodex().getFrontier().getFrontier()));
            for (ServerModelController allControllers : userGame.getGameLoopController().getActivePlayers().values()) {
                if (!allControllers.equals(this)) {
                    this.logOther(this.nickname, LogsOnClient.PLAYER_PLACED);
                } else {
                    this.logYou(LogsOnClient.YOU_PLACED);
                }
            }
            transitionTo(ViewState.DRAW_CARD);
        }
    }

    /**
     * @param placement the new Placement that is being added
     * @return a mockList containing only the newPlacement
     */
    private List<LightPlacement> getPlacementList(LightPlacement placement){
        List<LightPlacement> list = new ArrayList<>();
        list.add(placement);
        return list;
    }

    /**
     * Draw a card from the deckID-deck in the cardID-position
     * Transition to the newPlayer and set the current one to Idle
     * @param deckID the deck from which the card is drawn
     * @param cardID the position from where draw the card (buffer/deck)
     */
    @Override
    public void draw(DrawableCard deckID, int cardID) {
        CardInHand drawCard;
        if(cardID<0 || cardID>2){
            throw  new IllegalArgumentException(cardID + " is out of bound");
        }else{
            Game userGame = games.getGameFromUserNick(this.nickname);
            if(deckID == DrawableCard.GOLDCARD){
                Deck<GoldCard> goldDeck = userGame.getGoldCardDeck();
                //drawCard = drawACard(goldDeck, DrawableCard.GOLDCARD, cardID, userGame);
            }else {
                Deck<ResourceCard> resourceDeck = userGame.getResourceCardDeck();
                //drawCard = drawACard(resourceDeck, DrawableCard.RESOURCECARD, cardID, userGame);
            }
            logYou(LogsOnClient.CARD_DRAWN);
            User user = games.getUserFromNick(this.nickname);
            //user.getUserHand().addCard(drawCard);
            /*userGame.subscribe(this, new HandDiffAdd(Lightifier.lightifyToCard(drawCard), drawCard.canBePlaced(user.getUserCodex())),
                    new HandOtherDiffAdd(new LightBack(drawCard.getIdBack()), this.nickname));
            userGame.getGameLoopController().cardDrawn(this);*/
        }
    }

    /**
     * @param deck from which drawn a Card
     * @param cardID the position from where draw the card (buffer/deck)
     * @return the card drawn
     * @param <T> a CardInHand (GoldCard/ResourceCard)
     */
    private <T extends CardInHand> T drawACard(Deck<T> deck, int cardID) {
        T drawCard;
        if (cardID == 2) {
            drawCard = deck.drawFromDeck();
        } else {
            drawCard = deck.drawFromBuffer(cardID);
        }
        return drawCard;
    }

    /**
     * Check where the Player his and remove the reference to his controller.
     * If the player his in a game, unsubscribe his controller and delegate to the GameLoopController what to remove next
     */
    @Override
    public void disconnect(){
        controller3.disconnect();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ServerModelController) {
            ServerModelController other = (ServerModelController) obj;
            return this.nickname.equals(other.nickname);
        } else {
            return false;
        }
    }



    public ViewInterface getView() {
        return view;
    }
    public void log(LogsOnClient log){
        try {
            view.log(log.getMessage());
        }catch (Exception r){
            r.printStackTrace();
        }
    }
    public void transitionTo(ViewState state){
        System.out.println(nickname + ":" + state);
        try {
            view.transitionTo(state);
        }catch (Exception r){
            r.printStackTrace();
        }
    }
    public void updateLobbyYou(ModelDiffs<LightLobby> diff){
        try {
            view.updateLobby(diff);
        }catch (Exception r){
            r.printStackTrace();
        }
    }
    public void updateLobby(ModelDiffs<LightLobby> diff){
        try {
            view.logOthers(this.nickname + LogsOnClient.PLAYER_JOIN_LOBBY.getMessage());
            view.updateLobby(diff);
        }catch (Exception r){
            r.printStackTrace();
        }
    }
    public void updateLobbyList(ModelDiffs<LightLobbyList> diff){
        System.out.println(nickname + " updated lobbyList");
        try {
            view.updateLobbyList(diff);
        }catch (Exception r){
            r.printStackTrace();
        }
    }
    public void updateGame(ModelDiffs<LightGame> diff){
        try {
            view.updateGame(diff);
        }catch (Exception r) {
            r.printStackTrace();
        }
    }

    public void logYou(LogsOnClient log){
        try {
            view.log(log.getMessage());
        }catch (Exception r){
            r.printStackTrace();
        }
    }
    public void logOther(String prefix, LogsOnClient log){
        try{
            view.logOthers(prefix + log.getMessage());
        }catch (Exception r){
            r.printStackTrace();
        }
    }
    public void logErr(LogsOnClient log){
        try {
            view.logErr(log.getMessage());
        }catch (Exception r) {
            r.printStackTrace();
        }
    }

    public void logGame(LogsOnClient log){
        try {
            view.logGame(log.getMessage());
        }catch (Exception r){
            r.printStackTrace();
        }
    }
    /**
     * @return user nick
     */
    public String getNickname() {
        return nickname;
    }

    @Override
    public void gameStarted() {

    }
}
