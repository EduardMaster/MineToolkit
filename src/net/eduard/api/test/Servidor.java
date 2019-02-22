package net.eduard.api.test;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class Servidor {
	public static void main(String[] args) {
	System.out.println("Cu");

		try {
//			ServerSocket server = new ServerSocket(12345);
			System.out.println("Cu");
			System.out.println("Aparece console porra");
//			Socket client = server.accept();
			// Instancia o ServerSocket ouvindo a porta 12345
		      ServerSocket servidor = new ServerSocket(12345);
		      System.out.println("Servidor ouvindo a porta 12345");
		      while(true) {
		        // o método accept() bloqueia a execução até que
		        // o servidor receba um pedido de conexão
		        Socket cliente = servidor.accept();
		        System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
		        ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
		        saida.flush();
		        saida.writeObject(new Date());
		        saida.close();
		        cliente.close();
		      }  

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
