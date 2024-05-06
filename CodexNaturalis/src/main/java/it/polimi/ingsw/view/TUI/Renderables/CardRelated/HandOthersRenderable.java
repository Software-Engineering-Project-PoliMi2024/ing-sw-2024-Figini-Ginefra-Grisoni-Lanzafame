package it.polimi.ingsw.view.TUI.Renderables.CardRelated;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightHandOthers;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
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
     * @param controller The controller to interact with.
     */
    public HandOthersRenderable(String name, CardMuseum museum, LightGame game, CommandPrompt[] relatedCommands, ControllerInterface controller){
        super(name, museum, game, CardFace.FRONT, relatedCommands, controller);
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
            Resource resource = otherHand.getCards()[i];
            if(resource == null){
                continue;
            }

            Printer.print(getMuseum().getResourceBack(resource).toString());
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
                this.setTargetPlayer(nickname);
                this.render();
                break;
        }
    }

}
