package it.polimi.ingsw.view.TUI.observers;


public interface InputObserved {
    public interface ServerMsgObserved {
        public void attach(InputObserver observer);
        public void detach(InputObserver observer);
        public void notifyObservers(String input);
    }
}
