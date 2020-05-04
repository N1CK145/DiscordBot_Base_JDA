package de.n1ck145.PROJECT.main;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.lang.reflect.Array;

public class Main {
    private static Main main;
    private static ShardManager shardManager;
    private static String CommandPrefix = "--";
    private static String botToken;

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
        shardManager = new DefaultShardManagerBuilder(botToken).build();
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
}
