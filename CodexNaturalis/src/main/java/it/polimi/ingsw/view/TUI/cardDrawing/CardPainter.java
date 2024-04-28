package it.polimi.ingsw.view.TUI.cardDrawing;

import it.polimi.ingsw.model.cardReleted.cards.CardWithCorners;
import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.TUI.Renderables.drawables.CardRenderable;
import it.polimi.ingsw.view.TUI.Renderables.drawables.Drawable;
import it.polimi.ingsw.view.TUI.Styles.CardTextStyle;

import java.util.Map;
import java.util.Set;

public class CardPainter {
    private static void drawRequirements(GoldCard card, Drawable drawable){
        Map<Resource, Integer> requirements = card.getRequirements();

        if(requirements.isEmpty())
            return;

        int n = requirements.keySet().stream().mapToInt(r -> requirements.get(r) != 0 ? 1 : 0).sum();
        int y = drawable.getHeight() - 2;
        int x = (drawable.getWidth() - n) / 2;

        for(Resource resource : requirements.keySet()){
            if(requirements.get(resource) == 0)
                continue;

            drawable.addContent(CardTextStyle.getCollectableEmoji(resource), x, y);
            drawable.addContent(CardTextStyle.getNumberEmoji(requirements.get(resource)), x++, y + 1);
            x += 1 - n%2;
        }
    }
    private static void drawMultiplier(GoldCard card, Drawable drawable){
        if(card.getGoldCardPointMultiplier() != null){
            String multiplierEmojii = card.getGoldCardPointMultiplier().getTarget() == null ?
                    CardTextStyle.getCoveredCornerMultiplierEmoji() :
                    CardTextStyle.getCollectableEmoji(card.getGoldCardPointMultiplier().getTarget());

            drawable.addContent(multiplierEmojii, drawable.getWidth()/2, 1);
        }
    }

    private static void drawPoints(CardWithCorners card, Drawable drawable){
        if(card.getPoints() != 0)
            drawable.addContent(CardTextStyle.getNumberEmoji(card.getPoints()), drawable.getWidth()/2, 0);
    }

    private static void drawPermanentResources(CardWithCorners card, Drawable drawable){
        if(card.getPermanentResources(CardFace.BACK).isEmpty())
            return;

        int n = card.getPermanentResources(CardFace.BACK).size();
        int y = drawable.getHeight() / 2;
        int x = (drawable.getWidth() - n) / 2;

        for(Resource resource : card.getPermanentResources(CardFace.BACK)){
            drawable.addContent(CardTextStyle.getCollectableEmoji(resource), x++, y);
            x += 1 - n%2;
        }
    }

    private static void drawCorners(CardWithCorners card, CardFace face, Drawable drawable){
        for(CardCorner corner : CardCorner.values()){
            if(card.isCorner(corner, face) && card.getCollectableAt(corner, face) != null){
                String collectableEmoji = CardTextStyle.getCollectableEmoji(card.getCollectableAt(corner, face));

                String cornerFiller = CardTextStyle.getCornerFiller();

                Position offset = corner.getOffset();

                Position innerCornerOffset = new Position(
                        1 + (offset.getX() + 1) / 2 * (CardTextStyle.getCardWidth() - 3),
                        1 + (-offset.getY() + 1) / 2 * (CardTextStyle.getCardHeight() - 3)
                );

                drawable.addContent(collectableEmoji, innerCornerOffset.getX(), innerCornerOffset.getY());
                drawable.addContent(cornerFiller, innerCornerOffset.getX() + offset.getX(), innerCornerOffset.getY());
                drawable.addContent(cornerFiller, innerCornerOffset.getX(), innerCornerOffset.getY() - offset.getY());
                drawable.addContent(cornerFiller, innerCornerOffset.getX() + offset.getX(), innerCornerOffset.getY() -offset.getY());
            }
        }
    }

    private static void drawBasicFront(CardWithCorners card, String filler, Drawable drawable){
        //Fill the background
        drawable.fillContent(filler);

        //Draw the corners
        drawCorners(card, CardFace.FRONT, drawable);

        //Draw the points
        drawPoints(card, drawable);
    }

    private static void drawBasicBack(CardWithCorners card, String filler, Drawable drawable){
        //Fill the background
        drawable.fillContent(filler);

        //Draw the corners
        drawCorners(card, CardFace.BACK, drawable);

        //Draw the permanent resources
        drawPermanentResources(card, drawable);
    }

    public static TextCard drawResourceCard(ResourceCard card){
        //Draw the front
        Drawable front = new Drawable(CardTextStyle.getCardWidth(), CardTextStyle.getCardHeight());

        String bg_filler = CardTextStyle.getResourceFilling(card.getPermanentResources(CardFace.FRONT).stream().findFirst().orElse(null));

        drawBasicFront(card, bg_filler, front);


        //Draw the back
        Drawable back = new Drawable(CardTextStyle.getCardWidth(), CardTextStyle.getCardHeight());

        drawBasicBack(card, bg_filler, back);

        return new TextCard(front, back);
    }

    public static TextCard drawGoldCard(GoldCard card){
        //Draw the front
        Drawable front = new Drawable(CardTextStyle.getCardWidth(), CardTextStyle.getCardHeight());

        String bg_filler = CardTextStyle.getResourceFilling(card.getPermanentResources(CardFace.FRONT).stream().findFirst().orElse(null));

        drawBasicFront(card, bg_filler, front);

        drawMultiplier(card, front);

        drawRequirements(card, front);

        //Draw the back
        Drawable back = new Drawable(CardTextStyle.getCardWidth(), CardTextStyle.getCardHeight());

        drawBasicBack(card, bg_filler, back);

        return new TextCard(front, back);
    }

    public static  TextCard drawStartCard(StartCard card){
        //Draw the front
        Drawable front = new Drawable(CardTextStyle.getCardWidth(), CardTextStyle.getCardHeight());

        String bg_filler = CardTextStyle.getStartFilling();

        drawBasicFront(card, bg_filler, front);

        //Draw the back
        Drawable back = new Drawable(CardTextStyle.getCardWidth(), CardTextStyle.getCardHeight());

        drawBasicBack(card, bg_filler, back);

        return new TextCard(front, back);
    }
}
