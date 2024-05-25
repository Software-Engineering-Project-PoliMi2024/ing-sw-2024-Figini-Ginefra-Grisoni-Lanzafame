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

    @Test
    public void testLogGameInvocation() throws Exception {
        ServerHandlerTestSetup setup = setupServerHandler();

        ObjectOutputStream toServerHandler = setup.toServerHandler();
        ViewInterface mockView = setup.mockView();

        // Send a LogGameMsg to the ServerHandler
        ServerMsg logGameMsg = new LogGameMsg("Game message");
        logGameMsg.setIndex(1);
        toServerHandler.writeObject(logGameMsg);
        toServerHandler.flush();
        Thread.sleep(10);

        // Verify if logGame was called on the mockView
        verify(mockView, times(1)).logGame(anyString());
    }

    @Test
    public void testLogMsgInvocation() throws Exception {
        ServerHandlerTestSetup setup = setupServerHandler();

        ObjectOutputStream toServerHandler = setup.toServerHandler();
        ViewInterface mockView = setup.mockView();

        // Send a LogMsg to the ServerHandler
        ServerMsg logMsg = new LogMsg("Message");
        logMsg.setIndex(1);
        toServerHandler.writeObject(logMsg);
        toServerHandler.flush();
        Thread.sleep(10);

        // Verify if log was called on the mockView
        verify(mockView, times(1)).log(anyString());
    }

    @Test
    public void testLogOthersMsgInvocation() throws Exception {
        ServerHandlerTestSetup setup = setupServerHandler();

        ObjectOutputStream toServerHandler = setup.toServerHandler();
        ViewInterface mockView = setup.mockView();

        // Send a LogOthersMsg to the ServerHandler
        ServerMsg logOthersMsg = new LogOthersMsg("Other message");
        logOthersMsg.setIndex(1);
        toServerHandler.writeObject(logOthersMsg);
        toServerHandler.flush();
        Thread.sleep(10);

        // Verify if logOthers was called on the mockView
        verify(mockView, times(1)).logOthers(anyString());
    }

    @Test
    public void testSetFinalRankingMsgInvocation() throws Exception {
        ServerHandlerTestSetup setup = setupServerHandler();

        ObjectOutputStream toServerHandler = setup.toServerHandler();
        ViewInterface mockView = setup.mockView();

        // Send a SetFinalRankingMsg to the ServerHandler
        ServerMsg setFinalRankingMsg = new SetFinalRankingMsg(new String[]{"Player1", "Player2"}, new int[]{1, 2});
        setFinalRankingMsg.setIndex(1);
        toServerHandler.writeObject(setFinalRankingMsg);
        toServerHandler.flush();
        Thread.sleep(10);

        // Verify if setFinalRanking was called on the mockView
        verify(mockView, times(1)).setFinalRanking(any(), any());
    }

    @Test
    public void testUpdateGameMsgInvocation() throws Exception {
        ServerHandlerTestSetup setup = setupServerHandler();

        ObjectOutputStream toServerHandler = setup.toServerHandler();
        ViewInterface mockView = setup.mockView();

        // Send a UpdateGameMsg to the ServerHandler
        ServerMsg updateGameMsg = new UpdateGameMsg(null);
        updateGameMsg.setIndex(1);
        toServerHandler.writeObject(updateGameMsg);
        toServerHandler.flush();
        Thread.sleep(10);

        // Verify if updateGame was called on the mockView
        verify(mockView, times(1)).updateGame(any());
    }

    @Test
    public void testUpdateLobbyListMsgInvocation() throws Exception {
        ServerHandlerTestSetup setup = setupServerHandler();

        ObjectOutputStream toServerHandler = setup.toServerHandler();
        ViewInterface mockView = setup.mockView();

        // Send a UpdateLobbyListMsg to the ServerHandler
        ServerMsg updateLobbyListMsg = new UpdateLobbyListMsg(null);
        updateLobbyListMsg.setIndex(1);
        toServerHandler.writeObject(updateLobbyListMsg);
        toServerHandler.flush();
        Thread.sleep(10);

        // Verify if updateLobbyList was called on the mockView
        verify(mockView, times(1)).updateLobbyList(any());
    }

    @Test
    public void testUpdateLobbyMsgInvocation() throws Exception {
        ServerHandlerTestSetup setup = setupServerHandler();

        ObjectOutputStream toServerHandler = setup.toServerHandler();
        ViewInterface mockView = setup.mockView();

        // Send a UpdateLobbyMsg to the ServerHandler
        ServerMsg updateLobbyMsg = new UpdateLobbyMsg(null);
        updateLobbyMsg.setIndex(1);
        toServerHandler.writeObject(updateLobbyMsg);
        toServerHandler.flush();
        Thread.sleep(10);

        // Verify if updateLobby was called on the mockView
        verify(mockView, times(1)).updateLobby(any());
    }

    @Test
    public void testTransitionToMsgInvocation() throws Exception {
        //todo check why is slow. Probably because is something to do with the fact that a TransitionToMsg is used in the Setup
        ServerHandlerTestSetup setup = setupServerHandler();

        ObjectOutputStream toServerHandler = setup.toServerHandler();
        ViewInterface mockView = setup.mockView();

        // Send a TransitionToMsg to the ServerHandler
        ServerMsg transitionToMsg = new TransitionToMsg(ViewState.GAME_ENDING);
        transitionToMsg.setIndex(1);
        toServerHandler.writeObject(transitionToMsg);
        toServerHandler.flush();
        Thread.sleep(10);

        // Verify if transitionTo was called on the mockView
        verify(mockView, times(1)).transitionTo(ViewState.GAME_ENDING);
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
