package de.n1ck145.PROJECT.server;

import de.n1ck145.PROJECT.main.Main;
import de.n1ck145.PROJECT.utils.MySQL_Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ServerSettings {
    private HashMap<SETTING, String> serverSettings = new HashMap<>();

    public void setSetting(SETTING setting, String value){
            serverSettings.put(setting, value);
    }
    public String getSetting(SETTING setting){
        return serverSettings.get(setting);
    }
    public void saveToDatabase(){
        for (SETTING s: SETTING.values()) {
            Main.getDatabase().update("SET " + s.name() + "");
        }
    }

    public static ServerSettings getSettingsFromDatabase(String serverID) throws SQLException {
        ServerSettings s = new ServerSettings();
        for(SETTING setting : SETTING.values()){
            ResultSet resultSet = Main.getDatabase().getResult("SELECT " + setting.name() + " FROM tbl_ServerSettings " +
                    "WHERE ServerID LIKE '" + serverID + "';");
            resultSet.next();
            String value = resultSet.getString(0);

            s.setSetting(setting, value);
        }
        return s;
    }
}
