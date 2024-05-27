package it.polimi.ingsw.controller.ConnectionLayer;

import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;
import it.polimi.ingsw.connectionLayer.Socket.ClientMsg.ChoseSecretObjectiveMsg;
import it.polimi.ingsw.connectionLayer.Socket.ClientMsg.ClientMsg;
import it.polimi.ingsw.connectionLayer.Socket.ClientMsg.DisconnectMsg;
import it.polimi.ingsw.connectionLayer.Socket.ClientMsg.DrawMsg;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualView;
import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Random;

import static org.mockito.Mockito.*;

public class ClientHandlerTest {

    @Test
    public void testOutOfOrderMessage() throws IOException, InterruptedException {
        ClientHandlerTestSetup setup = setupClientHandler();
        ObjectOutputStream toClientHandler = setup.toClientHandler();
        ClientHandler clientHandler = setup.clientHandler;

        //Check if all out of order messages are stored in the serverHandler
        Random random = new Random();
        int numberOfMessages = random.nextInt(1, 10);
        for (int i = 0; i < numberOfMessages; i++) {
            ClientMsg msg = new DrawMsg(DrawableCard.GOLDCARD, 0);
            msg.setIndex(i + 1); //keep in mind that index 0 is the expected index
            toClientHandler.writeObject(msg);
            toClientHandler.flush();
        }
        Thread.sleep(30); //Allow some time for the clientHandler to process the messages
        Assertions.assertEquals(clientHandler.getReceivedMsg().size(), numberOfMessages);

        //Check if the messages are stored in the right order
        LinkedList<ClientMsg> messageList = new LinkedList<>(clientHandler.getReceivedMsg());
        for (int i = 0; i < numberOfMessages; i++) {
            Assertions.assertEquals(messageList.get(i).getIndex(), i + 1);
        }

        //Check what happens when the expected message is received after some out of order messages
        ClientMsg expectedMsg = new DisconnectMsg();
        expectedMsg.setIndex(0);
        toClientHandler.writeObject(expectedMsg);
        toClientHandler.flush();
        Thread.sleep(30); //Allow some time for the clientHandler to process all the messages in the queue
        Assertions.assertEquals(clientHandler.getReceivedMsg().size(), 0);
    }

    @Test
    public void testContinueMessagesWindow() throws IOException, InterruptedException {
        ClientHandlerTestSetup setup = setupClientHandler();
        ObjectOutputStream toClientHandler = setup.toClientHandler();
        ClientHandler clientHandler = setup.clientHandler;

        Random random = new Random();
        int numberOfMessages = random.nextInt(1, 10);
        for (int i = 0; i < numberOfMessages; i++) {
            ClientMsg continueMsg = new DrawMsg(DrawableCard.GOLDCARD, 0);
            continueMsg.setIndex(i + 1);
            toClientHandler.writeObject(continueMsg);
            toClientHandler.flush();

            ClientMsg notContinueMsg = new DisconnectMsg();
            notContinueMsg.setIndex((int) Math.pow(2, i + 4)); //the non-ContinueMsg will start with index  of 16
            toClientHandler.writeObject(notContinueMsg);
            toClientHandler.flush();
        }

        ClientMsg expectedMsg = new DrawMsg(DrawableCard.RESOURCECARD, 1);
        expectedMsg.setIndex(0);
        toClientHandler.writeObject(expectedMsg);
        toClientHandler.flush();
        Thread.sleep(30); //Allow some time for the clientHandler to process all of the messages in the queue

        //The number of messages in the queue should be equal to the number of messages that are not part of the continue window
        //The number of messages not present in the continue window is equal to numberOfMessages created in the loop above
        Assertions.assertEquals(clientHandler.getReceivedMsg().size(), numberOfMessages);
    }

    //This series of the test will test if the handle() method of the clientHandler invoke the right method on the controller
    @Test
    public void ChoseSecretObjectiveMsgTest4() throws Exception {
        ClientHandlerTestSetup setup = setupClientHandler();
        ObjectOutputStream toClientHandler = setup.toClientHandler();
        ControllerInterface mockController = setup.controller();

        ClientMsg choseSecretObjectiveMsg = new ChoseSecretObjectiveMsg(new LightCard(5));
        toClientHandler.writeObject(choseSecretObjectiveMsg);
        toClientHandler.flush();
        Thread.sleep(10);

        verify(mockController, times(1)).choseSecretObjective(any(LightCard.class));
    }

    private ClientHandlerTestSetup setupClientHandler() throws IOException {
        PipedInputStream inputPipe = new PipedInputStream();
        PipedOutputStream outputPipe = new PipedOutputStream();
        inputPipe.connect(outputPipe);

        ByteArrayOutputStream fromClientHandler = new ByteArrayOutputStream();

        Socket mockClient = mock(Socket.class);
        when(mockClient.getOutputStream()).thenReturn(fromClientHandler);
        when(mockClient.getInputStream()).thenReturn(inputPipe);

        when(mockClient.getInetAddress()).thenReturn(null);

        VirtualView mockView = mock(VirtualView.class);
        ControllerInterface mockController = mock(ControllerInterface.class);

        ClientHandler clientHandler = new ClientHandler(mockClient);
        Thread clientHandlerThread = new Thread(clientHandler, "testedClientHandler");
        clientHandlerThread.start();
        clientHandler.setOwner(mockView);
        clientHandler.setController(mockController);

        return new ClientHandlerTestSetup(clientHandler, new ObjectOutputStream(outputPipe), mockController);
    }

    private record ClientHandlerTestSetup(ClientHandler clientHandler, ObjectOutputStream toClientHandler, ControllerInterface controller) {}
}
