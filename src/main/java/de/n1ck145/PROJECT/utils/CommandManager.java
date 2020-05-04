package de.n1ck145.PROJECT.utils;

import java.awt.*;
import java.sql.ResultSet;
import java.util.ArrayList;

import de.n1ck145.PROJECT.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.sharding.ShardManager;

public class CommandManager {
    private static ShardManager manager = Main.getShardManager();

    public static void execute(String cmd, ArrayList<String> args, MessageReceivedEvent e) {
        switch (cmd.toLowerCase()) {
            case "stop":
                if(e.getMember().hasPermission(Permission.ADMINISTRATOR))
                    manager.shutdown();
                else
                    sendErrorMessageNoPermission(e.getChannel(), "ADMINISTRATOR");
                break;
            case "help":
                EmbedBuilder builder = new EmbedBuilder();
                String helpUser = "";
                helpUser += "`help` - shows this help\n";

                String helpAdmin = "";
                helpAdmin += "`stop` - stops the bot\n";

                builder.setTitle("Help:");
                builder.setColor(Color.decode("#912c24"));
                builder.setDescription("[argument] Required, <argument> Optional\n[More help](https://discordapp.com/channels/351346215964770305/706265113308561438)");
                builder.addField("User:", helpUser, false);
                builder.addField("Admin:", helpAdmin, false);
                builder.setThumbnail("https://lia-gaming.de/downloads/icons/question_mark_black.png");
                e.getChannel().sendMessage(builder.build()).queue();
                break;
            default:
                e.getChannel().sendMessage("Use `!help` to list commands!").queue();
                break;
        }
    }
    private static void sendErrorMessage(MessageChannel channel, String msg){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Error");
        builder.setColor(Color.decode("#aa0000"));
        builder.addField("Error Message:", msg, false);
        channel.sendMessage(builder.build()).queue();
    }
    private static void sendErrorMessageInvalidSyntax(MessageChannel channel){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Error");
        builder.setColor(Color.decode("#aa0000"));
        builder.addField("Error Message:", "Invalid Syntax. Use `help`!", false);
        channel.sendMessage(builder.build()).queue();
    }
    private static void sendErrorMessageNoPermission(MessageChannel channel, String permission){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Error");
        builder.setColor(Color.decode("#aa0000"));
        builder.addField("Error Message:", "No permission", false);
        builder.addField("Permission:", permission, false);
        channel.sendMessage(builder.build()).queue();
    }
    private static void sendErrorMessageMissingRole(MessageChannel channel, Role role){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Error");
        builder.setColor(Color.decode("#aa0000"));
        builder.addField("Error Message:", "Missing Role", false);
        builder.addField("Role:", role.getName(), false);
        channel.sendMessage(builder.build()).queue();
    }
    private static void sendSuccessMessage(MessageChannel channel, String message){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Success!");
        builder.setColor(Color.decode("#00ff00"));
        builder.addField("Message:", message, false);
        channel.sendMessage(builder.build()).queue();
    }
}