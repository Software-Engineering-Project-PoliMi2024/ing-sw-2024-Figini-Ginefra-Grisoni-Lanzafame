package it.polimi.ingsw.controller2;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffs.GameDiffRound;
import it.polimi.ingsw.lightModel.diffs.HandDiffAdd;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.view.ViewState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameLoopController {
    //A map containing the view for each ACTIVE player in the game
    private final Map<String, ServerModelController> activePlayers;
    private final Game game;

    /**
     * The constructor of the class
     * @param game being directed by the GameLoopController
     * @param activePlayers a map of nickname and control for each player
     */
    public GameLoopController(Game game, Map<String, ServerModelController> activePlayers){
        this.activePlayers = activePlayers;
        this.game = game;
    }

    /**
     * join a user back into the game after he left. Add his controller back to the activePlayer map
     * @param nickname of the user who is joining
     * @param controller of the user who is joining
     */
    public void joinGame(String nickname, ServerModelController controller){
        activePlayers.put(nickname, controller);
        for(User user : game.getGameParty().getUsersList()){
            if(user.getNickname().equals(nickname)){
                if(startCardIsPlaced(user) && secretObjectiveIsChose(user)){
                    controller.log(LogsOnClient.MID_GAME_JOINED);
                    controller.transitionTo(ViewState.IDLE);
                }else{
                    controller.log(LogsOnClient.NEW_GAME_JOINED);
                    if(!startCardIsPlaced(user)){
                        controller.transitionTo(ViewState.CHOOSE_START_CARD);
                        LightCard lightStartCard = Lightifier.lightifyToCard(getOrDrawStartCard(user));
                        controller.updateGame(new HandDiffAdd(lightStartCard, true));
                    }else{  //!secretObjectiveIsChose(user)
                        controller.transitionTo(ViewState.SELECT_OBJECTIVE);
                        for(LightCard secretObjectiveCardChoice : getOrDrawSecretObjectiveChoices(user)){
                            controller.updateGame(new HandDiffAdd(secretObjectiveCardChoice, true));
                        }
                    }
                }
                break;
            }
        }
    }
    /**
     * joinGame from the lobby
     */
    public void joinGame(){
        User user;
        for(String nick : game.getGameParty().getUsersList().stream().map(User::getNickname).toList()){
            ServerModelController controller = activePlayers.get(nick);
            if(controller == null){
                throw new NullPointerException("Controller not found");
            }else {
                controller.transitionTo(ViewState.CHOOSE_START_CARD);
                try {
                    user = game.getUserFromNick(nick);
                }catch (IllegalCallerException e){
                    throw new NullPointerException("User not found");
                }
                controller.log(LogsOnClient.NEW_GAME_JOINED);
                LightCard lightStartCard = Lightifier.lightifyToCard(getOrDrawStartCard(user));
                controller.updateGame(new HandDiffAdd(lightStartCard, true));
            }
        }
    }

    /**
     * @param user
     * @return true if user place his start card, false otherwise
     */
    private Boolean startCardIsPlaced(User user){
        return user.getUserCodex().getPlacementAt(new Position(0, 0)) != null;
    }


    /**
     * @param user
     * @return true if user chose his secretObjective, false otherwise
     */
    private Boolean secretObjectiveIsChose(User user){
        return user.getUserHand().getSecretObjective() != null;
    }

    /**
     * Draw a start card if the user never drawn one before or get it from the model
     * @param user
     * @return a StartCard
     */
    private StartCard getOrDrawStartCard(User user){
        StartCard startCard;
        if(user.getUserHand().getStartCard()==null){
            startCard = game.getStartingCardDeck().drawFromDeck();
            user.getUserHand().setStartCard(startCard);
        }else{
            startCard=user.getUserHand().getStartCard();
        }
        return startCard;
    }

    /**
     * Draw two ObjectiveCards if the user never drawn them before or get them from the model
     * @param user
     * @return a List two objectiveCard in a light version
     */
    private List<LightCard> getOrDrawSecretObjectiveChoices(User user){
        List<LightCard> secretLightObjectives = new ArrayList<>();
        if(user.getUserHand().getSecretObjectiveChoices()==null){
            for(ObjectiveCard card : drawObjectiveCard(user)){
                secretLightObjectives.add(Lightifier.lightifyToCard(card));
            }
        }else{
            for(ObjectiveCard card : user.getUserHand().getSecretObjectiveChoices()){
                secretLightObjectives.add(Lightifier.lightifyToCard(card));
            }
        }
        return secretLightObjectives;
    }

    /**
     * @param user which is drawing the objectiveCard
     * @return a list of Two objectiveCard
     */
    private List<ObjectiveCard> drawObjectiveCard(User user){
        List<ObjectiveCard> cardList = new ArrayList<>();
        for(int i=0;i<2;i++){
            cardList.add(this.game.getObjectiveCardDeck().drawFromDeck());
        }
        user.getUserHand().setSecretObjectiveChoice(cardList);
        return cardList;
    }
    public void leaveGame(ServerModelController controller, String nickname){
        activePlayers.remove(nickname, controller);
    }
    /**
     * If the controller is not the last to place his startCard, wait for the other active players to do so
     * The last activePlayer to place the card, ban everybody who left, and transition the others activePlayer to the Select-Objective state
     * Each controller than will draw the 2 possible secretObjective cards, and give them to the view.
     * @param controller of the user who placed the card
     */
    public void startCardPlaced(ServerModelController controller) {
        if(!everyoneHasPlace()){ //Not all activePlayer placed their starting Card
            controller.log(LogsOnClient.WAIT_STARTCARD);
        }else{
            this.checkForDisconnectedUsers();
            for(ServerModelController controller1 : activePlayers.values()){
                controller1.transitionTo(ViewState.SELECT_OBJECTIVE);
                User user = game.getUserFromNick(controller1.getNickname());
                for (LightCard secretObjectiveCardChoice : getOrDrawSecretObjectiveChoices(user)) {
                    controller1.updateGame(new HandDiffAdd(secretObjectiveCardChoice, true));
                }
            }
        }
    }

    /**
     * If the controller is not the last to chose his secretObjective, wait for the other active players to do so
     * The last activePlayer to choose the objective, ban everybody who left and retrieve the next player from the model
     * Each controller who is not the activePlayer will go in the Idle state, the other in placeCard
     * @param controller of the user who chose the objective
     */
    public void secretObjectiveChose(ServerModelController controller){
        if(!everyoneHasChose()){ //Not all activePlayer chose their secretObjective Card
            controller.log(LogsOnClient.WAIT_SECRET_OBJECTIVE);
        }else{
            this.checkForDisconnectedUsers();
            User currentPlayer;
            //A player might disconnect as soon as he chose is secretObjective. He is not banned, but he can't be the
            //first player of the gameLoop
            do{
                game.getGameParty().nextPlayer();
                currentPlayer = game.getGameParty().getCurrentPlayer();
            }while(!activePlayers.containsKey(currentPlayer.getNickname()));
            System.out.println("the next player is: " + currentPlayer.getNickname());

            for(ServerModelController controller1 : activePlayers.values()){
                controller1.updateGame(new GameDiffRound(currentPlayer.getNickname()));
                if (controller1.getNickname().equals(currentPlayer.getNickname())) {
                    controller1.transitionTo(ViewState.PLACE_CARD);
                } else {
                    controller1.transitionTo(ViewState.IDLE);
                }
            }
        }
    }

    /**
     * Elaborate the view for the currentPlayer and the nextOne
     * @param controller of the currentPlayer who ended his turn
     */
    public void cardDrawn(ServerModelController controller){
        //all of this code will be run only once by the soon-to-be ex currentPlayer
        User nextUser;
        do{
            game.getGameParty().nextPlayer();
            nextUser = game.getGameParty().getCurrentPlayer();
        }while(!activePlayers.containsKey(nextUser.getNickname()));
        System.out.println("the next player is: " + nextUser.getNickname());
        controller.transitionTo(ViewState.IDLE);
        for(ServerModelController serverModelController : activePlayers.values()){
            serverModelController.updateGame(new GameDiffRound(nextUser.getNickname()));
            if(serverModelController.getNickname().equals(nextUser.getNickname())){
                serverModelController.log(LogsOnClient.YOUR_TURN);
                serverModelController.transitionTo(ViewState.PLACE_CARD);
            }
        }

    }

    /**
     * if a player left the game and didn't join back before the end of a setup-state, his nick is removed from the gameParty
     */
    private void checkForDisconnectedUsers(){
        if(activePlayers.size() != game.getGameParty().getNumberOfMaxPlayer()){
            for(User user : game.getGameParty().getUsersList()){
                if(!activePlayers.containsKey(user.getNickname())){
                    game.getGameParty().removeUser(user);
                }
            }
        }
    }

    /**
     * @return true if every activePlayer has chosen their secretObjective
     */
    private boolean everyoneHasChose(){
        boolean everybodyHasChoose = true;
        for (String nick : activePlayers.keySet()) {
            User user = game.getUserFromNick(nick);
            if (user.getUserHand().getSecretObjectiveChoices() != null) {
                everybodyHasChoose = false;
            }
        }
        return everybodyHasChoose;
    }
    /**
     * @return true if every activePlayer has placed their startCard
     */
    private boolean everyoneHasPlace() {
        boolean everyoneHasPlace = true;
        for (String nick : activePlayers.keySet()) {
            User user = game.getUserFromNick(nick);
            if (user.getUserHand().getStartCard() != null) {
                everyoneHasPlace = false;
                break;
            }
        }
        return everyoneHasPlace;
    }
}
