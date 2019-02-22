package net.eduard.api.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

public class TestandoArquivos {

	public static void main(String[] args) throws Exception {
		File file = new File("E:/arquivo2.yml");
		YamlConfiguration config = new YamlConfiguration();
		config.set("Edu", "§2Boládo");
		FileOutputStream fileOutputStream = new FileOutputStream(file, false);
		fileOutputStream.write(config.saveToString().getBytes("UTF-8"));
		
//		File file2 = new File("E:/arquivo3.yml");
//		 FileOutputStream escrever = new FileOutputStream(file);
//		 FileOutputStream escrever2 = new FileOutputStream(file2);
//		String teste = "texto: Olá tudo bem com você\nRola de poço: Rolandinho";
//		escrever.write(teste.getBytes(StandardCharsets.UTF_8 ));
//		FileInputStream leitor = new FileInputStream(file);
//		byte[] bytesLidos = new byte[leitor.available()];
//		leitor.read(bytesLidos, 0, bytesLidos.length);
//		String texto = new String(bytesLidos,StandardCharsets.UTF_8  );
//		escrever2.write(texto.getBytes(StandardCharsets.ISO_8859_1));
//		System.out.println(texto);
		
	}
}
