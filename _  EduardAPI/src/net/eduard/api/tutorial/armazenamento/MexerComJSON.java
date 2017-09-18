package net.eduard.api.tutorial.armazenamento;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.stream.JsonReader;

public class MexerComJSON {
	private static HttpURLConnection getConnection(String url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);
		
		connection.setRequestMethod("GET");
		connection.setRequestProperty("User-Agent", "PiraCraft");

		return connection;
	}
	public static void main(String[] args) {
		try {
			HttpURLConnection conn = getConnection("https://api.mojang.com/users/profiles/minecraft/"+"EduardKillerPro");
			conn.setConnectTimeout(5000);
			if (conn.getResponseCode() == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				@SuppressWarnings("resource")
				JsonReader r = new JsonReader(reader);
				r.beginObject();
				r.skipValue();
//				System.out.println(r.nextName());
				System.out.println(r.nextString());
				r.skipValue();
////				System.out.println(r.nextName());;
				System.out.println(r.nextString());;
				r.endObject();

			}else{
			}
		} catch (IOException ex) {
			System.out.println("ERRO: " + ex.getMessage());
		}
	}
}
