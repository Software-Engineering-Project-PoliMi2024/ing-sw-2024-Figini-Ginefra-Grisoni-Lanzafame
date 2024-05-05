package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

/**
 * This class is the abstract class for all the renderables that are related to the game.
 */
public abstract class LightGameRenderable extends Renderable{
    private final LightGame lightGame;

    /**
     * Creates a new LightGameRenderable.
     * @param name The name of the renderable.
     * @param relatedCommands The commands related to this renderable.
     * @param lightGame The lightGame to render.
     * @param controller The controller to interact with.
     */
    public LightGameRenderable(String name, CommandPrompt[] relatedCommands, LightGame lightGame, ControllerInterface controller){
        super(name, relatedCommands, controller);
        this.lightGame = lightGame;
    }

    /**
     * Returns the lightGame.
     * @return The lightGame.
     */
    public LightGame getLightGame(){
        return lightGame;
    }
}
