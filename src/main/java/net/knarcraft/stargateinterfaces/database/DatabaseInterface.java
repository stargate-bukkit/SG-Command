package net.knarcraft.stargateinterfaces.database;

import net.knarcraft.stargateinterfaces.StargateInterfaces;
import net.knarcraft.stargateinterfaces.color.ColorModification;
import net.knarcraft.stargateinterfaces.color.ColorModificationCategory;
import net.knarcraft.stargateinterfaces.color.ModificationTargetWrapper;
import net.knarcraft.stargateinterfaces.util.FileHelper;
import net.kyori.adventure.text.format.TextColor;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseInterface {
    private final SQLiteDatabase database;

    public DatabaseInterface(SQLiteDatabase database){
        this.database = database;
    }

    public void createTablesIfNotExists() throws SQLException, IOException {
        try(Connection connection = database.getConnection()){
            try(PreparedStatement preparedStatement = prepareStatement(connection, InterfacesQuery.CREATE_TABLE_COLORS)){
                preparedStatement.execute();
            }
        }
    }

    public void insertColorsCategoryModification(ColorModification colorModification) {
        try(Connection connection = database.getConnection()){
            try(PreparedStatement preparedStatement = prepareStatement(connection, InterfacesQuery.CREATE_TABLE_COLORS)){
                preparedStatement.setString(1, colorModification.category().name());
                preparedStatement.setString(2, colorModification.pointerColor().asHexString());
                preparedStatement.setString(3, colorModification.textColor().asHexString());
                preparedStatement.setString(4, colorModification.modificationTargetWrapper().getTargetString());
                preparedStatement.execute();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public List<ColorModification> loadColorsCategoryModification(){
        List<ColorModification> output = new ArrayList<>();
        try(Connection connection = database.getConnection()){
            try(PreparedStatement preparedStatement = prepareStatement(connection, InterfacesQuery.SELECT_ALL_COLORS)){
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    output.add(parseColorModification(resultSet));
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    private ColorModification parseColorModification(ResultSet resultSet) throws SQLException {
        String categoryString = resultSet.getString("category");
        String pointerColorString = resultSet.getString("pointerColor");
        String textColorString = resultSet.getString("textColor");
        String targetString = resultSet.getString("target");
        return new ColorModification(ColorModificationCategory.valueOf(categoryString), TextColor.fromHexString(pointerColorString),
                TextColor.fromHexString(textColorString), ModificationTargetWrapper.createFromString(targetString));
    }

    public void removeColorModification(ColorModification colorModification){
        try(Connection connection = database.getConnection()){
            try(PreparedStatement preparedStatement = prepareStatement(connection, InterfacesQuery.DELETE_FROM_COLORS)){
                preparedStatement.setString(1,colorModification.category().name());
                preparedStatement.setString(2, colorModification.modificationTargetWrapper().getTargetString());
                preparedStatement.execute();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement prepareStatement(Connection connection, InterfacesQuery query) throws IOException, SQLException {
        try(InputStream inputStream = StargateInterfaces.class.getResourceAsStream("/database/" + query.name() + ".sql")){
            String querryString = FileHelper.readStreamToString(inputStream);
            return connection.prepareStatement(querryString);
        }
    }
}
