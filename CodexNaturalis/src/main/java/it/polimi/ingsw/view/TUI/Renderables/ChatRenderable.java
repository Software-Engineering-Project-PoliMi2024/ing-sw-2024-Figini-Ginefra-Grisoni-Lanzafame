package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.lightModel.LightModelConfig;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightChat;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.playerReleted.ChatMessage;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.view.ActualView;
import it.polimi.ingsw.view.TUI.Styles.CardTextStyle;
import it.polimi.ingsw.view.TUI.Styles.DecoratedString;
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
            PromptStyle.printInABox(messageToString(message), 30);
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
                            view.logChat("No message received yet");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        return;
                    }
                }else if (optionChose == 1) {
                    //Show only received private messages
                    chatHistory.removeIf(message -> message.getPrivacy() != ChatMessage.MessagePrivacy.PRIVATE || !message.getReceiver().equals(lightGame.getLightGameParty().getYourName()));
                    if(chatHistory.isEmpty()){
                        try {
                            view.logChat("No private message received yet");
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
                            view.logChat("No message sent yet");
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
                try{
                    view.getController().sendChatMessage(new ChatMessage(lightGame.getLightGameParty().getYourName(), answer.getAnswer(1), answer.getAnswer(0)));
                }catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    /**
     * Return the message as a string (decorated) based on the privacy and who is the sender
     * @param message the message to be converted
     * @return the message as a string
     */
    private String messageToString(ChatMessage message){
        String actualMessage = ": " + message.getMessage();
        if(message.getPrivacy() == ChatMessage.MessagePrivacy.PRIVATE){
            actualMessage = new DecoratedString(actualMessage, StringStyle.ITALIC).toString();
        }

        StringStyle senderStyle = getUserColor(message.getSender());

        if(message.getPrivacy() == ChatMessage.MessagePrivacy.PUBLIC){
            if(message.getSender().equals(lightGame.getLightGameParty().getYourName())){
                return new DecoratedString("You", senderStyle) + actualMessage;
            }else{
                return new DecoratedString(message.getSender(), senderStyle) + actualMessage;
            }
        }else{ //Private message
            StringStyle receiverStyle = getUserColor(message.getReceiver());

            if(message.getSender().equals(lightGame.getLightGameParty().getYourName())){
                return new DecoratedString("You", senderStyle)
                        + new DecoratedString(" to ", StringStyle.ITALIC).toString()
                        + new DecoratedString(message.getReceiver(), receiverStyle)
                        + actualMessage;
            }else{
                return new DecoratedString(message.getSender(), senderStyle)
                        + new DecoratedString(" to ", StringStyle.ITALIC).toString()
                        + new DecoratedString("you", receiverStyle)
                        + actualMessage;
            }
        }
    }

    private StringStyle getUserColor(String user){
        PawnColors pawn = lightGame.getLightGameParty().getPlayerColor(user);
        if(pawn == null){
            return StringStyle.RESET;
        }
        return CardTextStyle.convertPawnColor(pawn);
    }
}
