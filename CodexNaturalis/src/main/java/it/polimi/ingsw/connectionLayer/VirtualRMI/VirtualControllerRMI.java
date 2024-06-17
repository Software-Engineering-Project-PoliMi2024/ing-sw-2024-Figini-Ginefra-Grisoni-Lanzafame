package it.polimi.ingsw.connectionLayer.VirtualRMI;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.connectionLayer.ConnectionLayerServer;
import it.polimi.ingsw.connectionLayer.PingPongInterface;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.controller.Interfaces.ControllerInterface;
import it.polimi.ingsw.controller.LogsOnClientStatic;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.FatManLobbyList;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.GadgetGame;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.LittleBoyLobby;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.ChatMessage;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.*;

public class VirtualControllerRMI implements VirtualController {
    private final ThreadPoolExecutor controllerExecutor = new ThreadPoolExecutor(1, 4, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    private ScheduledExecutorService pingPongExecutor = Executors.newSingleThreadScheduledExecutor();
    private ViewInterface view;
    private PingPongInterface pingPongStub;
    private ControllerInterface controllerStub;
    @Override
    public void login(String nickname) {
        Future<Void> loginFuture = controllerExecutor.submit(()->{
            try {
                controllerStub.login(nickname);
                return null;
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });
        try {
            loginFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (Exception e){
            System.out.println("login " + e.getMessage());
            this.disconnect();
        }
    }


    @Override
    public void createLobby(String gameName, int maxPlayerCount) {
        Future<Void> createLobbyFuture = controllerExecutor.submit(()->{
            try {
                controllerStub.createLobby(gameName, maxPlayerCount);
                return null;
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });
        try {
            createLobbyFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (Exception e){
            System.out.println("createLobby " + e.getMessage());
            this.disconnect();
        }
    }

    @Override
    public void joinLobby(String lobbyName) {
        Future<Void> joinLobbyFuture = controllerExecutor.submit(()->{
            try {
                controllerStub.joinLobby(lobbyName);
                return null;
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });
        try {
            joinLobbyFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (Exception e){
            System.out.println("joinLobby " + e.getMessage());
            this.disconnect();
        }
    }

    @Override
    public void leaveLobby() {
        Future<Void> leaveLobbyFuture = controllerExecutor.submit(()->{
            try {
                controllerStub.leaveLobby();
                return null;
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });
        try {
            leaveLobbyFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (Exception e){
            System.out.println("leaveLobby " + e.getMessage());
            this.disconnect();
        }
    }

    @Override
    public void chooseSecretObjective(LightCard objectiveCard) {
        Future<Void> choseSecretObjectiveFuture = controllerExecutor.submit(()->{
            try {
                controllerStub.chooseSecretObjective(objectiveCard);
                return null;
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });
        try {
            choseSecretObjectiveFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (Exception e){
            System.out.println("chooseSecretObj " + e.getMessage());
            this.disconnect();
        }
    }

    @Override
    public void choosePawn(PawnColors color) {
        Future<Void> choosePawnFuture = controllerExecutor.submit(() -> {
            try {
                controllerStub.choosePawn(color);
                return null;
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });
        try {
            choosePawnFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println("choosePawn " + e.getMessage());
            this.disconnect();
        }
    }

    @Override
    public void sendChatMessage(ChatMessage message) {
        Future<Void> sendChatMessageFuture = controllerExecutor.submit(()->{
            try {
                controllerStub.sendChatMessage(message);
                return null;
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });
        try {
            sendChatMessageFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (Exception e){
            System.out.println("sendChatMessage " + e.getMessage());
            this.disconnect();
        }
    }

    @Override
    public void place(LightPlacement placement) {
        Future<Void> placeFuture = controllerExecutor.submit(()->{
            try {
                controllerStub.place(placement);
                return null;
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });
        try {
            //controllerStub.place(placement);
            placeFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (Exception e){
            System.out.println("place " + e.getMessage());
            this.disconnect();
        }
    }

    @Override
    public void draw(DrawableCard deckID, int cardID) {
        Future<Void> drawFuture = controllerExecutor.submit(()->{
            try {
                controllerStub.draw(deckID, cardID);
                return null;
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });
        try {
            drawFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (Exception e){
            System.out.println("draw " + e.getMessage());
            this.disconnect();
        }
    }

    public void pingPong() {
        Future<?> pong = pingPongExecutor.scheduleAtFixedRate(() -> {
            try {
                pingPongStub.checkEmpty();
            } catch (Exception e) {
                pingPongExecutor.shutdownNow();
                System.out.println("pingPong " + e.getMessage());
                this.disconnect();
            }
        }, Configs.pingPongFrequency, Configs.pingPongFrequency, TimeUnit.SECONDS);

    }


    public synchronized void disconnect(){
        pingPongExecutor.shutdownNow();
        pingPongExecutor = Executors.newSingleThreadScheduledExecutor();
        try {
            UnicastRemoteObject.unexportObject(this, true);
            UnicastRemoteObject.unexportObject(view, true);
        }catch (RemoteException r){
        }
        this.erase();
        try {
            view.logErr(LogsOnClientStatic.CONNECTION_LOST_CLIENT_SIDE);
            view.transitionTo(ViewState.SERVER_CONNECTION);
        }catch (Exception r){}
    }
    private void erase(){
        try {
            view.updateLobbyList(new FatManLobbyList());
            view.updateLobby(new LittleBoyLobby());
            view.updateGame(new GadgetGame());
            UnicastRemoteObject.unexportObject(this, true);
        }catch (Exception r){}
    }
    /**
     * Establishes a connection with the RMI server located at the specified IP address and port.
     * This method enables communication between a client and the server through RMI.
     * @param ip The IP address of the RMI server.
     * @param port The port number where the RMI server is listening.
     * @param view the view of the client
     */
    @Override
    public void connect(String ip, int port, ViewInterface view) {
        this.view = view;
        //Future<ConnectionLayerServer> connect = controllerExecutor.submit(() -> {});
        try {
            //ConnectionLayerServer serverStub = connect.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
            ConnectionLayerServer serverStub = null;
            try {
                Registry registry = LocateRegistry.getRegistry(ip, port);
                ConnectionLayerServer serverReference = (ConnectionLayerServer) registry.lookup(Configs.connectionLabelRMI);
                ViewInterface viewStub = (ViewInterface) UnicastRemoteObject.exportObject(view, 0);
                VirtualController controllerStub = (VirtualController) UnicastRemoteObject.exportObject(this, 0);
                serverReference.connect(controllerStub, viewStub, controllerStub);
                serverStub = serverReference;
                //return serverReference;
            } catch (Exception e){
                e.printStackTrace();
                //return null;
            }
            if(serverStub == null)
                throw new NullPointerException();
            this.pingPong();

        }
        catch (Exception e){
            try {
                e.printStackTrace();
                view.logErr(LogsOnClientStatic.CONNECTION_ERROR);
                view.transitionTo(ViewState.SERVER_CONNECTION);
            }catch (Exception ignored){}
        }
    }

    @Override
    public void setControllerStub(ControllerInterface controllerStub) {
        this.controllerStub = controllerStub;
    }

    public void setPingPongStub(PingPongInterface pingPongStub) {
        this.pingPongStub = pingPongStub;
    }

    @Override
    public void checkEmpty()  {

    }
}
