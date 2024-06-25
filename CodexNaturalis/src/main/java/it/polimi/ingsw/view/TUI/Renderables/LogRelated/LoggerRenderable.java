package it.polimi.ingsw.view.TUI.Renderables.LogRelated;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.view.TUI.Printing.Printable;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.Styles.StringStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LoggerRenderable extends Renderable {
    private final List<Printable> logs = new LinkedList<>();
    public LoggerRenderable(String name) {
        super(name, new CommandPrompt[0], null);
    }
    @Override
    public void render() {
        Printable logPrintable = new Printable("");
        for (Printable log : logs) {
            logPrintable.println(log);
        }
        Printer.print(logPrintable);
    }

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

    private synchronized void addLog(Printable log) {
        logs.add(log);
    }

    private synchronized void removeLog(Printable log) {
        logs.remove(log);
    }

    public void clearLogs() {
        logs.clear();
    }
}
