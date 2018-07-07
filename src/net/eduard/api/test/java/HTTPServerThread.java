package net.eduard.api.test.java;



import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.bukkit.plugin.java.JavaPlugin;


public class HTTPServerThread extends Thread {
	/*
	 * 
	 * @author Wiljafor1
	 * Servidor http para sistema de REST
	 */
    private JavaPlugin plugin;

    private int port;
    private ServerSocket socket;

    public HTTPServerThread(JavaPlugin plugin) throws IOException {
        this.plugin = plugin;
        socket = new ServerSocket(2165,10);
        System.out.println("Servidor de api aberto na porta 2165.");
    }

    @Override
    public void run() {
        while(true){
            try {
                Socket connected = socket.accept();
                (new HTTPClientThread(plugin,connected)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopSocket(){
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}