package net.eduard.api.tutorial.nivel_2;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

public class SalvarConfig implements Listener {

	public SalvarConfig(FileConfiguration config) {
		// teste do plugin: Sistema de nomeclatura de mensagem/opção
		// Teste do Plugin: Sistema de nomeclatura de mensagem/secao
		// teste-do-plugin: Sistema de nomeclatura de mensagem/opção
		// Teste-do-Plugin: Sistema de nomeclatura de mensagem/secao
		// TesteDoPlugin: Sistema de nomeclatura de classe
		// testeDoPlugin: Sistema de nomeclatura de variavel/pacote
		// teste_do_plugin: Sistema de nomeclatura de variavel/pacote
		// Teste_do_Plugin: Sistema de nomeclatura de variavel
		config.set("ALGO_NA_CONFIG", 1);
		config.set("algo_na_config", 1);
		config.set("AlgoNaConfig", 1);
		config.set("algoNaConfig", 1);
		config.set("algo-na-config", 1);
		config.set("algo na config", 1);
		config.set("Algo na Config", 1);
		config.set("Algo-na-Config", 1);
		config.set("$player", 1);
		config.set("$player$", 1);
		config.set("<player>", 1);
		config.set("%player%", 1);
		config.set("%player", 1);
		config.set("@player@", 1);
		config.set("@player", 1);
		config.set("{player}", 1);
		config.set("[player]", 1);
		config.set("(player)", 1);

	}
}
