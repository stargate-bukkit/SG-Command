package net.knarcraft.stargateinterfaces.command.style;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import jakarta.json.JsonObject;
import net.joshka.junit.json.params.JsonFileSource;
import net.knarcraft.stargateinterfaces.color.ColorModificationRegistry;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.command.defaults.VersionCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.sgrewritten.stargate.Stargate;
import org.sgrewritten.stargate.api.StargateAPI;

class CommandStyleTest {

    private PlayerMock commandSender;
    private StargateAPI stargate;
    private CommandStyle command;

    @BeforeEach
    void setUp() {
        ServerMock server = MockBukkit.mock();
        this.commandSender = server.addPlayer("test");
        this.stargate = MockBukkit.load(Stargate.class);
        ColorModificationRegistry registry = new ColorModificationRegistry();
        this.command = new CommandStyle(stargate.getRegistry(), registry);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @ParameterizedTest
    @JsonFileSource(resources = "/styleTests.json")
    void onCommand(JsonObject jsonObject) {
        String commandString = jsonObject.getString("command");
        String[] args = ArrayUtils.remove(commandString.split(" "),0);
        command.onCommand(commandSender,new VersionCommand("test"), "s", args);
    }
}