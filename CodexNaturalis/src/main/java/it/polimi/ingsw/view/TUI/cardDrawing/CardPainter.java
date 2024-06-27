package it.polimi.ingsw.view.TUI.cardDrawing;

import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.CollectableCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.DiagonalCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.LCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.TUI.Renderables.drawables.Drawable;
import it.polimi.ingsw.view.TUI.Styles.CardTextStyle;

import java.util.Map;

/**
 * This class is a painter of cards. It can draw a card on a Drawable.
 */
public class CardPainter {
    /**
     * Draws the requirements of a card. That being the resources needed to place a gold card.
     * @param card The card to draw.
     * @param drawable The drawable where to draw the card.
     */
    private static void drawRequirements(GoldCard card, Drawable drawable){
        //Get the requirements
        Map<Resource, Integer> requirements = card.getRequirements();

        //If there are no requirements, return
        if(requirements.isEmpty())
            return;

        //Count the number of requirements
        int n = requirements.keySet().stream().mapToInt(r -> requirements.get(r) != 0 ? 1 : 0).sum();

        //Draw the requirements
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

    /**
     * Draws the multiplier of a card. That being what the point of the cards are multiplied by when scoring.
     * @param card The card to draw.
     * @param drawable The drawable where to draw the multiplier.
     */
    private static void drawMultiplier(GoldCard card, Drawable drawable){
        if(card.getGoldCardPointMultiplier() != null){
            String multiplierEmojii = card.getGoldCardPointMultiplier().getTarget() == null ?
                    CardTextStyle.getCoveredCornerMultiplierEmoji() :
                    CardTextStyle.getCollectableEmoji(card.getGoldCardPointMultiplier().getTarget());

            drawable.addContent(multiplierEmojii, drawable.getWidth()/2, 1);
        }
    }

    /**
     * Draws the points of a card.
     * @param card The card to draw.
     * @param drawable The drawable where to draw the points.
     */
    private static void drawPoints(CardWithCorners card, Drawable drawable){
        if(card.getPoints() != 0)
            drawable.addContent(CardTextStyle.getNumberEmoji(card.getPoints()), drawable.getWidth()/2, 0);
    }

    /**
     * Draws the permanent resources of a card. That being the resources that are always on the card.
     * @param card The card to draw.
     * @param drawable The drawable where to draw the permanent resources.
     */
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

    /**
     * Draws the collectable multiplier of a gold card. That being which collectable whose number counts as multiplier.
     * @param multiplier The multiplier to draw.
     * @param drawable The drawable where to draw the multiplier.
     */
    private static void drawCollectableMultiplier(CollectableCardPointMultiplier multiplier, Drawable drawable){
        if(multiplier != null){
            int n = multiplier.getTargets().values().stream().mapToInt(i -> i).sum();
            int y = drawable.getHeight() / 2+1;
            int x = (drawable.getWidth() - n) / 2;

            for(Collectable collectable : multiplier.getTargets().keySet()){
                if(multiplier.getTargets().get(collectable) == 0)
                    continue;
                for(int i = 0; i < multiplier.getTargets().get(collectable); i++) {
                    drawable.addContent(CardTextStyle.getCollectableEmoji(collectable), x++, y);
                    x += 1 - n % 2;
                }
            }
        }
    }

    /**
     * Draws the diagonal multiplier of a gold card. That being a diagonal line of a resource that counts as multiplier.
     * @param multiplier The multiplier to draw.
     * @param drawable The drawable where to draw the multiplier.
     */
    private static void drawDiagonalMultiplier(DiagonalCardPointMultiplier multiplier, Drawable drawable) {
        if (multiplier != null) {
            Resource resource = multiplier.getColor();
            boolean upwards = multiplier.isUpwards();
            int y = drawable.getHeight() / 2;
            int x = (drawable.getWidth() / 2) - 1 - 2;
            int length = 3;
            String emoji = CardTextStyle.getResourceFilling(resource);

            if (upwards) {
                for (int i = 0; i < length; i++) {
                    int col = x + i;
                    int row = y - 1 + i;
                    drawable.addContent(emoji, col, row);
                }
            } else {
                for (int i = 0; i < length; i++) {
                    int col = x + i;
                    int row = y + 1 - i;
                    drawable.addContent(emoji, col, row);
                }
            }
        }
    }

    /**
     * Draws the L multiplier of a gold card. That being an L shape of a resource that counts as multiplier.
     * @param multiplier The multiplier to draw.
     * @param drawable The drawable where to draw the multiplier.
     */
    private static void drawLMultiplier(LCardPointMultiplier multiplier, Drawable drawable) {
        if (multiplier != null) {
            CardCorner corner = multiplier.corner();
            Resource singleResource = multiplier.singleResource();
            Resource doubleResource = multiplier.doubleResource();
            int y = drawable.getHeight() / 2;
            int x = drawable.getWidth() / 2 - 2;
            int length = 2;
            String emoji = CardTextStyle.getResourceFilling(singleResource);
            String emoji2 = CardTextStyle.getResourceFilling(doubleResource);

            if ( corner == CardCorner.TL) {
                drawable.addContent(emoji, x, y-1);
                int col = x - 1 ;
                for (int i = 0; i < length; i++) {
                    int row = y + i;
                    drawable.addContent(emoji2, col, row);
                }
            } else if ( corner == CardCorner.TR) {
                drawable.addContent(emoji, x, y-1);
                int col = x + 1 ;
                for (int i = 0; i < length; i++) {
                    int row = y + i;
                    drawable.addContent(emoji2, col, row);
                }
            }else if ( corner == CardCorner.BR) {
                drawable.addContent(emoji, x, y+1);
                int col = x + 1 ;
                for (int i = 0; i < length; i++) {
                    int row = y - i;
                    drawable.addContent(emoji2, col, row);
                }
            }else { //BL
                drawable.addContent(emoji, x, y+1);
                int col = x - 1 ;
                for (int i = 0; i < length; i++) {
                    int row = y - i;
                    drawable.addContent(emoji2, col, row);
                }
            }
        }
    }

    /**
     * Draws the corners of a card. That being the collectables placed in the corners.
     * @param card The card to draw.
     * @param face The face of the card to draw.
     * @param drawable The drawable where to draw the corners.
     */
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

    /**
     * Draws the common elements of a card front. That being the background, the corners and the points.
     * @param card The card to draw.
     * @param filler The filler to use.
     * @param drawable The drawable where to draw the common elements.
     */
    private static void drawBasicFront(CardWithCorners card, String filler, Drawable drawable){
        //Fill the background
        drawable.fillContent(filler);

        //Draw the corners
        drawCorners(card, CardFace.FRONT, drawable);

        //Draw the points
        drawPoints(card, drawable);
    }

    /**
     * Draws the common elements of a card back.
     * @param card The card to draw.
     * @param filler The filler to use.
     * @param drawable The drawable where to draw the back.
     */
    private static void drawBasicBack(CardWithCorners card, String filler, Drawable drawable){
        //Fill the background
        drawable.fillContent(filler);

        //Draw the corners
        drawCorners(card, CardFace.BACK, drawable);

        //Draw the permanent resources
        drawPermanentResources(card, drawable);
    }

    /**
     * Draws a resource card.
     * @param card The card to draw.
     * @return The drawable where to draw the resource card.
     */
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

    /**
     * Draws a gold card.
     * @param card The card to draw.
     * @return The drawable where to draw the gold card.
     */
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

    /**
     * Draws a start card.
     * @param card The card to draw.
     * @return The drawable where to draw the start card.
     */
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

    /**
     * Draws a frontier card.
     * @param number The number to draw.
     * @return The drawable where to draw the frontier card.
     */
    public static Drawable drawFrontierCard(int number){
        Drawable drawable = new Drawable(CardTextStyle.getCardWidth(), CardTextStyle.getCardHeight());

        //Draw the borders
        for(int i = 0; i < drawable.getWidth(); i++){
            drawable.addContent(CardTextStyle.getFrontierFilling(), i, 0);
            drawable.addContent(CardTextStyle.getFrontierFilling(), i, drawable.getHeight() - 1);
        }

        for(int i = 0; i < drawable.getHeight(); i++){
            drawable.addContent(CardTextStyle.getFrontierFilling(), 0, i);
            drawable.addContent(CardTextStyle.getFrontierFilling(), drawable.getWidth() - 1, i);
        }

        //Brake down the number in digits
        int[] digits = new int[3];
        for(int i = 0; i < 3; i++){
            digits[i] = number % 10;
            number /= 10;
        }

        //Draw the digits
        for(int i = 0; i < 3; i++){
            drawable.addContent(CardTextStyle.getNumberEmoji(digits[i]), drawable.getWidth() / 2 + 1 - i, drawable.getHeight() / 2);
        }

        return drawable;
    }

    /**
     * Draws an objective card with a collectable multiplier.
     * @param card The card to draw.
     * @return The drawable where to draw the objective card.
     */
    public static TextCard drawObjectiveCardCollectableMultiplier(ObjectiveCard card, CollectableCardPointMultiplier multiplier){
        Drawable drawable = new Drawable(CardTextStyle.getCardWidth(), CardTextStyle.getCardHeight());

        String bg_filler = CardTextStyle.getStartFilling();

        //Fill the background
        drawable.fillContent(bg_filler);

        //Draw the equal sign
        drawable.addContent(CardTextStyle.getEqualEmoji(), drawable.getWidth() / 2, drawable.getHeight() / 2);

        //Draw the points
        drawable.addContent(CardTextStyle.getNumberEmoji(card.getPoints()), drawable.getWidth() / 2, drawable.getHeight() / 2 - 1);

        //Draw the multiplier
        drawCollectableMultiplier(multiplier, drawable);

        return new TextCard(drawable, drawable);
    }

    /**
     * Draws an objective card with a diagonal multiplier.
     * @param card The card to draw.
     * @return The drawable where to draw the objective card.
     */
    public static TextCard drawObjectiveCardDiagonalMultiplier(ObjectiveCard card, DiagonalCardPointMultiplier multiplier){
        Drawable drawable = new Drawable(CardTextStyle.getCardWidth(), CardTextStyle.getCardHeight());

        String bg_filler = CardTextStyle.getStartFilling();

        //Fill the background
        drawable.fillContent(bg_filler);

        //Draw the Equal sign
        drawable.addContent(CardTextStyle.getEqualEmoji(), drawable.getWidth() / 2, drawable.getHeight() / 2);

        //Draw the points
        drawable.addContent(CardTextStyle.getNumberEmoji(card.getPoints()), drawable.getWidth() / 2 + 2, drawable.getHeight() / 2);

        //Draw the multiplier
        drawDiagonalMultiplier(multiplier, drawable);

        return new TextCard(drawable, drawable);
    }

    /**
     * Draws an objective card with an L multiplier.
     * @param card The card to draw.
     * @return The drawable where to draw the objective card.
     */
    public static TextCard drawObjectiveCardLMultiplier(ObjectiveCard card, LCardPointMultiplier multiplier){
        Drawable drawable = new Drawable(CardTextStyle.getCardWidth(), CardTextStyle.getCardHeight());

        String bg_filler = CardTextStyle.getStartFilling();

        //Fill the background
        drawable.fillContent(bg_filler);

        //Draw the Equal sign
        drawable.addContent(CardTextStyle.getEqualEmoji(), drawable.getWidth() / 2, drawable.getHeight() / 2);

        //Draw the points
        drawable.addContent(CardTextStyle.getNumberEmoji(card.getPoints()), drawable.getWidth() / 2 + 2, drawable.getHeight() / 2);

        //Draw the multiplier
        drawLMultiplier(multiplier, drawable);

        return new TextCard(drawable, drawable);
    }

}
