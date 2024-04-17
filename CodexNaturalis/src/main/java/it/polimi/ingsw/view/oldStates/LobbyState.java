package it.polimi.ingsw.view.oldStates;

import java.util.List;
public class LobbyState extends ViewState {
    private final String lobbyName;
    private final List<String> Users; // Lista giocatori lobby
    public LobbyState(String lobbyName, List<String> Users) {
        this.lobbyName = lobbyName;
        this.Users = Users;
    }
    // Metodo mostra nome della lobby e elenco giocatori
    public void displayLobbyInfo() {
        System.out.println("You are now in the lobby: " + lobbyName);
        System.out.println("Players in the lobby:");
        for (String player : Users) {
            System.out.println("- " + Users);
        }
    }
    // Metodo mostra messaggio uscita lobby
    public void displayQuitMessage() {
        System.out.println("Leaving the lobby...");
    }
}