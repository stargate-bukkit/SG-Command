package net.knarcraft.stargateinterfaces.command.style;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import net.joshka.junit.json.params.JsonFileSource;
import net.knarcraft.stargateinterfaces.color.ColorModification;
import net.knarcraft.stargateinterfaces.color.ColorModificationCategory;
import net.knarcraft.stargateinterfaces.color.ColorModificationRegistry;
import net.knarcraft.stargateinterfaces.color.ModificationTargetWrapper;
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
import java.util.List;

class CommandStyleTest {

    private PlayerMock commandSender;
    private StargateAPI stargate;
    private CommandStyle command;
    private SQLiteDatabase database;
    private DatabaseInterface databaseInterface;
    private final static File DATABASE_FILE = new File("src/test/resources/interfaces.db");

    @BeforeEach
    void setUp() throws SQLException, IOException {
        ServerMock server = MockBukkit.mock();
        this.commandSender = server.addPlayer("test");
        this.stargate = MockBukkit.load(Stargate.class);
        ColorModificationRegistry registry = new ColorModificationRegistry();
        this.database = new SQLiteDatabase(DATABASE_FILE);
        this.databaseInterface = new DatabaseInterface(database);
        this.databaseInterface.createTablesIfNotExists();
        this.command = new CommandStyle(stargate.getRegistry(), registry, databaseInterface);
    }

    @AfterEach
    void tearDown() throws IOException {
        MockBukkit.unmock();
        if(DATABASE_FILE.exists() && !DATABASE_FILE.delete()){
            throw new IOException("Unable to delete database file");
        }
    }

    @ParameterizedTest
    @JsonFileSource(resources = "/styleTabCompletionTests.json")
    void onCommand(JsonObject jsonObject) {
        String commandString = jsonObject.getString("command");
        String[] args = ArrayUtils.remove(commandString.split(" "),0);
        JsonArray jsonArray = jsonObject.getJsonArray("expected");
        for (int i = 0; i < jsonArray.size(); i++) {
            String value = jsonArray.getString(i);
            Assertions.assertTrue(command.onCommand(commandSender, new VersionCommand("Test"), "s", ArrayUtils.add(args, value)));
        }
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
    void onCommand_setClear(){
        String commandString1 = "style set global pointer #101010";
        String[] args1 = ArrayUtils.remove(commandString1.split(" "),0);
        command.onCommand(commandSender,new VersionCommand("test"), "s", args1);
        Assertions.assertNull(commandSender.nextMessage());
        List<ColorModification> colorModificationList = databaseInterface.loadColorsCategoryModification();
        Assertions.assertEquals(1, colorModificationList.size());
        ColorModification colorModification = colorModificationList.get(0);
        Assertions.assertNull(commandSender.nextMessage());
        Assertions.assertEquals(ColorModificationCategory.GLOBAL, colorModification.category());
        Assertions.assertTrue(new ModificationTargetWrapper<>("all").equals(colorModification.modificationTargetWrapper()));
        String commandString2 = "style clear global";
        String[] args2 = ArrayUtils.remove(commandString2.split(" "),0);
        command.onCommand(commandSender,new VersionCommand("test"), "s", args2);
        Assertions.assertNull(commandSender.nextMessage());
        Assertions.assertTrue(databaseInterface.loadColorsCategoryModification().isEmpty());
    }
}