package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;

import java.util.List;
import java.util.Map;

/**
 * Set final ranking message class
 */
public class SetFinalRankingMsg extends ServerMsg{
    /** The list of final calculated by the Server */
    private final List<String> ranking;

    /**
     * Class constructor
     * @param ranking The list of final calculated by the Server
     */
    public SetFinalRankingMsg(List<String> ranking) {
        this.ranking = ranking;
    }

    /**
     * Call the setFinalRanking method of the view
     * @param serverHandler Server handler where to process the message
     * @throws Exception If an error occurs during the processing of the message
     */
    @Override
    public void processMsg(ServerHandler serverHandler) throws Exception {
        serverHandler.getView().setFinalRanking(ranking);
    }
}
