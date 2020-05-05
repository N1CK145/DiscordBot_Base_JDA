package de.n1ck145.PROJECT.server;

import de.n1ck145.PROJECT.main.Main;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Server {
    private String id, name;
    private ServerSettings serverSettings;

    public Server(String id, String name) throws SQLException {
        this.id = id;
        this.name = name;
        this.serverSettings = new ServerSettings();

        ResultSet settingsSet = Main.getDatabase().getResult("SELECT * FROM tbl_serverSettings " +
                "WHERE ServerID LIKE '" + id + "';");
        settingsSet.next();

        serverSettings.setSetting(SETTING.PREFIX, settingsSet.getString("PREFIX"));
    }

    // Getter
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ServerSettings getServerSettings() {
        return serverSettings;
    }

    // Setter
    public void setName(String name) {
        this.name = name;
    }

    public void setServerSettings(ServerSettings serverSettings) {
        this.serverSettings = serverSettings;
    }
}
