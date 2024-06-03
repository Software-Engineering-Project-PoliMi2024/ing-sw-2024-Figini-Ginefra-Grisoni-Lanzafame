package it.polimi.ingsw.controller4.Interfaces;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.PawnColors;

import java.io.Serializable;
import java.rmi.Remote;

public interface GameControllerInterface extends Serializable {
    void chooseSecretObjective(String nickname, LightCard objectiveCard);
    void choosePawn(String nickname, PawnColors color);
    void place(String nickname, LightPlacement placement);
    void draw(String nickname, DrawableCard deckID, int cardID);
}
