package it.polimi.ingsw.controller.socket;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.socket.client.SocketClient;
import it.polimi.ingsw.controller.socket.messages.actionMessages.LoginMsg;


public class SocketController extends Controller {

    private SocketClient socketClient;

    public SocketController()
    {
        super();
    }

    @Override
    public void connect(String ip, int port, String nickname) {
        socketClient = new SocketClient(ip, port, nickname);
        Thread clientThread = new Thread(socketClient, "client");
        clientThread.start();

        while(socketClient.getServerHandler()==null || !socketClient.getServerHandler().isReady()){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        socketClient.getServerHandler().sendActionMessage(new LoginMsg(nickname));

        //Update view composition
    }

    public SocketClient getSocketClient()
    {
        return socketClient;
    }

}
