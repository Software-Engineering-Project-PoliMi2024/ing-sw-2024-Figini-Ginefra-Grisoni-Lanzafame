package it.polimi.ingsw.view;

public class ServerConnectState extends ViewState {

    // Metodo mostra messaggio iniziale che chiede indirizzo IP del server
    public void displayServerAddressRequest() {
        System.out.println("Enter the server's IP address:");
    }

    // Metodo mostra messaggio connessione riuscita
    public void displayConnectionSuccess(String ip, int port) {
        System.out.println("Connected to the server at " + ip + " on port " + port);
    }

    // Metodo mostra messaggio errore caso fallimento connessione
    public void displayConnectionError(String ip) {
        System.out.println("Can't connect to the server at " + ip + ". Check the address and try again.");
    }
}
