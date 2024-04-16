package it.polimi.ingsw.view.TUI.Renderables;

public class ConnectFormRenderable extends Renderable {
    private String ip = "";
    private String port = "";
    private String nickname = "";

    public ConnectFormRenderable() {
        super(null);
    }

    @Override
    public void render() {
        if (ip.isEmpty()) {
            System.out.println("Please enter server IP:");
        } else if (port.isEmpty()) {
            System.out.println("Please enter server port:");
        } else if (nickname.isEmpty()) {
            System.out.println("Please enter your nickname:");
        }
    }

    @Override
    public void update() {
        // re-render or clear the field
        render();
    }

    @Override
    public void updateInput(String input) {
        if (ip.isEmpty()) {
            ip = input;
        } else if (port.isEmpty()) {
            port = input;
        } else if (nickname.isEmpty()) {
            nickname = input;
        }
    }

}