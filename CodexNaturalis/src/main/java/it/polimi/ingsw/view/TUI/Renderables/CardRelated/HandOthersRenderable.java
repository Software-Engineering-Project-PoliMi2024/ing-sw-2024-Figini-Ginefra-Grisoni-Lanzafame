package it.polimi.ingsw.view.TUI.Renderables.CardRelated;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightHandOthers;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.TUI.Styles.CardTextStyle;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

/**
 * This class is a Renderable that can render the hand of players who are not the main player.
 */
public class HandOthersRenderable extends CardRenderable{
    /** The nickname of the player whose hand is being rendered. */
    private String targetPlayer = null;

    /**
     * Creates a new HandOthersRenderable.
     * @param name The name of the renderable.
     * @param museum The card museum to use.
     * @param game The lightGame to render.
     * @param relatedCommands The commands related to this renderable.
     * @param view The controller provider.
     */
    public HandOthersRenderable(String name, CardMuseum museum, LightGame game, CommandPrompt[] relatedCommands, ControllerProvider view){
        super(name, museum, game, CardFace.FRONT, relatedCommands, view);

    }

    /**
     * Sets the target player.
     * @param targetPlayer The target player.
     */
    private void setTargetPlayer(String targetPlayer){
        this.targetPlayer = targetPlayer;
    }

    /**
     * Renders the hand of the target player.
     */
    @Override
    public void render(){
        PromptStyle.printInABox("Hand of " + targetPlayer, CardTextStyle.getCardWidth() * 3);
        LightHandOthers otherHand = getLightGame().getHandOthers().get(this.targetPlayer);
        for(int i = 0; i < 3; i++){
            LightBack card = otherHand.getCards()[i];
            if(card == null){
                continue;
            }

            Printer.print(getMuseum().getBackFromId(card.idBack()).toString());
            Printer.println("");
        }
    }

    /**
     * Updates the renderable based on the command prompt result.
     * @param answer The command prompt result.
     */
    @Override
    public void updateCommand(CommandPromptResult answer){
        switch (answer.getCommand()){
            case CommandPrompt.PEEK:
                String nickname = answer.getAnswer(0);
                if(nickname.equals(this.getLightGame().getLightGameParty().getYourName())){
                    return;
                }

                if(!getLightGame().getCodexMap().containsKey(nickname)){
                    return;
                }

                this.setTargetPlayer(nickname);
                this.render();
                break;
        }
    }

}
