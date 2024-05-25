package it.polimi.ingsw.controller.ConnectionLayer;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;
import it.polimi.ingsw.connectionLayer.Socket.ServerMsg.LogErrMsg;
import it.polimi.ingsw.connectionLayer.Socket.ServerMsg.LogMsg;
import it.polimi.ingsw.connectionLayer.Socket.ServerMsg.ServerMsg;
import it.polimi.ingsw.connectionLayer.Socket.ServerMsg.TransitionToMsg;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.connectionLayer.VirtualSocket.VirtualControllerSocket;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServerHandlerTest {
    @Test
    public void testOutOfOrderMessage() throws IOException, InterruptedException {
        ServerHandlerTestSetup setup = setupServerHandler();

        ServerHandler serverHandler = setup.serverHandler();
        ObjectOutputStream toServerHandler = setup.toServerHandler();

        ServerMsg msg1 = new LogMsg("hi");
        msg1.setIndex(1);
        toServerHandler.writeObject(msg1);
        toServerHandler.flush();
        ServerMsg msg2 = new LogErrMsg("hi");
        msg2.setIndex(3);
        toServerHandler.writeObject(msg2);
        toServerHandler.flush();
        Thread.sleep(10); //Allow some time for the serverHandler to process the messages
        System.out.println(serverHandler.getRecivedMsgs());

    }

    private ServerHandlerTestSetup setupServerHandler() throws IOException {
        PipedInputStream inputPipe = new PipedInputStream();
        PipedOutputStream outputPipe = new PipedOutputStream();
        inputPipe.connect(outputPipe);

        //For some reason, the ObjectInputStream's constructor blocks the code flow until there is some inputData to read
        //This is handy in some scenario, e.g. checking if the right protocol is selected, but here we need to simulate a succefully connection
        ObjectOutputStream toServerHandler = new ObjectOutputStream(outputPipe);
        toServerHandler.writeObject(new TransitionToMsg(ViewState.LOGIN_FORM));
        toServerHandler.flush();

        ByteArrayOutputStream fromServerHandler = new ByteArrayOutputStream();
        Socket mockServer = mock(Socket.class);
        when(mockServer.getOutputStream()).thenReturn(fromServerHandler);
        when(mockServer.getInputStream()).thenReturn(inputPipe);

        when(mockServer.getInetAddress()).thenReturn(null);

        ViewInterface mockView = mock(ViewInterface.class);
        VirtualController mockController = mock(VirtualControllerSocket.class);

        ServerHandler serverHandler = new ServerHandler(mockServer);
        Thread serverHandlerThread = new Thread(serverHandler, "testedServerHandler");
        serverHandlerThread.start();
        serverHandler.setView(mockView);
        serverHandler.setOwner(mockController);

        return new ServerHandlerTestSetup(serverHandler, toServerHandler);
    }

    private record ServerHandlerTestSetup(ServerHandler serverHandler, ObjectOutputStream toServerHandler) {}
}
