package net.knarcraft.stargateinterfaces.command.style;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import net.joshka.junit.json.params.JsonFileSource;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.command.defaults.VersionCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.sgrewritten.stargate.Stargate;
import org.sgrewritten.stargate.api.StargateAPI;

import java.util.List;

class StyleTabCompleterTest {

    private StyleTabCompleter styleTabCompleter;
    private ServerMock server;
    private PlayerMock commandSender;
    private VersionCommand fakeCommand;
    private StargateAPI stargateAPI;

    @BeforeEach
    void setUp() {
        this.server = MockBukkit.mock();
        this.stargateAPI = MockBukkit.load(Stargate.class);
        this.commandSender = server.addPlayer("commandSender");
        this.styleTabCompleter = new StyleTabCompleter(stargateAPI.getRegistry());
        this.fakeCommand = new VersionCommand("fake");
        StyleCommandRegistry.trackColorCodes(commandSender, TextColor.fromHexString("#000000"));
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
        StyleCommandRegistry.unTrackAllCommandSenders();
    }

    @ParameterizedTest
    @JsonFileSource(resources = "/styleTabTests.json")
    void onTabComplete(JsonObject jsonObject) {
        String command = jsonObject.getString("command");
        String[] args = command.split(" ",-1);
        List<String> tabCompletions = styleTabCompleter.onTabComplete(commandSender, fakeCommand, "s", ArrayUtils.remove(args, 0));
        Assertions.assertNotNull(tabCompletions);
        JsonArray jsonArray = jsonObject.getJsonArray("expected");
        for (int i = 0; i < jsonArray.size(); i++) {
            String expectedValue = jsonArray.getString(i);
            Assertions.assertTrue(tabCompletions.contains(expectedValue), "Expected value: " + expectedValue +", got: " + tabCompletions);
        }
        Assertions.assertEquals(jsonArray.size(), tabCompletions.size(), "Actual:" + tabCompletions);
    }
}