package net.eduard.api.test;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class EnviadoDadosParaOSite {
	/*
	 * Feito por J-Dev
	 */
	public static void main(String[] args) {
		
		System.out.println("Abrindo console");
		try {
			
			URL link = new URL("http://localhost/pagina2.php");
			
			HttpURLConnection con = (HttpURLConnection) link.openConnection();
			con.setReadTimeout(5000);
			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8;");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestMethod("POST");
			
			String json = "teste={'teste':2}";
			OutputStream os = con.getOutputStream();
			os.write(json.getBytes("UTF-8"));
			os.close();
			
			InputStream is = con.getInputStream();
			DataInputStream dis = new DataInputStream(is);
			
			byte[] bytesLidos = new byte[dis.available()];
			dis.readFully(bytesLidos);
			String texto = new String(bytesLidos,"UTF-8");
			System.out.println(texto);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
}
