package it.polimi.ingsw.controller.ConnectionLayer;

import it.polimi.ingsw.connectionLayer.Socket.ClientMsg.*;
import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualView;
import it.polimi.ingsw.connectionLayer.VirtualSocket.VirtualControllerSocket;
import it.polimi.ingsw.controller.LogsOnClient;
import it.polimi.ingsw.view.ViewInterface;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

import static org.mockito.Mockito.*;

public class VirtualControllerSocketTest {
    @Test
    public void testUnsuccessfulConnect() throws Exception {
        //Test when  the server is unreachable
        VirtualControllerSocket virtualControllerSocket = new VirtualControllerSocket();
        ViewInterface mockView = mock(ViewInterface.class);
        virtualControllerSocket.connect("0.0.0.0", 4321, mockView);
        verify(mockView, times(1)).logErr(LogsOnClient.CONNECTION_ERROR.getMessage());
    }

    @Test
    public void testSuccessfulConnect() throws Exception {
        /*Random portGenerator = new Random();
        int port = portGenerator.nextInt(1000) + 5000;*/
        ServerSocket serverSocket = new ServerSocket(42069);
        Thread serverSocketThread = new Thread(() -> {
            while (true) {
                try {
                    serverSocket.accept();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        serverSocketThread.start();

        VirtualControllerSocket virtualControllerSocket = new VirtualControllerSocket();
        ServerHandler mockServerHandler = mock(ServerHandler.class);
        when(mockServerHandler.isReady()).thenReturn(true);
        virtualControllerSocket.setServerHanlder(mockServerHandler);

        virtualControllerSocket.connect("0.0.0.0", 42069, mock(VirtualView.class));
        verify(mockServerHandler, times(1)).setOwner(virtualControllerSocket);
        verify(mockServerHandler, times(1)).setView(any(ViewInterface.class));
        serverSocketThread.interrupt();
    }

    @Test
    public void loginTest() {
        VirtualControllerSocket virtualControllerSocket = new VirtualControllerSocket();
        ServerHandler mockServerHandler = mock(ServerHandler.class);

        virtualControllerSocket.setServerHanlder(mockServerHandler);
        virtualControllerSocket.login("username");

        verify(mockServerHandler, times(1)).sendServerMessage(any(LoginMsg.class));
    }

    @Test
    public void createLobbyTest() {
        VirtualControllerSocket virtualControllerSocket = new VirtualControllerSocket();
        ServerHandler mockServerHandler = mock(ServerHandler.class);

        virtualControllerSocket.setServerHanlder(mockServerHandler);
        virtualControllerSocket.createLobby("gameName", 4);

        verify(mockServerHandler, times(1)).sendServerMessage(any(CreateLobbyMsg.class));
    }

    @Test
    public void joinLobbyTest() {
        VirtualControllerSocket virtualControllerSocket = new VirtualControllerSocket();
        ServerHandler mockServerHandler = mock(ServerHandler.class);

        virtualControllerSocket.setServerHanlder(mockServerHandler);
        virtualControllerSocket.joinLobby("gameName");

        verify(mockServerHandler, times(1)).sendServerMessage(any(JoinLobbyMsg.class));
    }

    @Test
    public void disconnectTest() {
        VirtualControllerSocket virtualControllerSocket = new VirtualControllerSocket();
        ServerHandler mockServerHandler = mock(ServerHandler.class);

        virtualControllerSocket.setServerHanlder(mockServerHandler);
        virtualControllerSocket.disconnect();

        verify(mockServerHandler, times(1)).sendServerMessage(any(DisconnectMsg.class));
    }

    @Test
    public void leaveLobbyTest() {
        VirtualControllerSocket virtualControllerSocket = new VirtualControllerSocket();
        ServerHandler mockServerHandler = mock(ServerHandler.class);

        virtualControllerSocket.setServerHanlder(mockServerHandler);
        virtualControllerSocket.leaveLobby();

        verify(mockServerHandler, times(1)).sendServerMessage(any(LeaveLobbyMsg.class));
    }

    @Test
    public void choseSecretObjectiveTest() {
        VirtualControllerSocket virtualControllerSocket = new VirtualControllerSocket();
        ServerHandler mockServerHandler = mock(ServerHandler.class);

        virtualControllerSocket.setServerHanlder(mockServerHandler);
        virtualControllerSocket.choseSecretObjective(null);

        verify(mockServerHandler, times(1)).sendServerMessage(any(ChoseSecretObjectiveMsg.class));
    }

    @Test
    public void placeTest() {
        VirtualControllerSocket virtualControllerSocket = new VirtualControllerSocket();
        ServerHandler mockServerHandler = mock(ServerHandler.class);

        virtualControllerSocket.setServerHanlder(mockServerHandler);
        virtualControllerSocket.place(null);

        verify(mockServerHandler, times(1)).sendServerMessage(any(PlaceMsg.class));
    }

    @Test
    public void drawTest() {
        VirtualControllerSocket virtualControllerSocket = new VirtualControllerSocket();
        ServerHandler mockServerHandler = mock(ServerHandler.class);

        virtualControllerSocket.setServerHanlder(mockServerHandler);
        virtualControllerSocket.draw(null, 0);

        verify(mockServerHandler, times(1)).sendServerMessage(any(DrawMsg.class));
    }
}
