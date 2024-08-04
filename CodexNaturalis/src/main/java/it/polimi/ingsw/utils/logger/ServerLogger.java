package it.polimi.ingsw.utils.logger;

import java.util.concurrent.*;

public class ServerLogger {
    private final LoggerSources source;
    private final String additionalPrefix;
    private static final Executor logExecutor = Executors.newSingleThreadExecutor();;

    /**
     * constructor of the logger used to log messages on the server
     * @param source the class creating the log
     * @param additionalPrefix additional information about the source to add before the log print
     */
    public ServerLogger(LoggerSources source, String additionalPrefix){
        this.source = source;
        this.additionalPrefix = additionalPrefix;
    }

    /**
     * log a message with the specified level
     * @param level the level of relevance of the log (either INFO, WARNING, SEVERE)
     * @param msg the message to log
     */
    public void log(LoggerLevel level, String msg) {
        logExecutor.execute(() -> {
            System.out.println(source.getColor() + source.getSourceName()
                    + " " + additionalPrefix + " "
                    + "\033[0m" + " "
                    + level.getColor() + level.getLevel() + " " + msg);
        });
    }
}
