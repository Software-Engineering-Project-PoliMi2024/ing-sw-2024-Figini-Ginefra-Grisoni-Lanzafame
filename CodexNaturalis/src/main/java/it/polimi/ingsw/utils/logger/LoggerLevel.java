package it.polimi.ingsw.utils.logger;

public enum LoggerLevel {
    SEVERE("[SEVER]", "\u001B[31m"), //purple
    WARNING("[WARNING]", "\u001B[33m"), //cyan
    INFO("[INFO]", "\u001B[97m"); //bright white

    private final String level;
    private final String color;

    LoggerLevel(String level, String color){
        this.color = color;
        this.level = level;
    }

    public String getColor() {
        return color;
    }

    public String getLevel() {
        return level;
    }
}

