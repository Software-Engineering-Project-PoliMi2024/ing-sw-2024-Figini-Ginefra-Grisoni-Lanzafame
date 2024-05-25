package it.polimi.ingsw.controller.ConnectionLayer;

import it.polimi.ingsw.Server;
import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;
import it.polimi.ingsw.connectionLayer.Socket.ServerMsg.*;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.connectionLayer.VirtualSocket.VirtualControllerSocket;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class ServerHandlerTest {
    @Test
    public void testOutOfOrderMessage() throws IOException, InterruptedException, NoSuchMethodException {
        ServerHandlerTestSetup setup = setupServerHandler();

        ServerHandler serverHandler = setup.serverHandler();
        ObjectOutputStream toServerHandler = setup.toServerHandler();

        //Check if all out of order messages are stored in the serverHandler
        Random random = new Random();
        int numberOfMessages = random.nextInt(1, 10);
        for(int i = 0; i < numberOfMessages; i++) {
            ServerMsg msg = new LogMsg("hi");
            msg.setIndex(i+2); //keep in mind that index 0 is used during the setup
            toServerHandler.writeObject(msg);
            toServerHandler.flush();
        }
        Thread.sleep(30); //Allow some time for the serverHandler to process the messages
        Assertions.assertEquals(serverHandler.getRecivedMsgs().size(), numberOfMessages);

        //Check if the messages are stored in the right order
        LinkedList<ServerMsg> messageList = new LinkedList<>(serverHandler.getRecivedMsgs());
        for(int i = 0; i < numberOfMessages; i++) {
            Assertions.assertEquals(messageList.get(i).getIndex(), i+2);
        }

        //Check what happens when the expected message is received after some out of order messages
        ServerMsg expectedMsg = new LogMsg("hi");
        expectedMsg.setIndex(1);
        toServerHandler.writeObject(expectedMsg);
        toServerHandler.flush();
        Thread.sleep(30); //Allow some time for the serverHandler to all the messages in the queue
        Assertions.assertEquals(serverHandler.getRecivedMsgs().size(), 0);
    }

    @Test
    public void testContinueMessagesWindow() throws IOException, InterruptedException {
        ServerHandlerTestSetup setup = setupServerHandler();

        ServerHandler serverHandler = setup.serverHandler();
        ObjectOutputStream toServerHandler = setup.toServerHandler();

        Random random = new Random();
        int numberOfMessages = random.nextInt(1, 10);
        for(int i = 0; i < numberOfMessages; i++) {
            ServerMsg continueMsg = new LogErrMsg("hi");
            continueMsg.setIndex(i+2);
            toServerHandler.writeObject(continueMsg);
            toServerHandler.flush();

            ServerMsg notContinueMsg = new LogOthersMsg("hi");
            notContinueMsg.setIndex((int) Math.pow(2, i+4)); //the non ContinueMsg will start with index  of 16
            toServerHandler.writeObject(notContinueMsg);
            toServerHandler.flush();
        }
        ServerMsg expectedMsg = new LogMsg("hi");
        expectedMsg.setIndex(1);
        toServerHandler.writeObject(expectedMsg);
        toServerHandler.flush();
        Thread.sleep(30); //Allow some time for the serverHandler to all the messages in the queue

        //The number of messages in the queue should be equal to the number of messages that are not part of the continue window
        //The number of messages not present in the continue window is equal to numberOfMessages created in the loop above
        Assertions.assertEquals(serverHandler.getRecivedMsgs().size(), numberOfMessages);
    }

    @Test
    public void testLogErrInvocation() throws Exception {
        ServerHandlerTestSetup setup = setupServerHandler();

        ServerHandler serverHandler = setup.serverHandler();
        ObjectOutputStream toServerHandler = setup.toServerHandler();
        ViewInterface mockView = setup.mockView();

        // Send a LogErrMsg to the ServerHandler
        ServerMsg logErrMsg = new LogErrMsg("Error message");
        logErrMsg.setIndex(1);
        toServerHandler.writeObject(logErrMsg);
        toServerHandler.flush();
        Thread.sleep(10);

        // Verify if logErr was called on the mockView
        verify(mockView, times(1)).logErr(anyString());
    }

    private ServerHandlerTestSetup setupServerHandler() throws IOException {
        PipedInputStream inputPipe = new PipedInputStream();
        PipedOutputStream outputPipe = new PipedOutputStream();
        inputPipe.connect(outputPipe);

        //For some reason, the ObjectInputStream's constructor blocks the code flow until there is some inputData to read
        //This is handy in some scenario, e.g. checking if the right protocol is selected, but here I need to simulate a successful connection
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

        return new ServerHandlerTestSetup(serverHandler, toServerHandler, mockView);
    }

    private record ServerHandlerTestSetup(ServerHandler serverHandler, ObjectOutputStream toServerHandler, ViewInterface mockView) {}
}
