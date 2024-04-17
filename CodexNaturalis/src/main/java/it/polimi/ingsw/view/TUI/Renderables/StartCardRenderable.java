package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.model.cardReleted.cards.StartCard;

public class StartCardRenderable extends Renderable {
    private final StartCard startCard;
    private String selectStartCardFace = "";

    public StartCardRenderable(StartCard startCard) {
        super(null);
        this.startCard = startCard;
    }

    @Override
    public void render() {
        System.out.println("You have received a start card:");
        System.out.println(startCard);
        System.out.println("Choose which face of the card should be facing upwards:");
        System.out.println("1: FRONT");
        System.out.println("2: BACK");
    }

    @Override
    public void update() {
        // re-render or clear the field
        render();
    }

    @Override
    public void updateInput(String input) {
        this.selectStartCardFace = input.trim();
    }
}