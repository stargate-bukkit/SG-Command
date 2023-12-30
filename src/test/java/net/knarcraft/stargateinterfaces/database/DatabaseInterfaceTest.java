package net.knarcraft.stargateinterfaces.database;

import net.knarcraft.stargateinterfaces.color.ColorModification;
import net.knarcraft.stargateinterfaces.color.ColorModificationCategory;
import net.knarcraft.stargateinterfaces.color.ModificationTargetWrapper;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.DyeColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseInterfaceTest {

    private static final File SQLITE_FILE = new File("src/test/resources/test.db");
    private SQLiteDatabase database;
    private DatabaseInterface databaseInterface;
    private ColorModification colorModification1;
    private ColorModification colorModification2;

    @BeforeEach
    void setUp() throws SQLException, IOException {
        this.database = new SQLiteDatabase(SQLITE_FILE);
        this.databaseInterface = new DatabaseInterface(database);
        this.databaseInterface.createTablesIfNotExists();
        this.colorModification1 = new ColorModification(ColorModificationCategory.GLOBAL, TextColor.fromHexString("#000000"),
                TextColor.fromHexString("#000000"), new ModificationTargetWrapper<>("all"), DyeColor.BLACK);

        this.colorModification2 = new ColorModification(ColorModificationCategory.GLOBAL, TextColor.fromHexString("#FFFFFF"),
                TextColor.fromHexString("#FFFFFF"), new ModificationTargetWrapper<>("world"), DyeColor.WHITE);
    }

    @AfterEach
    void tearDown() throws IOException {
        Assertions.assertTrue(SQLITE_FILE.exists());
        if (!SQLITE_FILE.delete()) {
            throw new IOException("Unable to delete file: " + SQLITE_FILE);
        }
    }

    @Test
    void createTablesIfNotExists() throws SQLException {
        try (Connection connection = database.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM sqlite_schema WHERE type='table';")) {
                ResultSet tables = preparedStatement.executeQuery();
                int counter = 0;
                while (tables.next()) {
                    Assertions.assertTrue(getExpectedTables().contains(tables.getString("name")));
                    counter++;
                }
                Assertions.assertEquals(getExpectedTables().size(), counter);
            }
        }
    }

    @Test
    void insertRemoveLoadColorsCategoryModification() {
        databaseInterface.insertColorsCategoryModification(colorModification1);
        databaseInterface.insertColorsCategoryModification(colorModification2);
        List<ColorModification> colorModifications = databaseInterface.loadColorsCategoryModification();
        assertEquals(2, colorModifications.size());
        for (ColorModification loadedColorModification : colorModifications) {
            Assertions.assertTrue(colorModification1.equals(loadedColorModification) || colorModification2.equals(loadedColorModification));
        }
        databaseInterface.removeColorModification(colorModification2);
        List<ColorModification> loadedColorModification = databaseInterface.loadColorsCategoryModification();
        Assertions.assertEquals(colorModification1, loadedColorModification.get(0));
        Assertions.assertEquals(1, loadedColorModification.size());
    }

    @Test
    void updateColorModification() {
        databaseInterface.insertColorsCategoryModification(colorModification1);
        this.colorModification2 = new ColorModification(ColorModificationCategory.GLOBAL, TextColor.fromHexString("#FFFFFF"),
                TextColor.fromHexString("#FFFFFF"), new ModificationTargetWrapper<>("all"), DyeColor.BLACK);
        databaseInterface.updateColorModification(colorModification2);
        List<ColorModification> colorModifications = databaseInterface.loadColorsCategoryModification();
        Assertions.assertEquals(1, colorModifications.size());
        Assertions.assertEquals(colorModification2, colorModifications.get(0));
    }

    private static Set<String> getExpectedTables() {
        return Set.of("colors");
    }
}