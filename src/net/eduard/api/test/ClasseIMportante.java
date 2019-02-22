package net.eduard.api.test;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.yaml.snakeyaml.Yaml;

import com.google.gson.Gson;

public class ClasseIMportante {
public static void main(String[] args) {
	 Yaml yaml= new Yaml();
	    Map<String,Object> map= (Map<String, Object>) yaml.load("teste: 2");

	    JSONObject jsonObject=new JSONObject(map);
	   String string = jsonObject.toString();
}
public static HashMap<String, String> getMensagemBySite(String token) {
	
	
	
	try {
		
		URL link = new URL("http://localhost/pegarMesagem.php?token="+token);
		
		HttpURLConnection con = (HttpURLConnection) link.openConnection();
		con.setReadTimeout(5000);
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8;");
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestMethod("POST");
		
//		String json = "{\"mensagem1\":\"teste\"}";
//		OutputStream os = con.getOutputStream();
//		os.write(json.getBytes("UTF-8"));
//		os.close();
		
		InputStream is = con.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		byte[] bytesLidos = new byte[dis.available()];
		dis.readFully(bytesLidos);
		String texto = new String(bytesLidos,"UTF-8");
		Gson g = new Gson();
		HashMap<String,String> mensagems = g.fromJson(texto, HashMap.class);			
		
		return mensagems;
		
		
	} catch (Exception e) {
		e.printStackTrace();
		// TODO: handle exception
	}
	return null;
}public static String getNewToken() {

	try {

		URL link = new URL("http://localhost/gerarToken.php");
		HttpURLConnection con = (HttpURLConnection) link.openConnection();
		con.setReadTimeout(5000);
		con.setRequestProperty("Content-Type", "application/json; charset=UTF-8;");
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestMethod("POST");

		InputStream is = con.getInputStream();
		DataInputStream dis = new DataInputStream(is);

		byte[] bytesLidos = new byte[dis.available()];
		dis.readFully(bytesLidos);
		String texto = new String(bytesLidos, "UTF-8");

		return texto;

	} catch (Exception e) {
		e.printStackTrace();
		// TODO: handle exception
	}
	return null;
}
}
