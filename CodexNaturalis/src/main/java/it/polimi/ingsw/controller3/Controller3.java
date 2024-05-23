package it.polimi.ingsw.controller3;

import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.controller3.mediatorSubscriber.FullMediatorSubscriber;
import it.polimi.ingsw.controller3.mediators.LobbyListMediator;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

public class Controller3 implements ControllerInterface, FullMediatorSubscriber{
    private final ViewInterface view;
    private final ModelInteract modelInteract;
    private final MalevolentChecker malevolentChecker;

    public Controller3(MultiGame games, ViewInterface view) {
        this.view = view;
        this.modelInteract = new ModelInteract(games);
        this.malevolentChecker = new MalevolentChecker(games);
    }
    @Override
    public void login(String nickname) {
        LobbyListMediator lobbyListMediator = new LobbyListMediator();
    }

    @Override
    public void createLobby(String gameName, int maxPlayerCount) {

    }

    @Override
    public void joinLobby(String lobbyName) {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void leaveLobby() {

    }

    @Override
    public void choseSecretObjective(LightCard objectiveCard) {

    }

    @Override
    public void place(LightPlacement placement) {

    }

    @Override
    public void draw(DrawableCard deckID, int cardID) {

    }

    @Override
    public void updateGame(ModelDiffs<LightGame> diff) {
        try{
            view.updateGame(diff);
        }catch (Exception e) {
            logErr("Error in updateGame: " + e.getMessage());
        }
    }

    @Override
    public void setFinalRanking(String[] nicks, int[] points) {
        try {
            view.setFinalRanking(nicks, points);
        } catch (Exception e) {
            logErr("Error in setFinalRanking: " + e.getMessage());
        }
    }

    @Override
    public void updateLobbyList(ModelDiffs<LightLobbyList> diff) {
        try {
            view.updateLobbyList(diff);
        } catch (Exception e) {
            logErr("Error in updateLobbyList: " + e.getMessage());
        }
    }

    @Override
    public void log(String logMsg) {
        try {
            view.log(logMsg);
        } catch (Exception e) {
            logErr("Error in log: " + e.getMessage());
        }
    }

    @Override
    public void logErr(String logMsg) {
        try {
            view.logErr(logMsg);
        } catch (Exception e) {
            logErr("Error in logErr: " + e.getMessage());
        }
    }

    @Override
    public void logOthers(String logMsg) {
        try {
            view.logOthers(logMsg);
        } catch (Exception e) {
            logErr("Error in logOthers: " + e.getMessage());
        }
    }

    @Override
    public void logGame(String logMsg) {
        try {
            view.logGame(logMsg);
        } catch (Exception e) {
            logErr("Error in logGame: " + e.getMessage());
        }
    }

    public void transitionTo(ViewState state) {
        try {
            view.transitionTo(state);
        } catch (Exception e) {
            logErr("Error in transitionTo: " + e.getMessage());
        }
    }
}
