package it.polimi.ingsw.model.cardReleted.pointMultiplyer;

import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.cardReleted.utilityEnums.SpecialCollectable;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.model.playerReleted.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DiagonalCardPointMultiplierTest {

    private Codex codex;
    private DiagonalCardPointMultiplier multiplier;
    @BeforeEach
    void setUp() {
        codex = new Codex();
    }

    @Test
    void getMultiplier_correctCountX1_upwards() {
        multiplier = new DiagonalCardPointMultiplier(true, Resource.FUNGI);
        ResourceCard card1 = new ResourceCard(1, 1, Resource.FUNGI, 0, Map.of(
                CardCorner.BL, SpecialCollectable.EMPTY,
                CardCorner.BR, SpecialCollectable.EMPTY,
                CardCorner.TL, SpecialCollectable.EMPTY,
                CardCorner.TR, SpecialCollectable.EMPTY
        ));

        ResourceCard card2 = new ResourceCard(1, 1, Resource.PLANT, 0, Map.of(
                CardCorner.BL, SpecialCollectable.EMPTY,
                CardCorner.BR, SpecialCollectable.EMPTY,
                CardCorner.TL, SpecialCollectable.EMPTY,
                CardCorner.TR, SpecialCollectable.EMPTY
        ));

        codex.playCard(new Placement(new Position(0, 0), card1, CardFace.FRONT));
        codex.playCard(new Placement(new Position(1, 1), card1, CardFace.FRONT));
        codex.playCard(new Placement(new Position(2, 2), card1, CardFace.FRONT));
        codex.playCard(new Placement(new Position(3, 3), card2, CardFace.FRONT));

        assertEquals(1, multiplier.getMultiplier(codex));
    }

    @Test
    void getMultiplier_correctCountX1_downwards() {
        multiplier = new DiagonalCardPointMultiplier(false, Resource.FUNGI);

        ResourceCard card1 = new ResourceCard(1, 1, Resource.FUNGI, 0, Map.of(
                CardCorner.BL, SpecialCollectable.EMPTY,
                CardCorner.BR, SpecialCollectable.EMPTY,
                CardCorner.TL, SpecialCollectable.EMPTY,
                CardCorner.TR, SpecialCollectable.EMPTY
        ));

        codex.playCard(new Placement(new Position(0, 0), card1, CardFace.FRONT));
        codex.playCard(new Placement(new Position(1, -1), card1, CardFace.FRONT));
        codex.playCard(new Placement(new Position(2, -2), card1, CardFace.FRONT));

        assertEquals(1, multiplier.getMultiplier(codex));
    }

    @Test
    void getMultiplier_correctCountX1_downwards_conflict() {
        multiplier = new DiagonalCardPointMultiplier(false, Resource.FUNGI);

        ResourceCard card1 = new ResourceCard(1, 1, Resource.FUNGI, 0, Map.of(
                CardCorner.BL, SpecialCollectable.EMPTY,
                CardCorner.BR, SpecialCollectable.EMPTY,
                CardCorner.TL, SpecialCollectable.EMPTY,
                CardCorner.TR, SpecialCollectable.EMPTY
        ));

        ResourceCard card2 = new ResourceCard(1, 1, Resource.PLANT, 0, Map.of(
                CardCorner.BL, SpecialCollectable.EMPTY,
                CardCorner.BR, SpecialCollectable.EMPTY,
                CardCorner.TL, SpecialCollectable.EMPTY,
                CardCorner.TR, SpecialCollectable.EMPTY
        ));

        codex.playCard(new Placement(new Position(0, 0), card1, CardFace.FRONT));
        codex.playCard(new Placement(new Position(-1, 1), card1, CardFace.FRONT));
        codex.playCard(new Placement(new Position(1, -1), card1, CardFace.FRONT));
        codex.playCard(new Placement(new Position(-2, 2), card1, CardFace.FRONT));
        codex.playCard(new Placement(new Position(1, 1), card2, CardFace.FRONT));
        codex.playCard(new Placement(new Position(2, 2), card1, CardFace.FRONT));
        codex.playCard(new Placement(new Position(2, -2), card1, CardFace.FRONT));
        codex.playCard(new Placement(new Position(3, -3), card2, CardFace.FRONT));
        codex.playCard(new Placement(new Position(-3, 3), card1, CardFace.FRONT));


        assertEquals(2, multiplier.getMultiplier(codex));
    }

}