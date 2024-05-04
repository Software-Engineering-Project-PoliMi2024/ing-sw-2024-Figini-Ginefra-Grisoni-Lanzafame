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

public class HandOthersRenderable extends CardRenderable{
    private String targetPlayer = null;

    public HandOthersRenderable(String name, CardMuseum museum, LightGame game, CommandPrompt[] relatedCommands, ControllerInterface controller){
        super(name, museum, game, CardFace.FRONT, relatedCommands, controller);
    }

    public void setTargetPlayer(String targetPlayer){
        this.targetPlayer = targetPlayer;
    }

    @Override
    public void render(){
        PromptStyle.printInABox("Hand of " + targetPlayer, CardTextStyle.getCardWidth() * 3);
        LightHandOthers otherHand = getLightGame().getHandOthers().get(this.targetPlayer);
        for(int i = 0; i < 3; i++){
            Resource resource = otherHand.getCards()[i];
            Printer.print(getMuseum().getResourceBack(resource).toString());
        }
    }

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
