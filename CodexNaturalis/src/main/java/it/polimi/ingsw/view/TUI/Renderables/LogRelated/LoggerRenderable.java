package it.polimi.ingsw.view.TUI.Renderables.LogRelated;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.view.TUI.Printing.Printable;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.Styles.StringStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is a Renderable that represents a logger.
 * It is basically just a list of logs that can be printed.
 * Each log is a printable object that is eliminated after a certain amount of time set in the Configs.
 */
public class LoggerRenderable extends Renderable {
    /** The logs that have been added to the logger. */
    private final List<Printable> logs = new LinkedList<>();

    /**
     * Creates a new LoggerRenderable.
     * @param name The name of the renderable.
     */
    public LoggerRenderable(String name) {
        super(name, new CommandPrompt[0], null);
    }

    /**
     * Renders the logger.
     */
    @Override
    public void render() {
        Printable logPrintable = new Printable("");
        for (Printable log : logs) {
            logPrintable.println(log);
        }
        Printer.print(logPrintable);
    }

    /**
     * Adds a log to the logger. The log will be removed after a certain amount of time.
     * @param logMsg The message of the log.
     */
    public synchronized void addLog(String logMsg, StringStyle style) {
        Printable log = new Printable("");
        PromptStyle.printInABox(log, logMsg, 50, style);
        this.addLog(log);

        Thread timer = new Thread(() -> {
            try {
                Thread.sleep(Configs.logDurationTUI_millis);
                this.removeLog(log);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        timer.start();
    }

    /**
     * Adds a log to the logger. The log will be removed after a certain amount of time.
     * @param log The message of the log as a printable object.
     */
    private synchronized void addLog(Printable log) {
        logs.add(log);
    }

    /**
     * Removes a log from the logger.
     * @param log The log to remove.
     */
    private synchronized void removeLog(Printable log) {
        logs.remove(log);
    }
}
