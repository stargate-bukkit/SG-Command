package net.knarcraft.stargateinterfaces.command.style;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import jakarta.json.JsonObject;
import net.joshka.junit.json.params.JsonFileSource;
import net.knarcraft.stargateinterfaces.color.ColorModificationRegistry;
import net.knarcraft.stargateinterfaces.database.DatabaseInterface;
import net.knarcraft.stargateinterfaces.database.SQLiteDatabase;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.command.defaults.VersionCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.sgrewritten.stargate.Stargate;
import org.sgrewritten.stargate.api.StargateAPI;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

class CommandStyleTest {

    private PlayerMock commandSender;
    private StargateAPI stargate;
    private CommandStyle command;
    private SQLiteDatabase database;
    private DatabaseInterface databaseInterface;

    @BeforeEach
    void setUp() throws SQLException, IOException {
        ServerMock server = MockBukkit.mock();
        this.commandSender = server.addPlayer("test");
        this.stargate = MockBukkit.load(Stargate.class);
        ColorModificationRegistry registry = new ColorModificationRegistry();
        this.database = new SQLiteDatabase(new File("interfaces.db"));
        this.databaseInterface = new DatabaseInterface(database);
        this.databaseInterface.createTablesIfNotExists();
        this.command = new CommandStyle(stargate.getRegistry(), registry, databaseInterface);
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

    @Test
    void onCommand_notEnoughArguments_1(){
        String commandString = "style ";
        String[] args = ArrayUtils.remove(commandString.split(" "),0);
        command.onCommand(commandSender,new VersionCommand("test"), "s", args);
        Assertions.assertNotNull(commandSender.nextMessage());
        Assertions.assertTrue(databaseInterface.loadColorsCategoryModification().isEmpty());
    }

    @Test
    void onCommand_notEnoughArguments_2(){
        String commandString = "style";
        String[] args = ArrayUtils.remove(commandString.split(" "),0);
        command.onCommand(commandSender,new VersionCommand("test"), "s", args);
        Assertions.assertNotNull(commandSender.nextMessage());
        Assertions.assertTrue(databaseInterface.loadColorsCategoryModification().isEmpty());
    }

    @Test
    void onCommand_notEnoughArguments_3(){
        String commandString = "style set ";
        String[] args = ArrayUtils.remove(commandString.split(" "),0);
        command.onCommand(commandSender,new VersionCommand("test"), "s", args);
        Assertions.assertNotNull(commandSender.nextMessage());
        Assertions.assertTrue(databaseInterface.loadColorsCategoryModification().isEmpty());
    }

    @Test
    void onCommand_notEnoughArguments_4(){
        String commandString = "style set";
        String[] args = ArrayUtils.remove(commandString.split(" "),0);
        command.onCommand(commandSender,new VersionCommand("test"), "s", args);
        Assertions.assertNotNull(commandSender.nextMessage());
        Assertions.assertTrue(databaseInterface.loadColorsCategoryModification().isEmpty());
    }

    @Test
    void onCommand_1(){
        String commandString = "style set global pointer #101010";
        String[] args = ArrayUtils.remove(commandString.split(" "),0);
        command.onCommand(commandSender,new VersionCommand("test"), "s", args);
        Assertions.assertNotNull(commandSender.nextMessage());
        Assertions.assertTrue(databaseInterface.loadColorsCategoryModification().isEmpty());
    }

    @Test
    void onCommand_setClear(){
        String commandString1 = "style set global pointer #101010";
        String[] args1 = ArrayUtils.remove(commandString1.split(" "),0);
        command.onCommand(commandSender,new VersionCommand("test"), "s", args1);
        Assertions.assertNotNull(commandSender.nextMessage());
        Assertions.assertTrue(databaseInterface.loadColorsCategoryModification().isEmpty());
        String commandString2 = "style clear global";
        String[] args2 = ArrayUtils.remove(commandString1.split(" "),0);
        command.onCommand(commandSender,new VersionCommand("test"), "s", args2);
    }
}