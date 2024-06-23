package it.polimi.ingsw.utils.logger;

public enum LoggerSources {
    SERVER("server:","\u001B[37m"), //white
    PERSISTENCE("persistence:", "\u001B[32m"), //green
    LOBBY_GAME_LISTS_CONTROLLER("lobbyGameListColor:","\u001B[36m"), //cyan
    GAME_CONTROLLER("gameController:","\u001B[35m"); //purple

    private final String sourceName;
    private final String color;

    LoggerSources(String sourceName, String color){
        this.color = color;
        this.sourceName = sourceName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getColor() {
        return color;
    }
}
