package com.game.org.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Admin on 7/17/2015.
 */
public class TCPSocket implements GameSocket {
    protected int port; // port to connect
    protected ServerSocket serverSocket; // server socket
    protected ArrayList<Worker> clientList; // list of client socket
    protected Socket client;
    protected PrintWriter writer;
    public static ArrayList<String> messageQueue;

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void broadcastPing() {

    }

    @Override
    public void responsePing() {

    }

    @Override
    public void createServer() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            Socket client;
            while((client = this.serverSocket.accept()) != null) {
                this.clientList.add((new Worker()).setSocket(client).startThread());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createClient(String ip) {
        try {
            this.client = new Socket(ip,this.port);
            OutputStream out = this.client.getOutputStream();
            this.writer = new PrintWriter(out);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void reconnect() {

    }

    @Override
    public void sendMessage(String message) {
        writer.print(message);
        writer.flush();
    }

    @Override
    public void broadcastMessage(String message) {

    }

    class Worker extends Thread {
        private Socket client;

        public Worker setSocket(Socket client) {
            this.client = client;
            return this;
        }

        public Worker startThread() {
            this.start();
            return this;
        }

        @Override
        public void run() {
            String line;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
                while((line = reader.readLine()) != null) {
                    TCPSocket.messageQueue.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
