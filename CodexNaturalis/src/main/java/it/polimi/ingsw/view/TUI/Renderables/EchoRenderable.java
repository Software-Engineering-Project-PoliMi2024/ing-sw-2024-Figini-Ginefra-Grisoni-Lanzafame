package it.polimi.ingsw.view.TUI.Renderables;

public class EchoRenderable extends Renderable{
    private String input = "";

    public EchoRenderable(){
        super(0, 0);
    }

    @Override
    public void render() {
        System.out.println("Echo: " + input);
    }

    @Override
    public void update() {
        render();
    }

    @Override
    public void updateInput(String input) {
        this.input = input;
        this.render();
    }
}
