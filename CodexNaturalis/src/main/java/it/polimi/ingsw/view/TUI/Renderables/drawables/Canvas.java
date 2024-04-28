package it.polimi.ingsw.view.TUI.Renderables.drawables;

public class Canvas extends Drawable{
    public Canvas(int width, int height) {
        super(width, height);
    }

    public void draw(Drawable drawable, int x, int y){
        String[][] content = drawable.getContent();
        x -= drawable.getWidth() / 2;
        y -= drawable.getHeight() / 2;
        for(int i = 0; i < drawable.getHeight(); i++){
            for(int j = 0; j < drawable.getWidth(); j++){
                if(x+j >= 0 && x+j < this.getWidth() && y+i >= 0 && y+i < this.getHeight())
                    if(content[i][j] != null)
                        this.addContent(content[i][j], x + j, y + i);
            }
        }
    }
}
