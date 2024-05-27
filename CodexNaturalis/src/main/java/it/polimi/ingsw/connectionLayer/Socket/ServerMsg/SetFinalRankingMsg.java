package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;

public class SetFinalRankingMsg extends ServerMsg{
    private final String[] nicknames;
    private final int[] points;

    public SetFinalRankingMsg(String[] nicknames, int[] points) {
        this.nicknames = nicknames;
        this.points = points;
    }
    @Override
    public void processMsg(ServerHandler serverHandler) throws Exception {
        serverHandler.getView().setFinalRanking(nicknames, points);
    }
}
