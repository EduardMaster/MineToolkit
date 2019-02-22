package net.eduard.api.test;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Map.Entry;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/*
 * Feito por J-Dev
 */

public class RecebendoDadosDoSite {

	public static void main(String[] args) {
		try {
			URL link = new URL("http://localhost/pegarPermissoes?token=123");
			try {
				URLConnection conexaoFeita = link.openConnection();
				conexaoFeita.setUseCaches(false);
				InputStream entrada = conexaoFeita.getInputStream();
				System.out.println("Deu certo");
				DataInputStream lendo = new DataInputStream(entrada);
				System.out.println(lendo.available());
			
				GsonBuilder a = new GsonBuilder();
			
				byte[] todosBytesLidos = new byte[lendo.available()];
				lendo.readFully(todosBytesLidos);
//				String teste = lendo.readLine();
				System.out.println(""+Charset.defaultCharset().name());
				String string = new String(todosBytesLidos,Charset.forName("UTF-8"));
				System.out.println(string);
				JsonParser ae = new JsonParser();
				JsonElement elemento = ae.parse(string);
				
				if (elemento.isJsonObject()) {
					System.out.println("� um mapa");
					JsonObject mapa = elemento.getAsJsonObject();
					for (Entry<String, JsonElement> item : mapa.entrySet()) {
						System.out.println(item.getKey()+" : "+item.getValue()+"");
						
					}
				}
//				Gson g = new Gson();
				//				String a = lendo.readUTF();
//			
//				Scanner scan = new Scanner(lendo);
//				while (scan.hasNext()) {
//					String line = scan.nextLine();
//					System.out.println(line);
//				}
//				scan.close();

				System.out.println("Deu certo");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
