package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffObserverInterface.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffs.LobbyListDiffEdit;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.model.tableReleted.LobbyList;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LobbyListDiffPublisherTest {
    class ViewTest implements ViewInterface {
        public LightLobbyList lightLobbyList;

        public ViewTest(){
            lightLobbyList = new LightLobbyList();
        }

        @Override
        public void setState(ViewState state) throws RemoteException {

        }

        @Override
        public void transitionTo(ViewState state) throws RemoteException {

        }

        @Override
        public void postConnectionInitialization(ControllerInterface controller) throws RemoteException {

        }

        @Override
        public void log(String logMsg) throws RemoteException {

        }

        @Override
        public void updateLobbyList(ModelDiffs<LightLobbyList> diff) throws RemoteException {
            diff.apply(this.lightLobbyList);
            System.out.println("lobbyList updated");
        }

        @Override
        public void updateLobby(ModelDiffs<LightLobby> diff) throws RemoteException {

        }

        @Override
        public void updateGame(ModelDiffs<LightGame> diff) throws RemoteException {

        }

        @Override
        public void setFinalRanking(String[] nicks, int[] points) throws RemoteException {

        }

        public LightLobbyList getLightLobbyList(){
            return lightLobbyList;
        }
    }

    // if the view is subscribed to the lobbyList it will receive the updates
    @Test
    void subscribeLobby() {
        LobbyList lobbyList = new LobbyList();
        ViewTest view1 = new ViewTest();
        lobbyList.subscribe(view1);

        Lobby lobby = new Lobby(3, "gianni", "gianni1");
        lobbyList.addLobby(lobby);

        ArrayList<LightLobby> lobbiesDiff= new ArrayList<>();
        lobbiesDiff.add(Lightifier.lightify(lobby));
        lobbyList.subscribe(new LobbyListDiffEdit(lobbiesDiff, new ArrayList<>()));

        printLobbies(view1);
        assert view1.lightLobbyList.getLobbies().size() == 1;
    }

    private void printLobbies(ViewTest view){
        System.out.println("----------+----------");
        for(LightLobby lightLobby : view.getLightLobbyList().getLobbies()){
            System.out.println("# " + lightLobby.name());
            for(String nick : lightLobby.nicknames()){
                System.out.println("- "+nick);
            }
        }
    }

    // once disconnected the view will lose all previous memory of the lobbyList and don't receive updates
    @Test
    void unsubscribe() {
        LobbyList lobbyList = new LobbyList();
        Lobby lobbyAlreadyPresent = new Lobby(3, "gianni", "gianni1");
        Lobby lobbyToAddOnceDisconnected = new Lobby(3, "gianni", "gianni2");
        lobbyList.addLobby(lobbyAlreadyPresent);

        ViewTest view1 = new ViewTest();
        lobbyList.subscribe(view1);
        lobbyList.unsubscribe(view1);
        lobbyList.addLobby(lobbyToAddOnceDisconnected);

        ArrayList<LightLobby> lobbiesDiff= new ArrayList<>();
        lobbiesDiff.add(Lightifier.lightify(lobbyToAddOnceDisconnected));
        lobbyList.subscribe(new LobbyListDiffEdit(lobbiesDiff, new ArrayList<>()));

        lobbyList.subscribe(new LobbyListDiffEdit(lobbiesDiff, new ArrayList<>()));

        printLobbies(view1);
        assert view1.lightLobbyList.getLobbies().isEmpty();
    }

    // if a user subscribe to the lobbyList it will receive the list of lobbies
    @Test
    void subscribeUser() {
        LobbyList lobbyList = new LobbyList();
        Lobby lobby = new Lobby(3, "gianni", "gianni1");
        lobbyList.addLobby(lobby);

        ViewTest view1 = new ViewTest();
        lobbyList.subscribe(view1);

        printLobbies(view1);
        assert view1.lightLobbyList.getLobbies().size() == 1;
    }
}