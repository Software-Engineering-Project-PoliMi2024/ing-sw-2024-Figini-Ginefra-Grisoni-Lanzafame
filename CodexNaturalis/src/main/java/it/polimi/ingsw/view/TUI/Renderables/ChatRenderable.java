package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightChat;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.playerReleted.ChatMessage;
import it.polimi.ingsw.view.ActualView;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.Styles.StringStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;
import java.util.List;

public class ChatRenderable extends Renderable {
    private List<ChatMessage> chatHistory;
    private final LightGame lightGame;
    private final LightChat lightChat;
    private final ActualView view;

    /**
     * Constructor
     * @param name            the name of the renderable
     * @param relatedCommands the commands that are related to this renderable
     * @param lightGame       the light game of the player
     * @param view            the current view, which is also the controller provider
     */
    public ChatRenderable(String name, CommandPrompt[] relatedCommands, LightGame lightGame, ActualView view) {
        super(name, relatedCommands, view);
        this.lightGame = lightGame;
        this.lightChat = lightGame.getLightGameParty().getLightChat();
        this.view = view;
    }

    /**
     * Render the chat history
     */
    @Override
    public void render() {
        for (ChatMessage message : chatHistory) {
            PromptStyle.printInABox(messageToString(message), 30, messageToStyle(message));
        }
    }

    /**
     * Update the chatHistory based on the answer. Allow the render of private messages, public messages and sent messages
     * Allow the sending of public and private messages based on answers
     * @param answer the answer to the command
     */
    @Override
    public void updateCommand(CommandPromptResult answer) {
        switch (answer.getCommand()) {
            case CommandPrompt.VIEW_MESSAGE:
                int optionChose = Integer.parseInt(answer.getAnswer(0));
                this.chatHistory = lightChat.getChatHistory();
                if(optionChose == 0){
                    //Show all messages
                    if(chatHistory.isEmpty()){
                        try {
                            view.logErr("No message received yet");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        return;
                    }
                }else if (optionChose == 1) {
                    //Show only received private messages
                    chatHistory.removeIf(message -> message.getPrivacy() != ChatMessage.MessagePrivacy.PRIVATE);
                    if(chatHistory.isEmpty()){
                        try {
                            view.logErr("No private message received yet");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        return;
                    }
                } else if (optionChose == 2) {
                    //Show only sent messages
                    chatHistory.removeIf(message -> !message.getSender().equals(lightGame.getLightGameParty().getYourName()));
                    if(chatHistory.isEmpty()){
                        try {
                            view.logErr("No message sent yet");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        return;
                    }
                }
                this.render();
                break;
            case CommandPrompt.SEND_PUBLIC_MESSAGE:
                try {
                    view.getController().sendChatMessage(new ChatMessage(lightGame.getLightGameParty().getYourName(), answer.getAnswer(0)));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;

            case CommandPrompt.SEND_PRIVATE_MESSAGE:
                //todo if the player does not exist, it should force the reprint of the commands.
                // Atm, isLocal is set to true, but the send option actually interact with the controller
                String receiver = answer.getAnswer(0);
                if(!lightGame.getCodexMap().containsKey(receiver)){
                    try {
                        view.logErr("The player " + receiver + " does not exist");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }else if(receiver.equals(lightGame.getLightGameParty().getYourName())){
                    try{
                        view.logErr("You can't message yourself");
                    } catch (Exception e){
                        throw new RuntimeException(e);
                    }
                }else{
                    try {
                        view.getController().sendChatMessage(new ChatMessage(lightGame.getLightGameParty().getYourName(), answer.getAnswer(1), receiver));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
        }
    }

    /**
     * Return the message as a string based on the privacy and who is the receiver
     * @param message the message to be converted
     * @return the message as a string
     */
    private String messageToString(ChatMessage message){
        if(message.getPrivacy() == ChatMessage.MessagePrivacy.PUBLIC){
            return "To everyone: " + message.getMessage();
        }else{
            if(message.getReceiver().equals(lightGame.getLightGameParty().getYourName())){
                return "From " + message.getSender() + ": " + message.getMessage();
            }else{
                return "To " + message.getReceiver() + ": " + message.getMessage();
            }
        }
    }

    /**
     * Return the style of the message based on the sender and the privacy
     * The message will be GREEN if the sender is the player (#GreenBubble),
     * BLUE if the message is private, RESET (white) otherwise
     * @param message the message to be styled
     * @return the style of the message
     */
    private StringStyle messageToStyle(ChatMessage message){
        if(message.getSender().equals(lightGame.getLightGameParty().getYourName())) {
            return StringStyle.GREEN_FOREGROUND;
        }else if(message.getPrivacy() == ChatMessage.MessagePrivacy.PRIVATE) {
            return StringStyle.BLUE_FOREGROUND;
        }else{
            return StringStyle.RESET;
        }
    }
}
