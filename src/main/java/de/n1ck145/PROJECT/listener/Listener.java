package de.n1ck145.PROJECT.listener;

import java.util.ArrayList;

import de.n1ck145.PROJECT.main.Main;
import de.n1ck145.PROJECT.utils.CommandManager;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.ShardManager;

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
        }
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
