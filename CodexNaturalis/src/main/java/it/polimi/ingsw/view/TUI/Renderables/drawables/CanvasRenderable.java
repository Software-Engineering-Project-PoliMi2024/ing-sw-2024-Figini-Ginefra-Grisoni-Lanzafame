package it.polimi.ingsw.view.TUI.Renderables.drawables;

import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.TUI.Styles.CardTextStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

public class CanvasRenderable extends Renderable {
    private final Canvas canvas;
    public CanvasRenderable(int width, int height, CommandPrompt[] relatedCommands) {
        super(relatedCommands);
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
