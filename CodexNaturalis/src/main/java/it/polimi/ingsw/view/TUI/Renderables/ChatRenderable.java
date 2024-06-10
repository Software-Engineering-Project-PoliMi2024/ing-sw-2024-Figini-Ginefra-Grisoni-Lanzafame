package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.Styles.StringStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.util.PriorityQueue;
import java.util.Queue;

public class ChatRenderable extends Renderable {
    private Queue<String> chatHistory = new PriorityQueue<>();

    /**
     * Constructor
     * @param name            the name of the renderable
     * @param relatedCommands the commands that are related to this renderable
     * @param lightGame       the light game from which to get the message
     * @param view            the controller provider
     */
    public ChatRenderable(String name, CommandPrompt[] relatedCommands, LightGame lightGame, ControllerProvider view) {
        super(name, relatedCommands, view);
        chatHistory.add("Welcome to the chat");
        chatHistory.add("hi");
    }

    @Override
    public void render() {
        for (String message : chatHistory) {
            PromptStyle.printInABox(message, 30, StringStyle.ITALIC);
        }
    }

    @Override
    public void updateCommand(CommandPromptResult answer) {
        switch (answer.getCommand()) {
            case CommandPrompt.VIEW_MESSAGE:
                int optionChose = Integer.parseInt(answer.getAnswer(0));
                if (optionChose == 2) {
                    //TODO: implement filtering of only private message
                } else if (optionChose == 3) {
                    //TODO implement filtering of only sent message
                }
                this.render();
                break;
            case CommandPrompt.SEND_PUBLIC_MESSAGE:
                try {
                    //view.getController().sendMessage(answer.getAnswer(0));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case CommandPrompt.SEND_PRIVATE_MESSAGE:
                try {
                    //view.getController().sendMessage(answer.getAnswer(0), answer.getAnswer(1));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }
}
