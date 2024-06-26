package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;

/**
 * Log chat message class
 */
public class LogChatMsg extends ServerMsg{
    /** Message to log */
    private final String logMsg;

    /**
     * Class constructor
     * @param logMsg Message to log
     */
    public LogChatMsg(String logMsg) {
        this.logMsg = logMsg;
    }

    /**
     * Call the logChat method of the view
     * @param serverHandler Server handler where to process the message
     * @throws Exception If an error occurs during the processing of the message
     */
    @Override
    public void processMsg(ServerHandler serverHandler) throws Exception {
        serverHandler.getView().logChat(logMsg);
    }
}
