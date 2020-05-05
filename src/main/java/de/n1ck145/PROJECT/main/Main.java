package de.n1ck145.PROJECT.main;

import javax.security.auth.login.LoginException;

import de.n1ck145.PROJECT.listener.Listener;
import de.n1ck145.PROJECT.utils.MySQL_Connection;
import de.n1ck145.PROJECT.server.SETTING;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    private static Main main;
    private static ShardManager shardManager;
    private static String CommandPrefix = "--";
    private static String botToken;
    private static String version;
    private static Properties properties;
    private static MySQL_Connection database;

    public static void main(String[] args) {
        try {
            if(args.length == 2){
                if(args[0].equals("-token")){
                    botToken = args[1];

                    main = new Main();
                }else
                    System.out.println("use argument:\n-token [BOT_TOKEN]");
            }else
                System.out.println("use argument:\n-token [BOT_TOKEN]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Deprecated
    public Main() throws LoginException, IllegalArgumentException {
        version = "1.0.0alpha";
        properties = new Properties();
        initSettingsFile();
        initDatabase();

        shardManager = new DefaultShardManagerBuilder(botToken).build();
        shardManager.addEventListener(new Listener());
    }

    private void initSettingsFile(){
        try{
            String path = "settings.cfg";
            if(!new File(path).exists()){
                new File(path).createNewFile();
                Properties prop = new Properties();
                prop.setProperty("mysql.host", "localhost");
                prop.setProperty("mysql.port", "3306");
                prop.setProperty("mysql.database", "DiscordBot");
                prop.setProperty("mysql.username", "my_user");
                prop.setProperty("mysql.password", "secret");
                prop.store(new FileOutputStream(path), "");
                properties = prop;
            }else
                properties.load(new FileInputStream(path));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initDatabase(){
        database = new MySQL_Connection(
                properties.getProperty("mysql.host"),
                properties.getProperty("mysql.port"),
                properties.getProperty("mysql.database"),
                properties.getProperty("mysql.username"),
                properties.getProperty("mysql.password")
        );
        try{
            database.connect();
        } catch (SQLException e){
            System.err.println("Please insert valid database information in 'settings.xml'");
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        // Create tables
        database.update("CREATE TABLE IF NOT EXISTS tbl_server(" +
                "ServerID varchar(32) NOT NULL," +
                "ServerName varchar(64) NOT NULL," +
                "CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "PRIMARY KEY (ServerID)" +
                ");");

        String qry = "CREATE TABLE IF NOT EXISTS tbl_serverSettings(" +
                "ServerID varchar(32) NOT NULL,";
        for(SETTING s : SETTING.values()){
            if(s.name().equals("PREFIX"))
                qry += s.name() + " varchar(32) DEFAULT '--', ";
            else
                qry += s.name() + " varchar(32), ";

        }
        qry +=  "PRIMARY KEY (ServerID)," +
                "FOREIGN KEY (ServerID) REFERENCES tbl_server(ServerID)" +
                ");";
        database.update(qry);
    }

    public static Main getMain() {
        return main;
    }
    public static ShardManager getShardManager() {
        return shardManager;
    }
    public static String getCommandPrefix() {
        return CommandPrefix;
    }
    public static String getVersion() {
        return version;
    }

    public static MySQL_Connection getDatabase() {
        return database;
    }
}
