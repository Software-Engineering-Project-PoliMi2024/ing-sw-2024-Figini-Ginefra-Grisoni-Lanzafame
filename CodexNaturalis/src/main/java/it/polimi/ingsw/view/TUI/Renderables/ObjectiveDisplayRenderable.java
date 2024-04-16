package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import java.util.List;

public class ObjectiveDisplayRenderable extends Renderable {
    private final List<ObjectiveCard> objectiveCards;
    private String choseSecretObjective = "";

    public ObjectiveDisplayRenderable(List<ObjectiveCard> objectiveCards) {
        super(null);
        this.objectiveCards = objectiveCards;
    }

    @Override
    public void render() {
        System.out.println("Available Objective Cards:");
        for (int i = 0; i < objectiveCards.size(); i++) {
            System.out.println((i + 1) + ". " + objectiveCards.get(i).toString());
        }
    }

    @Override
    public void update() {
        // re-render or clear the field
        render();
    }

    @Override
    public void updateInput(String input) {
        this.choseSecretObjective = input.trim();
    }
}