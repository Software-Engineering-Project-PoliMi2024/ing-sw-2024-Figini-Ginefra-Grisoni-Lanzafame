package it.polimi.ingsw.controller2;

public abstract class ServerImplementation implements Runnable{
    private final int port;
    private final ServerModelController serverModelController;

    public ServerImplementation(ServerModelController serverModelController, int port) {
        this.serverModelController = serverModelController;
        this.port = port;
    }
    public void run() {}

    public int getPort() {
        return port;
    }

    public ServerModelController getServerModelController() {
        return serverModelController;
    }
    public void connect(String ip, int port) {}
}
