package it.polimi.ingsw.controller.RMI;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.RMI.remoteInterfaces.LoginRMI;
import it.polimi.ingsw.controller.RMI.remoteInterfaces.MultiGames;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.cardReleted.cards.DrawableCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.view.View;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ControllerRMI extends Controller {
    public String askName(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your nickname: ");
        return scanner.nextLine();
    }
    public void connect(String ip, int port, String nickname, View view, Controller controller){
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(ip, port);
            try {
                MultiGame games = ((MultiGames) registry.lookup(LabelAPI.GetMultiGames.getLabel())).getMultiGame();
                LoginRMI loginRMI = (LoginRMI) registry.lookup(LabelAPI.Login.getLabel());
                while(!loginRMI.login(nickname, games)){
                    System.out.println("Login failed : Nickname already taken");
                    //nickname = view.askNickname();
                    nickname = askName();
                }
                System.out.println("Login successful");
            } catch (Exception e) {
                System.err.println("Client exception during the Log In: " + e.toString());
                e.printStackTrace();
            }
            ClientRMI clientRMI = new ClientRMI(nickname, view, registry);
            Thread clientThread = new Thread(clientRMI, "client");
            clientThread.start();
        } catch (Exception e) {
            System.err.println("registry unreachable");
            e.printStackTrace();
        }


    }
    public void getActiveGameList(){}
    public void joinGame(String gameName, String nickname){}
    public void disconnect(){}
    public void leaveLobby(){}
    public void createGame(String gameName, int maxPlayerCount){}

    @Override
    public void selectStartCardFace(StartCard card, CardFace cardFace) {

    }

    public void peek(String nickName){}
    public void choseSecretObjective(ObjectiveCard objectiveCard){}
    public void place(Placement placement){}
    public void draw(DrawableCard deckID, int cardID){}
    public void leaveGame(){}
}
