package it.polimi.ingsw.utils.logger;

public enum LoggerSources {
    SERVER("server:","\u001B[37m"), //white
    PERSISTENCE("persistence:", "\u001B[32m"), //green
    LOBBY_GAME_LISTS_CONTROLLER("lobbyGameListColor:","\u001B[36m"), //cyan
    GAME_CONTROLLER("gameController:","\u001B[35m"); //purple

    private final String sourceName;
    private final String color;

    /**
     * constructor of the loggerSource enum
     * it is used to define the source of the log, specifying the name and the color of the source
     * @param sourceName the name of the source
     * @param color the color of the source
     */
    LoggerSources(String sourceName, String color){
        this.color = color;
        this.sourceName = sourceName;
    }

    /**
     * get the name of the source that is logging
     * @return the name of the source
     */
    public String getSourceName() {
        return sourceName;
    }

    /**
     * get the color of the source that is logging
     * @return the color of the source
     */
    public String getColor() {
        return color;
    }
}
