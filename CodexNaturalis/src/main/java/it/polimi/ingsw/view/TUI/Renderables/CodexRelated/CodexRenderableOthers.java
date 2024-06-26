package it.polimi.ingsw.view.TUI.Renderables.CodexRelated;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCodex;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.view.ActualView;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.rmi.RemoteException;

/**
 * This class is a Renderable that can render the codex of players who are not the main player.
 */
public class CodexRenderableOthers extends CodexRenderable{
    /** The nickname of the player whose codex is being rendered. */
    private String targetPlayer = null;

    private final ActualView view;

    /**
     * Creates a new CodexRenderableOthers.
     * @param name The name of the renderable.
     * @param lightGame The lightGame to render.
     * @param cardMuseum The cardMuseum to use.
     * @param relatedCommands The commands related to this renderable.
     */
    public CodexRenderableOthers(String name, ActualView view, LightGame lightGame, CardMuseum cardMuseum, CommandPrompt[] relatedCommands) {
        super(name, lightGame, cardMuseum, relatedCommands, view);
        this.view = view;
    }

    /**
     * Sets the target player.
     * @param targetPlayer The target player.
     */
    public void setTargetPlayer(String targetPlayer){
        this.targetPlayer = targetPlayer;
    }

    /**
     * Gets the target player.
     * @return The target player.
     */
    @Override
    protected LightCodex getCodex(){
        return lightGame.getCodexMap().get(this.targetPlayer);
    }

    /**
     * Draws the codex.
     */
    @Override
    public void render(){
        PromptStyle.printInABox("Codex of " + targetPlayer, 100);
        super.render();
    }

    /**
     * Updates the renderable based on the command prompt result.
     * @param answer The command prompt result.
     */
    @Override
    public void updateCommand(CommandPromptResult answer) {
        switch (answer.getCommand()){
            case CommandPrompt.PEEK:
                String nickname = answer.getAnswer(0);
                if(nickname.equals(lightGame.getLightGameParty().getYourName())){
                    try {
                        view.logErr("You can't peek at your own codex");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }

                if(!lightGame.getCodexMap().containsKey(nickname)){
                    try {
                        view.logErr("The player " + nickname + " does not exist");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }

                this.setTargetPlayer(nickname);
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
