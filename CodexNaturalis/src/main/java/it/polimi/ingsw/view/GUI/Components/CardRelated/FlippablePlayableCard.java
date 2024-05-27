package it.polimi.ingsw.view.GUI.Components.CardRelated;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;

import java.util.function.Consumer;

public class FlippablePlayableCard extends FlippableCardGUI {
    private boolean playable;
    public FlippablePlayableCard(LightCard target, boolean playable) {
        super(target);
        this.setPlayable(playable);
    }

    public boolean isPlayable() {
        return playable;
    }

    public void setPlayable(boolean playable) {
        this.playable = playable;
        this.update();
    }

    @Override
    public void update() {
        if(this.getFace() == CardFace.FRONT)
            this.getImageView().setOpacity(playable ? 1 : 0.5);
        else
            this.getImageView().setOpacity(1);

        super.update();
    }

    public void addThisByFlippablePlayable(Consumer<FlippablePlayableCard> parentAdder){
        parentAdder.accept(this);
        this.playAddingAnimation();
    }

    public void removeThisByFlippablePlayable(Consumer<FlippablePlayableCard> parentRemover){
        this.removingAnimation.setOnFinished(e -> parentRemover.accept(this));
        this.removingAnimation.play();
    }

}
