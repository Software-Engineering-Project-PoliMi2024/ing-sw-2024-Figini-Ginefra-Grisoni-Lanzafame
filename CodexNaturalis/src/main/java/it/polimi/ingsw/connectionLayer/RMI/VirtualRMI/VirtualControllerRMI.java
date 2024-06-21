package it.polimi.ingsw.connectionLayer.RMI.VirtualRMI;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.connectionLayer.ConnectionLayerServer;
import it.polimi.ingsw.connectionLayer.PingPongInterface;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.controller.Interfaces.ControllerInterface;
import it.polimi.ingsw.controller.LogsOnClient;
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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
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
    private Future<?> pong;

    public VirtualControllerRMI() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("google.com", 80));
            String ip = socket.getLocalAddress().getHostAddress();
            System.setProperty("java.rmi.server.hostname", ip);
        } catch (IOException e) {
            try {
                view.logErr(LogsOnClient.UNABLE_TO_GET_IP);
                view.transitionTo(ViewState.SERVER_CONNECTION);
            }catch (Exception ignored){}
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                UnicastRemoteObject.unexportObject(this, true);
                UnicastRemoteObject.unexportObject(view, true);
            } catch (Exception ignored) {}
        }));

    }

    @Override
    public void login(String nickname) {
        Future<?> loginFuture = controllerExecutor.submit(()->{
            try {
                controllerStub.login(nickname);
            } catch (Exception e) {
                throw new RuntimeException("VirtualControllerRMI.login: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }
        });
        try {
            loginFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        }catch (Exception e){
            e.printStackTrace();
            this.disconnect();
        }
    }


    @Override
    public void createLobby(String gameName, int maxPlayerCount) {
        Future<?> createLobbyFuture = controllerExecutor.submit(()->{
            try {
                controllerStub.createLobby(gameName, maxPlayerCount);
            } catch (Exception e) {
                throw new RuntimeException("VirtualControllerRMI.createLobby: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }
        });
        try {
            createLobbyFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        }catch (Exception e){
            e.printStackTrace();
            this.disconnect();
        }
    }

    @Override
    public void joinLobby(String lobbyName) {
        Future<?> joinLobbyFuture = controllerExecutor.submit(()->{
            try {
                controllerStub.joinLobby(lobbyName);
            } catch (Exception e) {
                throw new RuntimeException("VirtualControllerRMI.joinLobby: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }
        });
        try {
            joinLobbyFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        }catch (Exception e){
            e.printStackTrace();
            this.disconnect();
        }
    }

    @Override
    public void leaveLobby() {
        Future<?> leaveLobbyFuture = controllerExecutor.submit(()->{
            try {
                controllerStub.leaveLobby();
            } catch (Exception e) {
                throw new RuntimeException("VirtualControllerRMI.leaveLobby: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }
        });
        try {
            leaveLobbyFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        }catch (Exception e){
            e.printStackTrace();
            this.disconnect();
        }
    }

    @Override
    public void chooseSecretObjective(LightCard objectiveCard) {
        Future<?> choseSecretObjectiveFuture = controllerExecutor.submit(()->{
            try {
                controllerStub.chooseSecretObjective(objectiveCard);
            } catch (Exception e) {
                throw new RuntimeException("VirtualControllerRMI.chooseSecretObjective: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }
        });
        try {
            choseSecretObjectiveFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        }catch (Exception e){
            e.printStackTrace();
            this.disconnect();
        }
    }

    @Override
    public void choosePawn(PawnColors color) {
        Future<?> choosePawnFuture = controllerExecutor.submit(() -> {
            try {
                controllerStub.choosePawn(color);
            } catch (Exception e) {
                throw new RuntimeException("VirtualControllerRMI.choosePawn: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }
        });
        try {
            choosePawnFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        } catch (Exception e) {
            e.printStackTrace();
            this.disconnect();
        }
    }

    @Override
    public void sendChatMessage(ChatMessage message) {
        Future<?> sendChatMessageFuture = controllerExecutor.submit(()->{
            try {
                controllerStub.sendChatMessage(message);
            } catch (Exception e) {
                throw new RuntimeException("VirtualControllerRMI.sendChatMessage: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }
        });
        try {
            sendChatMessageFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        }catch (Exception e){
            e.printStackTrace();
            this.disconnect();
        }
    }

    @Override
    public void place(LightPlacement placement) {
        Future<?> placeFuture = controllerExecutor.submit(()->{
            try {
                controllerStub.place(placement);
            } catch (Exception e) {
                throw new RuntimeException("VirtualControllerRMI.place: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }
        });
        try {
            placeFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        }catch (Exception e){
            e.printStackTrace();
            this.disconnect();
        }
    }

    @Override
    public void draw(DrawableCard deckID, int cardID) {
        Future<?> drawFuture = controllerExecutor.submit(()->{
            try {
                controllerStub.draw(deckID, cardID);
            } catch (Exception e) {
                throw new RuntimeException("VirtualControllerRMI.draw: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }
        });
        try {
            drawFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        }catch (Exception e){
            e.printStackTrace();
            this.disconnect();
        }
    }

    public void pingPong() {
        this.pong = pingPongExecutor.scheduleAtFixedRate(() -> {
            try {
                pingPongStub.checkEmpty();
            } catch (Exception e) {
                this.disconnect();
            }
        }, Configs.pingPongFrequency, Configs.pingPongFrequency, TimeUnit.SECONDS);
    }


    public synchronized void disconnect(){
        pong.cancel(true);
        pingPongExecutor.shutdownNow();
        pingPongExecutor = Executors.newSingleThreadScheduledExecutor();
        try {
            UnicastRemoteObject.unexportObject(this, true);
            UnicastRemoteObject.unexportObject(view, true);

        }catch (RemoteException ignored){}
        this.eraseLightModel();
        try {
            view.logErr(LogsOnClient.CONNECTION_LOST_CLIENT_SIDE);
            view.transitionTo(ViewState.SERVER_CONNECTION);
        }catch (Exception ignored){}
    }

    private void eraseLightModel(){
        try {
            view.updateLobbyList(new FatManLobbyList());
            view.updateLobby(new LittleBoyLobby());
            view.updateGame(new GadgetGame());
        }catch (Exception ignored){}
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
        try {
            UnicastRemoteObject.unexportObject(this, true);
            UnicastRemoteObject.unexportObject(view, true);
        }catch (Exception ignored){}
        Future<?> connect = controllerExecutor.submit(() -> {
            try {
                Registry registry = LocateRegistry.getRegistry(ip, port);
                ConnectionLayerServer serverReference = (ConnectionLayerServer) registry.lookup(Configs.connectionLabelRMI);
                ViewInterface viewStub = (ViewInterface) UnicastRemoteObject.exportObject(view, 0);
                VirtualController controllerStub = (VirtualController) UnicastRemoteObject.exportObject(this, 0);
                serverReference.connect(controllerStub, viewStub, controllerStub);
            }catch (Exception e){
                throw new RuntimeException("VirtualControllerRMI.connect: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }
        });

        try {
            connect.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored) {
        }catch (Exception e){
            try {
                e.printStackTrace();
                view.logErr(LogsOnClient.CONNECTION_ERROR);
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
