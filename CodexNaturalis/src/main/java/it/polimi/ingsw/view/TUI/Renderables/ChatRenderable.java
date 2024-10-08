package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightChat;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.playerReleted.ChatMessage;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.view.ActualView;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.TUI.Styles.CardTextStyle;
import it.polimi.ingsw.view.TUI.Styles.DecoratedString;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.Styles.StringStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;
import java.util.List;

/**
 * This class is a Renderable that renders the chat.
 */
public class ChatRenderable extends Renderable {
    /** The chat history that is the list of exchanged messages. */
    private List<ChatMessage> chatHistory;
    /** The light game of the player. */
    private final LightGame lightGame;
    /** The light chat of the player. */
    private final LightChat lightChat;
    /** The view to interact with. */
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
                            this.localLogChat("No message received yet");
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
                            this.localLogChat("No private message received yet");
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
                            this.localLogChat("No message sent yet");
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

    /**
     * Return the color of the user based on the pawn color
     * @param user the user to get the color of
     * @return the color of the user
     */
    private StringStyle getUserColor(String user){
        PawnColors pawn = lightGame.getLightGameParty().getPlayerColor(user);
        if(pawn == null){
            return StringStyle.RESET;
        }
        return CardTextStyle.convertPawnColor(pawn);
    }

    /**
     * LogChat is a method call by the controller that causes a transitionTo and consequentially a re-render of the ActiveCommands
     * localLogChat is a method that logs a message to the view and does not cause a transition. It is only used for local messages
     * @param logMsg the message to be logged
     */
    private void localLogChat(String logMsg) {
        Printer.println("");
        PromptStyle.printInABox(logMsg,50, StringStyle.GOLD_FOREGROUND);
        Printer.println("");
    }
}
