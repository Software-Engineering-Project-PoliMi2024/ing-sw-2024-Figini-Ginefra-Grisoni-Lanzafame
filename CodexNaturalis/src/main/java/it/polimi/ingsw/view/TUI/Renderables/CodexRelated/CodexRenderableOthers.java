package it.polimi.ingsw.view.TUI.Renderables.CodexRelated;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCodex;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

public class CodexRenderableOthers extends CodexRenderable{
    private String targetPlayer = null;
    public CodexRenderableOthers(String name, LightGame lightGame, CardMuseum cardMuseum, CommandPrompt[] relatedCommands, ControllerInterface controller) {
        super(name, lightGame, cardMuseum, relatedCommands, controller);
    }

    public void setTargetPlayer(String targetPlayer){
        this.targetPlayer = targetPlayer;
    }

    @Override
    protected LightCodex getCodex(){
        return lightGame.getCodexMap().get(this.targetPlayer);
    }

    @Override
    public void render(){
        PromptStyle.printInABox("Codex of " + targetPlayer, 100);
        super.render();
    }

    @Override
    public void updateCommand(CommandPromptResult answer) {
        switch (answer.getCommand()){
            case CommandPrompt.PEEK:
                String nickname = answer.getAnswer(0);
                this.setTargetPlayer(nickname);
                drawCodex();
                this.render();
                break;
        }
    }
}
