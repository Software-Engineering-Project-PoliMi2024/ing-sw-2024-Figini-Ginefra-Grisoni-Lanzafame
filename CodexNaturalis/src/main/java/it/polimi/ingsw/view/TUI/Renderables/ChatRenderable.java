package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightChat;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.playerReleted.ChatMessage;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.Styles.StringStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;
import java.util.List;

public class ChatRenderable extends Renderable {
    private List<ChatMessage> chatHistory;
    private final LightGame lightGame;
    private final LightChat lightChat;

    /**
     * Constructor
     * @param name            the name of the renderable
     * @param relatedCommands the commands that are related to this renderable
     * @param lightGame       the light game of the player
     * @param view            the controller provider
     */
    public ChatRenderable(String name, CommandPrompt[] relatedCommands, LightGame lightGame, ControllerProvider view) {
        super(name, relatedCommands, view);
        this.lightGame = lightGame;
        this.lightChat = lightGame.getLightGameParty().getLightChat();
    }

    @Override
    public void render() {
        for (ChatMessage message : chatHistory) {
            PromptStyle.printInABox(message.getSender() + ": " + message.getMessage(), 30, StringStyle.ITALIC);
        }
    }

    @Override
    public void updateCommand(CommandPromptResult answer) {
        switch (answer.getCommand()) {
            case CommandPrompt.VIEW_MESSAGE:
                int optionChose = Integer.parseInt(answer.getAnswer(0));
                this.chatHistory = lightChat.getChatHistory();
                if(optionChose == 0){
                    //Show all messages
                    if(chatHistory.isEmpty()){
                        PromptStyle.printInABox("No message received yet", 30, StringStyle.ITALIC);
                        return;
                    }
                }else if (optionChose == 1) {
                    //Show only received private messages
                    chatHistory.removeIf(message -> message.getPrivacy() != ChatMessage.MessagePrivacy.PRIVATE);
                    if(chatHistory.isEmpty()){
                        PromptStyle.printInABox("No private message received yet", 30, StringStyle.ITALIC);
                        return;
                    }
                } else if (optionChose == 2) {
                    //Show only sent messages
                    chatHistory.removeIf(message -> !message.getSender().equals(lightGame.getLightGameParty().getYourName()));
                    if(chatHistory.isEmpty()){
                        PromptStyle.printInABox("No message sent yet", 30, StringStyle.ITALIC);
                        return;
                    }
                    for(ChatMessage message : chatHistory){
                        if(message.getPrivacy() == ChatMessage.MessagePrivacy.PUBLIC){
                            PromptStyle.printInABox("To everyone: " + message.getMessage(), 30, StringStyle.ITALIC);
                        }else{
                            PromptStyle.printInABox("To " + message.getReceiver() + ": " + message.getMessage(), 30, StringStyle.ITALIC);
                        }
                    }
                    return;
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
                try {
                    view.getController().sendChatMessage(new ChatMessage(lightGame.getLightGameParty().getYourName(), answer.getAnswer(0), answer.getAnswer(1)));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }
}
