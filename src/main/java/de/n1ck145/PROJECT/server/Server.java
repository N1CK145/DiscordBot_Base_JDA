package de.n1ck145.PROJECT.server;

public class Server {

    private String id, name;

    public Server(String id, String name){
        this.id = id;
        this.name = name;
    }

    // Getter
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Setter
    public void setName(String name) {
        this.name = name;
    }
}
