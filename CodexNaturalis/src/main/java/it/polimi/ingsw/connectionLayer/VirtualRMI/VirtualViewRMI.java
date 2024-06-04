package it.polimi.ingsw.connectionLayer.VirtualRMI;

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
import java.util.List;
import java.util.concurrent.*;

public class VirtualViewRMI implements VirtualView {
    private final ViewInterface viewStub;
    private PingPongInterface pingPongStub;
    private ControllerInterface controller;
    private final ThreadPoolExecutor viewExecutor = new ThreadPoolExecutor(1, 4, 10, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    private ScheduledExecutorService pingPongExecutor = Executors.newSingleThreadScheduledExecutor();
    private boolean notAlreadyDisconnected = false;
    /**
     * @param viewStub the stub of the view on the client
     * @throws RemoteException if a communication-related exception occurs during the execution of this method.
     */
    public VirtualViewRMI(ViewInterface viewStub) throws RemoteException {
        this.viewStub = viewStub;
    }
    public void pingPong(){
        Future<?> pong = pingPongExecutor.scheduleAtFixedRate(() -> {
                try {
                    pingPongStub.checkEmpty();
                } catch (Exception e) {
                    this.disconnect();
                    pingPongExecutor.shutdownNow();
                }
        }, Configs.pingPongFrequency, Configs.pingPongFrequency, TimeUnit.SECONDS);

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
        Future<Void> trasitionToFuture = viewExecutor.submit(()->{
            viewStub.transitionTo(state);
            return null;
        });
        try {
            trasitionToFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (Exception e){
            debug(e);
            //this.disconnect();
        }
    }

    @Override
    public void log(String logMsg) throws RemoteException {
        Future<Void> logFuture = viewExecutor.submit(()->{
            viewStub.log(logMsg);
            return null;
        });
        try {
            logFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (Exception e){
            debug(e);
            //this.disconnect();
        }
    }

    @Override
    public void logErr(String logMsg) throws RemoteException {
        Future<Void> logErrFuture = viewExecutor.submit(()->{
            viewStub.logErr(logMsg);
            return null;
        });
        try {
            logErrFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (Exception e){
            debug(e);
            //this.disconnect();
        }
    }

    @Override
    public void logOthers(String logMsg) throws RemoteException {
        Future<Void> logFuture = viewExecutor.submit(()->{
            viewStub.logOthers(logMsg);
            return null;
        });
        try {
            logFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (Exception e){
            debug(e);
            //this.disconnect();
        }
    }

    @Override
    public void logGame(String logMsg) throws RemoteException {
        Future<Void> logFuture = viewExecutor.submit(()-> {
            viewStub.logGame(logMsg);
            return null;
        });
        try {
            logFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (Exception e){
            debug(e);
            //this.disconnect();
        }
    }

    @Override
    public void updateLobbyList(ModelDiffs<LightLobbyList> diff) throws RemoteException {
        Future<Void> updateFuture = viewExecutor.submit(()->{
            viewStub.updateLobbyList(diff);
            return null;
        });
        try {
            updateFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (Exception e){
            debug(e);
            //this.disconnect();
        }
    }

    @Override
    public void updateLobby(ModelDiffs<LightLobby> diff) throws RemoteException {
        Future<Void> updateFuture = viewExecutor.submit(()->{
            viewStub.updateLobby(diff);
            return null;
        });
        try {
            updateFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (Exception e){
            debug(e);
            //this.disconnect();
        }
    }

    @Override
    public void updateGame(ModelDiffs<LightGame> diff) throws RemoteException {
        Future<Void> updateFuture = viewExecutor.submit(()->{
            viewStub.updateGame(diff);
            return null;
        });
        try {
            updateFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (Exception e){
            debug(e);
            //this.disconnect();
        }
    }

    @Override
    public void setFinalRanking(List<String> ranking) throws RemoteException {
        Future<Void> setFinalRankingFuture = viewExecutor.submit(()->{
            viewStub.setFinalRanking(ranking);
            return null;
        });
        try {
            setFinalRankingFuture.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (Exception e){
            debug(e);
            //this.disconnect();
        }
    }

    private synchronized void disconnect(){
        try {
            if(!notAlreadyDisconnected){
                notAlreadyDisconnected = true;
                controller.disconnect();
            }
            //UnicastRemoteObject.unexportObject(controller, true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setController(ControllerInterface controller) {
        notAlreadyDisconnected = false;
        this.controller = controller;
    }

    private void debug(Exception e){
        e.printStackTrace();
    }

}
