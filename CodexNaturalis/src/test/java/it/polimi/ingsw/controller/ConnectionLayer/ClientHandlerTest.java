package it.polimi.ingsw.controller.ConnectionLayer;

import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;
import it.polimi.ingsw.connectionLayer.Socket.ClientMsg.ChoseSecretObjectiveMsg;
import it.polimi.ingsw.connectionLayer.Socket.ClientMsg.ClientMsg;
import it.polimi.ingsw.connectionLayer.Socket.ClientMsg.DisconnectMsg;
import it.polimi.ingsw.connectionLayer.Socket.ClientMsg.DrawMsg;
import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;
import it.polimi.ingsw.connectionLayer.Socket.ServerMsg.LogMsg;
import it.polimi.ingsw.connectionLayer.Socket.ServerMsg.ServerMsg;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualView;
import it.polimi.ingsw.connectionLayer.VirtualSocket.VirtualControllerSocket;
import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.view.ViewInterface;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Random;

import static org.mockito.Mockito.*;

public class ClientHandlerTest {

    //@Test
    /*public void testOutOfOrderMessage() throws IOException, InterruptedException, NoSuchMethodException {
        ClientHandlerTestSetup setup = setupClientHandler();

        ClientHandler clientHandler = setup.clientHandler;
        ObjectOutputStream toClientHandler = setup.toClientHandler;

        Random random = new Random();
        int numberOfMessages = random.nextInt(1, 10);
        for (int i = 0; i < numberOfMessages; i++) {
            ClientMsg msg = new DisconnectMsg();
            msg.setIndex(i + 1); //keep in mind that index 0 is the expected index
            toClientHandler.writeObject(msg);
            toClientHandler.flush();
        }
        Thread.sleep(30);
        Assertions.assertEquals(numberOfMessages, clientHandler.getReceivedMsg().size());
    }*/

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
        clientHandler.setOwner(mockView);
        clientHandler.setController(mockController);
        Thread clientHandlerThread = new Thread(clientHandler);
        clientHandlerThread.start();

        return new ClientHandlerTestSetup(clientHandler, new ObjectOutputStream(outputPipe), mockController);
    }

    private record ClientHandlerTestSetup(ClientHandler clientHandler, ObjectOutputStream toClientHandler, ControllerInterface mockController) {
    }

}
