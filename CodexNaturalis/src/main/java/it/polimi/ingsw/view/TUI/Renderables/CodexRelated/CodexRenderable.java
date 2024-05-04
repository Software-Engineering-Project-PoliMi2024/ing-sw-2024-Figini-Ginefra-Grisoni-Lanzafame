package it.polimi.ingsw.view.TUI.Renderables.CodexRelated;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCodex;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.TUI.Renderables.drawables.Drawable;
import it.polimi.ingsw.view.TUI.Styles.CardTextStyle;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.cardDrawing.CardPainter;
import it.polimi.ingsw.view.TUI.cardDrawing.TextCard;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.util.Map;

public class CodexRenderable extends CanvasRenderable {
    protected final LightGame lightGame;
    private final CardMuseum cardMuseum;
    public CodexRenderable(String name, LightGame lightGame, CardMuseum cardMuseum, CommandPrompt[] relatedCommands, ControllerInterface controller) {
        super(name, 0, 0, relatedCommands, controller);
        this.lightGame = lightGame;
        this.cardMuseum = cardMuseum;
    }

    private Position gridToCanvas(Position p){
        return new Position(p.getX() * (CardTextStyle.getCardWidth() - 2) + canvas.getWidth() / 2, p.getY() * (CardTextStyle.getCardHeight() - 2) + canvas.getHeight() / 2);
    }

    protected LightCodex getCodex(){
        return lightGame.getCodexMap().get(lightGame.getLightGameParty().getYourName());
    }

    public void drawPlacement(LightPlacement placement){
        Position canvasPosition = gridToCanvas(placement.position());
        TextCard card = cardMuseum.get(placement.card().id());
        Drawable cardDrawable = card.get(placement.face());
        this.canvas.draw(cardDrawable, canvasPosition.getX(), canvasPosition.getY());
    }

    public void drawCodex(){
        Map<Position, LightPlacement> placementHistory = getCodex().getPlacementHistory();

        //compute the width and height of the codex
        Position LL_corner = new Position(0, 0);
        Position UR_corner = new Position(0, 0);
        for(Position p : placementHistory.keySet()){
            if(p.getX() < LL_corner.getX())
                LL_corner =  LL_corner.setX(p.getX());
            if(p.getY() < LL_corner.getY())
                LL_corner =  LL_corner.setY(p.getY());
            if(p.getX() > UR_corner.getX())
                UR_corner =  UR_corner.setX(p.getX());
            if(p.getY() > UR_corner.getY())
                UR_corner =  UR_corner.setY(p.getY());
        }

        int width = (Math.max(-LL_corner.getX(), UR_corner.getX()) + 3) * (CardTextStyle.getCardWidth() - 2) * 2;
        int height = (Math.max(-LL_corner.getY(), UR_corner.getY()) + 3) * (CardTextStyle.getCardHeight() - 2) * 2;

        //update the canvas
        this.canvas.setWidth(width);
        this.canvas.setHeight(height);

        //clear the canvas
        this.canvas.fillContent(CardTextStyle.getBackgroundEmoji());

        //draw the frontier
        for(int i = 0; i < getCodex().getFrontier().frontier().size(); i++){
            Position canvasPosition = gridToCanvas(getCodex().getFrontier().frontier().get(i));
            Drawable frontierDrawable = CardPainter.drawFrontierCard(i);
            this.canvas.draw(frontierDrawable, canvasPosition.getX(), canvasPosition.getY());
        }

        //draw the cards
        for(Position p : placementHistory.keySet()){
            drawPlacement(placementHistory.get(p));
        }
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void updateCommand(CommandPromptResult answer) {
        switch (answer.getCommand()){
            case CommandPrompt.DISPLAY_CODEX:
                drawCodex();
                this.render();
                break;
        }
    }

}
