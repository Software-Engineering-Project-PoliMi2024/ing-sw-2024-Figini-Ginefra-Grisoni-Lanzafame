package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.model.cardReleted.*;

import java.util.Map;

public class CardRenderable extends Renderable{

    private final CardWithCorners targetCard;
    private final CardFace face;

    private final String filler;

    public CardRenderable(CardWithCorners targetCard, CardFace face){
        this.targetCard = targetCard;
        this.face = face;
        this.filler = CardTextStyle.getResourceFilling(targetCard.getPermanentResources(face).stream().findFirst().orElse(null));
    }

    @Override
    public void render() {
        //Print first Line meant to be a border
        String firstLine = CardTextStyle.getCollectableEmoji(targetCard.getCollectableAt(CardCorner.TL, face)) +
                String.valueOf(CardTextStyle.getBorder()).repeat((CardTextStyle.getCardWidth() - 2) / 2) +
                ((targetCard.getPoints() != 0) ? CardTextStyle.getNumberEmojii(targetCard.getPoints()) : CardTextStyle.getBorder()) +
                String.valueOf(CardTextStyle.getBorder()).repeat((CardTextStyle.getCardWidth() - 2) / 2) +
                CardTextStyle.getCollectableEmoji(targetCard.getCollectableAt(CardCorner.TR, face));
        System.out.println(firstLine);

        String secondLine = CardTextStyle.getBorder() +
                ((targetCard.getCollectableAt(CardCorner.TL, face) == null) ? filler : CardTextStyle.getBorder()) +
                String.valueOf(filler).repeat((CardTextStyle.getCardWidth() - 4) / 2) +
                ((targetCard.getCollectableAt(CardCorner.TR, face) == null) ? filler : CardTextStyle.getBorder())+
                String.valueOf(filler).repeat((CardTextStyle.getCardWidth() - 4) / 2) +
                ((targetCard.getCollectableAt(CardCorner.TR, face) == null) ? filler : CardTextStyle.getBorder())+
                CardTextStyle.getBorder();

        System.out.println(secondLine);

        String body = (CardTextStyle.getBorder() +
                String.valueOf(filler).repeat(CardTextStyle.getCardWidth() - 2) +
                CardTextStyle.getBorder() + "\n").repeat((CardTextStyle.getCardHeight() - 4));

        System.out.print(body);

        String penultimateLine = CardTextStyle.getBorder() +
                ((targetCard.getCollectableAt(CardCorner.BL, face) == null) ? filler : CardTextStyle.getBorder()) +
                String.valueOf(filler).repeat(CardTextStyle.getCardWidth() - 4) +
                ((targetCard.getCollectableAt(CardCorner.BR, face) == null) ? filler : CardTextStyle.getBorder())+
                CardTextStyle.getBorder();


        System.out.println(penultimateLine);

        String lastLine = CardTextStyle.getCollectableEmoji(targetCard.getCollectableAt(CardCorner.BL, face)) +
                String.valueOf(CardTextStyle.getBorder()).repeat((CardTextStyle.getCardWidth() - 2)) +
                CardTextStyle.getCollectableEmoji(targetCard.getCollectableAt(CardCorner.BR, face));

        System.out.println(lastLine);



    }

    @Override
    public void update() {
        render();
    }

    @Override
    public void updateInput(String input) {
        // Do nothing
    }
}
