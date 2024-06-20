package it.polimi.ingsw.ConnectionLayer;


import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;
import it.polimi.ingsw.connectionLayer.Socket.ServerMsg.*;
import it.polimi.ingsw.connectionLayer.Socket.VirtualSocket.VirtualViewSocket;
import it.polimi.ingsw.view.ViewState;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VirtualViewSocketTest {
    @Test
    public void testTransitionTo() throws IOException {
        ClientHandler clientHandler = setUp();
        VirtualViewSocket virtualViewSocket = new VirtualViewSocket(clientHandler);
        virtualViewSocket.transitionTo(ViewState.LOBBY);

        verify(clientHandler, times(1)).sendServerMessage(any(TransitionToMsg.class));
    }

    @Test
    public void testLog() throws IOException {
        ClientHandler clientHandler = setUp();
        VirtualViewSocket virtualViewSocket = new VirtualViewSocket(clientHandler);
        virtualViewSocket.log("test");

        verify(clientHandler, times(1)).sendServerMessage(any(LogMsg.class));
    }

    @Test
    public void testLogOthers() throws IOException {
        ClientHandler clientHandler = setUp();
        VirtualViewSocket virtualViewSocket = new VirtualViewSocket(clientHandler);
        virtualViewSocket.logOthers("test");

        verify(clientHandler, times(1)).sendServerMessage(any(LogOthersMsg.class));
    }

    @Test
    public void testLogGame() throws IOException {
        ClientHandler clientHandler = setUp();
        VirtualViewSocket virtualViewSocket = new VirtualViewSocket(clientHandler);
        virtualViewSocket.logGame("test");

        verify(clientHandler, times(1)).sendServerMessage(any(LogGameMsg.class));
    }

    @Test
    public void updateLobbyList() throws IOException {
        ClientHandler clientHandler = setUp();
        VirtualViewSocket virtualViewSocket = new VirtualViewSocket(clientHandler);
        virtualViewSocket.updateLobbyList(null);

        verify(clientHandler, times(1)).sendServerMessage(any(UpdateLobbyListMsg.class));
    }

    @Test
    public void updateLobby() throws IOException {
        ClientHandler clientHandler = setUp();
        VirtualViewSocket virtualViewSocket = new VirtualViewSocket(clientHandler);
        virtualViewSocket.updateLobby(null);

        verify(clientHandler, times(1)).sendServerMessage(any(UpdateLobbyMsg.class));
    }

    @Test
    public void updateGame() throws IOException {
        ClientHandler clientHandler = setUp();
        VirtualViewSocket virtualViewSocket = new VirtualViewSocket(clientHandler);
        virtualViewSocket.updateGame(null);

        verify(clientHandler, times(1)).sendServerMessage(any(UpdateGameMsg.class));
    }

    @Test
    public void testSetFinalRanking() throws IOException {
        ClientHandler clientHandler = setUp();
        VirtualViewSocket virtualViewSocket = new VirtualViewSocket(clientHandler);
        List<String> ranking = new ArrayList<>();
        virtualViewSocket.setFinalRanking(ranking);

        verify(clientHandler, times(1)).sendServerMessage(any(SetFinalRankingMsg.class));
    }

    @Test
    public void testLogErr() throws IOException {
        ClientHandler clientHandler = setUp();
        VirtualViewSocket virtualViewSocket = new VirtualViewSocket(clientHandler);
        virtualViewSocket.logErr("test");

        verify(clientHandler, times(1)).sendServerMessage(any(LogErrMsg.class));
    }

    private ClientHandler setUp() throws IOException {
        ServerSocket serverSocket = mock(ServerSocket.class);
        Socket client = mock(Socket.class);

        ClientHandler clientHandler = mock(ClientHandler.class);
        ObjectOutputStream objectOutputStream = mock(ObjectOutputStream.class);
        ObjectInputStream objectInputStream = mock(ObjectInputStream.class);

        when(serverSocket.accept()).thenReturn(client);
        when(client.getOutputStream()).thenReturn(objectOutputStream);
        when(client.getInputStream()).thenReturn(objectInputStream);

        return clientHandler;
    }
}
