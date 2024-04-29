package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.view.TUI.Renderables.drawables.Canvas;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

public class CanvasRenderable extends Renderable {
    protected final Canvas canvas;
    public CanvasRenderable(String name, int width, int height, CommandPrompt[] relatedCommands, ControllerInterface controller) {
        super(name, relatedCommands, controller);
        canvas = new Canvas(width, height);
    }


    @Override
    public void render() {
        String[][] content = canvas.getContent();

        for(int i = 0; i < canvas.getHeight(); i++){
            for(int j = 0; j < canvas.getWidth(); j++){
                System.out.print(content[i][j]);
            }
            System.out.println();
        }
    }
}
