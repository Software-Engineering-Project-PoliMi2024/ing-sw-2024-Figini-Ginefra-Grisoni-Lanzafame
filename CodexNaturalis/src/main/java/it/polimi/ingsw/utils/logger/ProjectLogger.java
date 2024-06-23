package it.polimi.ingsw.utils.logger;

import java.util.concurrent.*;

public class ProjectLogger {
    private final LoggerSources source;
    private final String additionalPrefix;
    private final Executor logExecutor = Executors.newSingleThreadExecutor();;

    public ProjectLogger(LoggerSources source, String additionalPrefix){
        this.source = source;
        this.additionalPrefix = additionalPrefix;
    }

    public void log(LoggerLevel level, String msg) {
        logExecutor.execute(() -> {
            System.out.println(source.getColor() + source.getSourceName()
                    + " " + additionalPrefix + " "
                    + "\033[0m" + " "
                    + level.getColor() + level.getLevel() + " " + msg);
        });
    }
}
