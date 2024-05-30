package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.controller3.Controller3;
import it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators.TurnTaker;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.utilities.Pair;
import it.polimi.ingsw.view.ViewInterface;

import java.util.*;
import java.util.function.Predicate;

public class GameMaster {
    private final Game game;

    public GameMaster(Game game){
        this.game = game;
    }

    public synchronized void join(String nickname, ViewInterface view, TurnTaker turnTaker, Controller3 controller){
        if(game.isInStartCardState()) {
            controller.joinStartGame();
        }else if(game.inInSecretObjState()){
            game.subscribe(nickname, view, turnTaker, true);
            game.joinSecretObjective(nickname, game);
            controller.chooseObjective();
        }else {
            game.subscribe(nickname, view, turnTaker, true);
            game.joinMidGame(nickname);
            controller.takeTurn();
        }
    }

    public synchronized void joinStartGame(String nickname, ViewInterface view, TurnTaker turnTaker){
        game.subscribe(nickname, view, turnTaker, false);
        User user = game.getUserFromNick(nickname);
        if (user.getUserHand().getStartCard() == null && !user.hasPlacedStartCard()) {
            StartCard startCard = game.drawStartCard();
            user.getUserHand().setStartCard(startCard);
        }
        game.joinStartCard(nickname, game);
    }

    public synchronized void placeStartCard(String nickname, Placement startCardPlacement){
        User user = game.getUserFromNick(nickname);
        user.placeStartCard(startCardPlacement);
        //model: add cards to hand
        CardInHand resourceDeck = null;
        for (int i = 0; i < 2; i++) {
            Pair<CardInHand, CardInHand> resourceDrawnAndReplacement = game.drawAndGetReplacement(DrawableCard.RESOURCECARD, Configs.actualDeckPos);
            user.getUserHand().addCard(resourceDrawnAndReplacement.first());
            resourceDeck = resourceDrawnAndReplacement.second();
        }
        Pair<CardInHand, CardInHand> goldDrawnAndReplacement = game.drawAndGetReplacement(DrawableCard.GOLDCARD, Configs.actualDeckPos);
        user.getUserHand().addCard(goldDrawnAndReplacement.first());
        //notify everyone and update my lightModel
        LightBack resourceBack = new LightBack(resourceDeck.getIdBack());
        LightBack goldBack = new LightBack(goldDrawnAndReplacement.second().getIdBack());

        game.notifyStartCardFaceChoice(nickname, user, Lightifier.lightify(startCardPlacement), resourceBack, goldBack);

        if(otherHaveAllSelected(nickname)){
            this.removeInactivePlayers(User::hasPlacedStartCard);
            moveToSecretObjectivePhase(game);
        }
    }

    public synchronized void chooseSecretObjective(String nickname, ObjectiveCard objChoice){
        User user = game.getUserFromNick(nickname);

        user.setSecretObjective(objChoice);
        game.notifySecretObjectiveChoice(nickname, Lightifier.lightifyToCard(objChoice));

        if(otherHaveAllChosen(nickname)) {
            this.removeInactivePlayers(User::hasChosenObjective);
            game.notifyEndSetupStartActualGame();
        }
    }

    public synchronized void place(String nickname, Placement placement){
        User user = game.getUserFromNick(nickname);
        Codex codexBeforePlacement = new Codex(user.getUserCodex());
        user.playCard(placement);
        Set<CardInHand> hand = user.getUserHand().getHand();
        Codex codexAfterPlacement = user.getUserCodex();
        //update playability
        Map<LightCard, Boolean> FrontIdToPlayability = new HashMap<>();
        for(CardInHand cardInHand: hand){
            boolean oldPlayability = cardInHand.canBePlaced(codexBeforePlacement);
            boolean newPlayability = cardInHand.canBePlaced(codexAfterPlacement);
            if(oldPlayability != newPlayability){
                FrontIdToPlayability.put(Lightifier.lightifyToCard(cardInHand), newPlayability);
            }
        }

        //notify everyone
        game.notifyPlacement(nickname, Lightifier.lightify(placement), user.getUserCodex(), FrontIdToPlayability);

    }

    public synchronized void draw(String nickname, DrawableCard deckType, int cardID){
        User user = game.getUserFromNick(nickname);

        if(!game.areDeckEmpty()) {
            CardInHand drawnCard;
            CardInHand cardReplacement;
            Pair<CardInHand, CardInHand> drawnAndReplacement = game.drawAndGetReplacement(deckType, cardID);
            drawnCard = drawnAndReplacement.first();
            cardReplacement = drawnAndReplacement.second();

            user.getUserHand().addCard(drawnCard);

            game.notifyDraw(deckType, cardID, Lightifier.lightifyToCard(drawnCard),
                    Lightifier.lightifyToCard(cardReplacement), nickname,
                    drawnCard.canBePlaced(user.getUserCodex()));
        }

        if(game.checkForChickenDinner() && Objects.equals(game.getFirstActivePlayer(), nickname) && !game.isLastTurn()){
            game.setLastTurn(true);
            game.notifyLastTurn();
        }

        if(Objects.equals(game.getLastActivePlayer(), nickname) && game.isLastTurn()){
            //model update with points
            game.addObjectivePoints();
            //notify
            game.notifyGameEnded(game.getPointPerPlayerMap(), game.getWinners());
        }else{
            //turn
            int nextPlayerIndex = game.getNextActivePlayerIndex();
            User nextPlayer = game.getUsersList().get(nextPlayerIndex);
            if(nextPlayer.getNickname().equals(nickname)){
                //TODO timer
            }else {
                game.setPlayerIndex(nextPlayerIndex);
                game.notifyTurn(game.getCurrentPlayer().getNickname());
            }
        }
    }
    /*
    public synchronized void leave(String nickname){
        game.unsubscribe(nickname);
        User you = game.getUserFromNick(nickname);

        if (game.isInStartCardState()) {

            if(otherHaveAllSelected(nickname) && !you.hasPlacedStartCard()){
                this.removeInactivePlayers(User::hasPlacedStartCard);
                moveToSecretObjectivePhase(game);
            }
        } else if (game.inInSecretObjState()) {
            if (otherHaveAllChosen(nickname) && !you.hasChosenObjective()) {
                this.removeInactivePlayers(User::hasChosenObjective);
                User currentUserPlayer = game.getCurrentPlayer();
                if(currentUserPlayer.getNickname().equals(nickname)) {
                    int nextPlayerIndex = game.getNextPlayerIndex();
                    String nextPlayerNick = game.getUsersList().get(nextPlayerIndex).getNickname();

                    game.setPlayerIndex(nextPlayerIndex);
                    game.notifyFirstTurn(nextPlayerNick);
                }
                game.notifyEndSetupStartActualGame();
            }
        } else if (!game.isInSetup()) { //if the game is in the actual game phase
            if (game.getCurrentPlayer().getNickname().equals(nickname)) { //if current player leaves
                //check if the user haven't placed

                //check if the user has disconnected after placing
                if (you.getHandSize() < 3) {
                    DrawableCard deckType;
                    int pos;
                    do {
                        deckType = randomDeckType();
                        pos = randomDeckPosition();

                        cardDrawn = drawnAndReplacement.first();
                        cardReplacement = drawnAndReplacement.second();
                    } while (cardDrawn == null);

                    this.draw(nickname, this.randomDeckType(), this.randomDeckPosition());
                }
                int nextPlayerIndex = game.getNextActivePlayerIndex();
                String nextPlayerNick = game.getPlayerFromIndex(nextPlayerIndex).getNickname();
                //move on with the turns for the other players
                game.setPlayerIndex(nextPlayerIndex);
                game.notifyTurn(nextPlayerNick);
            }
        } else //TODO if game is in EndGame state
            throw new IllegalStateException("Controller.leaveGame: Game is in an invalid state");
        //If the game is empty, remove it from the MultiGame

        if (gameToLeave.getGameLoopController().getActivePlayers().isEmpty()) {
            multiGame.removeGame(gameToLeave);
        }
    }*/


    private synchronized void drawForAllSecretObjective(){
        for(User user : game.getUsersList()){
            this.drawObjectiveCard(user);
        }
        game.secretObjectiveSetup();
    }

    private synchronized void drawObjectiveCard(User user){
        List<ObjectiveCard> objectiveCards = new ArrayList<>();
        for(int i=0;i<2;i++){
            objectiveCards.add(game.getObjectiveCardDeck().drawFromDeck());
        }
        user.getUserHand().setSecretObjectiveChoice(objectiveCards);
    }

    private synchronized void moveToSecretObjectivePhase(Game game){
        this.drawForAllSecretObjective();
        game.notifyMoveToSelectObjState();
    }

    private synchronized void removeInactivePlayers(Predicate<User> check) {
        for (User user : game.getUsersList()) {
            if (!game.getActivePlayers().contains(user.getNickname())) {
                if(check.test(user)){
                    game.removeUser(user.getNickname());
                }
            }
        }
    }

    private synchronized boolean otherHaveAllSelected(String nicknamePerspective){
        boolean allPlaced = true;
        for (String nick : game.getActivePlayers()) {
            if (!nick.equals(nicknamePerspective) && !game.getUserFromNick(nick).hasPlacedStartCard()) {
                allPlaced = false;
            }
        }
        return allPlaced;
    }

    private synchronized boolean otherHaveAllChosen(String nicknamePerspective){
        boolean allChosen = true;
            for (User user : game.getUsersList()) {
                if (!user.getNickname().equals(nicknamePerspective) && !user.hasChosenObjective()) {
                    allChosen = false;
                }
            }
        return allChosen;
    }

    /**
     * @return a random card from a non-empty deck. If all decks are empty return null
     */

    private DrawableCard randomDeckType(){
        Random random = new Random();
        int deckNumber = random.nextInt(2);
        return deckNumber == 0 ? DrawableCard.RESOURCECARD : DrawableCard.GOLDCARD;
    }

    private int randomDeckPosition(){
        Random random = new Random();
        return random.nextInt(3);
    }

}
