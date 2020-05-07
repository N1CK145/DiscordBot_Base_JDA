package de.n1ck145.PROJECT.server;

import de.n1ck145.PROJECT.main.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ServerSettings {
    private HashMap<Setting, String> serverSettings = new HashMap<>();

    public void setSetting(Setting setting, String value){
        serverSettings.put(setting, value);
        System.out.println("Set setting " + setting.name() + " to " + value);
    }

    public String getSetting(Setting setting){
        return serverSettings.get(setting);
    }

    public void pushToDatabase(String serverID, Setting setting){
        Main.getDatabase().update("UPDATE `tbl_serverSettings` SET `" + setting.name() + "` = '" + serverSettings.get(setting) + "' " +
                "WHERE `tbl_serverSettings`.`ServerID` = '" + serverID + "';");
    }

    public static ServerSettings getSettingsFromDatabase(String serverID) throws SQLException {
        ServerSettings s = new ServerSettings();
        for(Setting setting : Setting.values()){
            ResultSet resultSet = Main.getDatabase().getResult("SELECT " + setting.name() + " FROM tbl_serverSettings " +
                    "WHERE ServerID LIKE '" + serverID + "';");
            resultSet.next();
            String value = resultSet.getString(1);

            s.setSetting(setting, value);
        }
        return s;
    }

}
