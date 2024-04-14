package it.polimi.ingsw.view.TUI.Renderables.drawables;

import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.TUI.Renderables.CardTextStyle;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;

public class CanvasRenderable extends Drawable {

    private Position center;

    public CanvasRenderable(int width, int height){
        super(width, height);
        fillContent(CardTextStyle.getBackgroundEmoji());
    }

    @Override
    public void update() {
        this.clearContent();
    }

    @Override
    public void updateInput(String input){
        //Empty method
    }

    private Position fromGridToCenter(Position gridPosition){
        return new Position(gridPosition.getX() * (CardTextStyle.getCardWidth() - 2), -gridPosition.getY() * (CardTextStyle.getCardHeight() -2));
    }

    private Position fromCenterToMatrix(Position center){
        return new Position(center.getX() + this.getWidth() / 2, center.getY() + this.getHeight() / 2);
    }

    private Position fromGridToMatrix(Position gridPosition){
        return fromCenterToMatrix(fromGridToCenter(gridPosition));
    }

    public void draw(Drawable drawable, Position gridPosition){
        Position matrixPosition = fromGridToMatrix(gridPosition);
        int x = matrixPosition.getX();
        int y = matrixPosition.getY();

        for(int i = 0; i < drawable.getHeight(); i++){
            for(int j = 0; j < drawable.getWidth(); j++){
                if(x + j >= 0 && x + j < this.getWidth() && y + i >= 0 && y + i < this.getHeight()){
                    this.addContent(drawable.getCharAt(j, i), x + j, y + i);
                }
            }
        }
    }

}
