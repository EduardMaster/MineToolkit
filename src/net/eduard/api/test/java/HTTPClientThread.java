package net.eduard.api.test.java;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

import org.bukkit.plugin.java.JavaPlugin;


public class HTTPClientThread extends Thread {

	private JavaPlugin plugin;

	private Socket socket;

	private BufferedReader inFromClient = null;

	private DataOutputStream outToClient = null;

	public HTTPClientThread(JavaPlugin plugin, Socket socket) {

		this.plugin = plugin;

		this.socket = socket;

	}

	@Override
	public void run() {
		try {
			inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outToClient = new DataOutputStream(socket.getOutputStream());

			String requestedString = inFromClient.readLine();
			String headerLine = requestedString;

			StringTokenizer tokenizer = new StringTokenizer(headerLine);
			String httpMethod = tokenizer.nextToken();
			String httpQueryString = tokenizer.nextToken();

			StringBuffer responseBuffer = new StringBuffer();
			String ips = socket.getRemoteSocketAddress().toString().replace("/", "");
			String[] ip = ips.split(":");
			System.out.println(requestedString);
			System.out.println(httpQueryString);
			if (httpMethod.equals("GET")) {
				if (httpQueryString.equals("/")) {
					responseBuffer.append("ip: " + ip[0]);
					responseBuffer.append("Api criado pelo wiljafor1 & TioSasuke");
				} else {
					if (httpQueryString.equals("/pegarminhaloc")) {
						
//							Gamer gp = GamerManager.getGamerByIP(ip[0]);
//							if(gp == null){
//								JSONObject obj = new JSONObject();
//								obj.put("error", "Player nao esta online");
//								obj.put("ip", ""+ ip[0]);
//								String json = "{$error$:$Player nao esta online$,$ip$:$"+ ip[0] +"$}";
//								responseBuffer.append(json.replace("$", String.valueOf('"')));	
//							}
//							else{
//								Bukkit.getOnlinePlayers().forEach(v -> {
//									if(gp.getId().equals(v.getUniqueId().toString())){
//										JSONArray amigos = new JSONArray();
//										JSONObject wiljam = new JSONObject();
//										JSONObject urzur = new JSONObject();
//										wiljam.put("name", "Wiljam");
//										wiljam.put("x", new Integer(1500));
//										wiljam.put("y", new Integer(50));
//										wiljam.put("z", new Integer(1500));
//										wiljam.put("health", new Integer(50));
//										wiljam.put("maxHealth", new Integer(100));
//										urzur.put("name", "Urzur");
//										urzur.put("x", new Integer(1250));
//										urzur.put("y", new Integer(50));
//										urzur.put("z", new Integer(1250));
//										urzur.put("health", new Integer(50));
//										urzur.put("maxHealth", new Integer(100));
//										amigos.add(wiljam);
//										amigos.add(urzur);
//										JSONObject obj = new JSONObject();
//										obj.put("name", v.getName());
//										obj.put("server", gp.getLastServer());
//										obj.put("x", new Integer(v.getLocation().getBlockX()));
//										obj.put("y", new Integer(v.getLocation().getBlockY()));
//										obj.put("z", new Integer(v.getLocation().getBlockZ()));
//										obj.put("health", new Integer((int) v.getHealth()));
//										obj.put("maxHealth", new Integer((int) v.getMaxHealth()));
//										obj.put("party", amigos);
//										//String json = "{$name$:"+ v.getName() +"$,$server$:$"+gp.getLastServer()+"$,$x$:"+v.getLocation().getBlockX()+",$y$:"+v.getLocation().getBlockY()+",$z$:"+v.getLocation().getBlockZ()+",$health$:20,$maxHealth$:20,$party$:[],$request$:{$timestamp$:1514245954}}";
//										responseBuffer.append(obj);
//									}
//								});
//							}
							//Player v = Bukkit.getPlayer((UUID) gp.getId()));
					} else if (httpQueryString.equals("/pegartodascontas")) {
//						plugin.getServer().getOnlinePlayers().forEach(v -> {
//							Jogador j = JogadorManager.getJogador(v);
//							responseBuffer.append(j.getCurrentAccount().getUltimolocal());
//						});

					}
				}
			}

			sendResponse(200, responseBuffer.toString(), false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendResponse(int statusCode, String responseString, boolean isFile) throws Exception {

		String statusLine = null;

		String serverdetails = "Server: Java HTTPServer";

		String contentLengthLine = null;

		String fileName = null;

		String contentTypeLine = "Content-Type: text/html" + "\r\n";

		FileInputStream fin = null;

		if (statusCode == 200)

			statusLine = "HTTP/1.1 200 OK" + "\r\n";

		else

			statusLine = "HTTP/1.1 404 Not Found" + "\r\n";

		if (isFile) {

			fileName = responseString;

			fin = new FileInputStream(fileName);

			contentLengthLine = "Content-Length: " + Integer.toString(fin.available()) + "\r\n";

			if (!fileName.endsWith(".htm") && !fileName.endsWith(".html"))

				contentTypeLine = "Content-Type: \r\n";

		} else {

			contentLengthLine = "Content-Length: " + responseString.length() + "\r\n";

		}

		outToClient.writeBytes(statusLine);

		outToClient.writeBytes(serverdetails);

		outToClient.writeBytes(contentTypeLine);

		outToClient.writeBytes(contentLengthLine);

		outToClient.writeBytes("Connection: close\r\n");

		outToClient.writeBytes("\r\n");

		if (isFile)

			sendFile(fin, outToClient);

		else

			outToClient.writeBytes(responseString);

		outToClient.close();

	}

	public void sendFile(FileInputStream fin, DataOutputStream out) throws Exception {

		byte[] buffer = new byte[1024];

		int bytesRead;

		while ((bytesRead = fin.read(buffer)) != -1) {

			out.write(buffer, 0, bytesRead);

		}

		fin.close();

	}

}