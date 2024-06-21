package it.polimi.ingsw.connectionLayer.RMI.VirtualRMI;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.connectionLayer.PingPongInterface;
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

public class VirtualViewRMI implements VirtualView {
    private final ViewInterface viewStub;
    private PingPongInterface pingPongStub;
    private ControllerInterface controller;
    private final ThreadPoolExecutor viewExecutor = new ThreadPoolExecutor(2, 4, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    private final ScheduledExecutorService pingPongExecutor = Executors.newScheduledThreadPool(2);
    private boolean alreadyDisconnected = false;
    private Future<?> pong;
    /**
     * @param viewStub the stub of the view on the client
     */
    public VirtualViewRMI(ViewInterface viewStub) {
        this.viewStub = viewStub;
    }

    @Override
    public void checkEmpty() throws RemoteException {

    }

    @Override
    public void setPingPongStub(PingPongInterface pingPongStub) {
        this.pingPongStub = pingPongStub;
    }

    @Override
    public void transitionTo(ViewState state) throws RemoteException {
        Future<?> trasitionToFuture = viewExecutor.submit(()->{
            try {
                viewStub.transitionTo(state);
            }catch (Exception e){
                throw new RuntimeException("VirtualViewRMI.transitionTo: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }
        });
        try {
            trasitionToFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        }catch (Exception e){
            e.printStackTrace();
            this.disconnect();
        }
    }

    @Override
    public void log(String logMsg) throws RemoteException {
        Future<?> logFuture = viewExecutor.submit(()->{
            try {
                viewStub.log(logMsg);
            }catch (Exception e){
                throw new RuntimeException("VirtualViewRMI.log: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }
        });
        try {
            logFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        }catch (Exception e){
            e.printStackTrace();
            this.disconnect();
        }
    }

    @Override
    public void logErr(String logMsg) throws RemoteException {
        Future<?> logErrFuture = viewExecutor.submit(()->{
            try {
                viewStub.logErr(logMsg);
            }catch (Exception e){
                throw new RuntimeException("VirtualViewRMI.logErr: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }
        });
        try {
            logErrFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        }catch (Exception e){
            e.printStackTrace();
            this.disconnect();
        }
    }

    @Override
    public void logOthers(String logMsg) throws RemoteException {
        Future<?> logFuture = viewExecutor.submit(()->{
            try {
                viewStub.logOthers(logMsg);
            }catch (Exception e){
                throw new RuntimeException("VirtualViewRMI.logOthers: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }
        });
        try {
            logFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        }catch (Exception e){
            e.printStackTrace();
            this.disconnect();
        }
    }

    @Override
    public void logGame(String logMsg) throws RemoteException {
        Future<?> logFuture = viewExecutor.submit(()-> {
            try {
                viewStub.logGame(logMsg);
            }catch (Exception e){
                throw new RuntimeException("VirtualViewRMI.logGame: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }
        });
        try {
            logFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        }catch (Exception e){
            e.printStackTrace();
            this.disconnect();
        }
    }

    @Override
    public void logChat(String logMsg) throws RemoteException {
        Future<?> logFuture = viewExecutor.submit(()->{
            try {
                viewStub.logChat(logMsg);
            }catch (Exception e){
                throw new RuntimeException("VirtualViewRMI.logChat: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }
        });
        try {
            logFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        }catch (Exception e){
            e.printStackTrace();
            this.disconnect();
        }
    }
    @Override
    public void updateLobbyList(ModelDiffs<LightLobbyList> diff) throws RemoteException {
        Future<?> updateFuture = viewExecutor.submit(()->{
            try {
                viewStub.updateLobbyList(diff);
            }catch (Exception e){
                throw new RuntimeException("VirtualViewRMI.updateLobbyList: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }

        });
        try {
            updateFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        }catch (Exception e){
            e.printStackTrace();
            this.disconnect();
        }
    }

    @Override
    public void updateLobby(ModelDiffs<LightLobby> diff) throws RemoteException {
        Future<?> updateFuture = viewExecutor.submit(()->{

        });
        try {
            viewStub.updateLobby(diff);
        }catch (Exception e){
            throw new RuntimeException("VirtualViewRMI.updateLobby: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
        }
        try {
            updateFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        }catch (Exception e){
            e.printStackTrace();
            this.disconnect();
        }
    }

    @Override
    public void updateGame(ModelDiffs<LightGame> diff) throws RemoteException {
        Future<?> updateFuture = viewExecutor.submit(()->{
            try {
                viewStub.updateGame(diff);
            }catch (Exception e){
                throw new RuntimeException("VirtualViewRMI.updateGame: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }
        });
        try {
            updateFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        }catch (Exception e){
            e.printStackTrace();
            this.disconnect();
        }
    }

    @Override
    public void setFinalRanking(List<String> ranking) throws RemoteException {
        Future<?> setFinalRankingFuture = viewExecutor.submit(()->{
            try {
                viewStub.setFinalRanking(ranking);
            }catch (Exception e){
                throw new RuntimeException("VirtualViewRMI.setFinalRanking: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }
        });
        try {
            setFinalRankingFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        }catch (Exception e){
            e.printStackTrace();
            this.disconnect();
        }
    }

    private synchronized void disconnect(){
        try {
            if(!alreadyDisconnected){
                alreadyDisconnected = true;
                controller.disconnect();
                pong.cancel(true);
                pingPongExecutor.shutdownNow();
            }
            UnicastRemoteObject.unexportObject(controller, true);
        }catch (InterruptedException ignored){
        }catch (Exception e){
            System.out.println("disconnect " + e.getMessage());
        }
    }

    public void setController(ControllerInterface controller) {
        alreadyDisconnected = false;
        this.controller = controller;
    }

    @Override
    public void pingPong() throws RemoteException {
        pong = pingPongExecutor.scheduleAtFixedRate(() -> {
            try {
                Future<?> ping = pingPongExecutor.submit(() -> {
                    try {
                        pingPongStub.checkEmpty();
                    } catch (Exception e) {
                        throw new RuntimeException("VirtualViewRMI.pinPong: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
                    }
                });
                ping.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
            }catch (InterruptedException ignored){
            } catch (Exception e) {
                e.printStackTrace();
                this.disconnect();
            }
        }, Configs.pingPongFrequency, 1, TimeUnit.SECONDS);
    }

}
