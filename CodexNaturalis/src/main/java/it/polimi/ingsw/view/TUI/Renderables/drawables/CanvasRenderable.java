package it.polimi.ingsw.view.TUI.Renderables.drawables;

import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.TUI.Renderables.CardTextStyle;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;

public class CanvasRenderable extends Drawable {

    // The center of the canvas in grid coordinates
    private Position center = new Position(0, 0);

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
        Position centerOffsetPosition = gridPosition.add(center.multiply(-1));

        return new Position(
                centerOffsetPosition.getX() * (CardTextStyle.getCardWidth() - 2),
                -centerOffsetPosition.getY() * (CardTextStyle.getCardHeight() -2)
        );
    }

    private Position fromCenterToMatrix(Position center){
        return new Position(center.getX() + this.getWidth() / 2, center.getY() + this.getHeight() / 2);
    }

    private Position fromGridToMatrix(Position gridPosition){
        return fromCenterToMatrix(fromGridToCenter(gridPosition));
    }

    public void draw(Drawable drawable, Position gridPosition){
        Position matrixPosition = fromGridToMatrix(gridPosition);
        int x = matrixPosition.getX() - drawable.getWidth() / 2;
        int y = matrixPosition.getY() - drawable.getHeight() / 2;

        for(int i = 0; i < drawable.getHeight(); i++){
            for(int j = 0; j < drawable.getWidth(); j++){
                if(x + j >= 0 && x + j < this.getWidth() && y + i >= 0 && y + i < this.getHeight()){
                    this.addContent(drawable.getCharAt(j, i), x + j, y + i);
                }
            }
        }
    }

    public void setCenter(Position center){

        Position matrixOffset = fromGridToCenter(center.add(this.center.multiply(-1)));

        this.center = center;



        //Translate the content to the new center
        String[][] newContent = new String[this.getHeight()][this.getWidth()];

        for(int i = 0; i < this.getHeight(); i++){
            for(int j = 0; j < this.getWidth(); j++){
                if(i + matrixOffset.getY() >= 0 && i + matrixOffset.getY() < this.getHeight() &&
                        j + matrixOffset.getX() >= 0 && j + matrixOffset.getX() < this.getWidth()){
                    newContent[i][j] = this.getCharAt(j + matrixOffset.getX(), i + matrixOffset.getY());
                } else {
                    newContent[i][j] = CardTextStyle.getBackgroundEmoji();
                }
            }
        }

        this.setContent(newContent);
    }

}
