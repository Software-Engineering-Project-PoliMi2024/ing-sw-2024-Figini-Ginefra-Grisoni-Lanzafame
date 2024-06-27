package it.polimi.ingsw.connectionLayer.RMI.VirtualRMI;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.connectionLayer.HeartBeatInterface;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualView;
import it.polimi.ingsw.controller.Interfaces.ControllerInterface;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.*;

/**
 * This class is the RMI implementation of the VirtualView interface.
 * It is used to send updates to the client.
 */
public class VirtualViewRMI implements VirtualView {
    /** The stub of the view on the client */
    private final ViewInterface viewStub;
    /** The stub of the heartBeat interface. It contains the method call on the client to check if the connection is still alive*/
    private HeartBeatInterface heartBeatStub;
    /** The controller of the game */
    private ControllerInterface controller;
    /** The executor that manages the sending of client commands. It delegates his work to the virtualViewExecutor*/
    private final ThreadPoolExecutor virtualViewExecutor = new ThreadPoolExecutor(1, 1, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    /** The executor that actually manage the sending of client commands */
    private final ExecutorService viewUpdateExecutor = Executors.newSingleThreadExecutor();
    /** The executor that manages the heartBeat mechanism */
    private final ScheduledExecutorService heartBeatExecutor = Executors.newScheduledThreadPool(2);
    /** A flag that indicates if the client has already disconnected for avoid reboot*/
    private boolean alreadyDisconnected = false;
    /** The future of the heartBeat. It is used to check if the connection is still alive every pingPongPeriod second*/
    private Future<?> heartBeatFuture;

    /**
     * Constructor of the class
     * @param viewStub the stub of the view on the client
     */
    public VirtualViewRMI(ViewInterface viewStub) {
        this.viewStub = viewStub;
    }

    /**
     * Empty method call by the server to check if the connection is still alive.
     */
    @Override
    public void checkEmpty()  {

    }

    /**
     * Set the stub of the heartBeat interface
     * @param heartBeatStub the stub of the heartBeat interface
     */
    @Override
    public void setHeartBeatStub(HeartBeatInterface heartBeatStub) {
        this.heartBeatStub = heartBeatStub;
    }

    /**
     * Call the transitionTo method on the view
     * If the client does not respond in Configs.secondsTimeOut seconds, it logs an error on the server console and close the connection
     * @param state ViewState to which the client must transition to
     */
    @Override
    public void transitionTo(ViewState state) {
        virtualViewExecutor.execute(()-> {
            Future<?> trasitionToFuture = viewUpdateExecutor.submit(() -> {
                try {
                    viewStub.transitionTo(state);
                } catch (Exception e) {
                    throw new RuntimeException("VirtualViewRMI.transitionTo: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
                }
            });
            try {
                trasitionToFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                Configs.printStackTrace(e);
                this.disconnect();
            }
        });
    }

    /**
     * Call the log method on the view
     * If the client does not respond in Configs.secondsTimeOut seconds, it logs an error on the server console and close the connection
     * @param logMsg the String to log
     */
    @Override
    public void log(String logMsg) {
        virtualViewExecutor.execute(()-> {
            Future<?> logFuture = viewUpdateExecutor.submit(() -> {
                try {
                    viewStub.log(logMsg);
                } catch (Exception e) {
                    throw new RuntimeException("VirtualViewRMI.log: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
                }
            });
            try {
                logFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                Configs.printStackTrace(e);
                this.disconnect();
            }
        });
    }

    /**
     * Call the logErr method on the view
     * If the client does not respond in Configs.secondsTimeOut seconds, it logs an error on the server console and close the connection
     * @param logMsg the String to log
     */
    @Override
    public void logErr(String logMsg) {
        virtualViewExecutor.execute(()-> {
            Future<?> logErrFuture = viewUpdateExecutor.submit(() -> {
                try {
                    viewStub.logErr(logMsg);
                } catch (Exception e) {
                    throw new RuntimeException("VirtualViewRMI.logErr: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
                }
            });
            try {
                logErrFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                Configs.printStackTrace(e);
                this.disconnect();
            }
        });
    }

    /**
     * Call the logOthers method on the view
     * If the client does not respond in Configs.secondsTimeOut seconds, it logs an error on the server console and close the connection
     * @param logMsg the String to log
     */
    @Override
    public void logOthers(String logMsg) {
        virtualViewExecutor.execute(()-> {
            Future<?> logFuture = viewUpdateExecutor.submit(() -> {
                try {
                    viewStub.logOthers(logMsg);
                } catch (Exception e) {
                    throw new RuntimeException("VirtualViewRMI.logOthers: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
                }
            });
            try {
                logFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                Configs.printStackTrace(e);
                this.disconnect();
            }
        });
    }

    /**
     * Call the logGame method on the view
     * If the client does not respond in Configs.secondsTimeOut seconds, it logs an error on the server console and close the connection
     * @param logMsg the String to log
     */
    @Override
    public void logGame(String logMsg) {
        virtualViewExecutor.execute(()-> {
            Future<?> logFuture = viewUpdateExecutor.submit(() -> {
                try {
                    viewStub.logGame(logMsg);
                } catch (Exception e) {
                    throw new RuntimeException("VirtualViewRMI.logGame: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
                }
            });
            try {
                logFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                Configs.printStackTrace(e);
                this.disconnect();
            }
        });
    }

    /**
     * Call the logChat method on the view
     * If the client does not respond in Configs.secondsTimeOut seconds, it logs an error on the server console and close the connection
     * @param logMsg the String to log
     */
    @Override
    public void logChat(String logMsg) {
        virtualViewExecutor.execute(()-> {
            Future<?> logFuture = viewUpdateExecutor.submit(() -> {
                try {
                    viewStub.logChat(logMsg);
                } catch (Exception e) {
                    throw new RuntimeException("VirtualViewRMI.logChat: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
                }
            });
            try {
                logFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                Configs.printStackTrace(e);
                this.disconnect();
            }
        });
    }

    /**
     * Call the updateLobbyList method on the view for updating the lightLobby in the lightModel
     * If the client does not respond in Configs.secondsTimeOut seconds, it logs an error on the server console and close the connection
     * @param diff the diff containing an updated version of the lightLobbyList
     */
    @Override
    public void updateLobbyList(ModelDiffs<LightLobbyList> diff) {
        virtualViewExecutor.execute(()-> {
            Future<?> updateFuture = viewUpdateExecutor.submit(() -> {
                try {
                    viewStub.updateLobbyList(diff);
                } catch (Exception e) {
                    throw new RuntimeException("VirtualViewRMI.updateLobbyList: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
                }

            });
            try {
                updateFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                Configs.printStackTrace(e);
                this.disconnect();
            }
        });
    }

    /**
     * Call the updateLobby method on the view for updating the lightLobby in the lightModel
     * If the client does not respond in Configs.secondsTimeOut seconds, it logs an error on the server console and close the connection
     * @param diff the diff containing an updated version of the lightLobby
     */
    @Override
    public void updateLobby(ModelDiffs<LightLobby> diff) {
        virtualViewExecutor.execute(()-> {
            Future<?> updateFuture = viewUpdateExecutor.submit(() -> {
                try{
                    viewStub.updateLobby(diff);
                }catch (Exception e) {
                    throw new RuntimeException("VirtualViewRMI.updateLobby: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
                }
            });
            try {
                updateFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                Configs.printStackTrace(e);
                this.disconnect();
            }
        });
    }

    /**
     * Call the updateGame method on the view for updating the lightGame in the lightModel
     * If the client does not respond in Configs.secondsTimeOut seconds, it logs an error on the server console and close the connection
     * @param diff the diff containing an updated version of the lightGame
     */
    @Override
    public void updateGame(ModelDiffs<LightGame> diff) {
        virtualViewExecutor.execute(()-> {
            Future<?> updateFuture = viewUpdateExecutor.submit(() -> {
                try {
                    viewStub.updateGame(diff);
                } catch (Exception e) {
                    throw new RuntimeException("VirtualViewRMI.updateGame: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
                }
            });
            try {
                updateFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
                Configs.printStackTrace(e);
                this.disconnect();
            }
        });
    }

    private synchronized void disconnect(){
        try {
            if(!alreadyDisconnected){
                alreadyDisconnected = true;
                heartBeatFuture.cancel(true);
                heartBeatExecutor.shutdownNow();
                controller.leave();
                virtualViewExecutor.shutdownNow();
                viewUpdateExecutor.shutdownNow();
            }
            UnicastRemoteObject.unexportObject(controller, true);
        }catch (InterruptedException ignored){
        }catch (Exception e){
            Configs.printStackTrace(e);
        }
    }

    /**
     * Set the controller of the game
     * @param controller the controller of the game
     */
    public void setController(ControllerInterface controller) {
        alreadyDisconnected = false;
        this.controller = controller;
    }

    /**
     * Call the checkEmpty method on the client to check if the connection is still alive.
     * If the client does not respond in Configs.secondsTimeOut seconds, it logs an error and close the connection
     */
    @Override
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
            }
        }, Configs.heartBeatPeriod, Configs.heartBeatPeriod, TimeUnit.SECONDS);
    }

}
