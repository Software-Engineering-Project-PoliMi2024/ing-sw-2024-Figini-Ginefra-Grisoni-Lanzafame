package it.polimi.ingsw.utils.logger;

public enum LoggerLevel {
    SEVERE("[SEVER]", "\u001B[31m"), //purple
    WARNING("[WARNING]", "\u001B[33m"), //cyan
    INFO("[INFO]", "\u001B[97m"); //bright white

    private final String level;
    private final String color;

    /**
     * constructor of the loggerLevel enum
     * defines the possible levels of the log, specifying the name and the color of the level
     * @param level the name of the level
     * @param color the color associated with the level
     */
    LoggerLevel(String level, String color){
        this.color = color;
        this.level = level;
    }

    /**
     * @return the color associated with the level
     */
    public String getColor() {
        return color;
    }

    /**
     * @return the name of the level
     */
    public String getLevel() {
        return level;
    }
}

