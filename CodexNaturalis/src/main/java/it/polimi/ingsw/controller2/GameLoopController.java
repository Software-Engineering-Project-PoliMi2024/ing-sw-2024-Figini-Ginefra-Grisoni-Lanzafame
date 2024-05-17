package it.polimi.ingsw.controller2;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffs.game.*;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.view.ViewState;

import java.io.Serializable;
import java.util.*;

public class GameLoopController implements Serializable {
    //A map containing the controller for each ACTIVE player in the game
    //Todo this map is useless now that we store the nickname in the controller
    private final Map<String, ServerModelController> activePlayers;
    private final Game game;
    private Timer countdownTimer;

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
     * Join a user back into the game after he left. Add his controller back to the activePlayer map
     * @param nickname of the user who is joining
     * @param controller of the user who is joining
     */
    public void joinGame(String nickname, ServerModelController controller){
        game.subscribe(controller, nickname);

        if(countdownTimer!=null){
            countdownTimer.cancel();
            countdownTimer = null;
            ServerModelController lastController = activePlayers.values().iterator().next();
            activePlayers.put(nickname, controller);
            this.logYouAndOther(controller, LogsOnClient.MID_GAME_JOINED, LogsOnClient.PLAYER_REJOINED);
            lastController.logGame(LogsOnClient.COUNTDOWN_INTERRUPTED);
        }else {
            activePlayers.put(nickname, controller);
            this.logYouAndOther(controller, LogsOnClient.MID_GAME_JOINED, LogsOnClient.PLAYER_REJOINED);
        }

        User joiningUser = game.getUserFromNick(nickname);
        if(everyoneActiveHasPlaced() && everyoneActiveHasChosen()) { //player rejoined in the middle of the game
            controller.transitionTo(ViewState.IDLE);
        }else{
            activePlayers.remove(nickname);
            //The player rejoined a single-player game, so he matches the same view as the one-player-left.
            // OR the game is in the setup phase
            if((everyoneActiveHasPlaced() && !startCardIsPlaced(joiningUser)) || !everyoneActiveHasPlaced()){
                activePlayers.put(nickname, controller);
                controller.transitionTo(ViewState.CHOOSE_START_CARD);
                LightCard lightStartCard = Lightifier.lightifyToCard(getOrDrawStartCard(joiningUser));
                controller.updateGame(new HandDiffAdd(lightStartCard, true));
            }else if((everyoneActiveHasChosen() && !secretObjectiveIsChose(joiningUser)) || !everyoneActiveHasChosen()){
                activePlayers.put(nickname, controller);
                controller.transitionTo(ViewState.SELECT_OBJECTIVE);
                for(LightCard secretObjectiveCardChoice : getOrDrawSecretObjectiveChoices(joiningUser)){
                    controller.updateGame(new HandDiffAdd(secretObjectiveCardChoice, true));
                }
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
            game.subscribe(controller, nick);
            if(controller == null){
                throw new NullPointerException("Controller not found");
            }else {
                controller.logYou(LogsOnClient.NEW_GAME_JOINED);
                controller.transitionTo(ViewState.CHOOSE_START_CARD);
                try {
                    user = game.getUserFromNick(nick);
                }catch (IllegalCallerException e){
                    throw new NullPointerException("User not found");
                }
                LightCard lightStartCard = Lightifier.lightifyToCard(getOrDrawStartCard(user));
                controller.updateGame(new HandDiffAdd(lightStartCard, true));
            }
        }
    }

    /**
     * @param user who is playing
     * @return true if the user has placed his start card, false otherwise
     */
    private Boolean startCardIsPlaced(User user){
        return user.getUserCodex().getPlacementAt(new Position(0, 0)) != null;
    }


    /**
     * @param user who is playing
     * @return true if user chose his secretObjective, false otherwise
     */
    private Boolean secretObjectiveIsChose(User user){
        return user.getUserHand().getSecretObjective() != null;
    }

    /**
     * Draw a start card if the user never drawn one before or get it from the model
     * @param user who is drawing the startCard
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
     * @param user who is drawing the objectiveCard
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

    /**
     * Remove the controller from the activePlayers map,
     * if the controller is the current player, calculate the next player
     * @param controller of the user who is leaving
     */
    public void leaveGame(ServerModelController controller){
        String leavingPlayerNick = controller.getNickname();
        User leavingUser = game.getUserFromNick(leavingPlayerNick);

        activePlayers.remove(leavingPlayerNick, controller);

        if(activePlayers.size() == 1){
            this.onePlayerLeft();
        }else{
            for(ServerModelController stillActiveController : activePlayers.values()){
                stillActiveController.logOther(controller.getNickname(), LogsOnClient.PLAYER_GAME_LEFT);
            }
            //The leaving player is the current player
            if(hasEveryPlayerHasPlacedAndChosen(leavingUser) && leavingUser.equals(game.getGameParty().getCurrentPlayer())){
                //The leaving player has to draw a card if possible
                if(leavingUser.getUserHand().getHandSize()<3){
                    CardInHand card = drawRandomCard();
                    if(card != null){ //the decks are not empty
                        leavingUser.getUserHand().addCard(card);
                        //I do not notify the leaving player, because his view is not reachable anymore
                        game.subscribe(new HandOtherDiffAdd(Lightifier.lightifyToResource(card), leavingPlayerNick));
                    }
                }
                this.gameLoop();
                //The leaving player is the last that need to place their startCard
            }else if(everyoneActiveHasPlaced() && leavingUser.getUserHand().getStartCard()!=null){
                this.logAll(LogsOnClient.EVERYONE_PLACED_STARTCARD);
                this.secretObjectiveSetup();
                //The leaving player is the last that need to choose his secretObjective
            }else if(everyoneActiveHasChosen() && leavingUser.getUserHand().getSecretObjectiveChoices() != null){
                this.logAll(LogsOnClient.EVERYONE_CHOSE);
                this.gameLoopStarter();
            }
        }
    }

    private boolean hasEveryPlayerHasPlacedAndChosen(User notActiveUser) {
        return everyoneActiveHasPlaced() && everyoneActiveHasChosen() &&
                this.startCardIsPlaced(notActiveUser) && this.secretObjectiveIsChose(notActiveUser);
    }

    private void onePlayerLeft() {
        ServerModelController lastController = activePlayers.values().iterator().next();
        lastController.logGame(LogsOnClient.LAST_PLAYER);
        lastController.logGame(LogsOnClient.COUNTDOWN_START);

        countdownTimer = new Timer();
        countdownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                endGame();
            }
        }, Configs.timerDurationSeconds * 1000L);
    }
    /**
     * @return a random card from a non-empty deck. If all decks are empty return null
     */
    private CardInHand drawRandomCard(){
        CardInHand card = null;
        if(!game.decksAreEmpty()){
            Random random = new Random();
            int deckNumber = random.nextInt(2);
            card = (deckNumber == 0 ? game.getResourceCardDeck().drawFromDeck() : game.getGoldCardDeck().drawFromDeck());
            if(card==null){ //the first random deck is empty, drawn from the other deck
                deckNumber = (deckNumber == 0 ? 1 : 0);
                card = (deckNumber == 0 ? game.getResourceCardDeck().drawFromDeck() : game.getGoldCardDeck().drawFromDeck());
            }
            if(card == null){ //both decks are empty, drawn from a random buffer. There is at least one buffer not empty, due to the first "if"
                int randomDeckBufferSize = (deckNumber == 0 ? game.getResourceCardDeck().getBuffer().size() : game.getGoldCardDeck().getBuffer().size());
                if(randomDeckBufferSize == 0){ //the buffer from the randomDeck is empty
                    deckNumber = (deckNumber == 0 ? 1 : 0);
                    randomDeckBufferSize = (deckNumber == 0 ? game.getResourceCardDeck().getBuffer().size() : game.getGoldCardDeck().getBuffer().size());
                }
                card = (deckNumber == 0 ? game.getResourceCardDeck().drawFromBuffer(random.nextInt(randomDeckBufferSize)) : game.getGoldCardDeck().drawFromBuffer(random.nextInt(randomDeckBufferSize)));
            }
        }
        return card;
    }

    /**
     * If the controller is not the last to place his startCard, wait for the other active players to do so
     * The last activePlayer to place the card, ban everybody who left, and transition the others activePlayer to the Select-Objective state
     * Each controller than will draw the 2 possible secretObjective cards, and give them to the view.
     * @param controller of the user who placed the card
     */
    public void startCardPlaced(ServerModelController controller) {
        this.logYouAndOther(controller, LogsOnClient.YOU_PLACE_STARTCARD, LogsOnClient.PLAYER_PLACE_STARTCARD);
        if(!everyoneActiveHasPlaced()){ //Not all activePlayer placed their starting Card
            controller.logYou(LogsOnClient.WAIT_STARTCARD);
            controller.transitionTo(ViewState.WAITING_STATE);
        }else{
            this.logAll(LogsOnClient.EVERYONE_PLACED_STARTCARD);
            this.checkForDisconnectedUsers();
            this.secretObjectiveSetup();
        }
    }

    /**
     * If the controller is not the last to chose his secretObjective, wait for the other active players to do so
     * The last activePlayer to choose the objective, ban everybody who left and retrieve the next player from the model
     * Each controller who is not the activePlayer will go in the Idle state, the other in placeCard
     * @param controller of the user who chose the objective
     */
    public void secretObjectiveChose(ServerModelController controller){
        logYouAndOther(controller, LogsOnClient.YOU_CHOSE, LogsOnClient.PLAYER_CHOSE);
        if(!everyoneActiveHasChosen()){ //Not all activePlayer chose their secretObjective Card
            controller.logYou(LogsOnClient.WAIT_SECRET_OBJECTIVE);
            controller.transitionTo(ViewState.WAITING_STATE);
        }else{
            this.checkForDisconnectedUsers();
            this.logAll(LogsOnClient.EVERYONE_CHOSE);
            this.logAll(LogsOnClient.DECK_SHUFFLE);
            //A player might disconnect while he is in waiting, after he chose the secretObj.
            // He is not banned, but he can't be the first player of the gameLoop
            this.gameLoopStarter();
        }
    }

    /**
     * Elaborate the view for the currentPlayer. Calculate the nextPlayer
     * @param controller of the currentPlayer who ended his turn
     */
    public void cardDrawn(ServerModelController controller){
        this.logYouAndOther(controller, LogsOnClient.YOU_PLACED, LogsOnClient.YOU_DRAW);
        controller.transitionTo(ViewState.WAITING_STATE);
        this.gameLoop(controller);
    }

    /**
     * @return true if the conditions for triggering the last turn are met
     */
    private boolean checkForChickenDinner() {
        boolean thereAreWinner = false;
        if(game.decksAreEmpty()){
            thereAreWinner=true;
        }else{
            for(String nick : activePlayers.keySet()){
                if(game.getUserFromNick(nick).getUserCodex().getPoints()>=20){
                    thereAreWinner = true;
                    break;
                }
            }
        }
        return thereAreWinner;
    }

    /**
     * if a player left the game and didn't join back before the end of a setup-state, his nick is removed from the gameParty
     */
    private void checkForDisconnectedUsers(){
        if(activePlayers.size() != game.getGameParty().getNumberOfMaxPlayer()){
            for(User user : game.getGameParty().getUsersList()){
                if(!activePlayers.containsKey(user.getNickname())){
                    game.removeUser(user);
                }
            }
        }
    }

    /**
     * @return true if every activePlayer has chosen their secretObjective
     */
    private boolean everyoneActiveHasChosen(){
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
    private boolean everyoneActiveHasPlaced() {
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

    /**
     * @return the User of the next activePlayer in the game
     */
    private User calculateNextPlayer(){
        User nextPlayer;
        do{
            nextPlayer = game.nextPlayer();
        }while(!activePlayers.containsKey(nextPlayer.getNickname()));
        System.out.println("the next player is: " + nextPlayer.getNickname());
        return nextPlayer;
    }

    /**
     * Calculate the next currentPlayer, if it is the firstPlayer in order and conditions are met set the lastTurn=true;
     * If lastTurn is true and the next currentPlayer is the firstPlayer end the game
     * Adjust the view of each activePlayer accordingly
     */
    private void gameLoop(ServerModelController oldCurrentPlayerController){
        User nextPlayer = calculateNextPlayer();
        if(game.isLastTurn() && nextPlayer.equals(game.getGameParty().getFirstPlayerInOrder())){
            this.endGame();
        } else if (checkForChickenDinner() && nextPlayer.equals(game.getGameParty().getFirstPlayerInOrder()) && !game.isLastTurn()){
            game.setLastTurn(true);
            this.logAll(LogsOnClient.LAST_TURN);
        }
        this.logYouAndOther(activePlayers.get(nextPlayer.getNickname()), LogsOnClient.YOUR_TURN, LogsOnClient.PLAYER_TURN);
        oldCurrentPlayerController.transitionTo(ViewState.IDLE);
        activePlayers.get(nextPlayer.getNickname()).transitionTo(ViewState.PLACE_CARD);
    }

    /**
     * Calculate the next currentPlayer, if it is the firstPlayer in order and conditions are met set the lastTurn=true;
     * If lastTurn is true and the next currentPlayer is the firstPlayer end the game. Adjust the view of each activePlayer accordingly
     * Do not change the view of the (old)currentPlayer, useful when the (old)currentPlayer left.
     */
    private void gameLoop(){
        User nextPlayer = calculateNextPlayer();
        if(game.isLastTurn() && nextPlayer.equals(game.getGameParty().getFirstPlayerInOrder())){
            this.endGame();
        } else if (checkForChickenDinner() && nextPlayer.equals(game.getGameParty().getFirstPlayerInOrder()) && !game.isLastTurn()){
            game.setLastTurn(true);
            this.logAll(LogsOnClient.LAST_TURN);
        }
        this.logYouAndOther(activePlayers.get(nextPlayer.getNickname()), LogsOnClient.YOUR_TURN, LogsOnClient.PLAYER_TURN);
        activePlayers.get(nextPlayer.getNickname()).transitionTo(ViewState.PLACE_CARD);
    }

    /**
     * Calculate the next currentPlayer, update his view, and update the view of all other client transitioning them from
     * choseSecretObjective to IDLE
     */
    private void gameLoopStarter() {
        User nextPlayer = calculateNextPlayer();
        for(ServerModelController controller : activePlayers.values()){
            controller.updateGame(new GameDiffCurrentPlayer(nextPlayer.getNickname()));
            if (controller.getNickname().equals(nextPlayer.getNickname())) {
                controller.logYou(LogsOnClient.YOUR_TURN);
                controller.transitionTo(ViewState.PLACE_CARD);
            } else {
                controller.transitionTo(ViewState.IDLE);
            }
        }
    }

    /**
     * Calculate the winner of the game. Change the view of each controller to GAME_ENDING
     */
    private void endGame() {
        //Calculate all possibleWinners player(s) who scored the max amount of points in the game
        int maxPoint = activePlayers.keySet().stream().mapToInt(this::getPlayerPoint).max().orElse(0);
        List<String> possibleWinners = activePlayers.keySet().stream().filter(nick -> this.getPlayerPoint(nick) == maxPoint).toList();
        //calculate the real winner(s) by checking the points obtain from the objective Card
        List<String> realWinner;
        if(possibleWinners.size()!=1){
            int winnerPoint = activePlayers.keySet().stream().mapToInt(this::getWinnerPoint).max().orElse(0);
            realWinner = activePlayers.keySet().stream().filter(nick -> this.getWinnerPoint(nick) == winnerPoint).toList();
        }else{
            realWinner = new ArrayList<>(possibleWinners);
        }
        this.logAll(LogsOnClient.GAME_END);
        for(ServerModelController controller : activePlayers.values()){
            controller.updateGame(new GameDiffWinner(realWinner));
            controller.transitionTo(ViewState.GAME_ENDING);
        }
    }

    /**
     * @param nick of the player
     * @return the point of the nick obtain exclusively from the common and secret Objective card
     */
    private int getWinnerPoint(String nick){
        User user = game.getUserFromNick(nick);
        return game.getCommonObjective().stream().mapToInt(objectiveCard -> objectiveCard.getPoints(user.getUserCodex())).sum()
                + user.getUserHand().getSecretObjective().getPoints(user.getUserCodex());
    }
    /**
     * @param nick of the player
     * @return the point of the nick obtain during the game
     */
    private int getPlayerPoint(String nick){
        User user = game.getUserFromNick(nick);
        return user.getUserCodex().getPoints() +
                game.getCommonObjective().stream().mapToInt(objectiveCard -> objectiveCard.getPoints(user.getUserCodex())).sum()
                + game.getUserFromNick(nick).getUserHand().getSecretObjective().getPoints(user.getUserCodex());
    }


    /**
     * For each activePlayer, draw 2 objectiveCard, populate the hand, and send all the diffs to the view
     */
    private void secretObjectiveSetup(){
        for(ServerModelController controller : activePlayers.values()){
            controller.transitionTo(ViewState.SELECT_OBJECTIVE);
            User user = game.getUserFromNick(controller.getNickname());
            //Draw and send diff about my CardInHands to me and others
            for(int i = 0; i<2; i++){
                CardInHand resourceCard = game.getResourceCardDeck().drawFromDeck();
                user.getUserHand().addCard(resourceCard);
                game.subscribe(controller, new HandDiffAdd(Lightifier.lightifyToCard(resourceCard), resourceCard.canBePlaced(user.getUserCodex())),
                        new HandOtherDiffAdd(Lightifier.lightifyToResource(resourceCard), controller.getNickname()));
            }
            CardInHand goldCard = game.getGoldCardDeck().drawFromDeck();
            user.getUserHand().addCard(goldCard);
            game.subscribe(controller, new HandDiffAdd(Lightifier.lightifyToCard(goldCard), goldCard.canBePlaced(user.getUserCodex())),
                    new HandOtherDiffAdd(Lightifier.lightifyToResource(goldCard), controller.getNickname()));

            LightCard[] lightCommonObj = game.getCommonObjective().stream().map(Lightifier::lightifyToCard).toArray(LightCard[]::new);
            controller.updateGame(new GameDiffPublicObj(lightCommonObj));

            for (LightCard secretObjectiveCardChoice : getOrDrawSecretObjectiveChoices(user)) {
                controller.updateGame(new HandDiffAddOneSecretObjectiveOption(secretObjectiveCardChoice));
            }
        }
    }

    public Map<String, ServerModelController> getActivePlayers() {
        return activePlayers;
    }

    private void logYouAndOther(ServerModelController controller, LogsOnClient youLog, LogsOnClient theirLog){
        for(ServerModelController allControllers : activePlayers.values()){
            if(!allControllers.equals(controller)){
                allControllers.logOther(controller.getNickname(), theirLog);
            }else{
                controller.logYou(youLog);
            }
        }
    }

    private void logAll(LogsOnClient logsOnClient) {
        for(ServerModelController allController : activePlayers.values()){
            allController.logGame(logsOnClient);
        }
    }
}