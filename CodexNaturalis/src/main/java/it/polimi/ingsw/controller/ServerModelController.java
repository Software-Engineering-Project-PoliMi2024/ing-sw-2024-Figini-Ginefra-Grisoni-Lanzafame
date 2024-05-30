package it.polimi.ingsw.controller;


import it.polimi.ingsw.controller3.Controller3;
import it.polimi.ingsw.controller4.Interfaces.ControllerInterface;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.diffs.*;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.lightModel.diffPublishers.DiffSubscriber;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.util.List;

public class ServerModelController implements ControllerInterface, DiffSubscriber, ViewInterface {
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
    public void chooseSecretObjective(LightCard card){
        controller3.chooseSecretObjective(card);
    }

    /**
     * Update the deck, Hand, Codex for the player and others
     * @param placement the LightPlacement created by placing the card
     */
    @Override
    public void place(LightPlacement placement) {
        controller3.place(placement);
    }

    /**
     * Draw a card from the deckID-deck in the cardID-position
     * Transition to the newPlayer and set the current one to Idle
     * @param deckID the deck from which the card is drawn
     * @param cardID the position from where draw the card (buffer/deck)
     */
    @Override
    public void draw(DrawableCard deckID, int cardID) {
        controller3.draw(deckID, cardID);
            //user.getUserHand().addCard(drawCard);
            /*userGame.subscribe(this, new HandDiffAdd(Lightifier.lightifyToCard(drawCard), drawCard.canBePlaced(user.getUserCodex())),
                    new HandOtherDiffAdd(new LightBack(drawCard.getIdBack()), this.nickname));
            userGame.getGameLoopController().cardDrawn(this);*/

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

    @Override
    public void updateLobby(ModelDiffs<LightLobby> diff) {

    }

    public String getNickname(){
        return this.nickname;
    }

    @Override
    public void gameStarted() {

    }

    @Override
    public void updateGame(ModelDiffs<LightGame> diff) {

    }

    @Override
    public void setFinalRanking(List<String> ranking) throws Exception {

    }

    @Override
    public void updateLobbyList(ModelDiffs<LightLobbyList> diff) {

    }

    @Override
    public void transitionTo(ViewState state) {

    }

    @Override
    public void log(String logMsg) {

    }

    @Override
    public void logErr(String logMsg) {

    }

    @Override
    public void logOthers(String logMsg) {

    }

    @Override
    public void logGame(String logMsg) {

    }

    public void logGame(LogsOnClient msg){}
    public void logYou(LogsOnClient msg){}

    public void logOther(String log){

    }
}
