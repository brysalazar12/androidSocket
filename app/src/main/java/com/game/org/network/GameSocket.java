package com.game.org.network;

/**
 * Created by Admin on 7/17/2015.
 */
public interface GameSocket {
    public void setPort(int port); // set port number
    public void broadcastPing(); // ping all within the network
    public void responsePing(); // response to ping
    public void createServer(); // create server socket that listen to all connecting client
    public void createClient(String ip); // create client socket
    public void disconnect();
    public void reconnect();
    public void sendMessage(String message);
    public void broadcastMessage(String message);

}
