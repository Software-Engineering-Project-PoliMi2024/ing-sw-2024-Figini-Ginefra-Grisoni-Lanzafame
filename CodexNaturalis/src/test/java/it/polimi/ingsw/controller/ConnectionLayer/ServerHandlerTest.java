package it.polimi.ingsw.controller.ConnectionLayer;

import it.polimi.ingsw.Server;
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
import java.util.LinkedList;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServerHandlerTest {
    @Test
    public void testOutOfOrderMessage() throws IOException, InterruptedException {
        ServerHandlerTestSetup setup = setupServerHandler();
        ServerHandler serverHandler = setup.serverHandler();
        ObjectOutputStream toServerHandler = setup.toServerHandler();
        Random random = new Random();
        int numberOfMessages = random.nextInt(1, 10);
        for(int i = 0; i < numberOfMessages; i++) {
            ServerMsg msg = new LogMsg("hi");
            msg.setIndex(i+2); //keep in mind that index 0 is used during the setup
            toServerHandler.writeObject(msg);
            toServerHandler.flush();
        }
        Thread.sleep(100); //Allow some time for the serverHandler to process the messages
        //Check if all messages are stored in the serverHandler
        assertEquals(serverHandler.getRecivedMsgs().size(), numberOfMessages);

        //Check if the messages are stored in the right order
        LinkedList<ServerMsg> messageList = new LinkedList<>(serverHandler.getRecivedMsgs());
        for(int i = 0; i < numberOfMessages; i++) {
            assertEquals(messageList.get(i).getIndex(), i+2);
        }
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
