package it.polimi.ingsw.controller.PublicController;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.controller4.GameController;
import it.polimi.ingsw.controller4.Interfaces.GameControllerInterface;
import it.polimi.ingsw.controller4.LogsOnClientStatic;
import it.polimi.ingsw.controller4.persistence.PersistenceFactory;
import it.polimi.ingsw.lightModel.DiffGenerator;
import it.polimi.ingsw.lightModel.Heavifier;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffs.game.*;
import it.polimi.ingsw.lightModel.diffs.game.codexDiffs.CodexDiffPlacement;
import it.polimi.ingsw.lightModel.diffs.game.deckDiffs.DeckDiffDeckDraw;
import it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs.GameDiffCurrentPlayer;
import it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs.GameDiffFirstPlayer;
import it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs.GameDiffPlayerActivity;
import it.polimi.ingsw.lightModel.diffs.game.handDiffOther.HandOtherDiff;
import it.polimi.ingsw.lightModel.diffs.game.handDiffOther.HandOtherDiffAdd;
import it.polimi.ingsw.lightModel.diffs.game.handDiffOther.HandOtherDiffRemove;
import it.polimi.ingsw.lightModel.diffs.game.handDiffs.*;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.GadgetGame;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.cards.CardTable;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.*;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.utilities.Pair;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;

public class GameControllerPublic {
    public GameController gameController;

    public GameControllerPublic(GameController gameController) {
        this.gameController = gameController;
    }

    public CardTable getCardTable() {
        try {
            Field field = gameController.getClass().getDeclaredField("cardTable");
            field.setAccessible(true);
            return (CardTable) field.get(gameController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Game getGame() {
        try {
            Field field = gameController.getClass().getDeclaredField("game");
            field.setAccessible(true);
            return (Game) field.get(gameController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, ViewInterface> getViewMap() {
        try {
            Field field = gameController.getClass().getDeclaredField("playerViewMap");
            field.setAccessible(true);
            return (Map<String, ViewInterface>) field.get(gameController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
