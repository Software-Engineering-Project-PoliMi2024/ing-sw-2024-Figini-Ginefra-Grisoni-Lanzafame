package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyDiff;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyDiffEditLogin;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class LobbyRenderableTest {

    LobbyRenderable renderable;
    @BeforeEach
    void setUp() {
        LightLobby lightLobby = new LightLobby();
        renderable = new LobbyRenderable("name", lightLobby, new CommandPrompt[]{}, null);

        LobbyDiff diff = new LobbyDiffEditLogin(List.of("user1", "user2", "user3"), new ArrayList<>(), "name", 4);
        diff.apply(lightLobby);
    }

    @Test
    void displayRenderCommand() {
        CommandPromptResult answer = new CommandPromptResult(CommandPrompt.DISPLAY_LOBBY, new String[]{});
        renderable.updateCommand(answer);

    }
}