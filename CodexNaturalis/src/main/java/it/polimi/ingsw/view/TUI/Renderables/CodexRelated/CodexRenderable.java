package it.polimi.ingsw.view.TUI.Renderables.CodexRelated;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCodex;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Renderables.drawables.Drawable;
import it.polimi.ingsw.view.TUI.Styles.CardTextStyle;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.cardDrawing.CardPainter;
import it.polimi.ingsw.view.TUI.cardDrawing.TextCard;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.util.List;
import java.util.Map;

/**
 * This class is a Renderable that can render the main player's codex.
 */
public class CodexRenderable extends CanvasRenderable {
    protected final LightGame lightGame;
    private final CardMuseum cardMuseum;
    protected Position center = new Position(0, 0);

    /**
     * Creates a new CodexRenderable.
     * @param name The name of the renderable.
     * @param lightGame The lightGame to render.
     * @param cardMuseum The cardMuseum to use.
     * @param relatedCommands The commands related to this renderable.
     * @param view The controller to interact with.
     */
    public CodexRenderable(String name, LightGame lightGame, CardMuseum cardMuseum, CommandPrompt[] relatedCommands, ControllerProvider view) {
        super(name, 0, 0, relatedCommands, view);
        this.lightGame = lightGame;
        this.cardMuseum = cardMuseum;
    }

    /**
     * Converts a grid position to a canvas position. The Grid position is the position of the card in the codex in the same coordinate system as the model.
     * @param p The grid position.
     * @return The canvas position.
     */
    private Position gridToCanvas(Position p){
        return new Position((p.getX() - center.getX()) * (CardTextStyle.getCardWidth() - 2) + canvas.getWidth() / 2, -(p.getY() - center.getY()) * (CardTextStyle.getCardHeight() - 2) + canvas.getHeight() / 2);
    }

    /**
     * Gets the codex of the main player. This method is overridden in CodexRenderableOthers.
     * @return The codex of the main player.
     */
    protected LightCodex getCodex(){
        return lightGame.getCodexMap().get(lightGame.getLightGameParty().getYourName());
    }

    /**
     * Draws a placement on the canvas.
     * @param placement The placement to draw.
     */
    public void drawPlacement(LightPlacement placement){
        Position canvasPosition = gridToCanvas(placement.position());
        TextCard card = cardMuseum.get(placement.card().idFront());
        Drawable cardDrawable = card.get(placement.face());
        this.canvas.draw(cardDrawable, canvasPosition.getX(), canvasPosition.getY());
    }

    /**
     * Draws the codex on the canvas.
     */
    public void drawCodex(){
        List<LightPlacement> placementHistory = getCodex().getPlacementHistory();

        //compute the width and height of the codex
        Position LL_corner = new Position(0, 0);
        Position UR_corner = new Position(0, 0);
        for(LightPlacement placement : placementHistory){
            Position p = placement.position();
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
        width = Math.min(width, CardTextStyle.codexMaxWidth);

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
        for(LightPlacement placement : placementHistory){
            drawPlacement(placement);
        }
    }

    /**
     * Renders the codex.
     */
    @Override
    public void render() {
        drawCodex();
        super.render();
    }

    /**
     * Updates the renderable based on the command prompt result.
     * @param answer The answer to the command.
     */
    @Override
    public void updateCommand(CommandPromptResult answer) {
        switch (answer.getCommand()){
            case CommandPrompt.DISPLAY_CODEX:
                this.render();
                break;
            case CommandPrompt.MOVE_CODEX:
                int dx = Integer.parseInt(answer.getAnswer(0));
                int dy = Integer.parseInt(answer.getAnswer(1));

                Position offset = new Position(dx, dy);
                center = center.add(offset);
                this.render();
                break;
            case CommandPrompt.RECENTER_CODEX:
                center = new Position(0, 0);
                this.render();
                break;

        }
    }

}
