package it.polimi.ingsw.view.GUI.Components.CardRelated;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;

import java.util.function.Consumer;

/**
 * This class represents a card that can be flipped by the player and that can be placed in the codex
 */
public class FlippablePlayableCard extends FlippableCardGUI {
    /** true if the card is playable, false otherwise */
    private boolean playable;

    /**
     * Creates a new FlippablePlayableCard.
     * @param target the card that this GUI represents.
     * @param playable true if the card is playable, false otherwise.
     */
    public FlippablePlayableCard(LightCard target, boolean playable) {
        super(target);
        this.setPlayable(playable);
    }

    /**
     * Returns true if the card is playable, false otherwise.
     * @return true if the card is playable, false otherwise.
     */
    public boolean isPlayable() {
        return playable;
    }

    /**
     * Sets the card as playable or not.
     * @param playable true if the card is playable, false otherwise.
     */
    public void setPlayable(boolean playable) {
        this.playable = playable;
        this.update();
    }

    /**
     * Updates the card according to its state.
     */
    @Override
    public void update() {
        if(this.getFace() == CardFace.FRONT)
            this.getImageView().setOpacity(playable ? 1 : 0.5);
        else
            this.getImageView().setOpacity(1);

        super.update();
    }

    /**
     * Adds this card to the parent using the given Consumer and then plays the adding animation.
     * @param parentAdder the method that adds this card to the parent.
     */
    public void addThisByFlippablePlayable(Consumer<FlippablePlayableCard> parentAdder){
        parentAdder.accept(this);
        this.playAddingAnimation();
    }

    /**
     * Removes this card from the parent using the given Consumer and then plays the removing animation.
     * @param parentRemover the method that removes this card from the parent.
     */
    public void removeThisByFlippablePlayable(Consumer<FlippablePlayableCard> parentRemover){
        this.removingAnimation.setOnFinished(e -> parentRemover.accept(this));
        this.removingAnimation.play();
    }

}
