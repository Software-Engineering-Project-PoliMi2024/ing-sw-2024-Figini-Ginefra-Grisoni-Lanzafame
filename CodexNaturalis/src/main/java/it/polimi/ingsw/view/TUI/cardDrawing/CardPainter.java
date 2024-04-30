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
