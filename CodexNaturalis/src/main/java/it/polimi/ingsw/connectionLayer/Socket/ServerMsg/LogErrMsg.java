package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;


/**
 * Log error message class
 */
public class LogErrMsg extends ServerMsg{
    /** Message to log */
    private final String logMsg;

    /**
     * Class constructor
     * @param logMsg the message to log on the view
     */
    public LogErrMsg(String logMsg) {
        this.logMsg = logMsg;
    }

    /**
     * Call the logErr method of the view
     * @param serverHandler Server handler where to process the message
     * @throws Exception If an error occurs during the processing of the message
     */
    @Override
    public void processMsg(ServerHandler serverHandler) throws Exception {
        serverHandler.getView().logErr(logMsg);
    }
}
