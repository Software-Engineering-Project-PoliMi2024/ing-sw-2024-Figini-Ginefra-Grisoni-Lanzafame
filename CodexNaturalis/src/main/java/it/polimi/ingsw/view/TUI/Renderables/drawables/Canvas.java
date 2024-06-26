package it.polimi.ingsw.view.TUI.Renderables.drawables;

/**
 * This class is a drawable that represents a canvas. That is, a drawable in which other drawables can be drawn.
 */
public class Canvas extends Drawable{
    /**
     * Creates a new Canvas.
     * @param width The width of the canvas.
     * @param height The height of the canvas.
     */
    public Canvas(int width, int height) {
        super(width, height);
    }

    /**
     * Draws the given drawable at the given coordinates.
     * @param drawable The drawable to draw.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
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
