package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

public abstract class LightGameRenderable extends Renderable{
    private final LightGame lightGame;
    public LightGameRenderable(String name, CommandPrompt[] relatedCommands, LightGame lightGame, ControllerInterface controller){
        super(name, relatedCommands, controller);
        this.lightGame = lightGame;
    }

    public LightGame getLightGame(){
        return lightGame;
    }
}
