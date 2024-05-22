package it.polimi.ingsw.model.cardReleted.pointMultiplyer;

import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.*;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.model.playerReleted.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CoveredCornersCardPointMultiplierTest {

    private Codex codex;
    private CoveredCornersCardPointMultiplier multiplier;

    private GoldCard card;

    @BeforeEach
    void setUp() {
        codex = new Codex();
        multiplier = new CoveredCornersCardPointMultiplier();
        card = new GoldCard(1, 1, 1, new HashMap<>(), multiplier, new HashMap<>(), Resource.PLANT);
    }

    @Test
    void getMultiplier_correctCountX1TR() {
        ResourceCard card1 = new ResourceCard(1, 1, Resource.FUNGI, 0, Map.of(
                CardCorner.TR, SpecialCollectable.EMPTY
        ));
        codex.playCard(new Placement(new Position(0, 0), card1, CardFace.FRONT));
        codex.playCard(new Placement(new Position(1, 1), card, CardFace.FRONT));

        assertEquals(1, multiplier.getMultiplier(codex, card));
    }

    @Test
    void getMultiplier_correctCountX1BL() {
        ResourceCard card1 = new ResourceCard(1, 1, Resource.FUNGI, 0, Map.of(
                CardCorner.BL, SpecialCollectable.EMPTY
        ));
        codex.playCard(new Placement(new Position(0, 0), card1, CardFace.FRONT));
        codex.playCard(new Placement(new Position(-1, -1), card, CardFace.FRONT));

        assertEquals(1, multiplier.getMultiplier(codex, card));
    }

    @Test
    void getMultiplier_correctCountX2TRBL() {
        ResourceCard card1 = new ResourceCard(1, 1, Resource.FUNGI, 0, Map.of(
                CardCorner.BL, SpecialCollectable.EMPTY,
                CardCorner.BR, SpecialCollectable.EMPTY,
                CardCorner.TL, SpecialCollectable.EMPTY,
                CardCorner.TR, SpecialCollectable.EMPTY
        ));
        codex.playCard(new Placement(new Position(0, 0), card1, CardFace.FRONT));
        codex.playCard(new Placement(new Position(1, 1), card1, CardFace.FRONT));
        codex.playCard(new Placement(new Position(1, -1), card1, CardFace.FRONT));
        codex.playCard(new Placement(new Position(2, 0), card, CardFace.FRONT));

        assertEquals(2, multiplier.getMultiplier(codex, card));
    }
}