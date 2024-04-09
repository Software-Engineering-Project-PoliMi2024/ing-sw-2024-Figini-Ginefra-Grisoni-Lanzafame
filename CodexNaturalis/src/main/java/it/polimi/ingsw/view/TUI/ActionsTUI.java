package it.polimi.ingsw.view.TUI;

public enum ActionsTUI {
    CREATE_GAME("createGame", "", "to create a game"),
    JOIN_GAME("join", "<gameName>", "to join a game"),
    REFRESH("refresh", "", "to refresh the game list");

    private final String inputExpected;
    private final String parameterExpected;
    private final String printedDescription;
    private ActionsTUI(String inputExpected, String parameterExpected, String printedDescription) {
        this.inputExpected = inputExpected;
        this.parameterExpected = parameterExpected;
        this.printedDescription = printedDescription;
    }

    // Method to get the string value associated with the enum constant
    public String getInputExpected() {
        return inputExpected;
    }
    public String getPrintedDescription() {
        return printedDescription;
    }

    public String getParameterExpected() {
        return parameterExpected;
    }
}
