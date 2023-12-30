package net.knarcraft.stargateinterfaces.command.style;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StyleCommandRegistryTest {

    private PlayerMock commandSender;
    private StyleCommandRegistry styleCommandRegistry;

    @BeforeEach
    void setUp(){
        ServerMock server = MockBukkit.mock();
        this.commandSender = server.addPlayer("player");
        this.styleCommandRegistry = new StyleCommandRegistry();
    }

    @AfterEach
    void tearDown(){
        MockBukkit.unmock();
    }
    @Test
    void unTrackCommandSender() {
        TextColor color1 = TextColor.fromHexString("#000000");
        styleCommandRegistry.trackColorCodes(commandSender,color1);
        styleCommandRegistry.unTrackCommandSender(commandSender);
        Assertions.assertNull(styleCommandRegistry.getTrackedColors(commandSender));
    }

    @Test
    void unTrackAllCommandSenders() {
        TextColor color1 = TextColor.fromHexString("#000000");
        styleCommandRegistry.trackColorCodes(commandSender,color1);
        styleCommandRegistry.unTrackAllCommandSenders();
        Assertions.assertNull(styleCommandRegistry.getTrackedColors(commandSender));
    }

    @Test
    void trackColorCodes_textColorClash() {
        TextColor color1 = TextColor.fromHexString("#000000");
        TextColor color2 = TextColor.fromHexString("#000000");
        styleCommandRegistry.trackColorCodes(commandSender,color1);
        Assertions.assertEquals(1, styleCommandRegistry.getTrackedColors(commandSender).size());
        styleCommandRegistry.trackColorCodes(commandSender,color2);
        Assertions.assertEquals(1, styleCommandRegistry.getTrackedColors(commandSender).size());

    }
}