package it.polimi.ingsw.view.TUI.Renderables.CodexRelated;

import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Printing.Printable;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.TUI.Renderables.drawables.Canvas;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

/**
 * This class is a Renderable that can render a canvas.
 * The canvas is a 2D grid of strings.
 */
public class CanvasRenderable extends Renderable {
    /** The canvas to render. */
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
     * Renders the canvas atomically.
     */
    @Override
    public void render() {
        Printable printable = new Printable("");
        renderToPrintable(printable);
        Printer.print(printable);
    }

    /**
     * Renders the canvas to a printable.
     * @param printable The printable to render to.
     */
    public void renderToPrintable(Printable printable){
        String[][] content = canvas.getContent();

        for(int i = 0; i < canvas.getHeight(); i++){
            for(int j = 0; j < canvas.getWidth(); j++){
                printable.print(content[i][j]);
            }
            printable.println("");
        }
    }
}
