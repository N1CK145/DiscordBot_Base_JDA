package de.n1ck145.PROJECT.listener;

import java.sql.SQLException;
import java.util.ArrayList;

import de.n1ck145.PROJECT.main.Main;
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
        manager.setStatus(OnlineStatus.ONLINE);
        manager.setActivity(Activity.playing("v." + Main.getVersion()));

        System.out.println("Bot online! Version: " + Main.getVersion());
        System.out.println("Guilds:");
        for (Guild g : manager.getGuilds()){
            System.out.println("\t- " + g.getName());
            Main.getDatabase().update("INSERT IGNORE INTO tbl_server(ServerID, ServerName) " +
                    "VALUES ('" + g.getId() + "', '" + g.getName() + "');");
            Main.getDatabase().update("UPDATE tbl_server SET ServerName = '" + g.getName() + "';");
            Main.getDatabase().update("INSERT IGNORE INTO tbl_serverSettings(ServerID) " +
                    "VALUES ('" + g.getId() + "');");

            try {
                if(g.getId() != null)
                    ServerManager.addServer(new Server(g.getId(), g.getName()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        gameUpdater();
    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        Guild g = event.getGuild();
        Main.getDatabase().update("INSERT IGNORE INTO tbl_server(ServerID, ServerName) " +
                "VALUES ('" + g.getId() + "', '" + g.getName() + "');");
        Main.getDatabase().update("UPDATE tbl_server SET ServerName = '" + g.getName() + "';");
        Main.getDatabase().update("INSERT IGNORE INTO tbl_serverSettings(ServerID) " +
                "VALUES ('" + g.getId() + "');");
    }

    @Override
    public void onGuildLeave(@Nonnull GuildLeaveEvent event) {
        ServerManager.removeServer(event.getGuild().getId());
        System.out.println("Quit server " + event.getGuild().getId() + "(" + event.getGuild().getName() + ")");
    }

    private void gameUpdater(){
        new Thread(() -> {
            while(true){
                Main.getShardManager().setActivity(Activity.playing("auf " + ServerManager.getServerCount() + " Server"));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onShutdown(ShutdownEvent event) {
        manager.setStatus(OnlineStatus.OFFLINE);
        System.out.println("Bot offline!");
    }

    @Override // COMMANDS
    public void onMessageReceived(MessageReceivedEvent event) {
        String msg = event.getMessage().getContentDisplay();

        if(!msg.startsWith(Main.getCommandPrefix()))
            return;
        if(event.getAuthor().isBot())
            return;
        if(event.getChannel().getType() != ChannelType.TEXT)
            return;
        if(msg.length() == Main.getCommandPrefix().length())
            return;

        System.out.println(event.getAuthor().getAsTag() + ": " + msg);

        String cmd = msg.substring(Main.getCommandPrefix().length()).split(" ")[0];
        ArrayList<String> args = new ArrayList<String>();

        try {
            for(String arg : msg.substring(Main.getCommandPrefix().length() + cmd.length() + 1).split(" ")) {
                args.add(arg);
            }
        } catch (Exception e) {}


        CommandManager.execute(cmd, args, event);
    }
}
