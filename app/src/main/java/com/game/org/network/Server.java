package com.game.org.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Admin on 7/17/2015.
 */
public class Server {

    protected Socket clientSocket;
    protected volatile static ServerSocket serverSocket;
    protected int port;
    protected volatile static boolean isListenConnection;
    protected volatile static ArrayList<Socket> socketList;

    public Server(int port) {
        this.port = port;
        Server.isListenConnection = true;
    }

    public void stopListening() {
        Server.isListenConnection = false;
        try {
            Server.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            Server.serverSocket = new ServerSocket(this.port);

            // listen for client to connect
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(Server.isListenConnection) {
                        try {
                            Socket client = Server.serverSocket.accept();
                            new Worker().setSocket(client,Server.socketList).startThread();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Worker extends Thread {
        private Socket client;
        private ArrayList<Socket> socketList;

        public Worker setSocket(Socket client, ArrayList<Socket> socketList) {
            this.socketList = socketList;
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
                    //TCPSocket.messageQueue.add(line);
                    for(int i = 0; i < socketList.size(); i++) {

                        // broadcast to all socket except the one who send the message
                        if(socketList.get(i).getInetAddress() != this.client.getInetAddress()) {
                            PrintWriter writer = new PrintWriter(socketList.get(i).getOutputStream());
                            writer.println(line);
                            writer.flush();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
