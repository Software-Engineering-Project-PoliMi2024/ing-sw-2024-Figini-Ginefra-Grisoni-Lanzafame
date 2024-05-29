package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;

import java.util.List;
import java.util.Map;

public class SetFinalRankingMsg extends ServerMsg{
    private final List<String> ranking;

    public SetFinalRankingMsg(List<String> ranking) {
        this.ranking = ranking;
    }
    @Override
    public void processMsg(ServerHandler serverHandler) throws Exception {
        serverHandler.getView().setFinalRanking(ranking);
    }
}
