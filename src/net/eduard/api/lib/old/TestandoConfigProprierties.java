package net.eduard.api.lib.old;

import java.io.IOException;

/**
 * Executando um teste sobre a calsse {@link ConfigProperties}
 * 
 * @author Eduard
 * @deprecated Classe feita apenas para testes
 * @since 0.7
 */

public class TestandoConfigProprierties {
	public static void main(String[] args) throws IOException {
		ConfigProperties c1 = new ConfigProperties("C:/API/teste.properties");
		c1.reloadConfig();
		c1.setSection("g", "e");
		c1.saveConfig();
		c1.showLines();
	}
}
