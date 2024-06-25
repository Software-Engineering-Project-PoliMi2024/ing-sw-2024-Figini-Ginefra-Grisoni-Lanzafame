package it.polimi.ingsw.view.TUI.Renderables.LogRelated;

import it.polimi.ingsw.view.TUI.Printing.Printable;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.Styles.StringStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

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

    public void addLog(String logMsg, StringStyle style) {
        Printable log = new Printable("");
        PromptStyle.printInABox(log, logMsg, 50, style);

        logs.add(log);
    }

    public void clearLogs() {
        logs.clear();
    }
}
