package it.polimi.ingsw.controller.ConnectionLayer;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;
import it.polimi.ingsw.connectionLayer.Socket.ServerMsg.*;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.connectionLayer.VirtualSocket.VirtualControllerSocket;
import it.polimi.ingsw.controller.LogsOnClientStatic;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import static org.mockito.Mockito.*;

public class ServerHandlerTest {

    @Test
    public void testTimeoutException() throws Exception {

        Random portGenerator = new Random();
        int port = portGenerator.nextInt(10000, 20000);
        ServerSocket serverSocket = new ServerSocket(port);
        Thread serverThread = new Thread(() -> {
            while (true) {
                System.out.println("Waiting for connection");
                try {
                    Socket client = serverSocket.accept();
                    System.out.println("Connection accepted");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        serverThread.start();

        PipedInputStream inputPipe = new PipedInputStream();
        PipedOutputStream outputPipe = new PipedOutputStream();
        inputPipe.connect(outputPipe);
        ByteArrayOutputStream fromServerHandler = new ByteArrayOutputStream();
        Socket mockServer = mock(Socket.class);
        when(mockServer.getOutputStream()).thenReturn(fromServerHandler);
        when(mockServer.getInputStream()).thenReturn(inputPipe);
        when(mockServer.getInetAddress()).thenReturn(null);

        ViewInterface mockView = mock(ViewInterface.class);
        VirtualController mockController = mock(VirtualControllerSocket.class);

        ServerHandler serverHandler = new ServerHandler(mockServer);
        serverHandler.setView(mockView);
        serverHandler.setOwner(mockController);
        serverHandler.run();
        //If the exception is correctly thrown and catch, a Connection_Error message should be logged to the view
        verify(mockView, times(1)).logErr(LogsOnClientStatic.CONNECTION_ERROR);
    }

    @Test
    public void testOutOfOrderMessage() throws IOException, InterruptedException, NoSuchMethodException {
        ServerHandlerTestSetup setup = setupServerHandler();

        ServerHandler serverHandler = setup.serverHandler();
        ObjectOutputStream toServerHandler = setup.toServerHandler();

        //Check if all out of order messages are stored in the serverHandler
        Random random = new Random();
        int numberOfMessages = random.nextInt(1, 10);
        for (int i = 0; i < numberOfMessages; i++) {
            ServerMsg msg = new LogMsg("hi");
            msg.setIndex(i + 1); //keep in mind that index 0 is the expected index
            toServerHandler.writeObject(msg);
            toServerHandler.flush();
        }
        Thread.sleep(30); //Allow some time for the serverHandler to process the messages
        Assertions.assertEquals(numberOfMessages, serverHandler.getReceivedMsg().size());

        //Check if the messages are stored in the right order
        LinkedList<ServerMsg> messageList = new LinkedList<>(serverHandler.getReceivedMsg());
        for (int i = 0; i < numberOfMessages; i++) {
            Assertions.assertEquals(messageList.get(i).getIndex(), i + 1);
        }

        //Check what happens when the expected message is received after some out of order messages
        ServerMsg expectedMsg = new LogMsg("hi");
        expectedMsg.setIndex(0);
        toServerHandler.writeObject(expectedMsg);
        toServerHandler.flush();
        Thread.sleep(30); //Allow some time for the serverHandler to process all the messages in the queue
        Assertions.assertEquals(serverHandler.getReceivedMsg().size(), 0);
    }

    @Test
    public void testContinueMessagesWindow() throws IOException, InterruptedException {
        ServerHandlerTestSetup setup = setupServerHandler();

        ServerHandler serverHandler = setup.serverHandler();
        ObjectOutputStream toServerHandler = setup.toServerHandler();

        Random random = new Random();
        int numberOfMessages = random.nextInt(1, 10);
        for (int i = 0; i < numberOfMessages; i++) {
            ServerMsg continueMsg = new LogErrMsg("hi");
            continueMsg.setIndex(i + 1);
            toServerHandler.writeObject(continueMsg);
            toServerHandler.flush();

            ServerMsg notContinueMsg = new LogOthersMsg("hi");
            notContinueMsg.setIndex((int) Math.pow(2, i + 4)); //the non-ContinueMsg will start with index  of 16
            toServerHandler.writeObject(notContinueMsg);
            toServerHandler.flush();
        }
        ServerMsg expectedMsg = new LogMsg("hi");
        expectedMsg.setIndex(0);
        toServerHandler.writeObject(expectedMsg);
        toServerHandler.flush();
        Thread.sleep(30); //Allow some time for the serverHandler to process all of the messages in the queue

        //The number of messages in the queue should be equal to the number of messages that are not part of the continue window
        //The number of messages not present in the continue window is equal to numberOfMessages created in the loop above
        Assertions.assertEquals(serverHandler.getReceivedMsg().size(), numberOfMessages);
    }

    //This series of the test will test if the handle() method of the serverHandler correctly invokes the right method on the view
    @Test
    public void testLogErrInvocation() throws Exception {
        ServerHandlerTestSetup setup = setupServerHandler();

        ObjectOutputStream toServerHandler = setup.toServerHandler();
        ViewInterface mockView = setup.mockView();

        // Send a LogErrMsg to the ServerHandler
        ServerMsg logErrMsg = new LogErrMsg("Error message");
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
        ServerMsg setFinalRankingMsg = new SetFinalRankingMsg(List.of("Player1", "Player2"));
        toServerHandler.writeObject(setFinalRankingMsg);
        toServerHandler.flush();
        Thread.sleep(10);

        // Verify if setFinalRanking was called on the mockView
        verify(mockView, times(1)).setFinalRanking(any());
    }

    @Test
    public void testUpdateGameMsgInvocation() throws Exception {
        ServerHandlerTestSetup setup = setupServerHandler();

        ObjectOutputStream toServerHandler = setup.toServerHandler();
        ViewInterface mockView = setup.mockView();

        // Send a UpdateGameMsg to the ServerHandler
        ServerMsg updateGameMsg = new UpdateGameMsg(null);
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
        toServerHandler.writeObject(updateLobbyMsg);
        toServerHandler.flush();
        Thread.sleep(10);

        // Verify if updateLobby was called on the mockView
        verify(mockView, times(1)).updateLobby(any());
    }

    @Test
    public void testTransitionToMsgInvocation() throws Exception {
        //todo check why this test is so slow. It is so because it is the first test to be executed?
        ServerHandlerTestSetup setup = setupServerHandler();

        ObjectOutputStream toServerHandler = setup.toServerHandler();
        ViewInterface mockView = setup.mockView();

        // Send a TransitionToMsg to the ServerHandler
        ServerMsg transitionToMsg = new TransitionToMsg(ViewState.GAME_ENDING);
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

        return new ServerHandlerTestSetup(serverHandler, new ObjectOutputStream(outputPipe), mockView);
    }

    private record ServerHandlerTestSetup(ServerHandler serverHandler, ObjectOutputStream toServerHandler,
                                          ViewInterface mockView) {
    }
}
