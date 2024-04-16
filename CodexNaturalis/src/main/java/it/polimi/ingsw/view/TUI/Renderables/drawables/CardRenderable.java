package it.polimi.ingsw.view.TUI.Renderables.drawables;

import it.polimi.ingsw.model.cardReleted.cards.CardWithCorners;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.TUI.Renderables.Styles.CardTextStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

public class CardRenderable extends Drawable {

    private final CardWithCorners targetCard;
    private final CardFace face;
    private final String filler;

    public CardRenderable(CardWithCorners targetCard, CardFace face, CommandPrompt[] relatedCommands) {
        super(CardTextStyle.getCardWidth(), CardTextStyle.getCardHeight(), relatedCommands);
        this.targetCard = targetCard;
        this.face = face;
        this.filler = CardTextStyle.getResourceFilling(targetCard.getPermanentResources(face).stream().findFirst().orElse(null));
        update();
    }

    @Override
    public void update() {
        this.fillContent(filler);

        //Draw the border of the card
        for(int i = 0; i < CardTextStyle.getCardWidth(); i++){
            this.addContent(CardTextStyle.getBorder(), i, 0);
            this.addContent(CardTextStyle.getBorder(), i, CardTextStyle.getCardHeight() - 1);
        }

        for(int i = 1; i < CardTextStyle.getCardHeight() - 1; i++){
            this.addContent(CardTextStyle.getBorder(), 0, i);
            this.addContent(CardTextStyle.getBorder(), CardTextStyle.getCardWidth() - 1, i);
        }

        //Draw the corners
        for(CardCorner corner : CardCorner.values()){
            String collectableEmoji =
                    targetCard.getCollectableAt(corner, face) == null ?
                            filler :
                            CardTextStyle.getCollectableEmoji(targetCard.getCollectableAt(corner, face));

            Position offset = corner.getOffset();

            Position innerCornerOffset = new Position(
                    1 + (offset.getX() + 1) / 2 * (CardTextStyle.getCardWidth() - 3),
                    1 + (offset.getY() + 1) / 2 * (CardTextStyle.getCardHeight() - 3)
            );

            this.addContent(collectableEmoji, innerCornerOffset.getX(), innerCornerOffset.getY());
        }

        //Draw the points
        if(targetCard.getPoints() != 0)
            this.addContent(CardTextStyle.getNumberEmoji(targetCard.getPoints()), getWidth()/2, 0);
        else
            this.addContent(CardTextStyle.getBorder(), getWidth()/2, 0);
    }

    @Override
    public void updateInput(String input) {
        // Do nothing
    }
}
