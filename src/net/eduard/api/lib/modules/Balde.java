package net.eduard.api.lib.modules;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

/**
 * Classe Bukkit com comandos traduzidos para o Português
 * 
 * @author Eduard
 * @version 0.1
 * @since 2.5
 */
public class Balde {

	public static void registrarCraft(Recipe craft) {
		Bukkit.addRecipe(craft);
	}
 
	public static void banirIP(String endereçoIP) {

		Bukkit.banIP(endereçoIP);
	}

	public static void enviarMensagemParaTodos(String mensagem) {
		Bukkit.broadcastMessage(mensagem);
	}

	public static void enviarMensagemParaQuemTemPermissão(String mensagem, String permissão) {
		Bukkit.broadcast(mensagem, permissão);
	}

	public static void removerCraftsExistentes() {
		Bukkit.clearRecipes();
	}

	public static ConsoleCommandSender pegarConsoleDoServidor() {
		return Bukkit.getConsoleSender();
	}

	public static void enviarMensagemParaConsole(String mensagem) {
		pegarConsoleDoServidor().sendMessage(mensagem);
	}

	public static void executarComandoNoConsole(String console) {
		Bukkit.dispatchCommand(pegarConsoleDoServidor(), console);
	}

	public static void criarMapaDoMundo(World mundo) {
		Bukkit.createMap(mundo);
	}

	public static boolean estaPermitidoIrNoMundoFim() {
		return Bukkit.getAllowEnd();
	}

	public static boolean estaPermitidoVoarNosMundos() {
		return Bukkit.getAllowFlight();
	}

	public static String pegarIPDoServidor() {
		return Bukkit.getIp();
	}

	public static String pegarVersãoDoServidor() {
		return Bukkit.getBukkitVersion();
	}

	public static boolean estaGerandoEstruturas() {
		return Bukkit.getGenerateStructures();
	}

	public static int pegarQuantidadeLimiteDeAnimaisVivos() {
		return Bukkit.getAnimalSpawnLimit();
	}

	public static int pegarQuantidadeLimiteDeAnimaisMarinhosVivos() {
		return Bukkit.getWaterAnimalSpawnLimit();
	}

	public static Set<OfflinePlayer> pegarJogadoresBanidos() {
		return Bukkit.getBannedPlayers();
	}

	public static boolean estaPermitidoIrNoNether() {
		return Bukkit.getAllowNether();
	}

	public static List<World> pegarMundos() {
		return Bukkit.getWorlds();
	}

	public static String pegarPortaDoServidor() {
		return "" + Bukkit.getPort();
	}

	public static int pegarQuantidadeMaximaDeJogadores() {
		return Bukkit.getMaxPlayers();
	}

	public static String pegarMOTD() {
		return Bukkit.getMotd();
	}

	public static ItemFactory pegarFabricaDeItens() {
		return Bukkit.getItemFactory();
	}

	public static Logger pegarInformador() {
		return Bukkit.getLogger();
	}

	public static File pegarArquivoOndeFicaSalvoOsMundos() {
		return Bukkit.getWorldContainer();
	}

	public static World pegarMundoPeloNome(String nome) {
		return Bukkit.getWorld(nome);
	}

	public static World pegarMundoPeloUUID(UUID id) {
		return Bukkit.getWorld(id);
	}

	public static int pegarRaioDaParteInicialDoMundo(UUID id) {
		return Bukkit.getSpawnRadius();
	}

	public static String pegarTipoServidor() {
		return Bukkit.getName();
	}

	public static int pegarDistanciaDeReceberInformaçõesDoServidor() {
		return Bukkit.getViewDistance();
	}

	public static int pegarQuantidadeDeMaximaDeJogadores() {
		return Bukkit.getMaxPlayers();
	}

	public static String pegarMensagemDeDesligamentoDoServidor() {
		return Bukkit.getShutdownMessage();
	}

	public static String pegarIDDoServidor() {
		return Bukkit.getServerId();
	}

	public static String pegarNomeDoServidor() {
		return Bukkit.getServerName();
	}

	public static ScoreboardManager pegarGerenciadorDeScoreboards() {
		return Bukkit.getScoreboardManager();
	}

	public static Scoreboard pegarScoreboardPadrão() {
		return Bukkit.getScoreboardManager().getMainScoreboard();
	}

	public static Map<String, String[]> pegarNomeAuxiliaresDosComandos() {
		return Bukkit.getCommandAliases();
	}

	public static boolean estaNoModoDifcil() {
		return Bukkit.isHardcore();
	}

	public static int pegarLimiteDeMonstrosVivos() {
		return Bukkit.getMonsterSpawnLimit();
	}

	public static Set<OfflinePlayer> pegarAdministradoresDoServidor() {
		return Bukkit.getOperators();
	}

	public static boolean estaAtivadoOriginal() {
		return Bukkit.getOnlineMode();
	}

	public static Messenger pegarMensageiro() {
		return Bukkit.getMessenger();
	}

	public static List<Recipe> pegarCraftsExistentesDoItem(ItemStack item) {
		return Bukkit.getRecipesFor(item);
	}

	public static GameMode pegarModoDeJogoPadrãoDoServidor() {
		return Bukkit.getDefaultGameMode();

	}

	public static Set<OfflinePlayer> pegarJogadorNaListaBranca() {
		return Bukkit.getWhitelistedPlayers();
	}
	
	

	public static Inventory criarInventarioDuplo(String titulo) {
		return criarInventario(null, InventoryType.CHEST, titulo);
	}

	public static Inventory criarInventario(InventoryType tipo, String titulo) {
		return criarInventario(null, tipo, titulo);
	}

	public static Inventory criarInventario(InventoryHolder dono, InventoryType tipo, String titulo) {
		return Bukkit.createInventory(dono, tipo, titulo);
	}
}
