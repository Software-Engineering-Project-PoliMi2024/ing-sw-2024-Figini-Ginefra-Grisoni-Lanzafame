package it.polimi.ingsw.view.TUI.Renderables.drawables;

public class Canvas extends Drawable{
    public Canvas(int width, int height) {
        super(width, height);
    }

    public void draw(Drawable drawable, int x, int y){
        String[][] content = drawable.getContent();
        for(int i = 0; i < drawable.getHeight(); i++){
            for(int j = 0; j < drawable.getWidth(); j++){
                this.addContent(content[i][j], x + j, y + i);
            }
        }
    }
}
