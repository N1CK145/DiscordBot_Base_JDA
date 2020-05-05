package de.n1ck145.PROJECT.server;

import java.util.ArrayList;

public class ServerManager {

    private static ArrayList<Server> serverList = new ArrayList<>();

    public static Server getServer(String id){
        for (Server s : serverList){
            if(s.getId().equals(id))
                return s;
        }
        return null;
    }
    public static void addServer(Server server){
        serverList.add(server);
    }
    public static void removeServer(String id){
        serverList.remove(getServer(id));
    }
    public static int getServerCount(){
        return serverList.size();
    }
}
