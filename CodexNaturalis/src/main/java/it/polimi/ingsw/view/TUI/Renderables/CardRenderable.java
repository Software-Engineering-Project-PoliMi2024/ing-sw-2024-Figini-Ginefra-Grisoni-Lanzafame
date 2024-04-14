package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.model.cardReleted.*;

public class CardRenderable extends Renderable{

    private final CardWithCorners targetCard;
    private final CardFace face;

    private final String filler;

    public CardRenderable(CardWithCorners targetCard, CardFace face){
        super(CardTextStyle.getCardWidth(), CardTextStyle.getCardHeight());
        this.targetCard = targetCard;
        this.face = face;
        this.filler = CardTextStyle.getResourceFilling(targetCard.getPermanentResources(face).stream().findFirst().orElse(null));
        update();
    }

    @Override
    public void render() {
        this.getContent().forEach(System.out::println);
    }

    @Override
    public void update() {
        this.clearContent();

        //Print first Line meant to be a border
        String firstLine = CardTextStyle.getCollectableEmoji(targetCard.getCollectableAt(CardCorner.TL, face)) +
                String.valueOf(CardTextStyle.getBorder()).repeat((CardTextStyle.getCardWidth() - 2) / 2) +
                ((targetCard.getPoints() != 0) ? CardTextStyle.getNumberEmoji(targetCard.getPoints()) : CardTextStyle.getBorder()) +
                String.valueOf(CardTextStyle.getBorder()).repeat((CardTextStyle.getCardWidth() - 2) / 2) +
                CardTextStyle.getCollectableEmoji(targetCard.getCollectableAt(CardCorner.TR, face));
        this.addContent(firstLine);

        String secondLine = CardTextStyle.getBorder() +
                ((targetCard.getCollectableAt(CardCorner.TL, face) == null) ? filler : CardTextStyle.getBorder()) +
                String.valueOf(filler).repeat((CardTextStyle.getCardWidth() - 4) / 2) +
                ((targetCard.getCollectableAt(CardCorner.TR, face) == null) ? filler : CardTextStyle.getBorder())+
                String.valueOf(filler).repeat((CardTextStyle.getCardWidth() - 4) / 2) +
                ((targetCard.getCollectableAt(CardCorner.TR, face) == null) ? filler : CardTextStyle.getBorder())+
                CardTextStyle.getBorder();
        this.addContent(secondLine);

        for(int i = 0; i < (CardTextStyle.getCardHeight() - 4); i++){
            this.addContent(
                    CardTextStyle.getBorder() +
                            String.valueOf(filler).repeat(CardTextStyle.getCardWidth() - 2) +
                            CardTextStyle.getBorder()
            );
        }

        String penultimateLine = CardTextStyle.getBorder() +
                ((targetCard.getCollectableAt(CardCorner.BL, face) == null) ? filler : CardTextStyle.getBorder()) +
                String.valueOf(filler).repeat(CardTextStyle.getCardWidth() - 4) +
                ((targetCard.getCollectableAt(CardCorner.BR, face) == null) ? filler : CardTextStyle.getBorder())+
                CardTextStyle.getBorder();
        this.addContent(penultimateLine);

        String lastLine = CardTextStyle.getCollectableEmoji(targetCard.getCollectableAt(CardCorner.BL, face)) +
                String.valueOf(CardTextStyle.getBorder()).repeat((CardTextStyle.getCardWidth() - 2)) +
                CardTextStyle.getCollectableEmoji(targetCard.getCollectableAt(CardCorner.BR, face));
        this.addContent(lastLine);
    }

    @Override
    public void updateInput(String input) {
        // Do nothing
    }
}
