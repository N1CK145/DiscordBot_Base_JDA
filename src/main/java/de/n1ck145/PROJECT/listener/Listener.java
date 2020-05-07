package de.n1ck145.PROJECT.listener;

import java.sql.SQLException;
import java.util.ArrayList;

import de.n1ck145.PROJECT.main.Main;
import de.n1ck145.PROJECT.server.ServerSettings;
import de.n1ck145.PROJECT.server.Setting;
import de.n1ck145.PROJECT.server.Server;
import de.n1ck145.PROJECT.server.ServerManager;
import de.n1ck145.PROJECT.utils.CommandManager;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.annotation.Nonnull;

public class Listener extends ListenerAdapter{
    private ShardManager manager = Main.getShardManager();

    @Override
    public void onReady(ReadyEvent event) {
        manager.setStatus(OnlineStatus.DO_NOT_DISTURB);
        manager.setActivity(Activity.playing("v." + Main.getVersion()));

        System.out.println("Bot online! Version: " + Main.getVersion());
        System.out.println("Guilds:");
        for (Guild g : manager.getGuilds()){
            System.out.println("\t- " + g.getName());
            // Server Table
            Main.getDatabase().update("INSERT IGNORE INTO tbl_server(ServerID, ServerName) " +
                    "VALUES ('" + g.getId() + "', '" + g.getName() + "');");
            Main.getDatabase().update("UPDATE tbl_server SET ServerName = '" + g.getName() + "' WHERE ServerID LIKE '" + g.getId() + "';");
            // Settings Table
            Main.getDatabase().update("INSERT IGNORE INTO tbl_serverSettings(ServerID) " +
                    "VALUES ('" + g.getId() + "');");

            try {
                if(g.getId() != null){
                    ServerManager.addServer(new Server(g.getId(), g.getName()));
                    ServerSettings s = ServerSettings.getSettingsFromDatabase(g.getId());
                    ServerManager.getServer(g.getId()).setServerSettings(s);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        Main.getShardManager().setActivity(Activity.playing("on " + ServerManager.getServerCount() + " server!"));
        manager.setStatus(OnlineStatus.ONLINE);
    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        Guild g = event.getGuild();
        // Server Table
        Main.getDatabase().update("INSERT IGNORE INTO tbl_server(ServerID, ServerName) " +
                "VALUES ('" + g.getId() + "', '" + g.getName() + "');");
        Main.getDatabase().update("UPDATE tbl_server SET ServerName = '" + g.getName() + "' WHERE ServerID LIKE '" + g.getId() + "';");
        // Settings Table
        Main.getDatabase().update("INSERT IGNORE INTO tbl_serverSettings(ServerID) " +
                "VALUES ('" + g.getId() + "');");
        Main.getShardManager().setActivity(Activity.playing("on " + (ServerManager.getServerCount() + 1) + " server!"));
        System.out.println("Joined " + event.getGuild().getId() + "(" + event.getGuild().getName() + ")");
    }

    @Override
    public void onGuildLeave(@Nonnull GuildLeaveEvent event) {
        ServerManager.removeServer(event.getGuild().getId());
        Main.getShardManager().setActivity(Activity.playing("on " + ServerManager.getServerCount() + " server!"));
        System.out.println("Quit server " + event.getGuild().getId() + "(" + event.getGuild().getName() + ")");
    }

    @Override
    public void onShutdown(ShutdownEvent event) {
        manager.setStatus(OnlineStatus.OFFLINE);
        System.out.println("Bot offline!");
    }

    @Override // COMMANDS
    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentDisplay();
        String prefix = ServerManager.getServer(event.getGuild().getId())
                .getServerSettings()
                .getSetting(Setting.PREFIX);

        if(!msg.startsWith(prefix))
            return;
        if(event.getAuthor().isBot())
            return;
        if(event.getChannel().getType() != ChannelType.TEXT)
            return;
        if(msg.length() == prefix.length())
            return;

        System.out.println(event.getAuthor().getAsTag() + ": " + msg);

        String cmd = msg.substring(prefix.length()).split(" ")[0];
        ArrayList<String> args = new ArrayList<String>();

        try {
            for(String arg : msg.substring(prefix.length() + cmd.length() + 1).split(" ")) {
                args.add(arg);
            }
        } catch (Exception e) {}


        CommandManager.execute(cmd, args, event);
    }
}
