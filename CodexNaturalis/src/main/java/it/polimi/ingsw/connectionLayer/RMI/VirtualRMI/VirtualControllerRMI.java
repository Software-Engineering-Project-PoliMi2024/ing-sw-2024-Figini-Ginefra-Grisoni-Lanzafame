package it.polimi.ingsw.connectionLayer.RMI.VirtualRMI;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.connectionLayer.ConnectionLayerServer;
import it.polimi.ingsw.connectionLayer.HeartBeatInterface;
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

/**
 * This class is the RMI implementation of the VirtualController interface.
 * It is used to communicate with the server through RMI.
 */
public class VirtualControllerRMI implements VirtualController {
    /** The executor that manages the sending of controller commands. It delegates his work to the controllerExecutor*/
    private final ThreadPoolExecutor controllerExecutor = new ThreadPoolExecutor(1, 2, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    /** The executor that actually runs the communication to the server*/
    private final ExecutorService controllerCommandsExecutor = Executors.newSingleThreadExecutor();
    /** The executor that manages the ping pong from the client to the server*/
    private ScheduledExecutorService heartBeatExecutor = Executors.newScheduledThreadPool(2);
    /** The view of the client*/
    private ViewInterface view;
    /** The stub of the heartBeat interface. It contains the method call on the server to check if the connection is still alive*/
    private HeartBeatInterface heartBeatStub;
    /** The stub of the controller interface. It contains all the method that can be call on the sever*/
    private ControllerInterface controllerStub;
    /** The future of the heartBeat. It is used to check if the connection is still alive every pingPongPeriod second*/
    private Future<?> heartBeatFuture;

    /**
     * Constructs a VirtualControllerRMI object. It adds a shutdown hook to the JVM that unexports the view and the VirtualController.
     * It also shuts down the heartBeatExecutor when the JVM is shutting down.
     */
    public VirtualControllerRMI() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                UnicastRemoteObject.unexportObject(this, true);
                UnicastRemoteObject.unexportObject(view, true);
            } catch (Exception ignored) {}
            controllerExecutor.shutdownNow();
            controllerCommandsExecutor.shutdownNow();
            heartBeatExecutor.shutdownNow();
        }));

    }

    /**
     * Call the login method of the controller on the server.
     * If the server does not respond in Configs.secondsTimeOut seconds, it logs an error and disconnects the client.
      * @param nickname the nickname chosen by the player
     */
    @Override
    public void login(String nickname) {
        controllerExecutor.execute(()->{
            Future<?> loginFuture = controllerCommandsExecutor.submit(()-> {
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
                Configs.printStackTrace(e);
                this.disconnect();
            }
        });
    }

    /**
     * Call the createLobby method of the controller on the server.
     * If the server does not respond in Configs.secondsTimeOut seconds, it logs an error and disconnects the client.
     * @param gameName the name of the lobby
     * @param maxPlayerCount the maximum number of players that can join the lobby
     */
    @Override
    public void createLobby(String gameName, int maxPlayerCount) {
        controllerExecutor.execute(()->{
            Future<?> createLobbyFuture = controllerCommandsExecutor.submit(()->{
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
                Configs.printStackTrace(e);
                this.disconnect();
            }
        });

    }

    /**
     * Call the joinLobby method of the controller on the server.
     * If the server does not respond in Configs.secondsTimeOut seconds, it logs an error and disconnects the client.
     * @param lobbyName the name of the lobby that the player wants to join
     */
    @Override
    public void joinLobby(String lobbyName) {
        controllerExecutor.execute(()->{
            Future<?> joinLobbyFuture = controllerCommandsExecutor.submit(()->{
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
                Configs.printStackTrace(e);
                this.disconnect();
            }
        });
    }

    /**
     * Call the leaveLobby method of the controller on the server.
     * If the server does not respond in Configs.secondsTimeOut seconds, it logs an error and disconnects the client.
     */
    @Override
    public void leaveLobby() {
        controllerExecutor.execute(()-> {
            Future<?> leaveLobbyFuture = controllerCommandsExecutor.submit(() -> {
                try {
                    controllerStub.leaveLobby();
                } catch (Exception e) {
                    throw new RuntimeException("VirtualControllerRMI.leaveLobby: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
                }
            });
            try {
                leaveLobbyFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                Configs.printStackTrace(e);
                this.disconnect();
            }
        });
    }

    /**
     * Call the chooseSecretObjective method of the controller on the server.
     * If the server does not respond in Configs.secondsTimeOut seconds, it logs an error and disconnects the client.
     * @param objectiveCard the secret objective card chosen by the player
     */
    @Override
    public void chooseSecretObjective(LightCard objectiveCard) {
        controllerExecutor.execute(()-> {
            Future<?> choseSecretObjectiveFuture = controllerCommandsExecutor.submit(() -> {
                try {
                    controllerStub.chooseSecretObjective(objectiveCard);
                } catch (Exception e) {
                    throw new RuntimeException("VirtualControllerRMI.chooseSecretObjective: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
                }
            });
            try {
                choseSecretObjectiveFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                Configs.printStackTrace(e);
                this.disconnect();
            }
        });
    }

    /**
     * Call the choosePawn method of the controller on the server.
     * If the server does not respond in Configs.secondsTimeOut seconds, it logs an error and disconnects the client.
     * @param color the pawn color chosen by the player
     */
    @Override
    public void choosePawn(PawnColors color) {
        controllerExecutor.execute(()-> {
            Future<?> choosePawnFuture = controllerCommandsExecutor.submit(() -> {
                try {
                    controllerStub.choosePawn(color);
                } catch (Exception e) {
                    throw new RuntimeException("VirtualControllerRMI.choosePawn: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
                }
            });
            try {
                choosePawnFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                Configs.printStackTrace(e);
                this.disconnect();
            }
        });
    }

    /**
     * Call the sendChatMessage method of the controller on the server.
     * If the server does not respond in Configs.secondsTimeOut seconds, it logs an error and disconnects the client.
     * @param message the message sent by the player
     */
    @Override
    public void sendChatMessage(ChatMessage message) {
        controllerExecutor.execute(()-> {
            Future<?> sendChatMessageFuture = controllerCommandsExecutor.submit(() -> {
                try {
                    controllerStub.sendChatMessage(message);
                } catch (Exception e) {
                    throw new RuntimeException("VirtualControllerRMI.sendChatMessage: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
                }
            });
            try {
                sendChatMessageFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                Configs.printStackTrace(e);
                this.disconnect();
            }
        });
    }

    /**
     * Call the place method of the controller on the server.
     * If the server does not respond in Configs.secondsTimeOut seconds, it logs an error and disconnects the client.
     * @param placement the lightPlacement decided by player
     */
    @Override
    public void place(LightPlacement placement) {
        controllerExecutor.execute(()-> {
            Future<?> placeFuture = controllerCommandsExecutor.submit(() -> {
                try {
                    controllerStub.place(placement);
                } catch (Exception e) {
                    throw new RuntimeException("VirtualControllerRMI.place: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
                }
            });
            try {
                placeFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                Configs.printStackTrace(e);
                this.disconnect();
            }
        });
    }

    /**
     * Call the checkEmpty method on the server to check if the connection is still alive.
     * If the server does not respond in Configs.secondsTimeOut seconds, it logs an error and disconnects the client.
     */
    public void heartBeat() {
        heartBeatFuture = heartBeatExecutor.scheduleAtFixedRate(() -> {
            try {
                Future<?> ping = heartBeatExecutor.submit(() -> {
                    try {
                        heartBeatStub.checkEmpty();
                    } catch (Exception e) {
                        throw new RuntimeException("VirtualViewRMI.pinPong: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
                    }
                });
                ping.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
            }catch (InterruptedException ignored){
            } catch (Exception e) {
                Configs.printStackTrace(e);
                this.disconnect();
                notifyLostServerConnection();
            }
        }, Configs.heartBeatPeriod, Configs.heartBeatPeriod, TimeUnit.SECONDS);
    }

    /**
     * Call the draw method of the controller on the server.
     * If the server does not respond in Configs.secondsTimeOut seconds, it logs an error and disconnects the client.
     * @param deckID the deck from which the card is drawn (either Resource or Gold)
     * @param cardID the position of the card to draw (0,1 for the buffer, 2 for the deck)
     */
    @Override
    public void draw(DrawableCard deckID, int cardID) {
        controllerExecutor.execute(()-> {
            Future<?> drawFuture = controllerCommandsExecutor.submit(() -> {
                try {
                    controllerStub.draw(deckID, cardID);
                } catch (Exception e) {
                    throw new RuntimeException("VirtualControllerRMI.draw: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
                }
            });
            try {
                drawFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                Configs.printStackTrace(e);
                this.disconnect();
            }
        });
    }

    /**
     * Call the leave method of the controller on the server.
     * If the server does not respond in Configs.secondsTimeOut seconds, it logs an error
     * It will always call the disconnect method to close the connection and erase the light model
     * @throws Exception if the server does not respond in Configs.secondsTimeOut seconds
     */
    @Override
    public synchronized void leave() throws Exception {
        controllerExecutor.execute(()-> {
            Future<?> leaveFuture = controllerCommandsExecutor.submit(() -> {
                try {
                    controllerStub.leave();
                } catch (Exception e) {
                    throw new RuntimeException("VirtualControllerRMI.leave: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
                }
            });
            try {
                leaveFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                Configs.printStackTrace(e);
            }finally {
                this.disconnect();
            }
        });
    }

    /**
     * Disconnect the client from the server. It closes the connection and erases the light model.
     * It also transitions to the server connection view.
     */
    @Override
    public synchronized void disconnect(){
        this.eraseLightModel();
        try {
            view.transitionTo(ViewState.SERVER_CONNECTION);
        }catch (Exception ignored){}
        this.closeConnection();
    }

    /**
     * Close the connection with the server. It stops the heartBeatExecutor and unexports the virtualController and the view.
     */
    private synchronized void closeConnection(){
        heartBeatFuture.cancel(true);
        heartBeatExecutor.shutdown();
        heartBeatExecutor = Executors.newSingleThreadScheduledExecutor();
        try {
            UnicastRemoteObject.unexportObject(this, true);
            UnicastRemoteObject.unexportObject(view, true);
        }catch (RemoteException ignored){}
    }

    /**
     * Notify the client that the connection with the server has been lost.
     */
    public synchronized void notifyLostServerConnection(){
        try {
            view.logErr(LogsOnClient.CONNECTION_LOST_CLIENT_SIDE);
        }catch (Exception ignored){}
    }

    /**
     * Erase the light model though the NuclearDiff.
     */
    private void eraseLightModel(){
        try {
            view.updateLobbyList(new FatManLobbyList());
            view.updateLobby(new LittleBoyLobby());
            view.updateGame(new GadgetGame());
        }catch (Exception ignored){}
    }

    /**
     /**
     * Constructs a VirtualControllerRMI object. This constructor attempts to set the IP address of the client
     * to the one assigned to the server.
     * If there is no internet connection, it logs an error and transitions
     * to the server connection view. If the connection is successful, it calls System.setProperty to set the
     * IP address of the client to the one assigned by the server.
     * @param ip The IP address of the RMI server.
     * @param port The port number where the RMI server is listening.
     * @param view the view of the client
     */
    @Override
    public void connect(String ip, int port, ViewInterface view) {
        this.view = view;
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("google.com", 80));
            String localIp = socket.getLocalAddress().getHostAddress();
            System.setProperty("java.rmi.server.hostname", localIp);
        } catch (IOException e) {
            try {
                view.logErr(LogsOnClient.UNABLE_TO_GET_IP);
                view.transitionTo(ViewState.SERVER_CONNECTION);
            }catch (Exception ignored){}
        }
        try {
            UnicastRemoteObject.unexportObject(this, true);
            UnicastRemoteObject.unexportObject(view, true);
        }catch (Exception ignored){}
        Future<?> connect = controllerCommandsExecutor.submit(() -> {
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
                Configs.printStackTrace(e);
                view.logErr(LogsOnClient.CONNECTION_ERROR);
                view.transitionTo(ViewState.SERVER_CONNECTION);
            }catch (Exception ignored){}
        }
    }

    /**
     * Set the controllerStub of the client to the controllerStub passed as parameter.
     * @param controllerStub the controllerStub of the client
     */
    @Override
    public void setControllerStub(ControllerInterface controllerStub) {
        this.controllerStub = controllerStub;
    }

    /**
     * Set the heartBeatStub of the client to the heartBeatStub passed as parameter.
     * @param heartBeatStub the heartBeatStub of the client
     */
    public void setHeartBeatStub(HeartBeatInterface heartBeatStub) {
        this.heartBeatStub = heartBeatStub;
    }

    /**
     * Empty method call by the server to check if the connection is still alive.
     */
    @Override
    public void checkEmpty()  {

    }
}
