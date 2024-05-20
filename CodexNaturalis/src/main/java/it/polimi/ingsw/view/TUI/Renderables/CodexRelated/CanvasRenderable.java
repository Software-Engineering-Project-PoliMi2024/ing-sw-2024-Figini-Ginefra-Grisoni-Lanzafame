package it.polimi.ingsw.view.TUI.Renderables.CodexRelated;

import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.TUI.Renderables.drawables.Canvas;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

/**
 * This class is a Renderable that can render a canvas.
 */
public class CanvasRenderable extends Renderable {
    protected final Canvas canvas;

    /**
     * Creates a new CanvasRenderable.
     * @param name The name of the renderable.
     * @param width The width of the canvas.
     * @param height The height of the canvas.
     * @param relatedCommands The commands related to this renderable.
     * @param view The controller provider.
     */
    public CanvasRenderable(String name, int width, int height, CommandPrompt[] relatedCommands, ControllerProvider view) {
        super(name, relatedCommands, view);
        canvas = new Canvas(width, height);
    }


    /**
     * Renders the canvas.
     */
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
