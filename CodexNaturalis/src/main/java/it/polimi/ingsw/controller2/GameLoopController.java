package it.polimi.ingsw.controller2;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.Lightifier;
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
    public GameLoopController(Game game, Map<String, ServerModelController> activePlayers){
        this.activePlayers = activePlayers;
        this.game = game;
    }

    //Joining of a player after game creation. I need this method to put his controller back in the activePlayers map
    public void joinGame(String nickname, ServerModelController controller){
        activePlayers.put(nickname, controller);
        for(User user : game.getGameParty().getUsersList()){
            if(user.getNickname().equals(nickname)){
                if(startCardIsPlaced(user) && secretObjectiveIsChose(user)){
                    controller.log(LogsFromServer.MID_GAME_JOINED);
                    controller.transitionTo(ViewState.IDLE);
                }else{
                    controller.log(LogsFromServer.NEW_GAME_JOINED);
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
                controller.log(LogsFromServer.NEW_GAME_JOINED);
                LightCard lightStartCard = Lightifier.lightifyToCard(getOrDrawStartCard(user));
                controller.updateGame(new HandDiffAdd(lightStartCard, true));
            }
        }
    }

    private Boolean startCardIsPlaced(User user){
        return user.getUserCodex().getPlacementAt(new Position(0, 0)) != null;
    }

    private Boolean secretObjectiveIsChose(User user){
        return user.getUserHand().getSecretObjective() != null;
    }

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
        for(int i=0;i<1;i++){
            cardList.add(this.game.getObjectiveCardDeck().drawFromDeck());
        }
        user.getUserHand().setSecretObjectiveChoice(cardList);
        return cardList;
    }
    public void placeCard(){

    }

    public void leaveGame(ServerModelController controller, String nickname){
        activePlayers.remove(nickname, controller);
    }

    public void startCardPlaced() {
        boolean everyoneHasPlace = false;
        while (!everyoneHasPlace) {
            everyoneHasPlace = true;
            for (String nick : activePlayers.keySet()) {
                User user = game.getUserFromNick(nick);
                if (user.getUserHand().getStartCard() != null) {
                    everyoneHasPlace = false;
                    break;
                }
            }
            if (everyoneHasPlace) {
                break; // Exit the while loop if everyone has their startCard placed
            }
        }
        //When every activePlayer has placed the startCard, remove players who left, go on the next state
        if(activePlayers.size() != game.getGameParty().getNumberOfMaxPlayer()){
            for(User user : game.getGameParty().getUsersList()){
                if(!activePlayers.containsKey(user.getNickname())){
                    game.getGameParty().removeUser(user);
                }
            }
        }
        for(ServerModelController controller : activePlayers.values()) {
            controller.transitionTo(ViewState.SELECT_OBJECTIVE);
            User user;
            try {
                user = game.getUserFromNick(controller.getNickname());
            } catch (IllegalCallerException e) {
                throw new NullPointerException("User not found");
            }
            for (LightCard secretObjectiveCardChoice : getOrDrawSecretObjectiveChoices(user)) {
                controller.updateGame(new HandDiffAdd(secretObjectiveCardChoice, true));
            }
        }
    }


    public void gameLoop(){

    }
}
