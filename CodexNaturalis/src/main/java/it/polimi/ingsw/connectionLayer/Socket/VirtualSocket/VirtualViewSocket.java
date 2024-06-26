package it.polimi.ingsw.connectionLayer.Socket.VirtualSocket;

import it.polimi.ingsw.connectionLayer.PingPongInterface;
import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;
import it.polimi.ingsw.connectionLayer.Socket.ServerMsg.*;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualView;
import it.polimi.ingsw.controller.Interfaces.ControllerInterface;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.ViewState;

import java.rmi.RemoteException;
import java.util.List;

/**
 * This class runs on the server, and it represents the view on the server.
 * Each action that the view can do is translated into a message sent to the client.
 */
public class VirtualViewSocket implements VirtualView {
    /**The controller that is running on the server*/
    private ControllerInterface controller;
    /**The object that runs the effective communication to the Client*/
    private final ClientHandler clientHandler;

    /**
     * The constructor of the class
     */
    public VirtualViewSocket(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }
    @Override
    public void checkEmpty() throws RemoteException {

    }

    @Override
    public void setPingPongStub(PingPongInterface pingPongStub) throws RemoteException {

    }

    /**
     * Sets the controller
     */
    @Override
    public void setController(ControllerInterface controller) throws RemoteException {
        this.controller = controller;
    }

    @Override
    public void pingPong() throws RemoteException {

    }

    /**
     * Sends a transitionTo message to the client
     * @param state the viewState to transition to
     */
    @Override
    public void transitionTo(ViewState state) throws RemoteException {
        clientHandler.sendServerMessage(new TransitionToMsg(state));
    }

    /**
     * Sends a log message to the client
     * @param logMsg the string to display
     */
    @Override
    public void log(String logMsg) throws RemoteException {
        clientHandler.sendServerMessage(new LogMsg(logMsg));
    }

    /**
     * Send a logErr message to the client
     * @param logMsg the string to display
     */
    @Override
    public void logErr(String logMsg) throws RemoteException {
        clientHandler.sendServerMessage(new LogErrMsg(logMsg));
    }

    /**
     * Send a logOthers message to the client
     * @param logMsg the string to display
     */
    @Override
    public void logOthers(String logMsg) throws RemoteException {
        clientHandler.sendServerMessage(new LogOthersMsg(logMsg));
    }

    /**
     * Send a logGame message to the client
     * @param logMsg the string to display
     */
    @Override
    public void logGame(String logMsg) throws RemoteException {
        clientHandler.sendServerMessage(new LogGameMsg(logMsg));
    }

    /**
     * Send a logChat message to the client
     * @param logMsg the string to display
     */
    @Override
    public void logChat(String logMsg) throws Exception {
        clientHandler.sendServerMessage(new LogChatMsg(logMsg));
    }

    /**
     * Send a LightLobbyList diff to the client
     * @param diff the diff to send
     */
    @Override
    public void updateLobbyList(ModelDiffs<LightLobbyList> diff) throws RemoteException {
        clientHandler.sendServerMessage(new UpdateLobbyListMsg(diff));
    }

    /**
     * Send a LightLobby diff to the client
     * @param diff the diff to send
     */
    @Override
    public void updateLobby(ModelDiffs<LightLobby> diff) throws RemoteException {
        clientHandler.sendServerMessage(new UpdateLobbyMsg(diff));
    }

    /**
     * Send a LightGame diff to the client
     * @param diff the diff to send
     */
    @Override
    public void updateGame(ModelDiffs<LightGame> diff) throws RemoteException {
        clientHandler.sendServerMessage(new UpdateGameMsg(diff));
    }

    /**
     * Send a setFinalRanking List to the client
     * @param ranking list of the final ranking
     */
    @Override
    public void setFinalRanking(List<String> ranking) throws RemoteException {
        clientHandler.sendServerMessage(new SetFinalRankingMsg(ranking));
    }

    public ControllerInterface getController() {
        return controller;
    }
}
