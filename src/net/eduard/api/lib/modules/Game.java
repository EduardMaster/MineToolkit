package net.eduard.api.lib.modules;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.map.MapView;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;






/**
 * No dia 17/01/2020 a API GameAPI, WorldAPI e classes KeyType, Point, Replacer, foram adicionadas a uma classe
 * chamada Game onde terá tudo dentro para ser uma versão da Mine só que em uma classe modular e não presa a toda a API EduardAPI
 * <br>
 * Antiga API de controle de Entidades, Localizacoes e muito mais<br>
 * <br>
 * Depedencia {@link Extra}
 * <br>
 *  Classe Integrada chamada WorldAPI:<br>
 *  API de controle e manipulação de Mundos e Localizações e Cuboids (Uma expecie
 * de Bloco retangular)<br>
 *  Dependências: {@link LocationEffect} , {@link Point}
 *	<br>
 *	  Classe Integrada chaamda Balde:<br>
 *	Classe Bukkit com comandos traduzidos para o Português
 *
 * @version 1.0
 * @since Lib v1.0 <br> EduardAPI 5.2
 * @author Eduard
 *
 */
public final class Game {





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

	public static MapView criarMapaDoMundo(World mundo) {
		return Bukkit.createMap(mundo);
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

	/**
	 * Mensagem de Boas Vindas, mensagem que vista na hora de conectar ao servidor
	 * @return MOTD
	 */
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

	public static int pegarRaioDaParteInicialDoMundoPadrão() {
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

	/*

	----------------------------- ClASSES DEPENDECIAS ABAIXO ---------------------------

	 */

	/**
	 * Efeito a fazer na Localização
	 *
	 * @author Eduard
	 *
	 */
	public interface LocationEffect {

		boolean effect(Location location);
	}

	/**
	 * Interface de criar Replacer (Placeholders)
	 *
	 * @author Eduard
	 *
	 */
	public  interface Replacer {
		/**
		 * Retorna o valor do Placeholder
		 *
		 * @param player Jogador
		 * @return O placeholder
		 */
		Object getText(Player player);
	}


	/**
	 * Ponto de direção usado para fazer um RADAR
	 *  @author Internet
	 *
	 *
	 */
	public  enum Point {
		N('N'), NE('/'), E('O'), SE('\\'), S('S'), SW('/'), W('L'), NW('\\');

		public final char asciiChar;

		private Point(char asciiChar) {
			this.asciiChar = asciiChar;
		}

		@Override
		public String toString() {
			return String.valueOf(this.asciiChar);
		}

		public String toString(boolean isActive, ChatColor colorActive, String colorDefault) {
			return (isActive ? colorActive : colorDefault) + String.valueOf(this.asciiChar);
		}

		public String toString(boolean isActive, String colorActive, String colorDefault) {
			return (isActive ? colorActive : colorDefault) + String.valueOf(this.asciiChar);
		}
	}


		/**
		 * Mapa que armazena as Armaduras dos jogadores
		 */
		public static Map<Player, ItemStack[]> INV_ARMOURS = new HashMap<>();
		/**
		 * Mapa que armazena os Itens dos jogadores tirando as Armaduras
		 */
		public static Map<Player, ItemStack[]> INV_ITEMS = new HashMap<>();

		/**
		 * Cria um item da Cabeça do Jogador
		 * 
		 * @param name
		 *            Nome
		 * @param owner
		 *            Nome do Jogador
		 * @param amount
		 *            Quantidade
		 * @param lore
		 *            Descri§§o (Lista)
		 * @return O Item da Cabe§a do jogador criada
		 */
		public static ItemStack newHead(String name, String owner, int amount,
				List<String> lore) {
			ItemStack item = new ItemStack(Material.SKULL_ITEM, amount,
					(short) SkullType.PLAYER.ordinal());
			SkullMeta meta = (SkullMeta) item;
			meta.setOwner(owner);
			meta.setDisplayName(name);
			meta.setLore(lore);
			item.setItemMeta(meta);
			return item;
		}
		
		/**
		 * Testa se o numero passado § da coluna expecificada
		 * 
		 * @param index
		 *            Numero
		 * @param colunm
		 *            Coluna
		 * @return O resultado do teste
		 */
		public static boolean isColumn(int index, int colunm) {
			return getColumn(index) == colunm;
		}
		/**
		 * Pega um Item aleatorio baseado na lista
		 * 
		 * @param items
		 *            Lista de Itens
		 * @return O item aletario
		 */
		public static ItemStack getRandomItem(List<ItemStack> items) {

			return Extra.getRandom(items);
		}

		/**
		 * Pega um Item aleatorio baseado no vetor
		 * 
		 * @param items
		 *            Vetor de Itens
		 * @return O item aletario
		 */
		public static ItemStack getRandomItem(ItemStack... items) {

			return Extra.getRandom(items);
		}

		/**
		 * Limpa o Inventario da Entidade viva
		 * 
		 * @param entity
		 *            Entidade viva
		 */
		public static void clearArmours(LivingEntity entity) {
			entity.getEquipment().setArmorContents(null);
		}
		/**
		 * Limpa a Hotbar do Jogador
		 * 
		 * @param player
		 *            Jogador
		 */
		public static void clearHotBar(Player player) {
			for (int i = 0; i < 9; i++) {
				player.getInventory().setItem(i, null);
			}
		}
		/**
		 * Cria um item da cabe§a do Jogador
		 * 
		 * @param name
		 * @param skull
		 * @return
		 */

		public static ItemStack newSkull(String name, String skull) {

			return setSkull(newItem(name, Material.SKULL_ITEM, 3), skull);
		}
		public static int getPosition(int line, int column) {
			int value = (line - 1) * 9;
			return value + column - 1;
		}

		/**
		 * Modifica um Item transformando ele na Cabe§a do Jogador
		 * 
		 * @param item
		 *            Item
		 * @param name
		 * @return Nome do Jogador
		 */
		public static ItemStack setSkull(ItemStack item, String name) {
			SkullMeta meta = (SkullMeta) item.getItemMeta();
			meta.setOwner(name);
			item.setItemMeta(meta);
			return item;
		}
		/**
		 * Limpa todo o Inventario do Jogador
		 * 
		 * @param player
		 */
		public static void clearInventory(Player player) {
			clearItens(player);
			clearArmours(player);
		}
		/**
		 * Limpa os itens da Entidade viva
		 * 
		 * @param entity
		 *            Entidade viva
		 */
		public static void clearItens(LivingEntity entity) {
			entity.getEquipment().clear();

		}

		/**
		 * Restaura os itens armazenado no Jogador
		 * 
		 * @param player
		 *            Jogador
		 */
		public static void getItems(Player player) {
			if (INV_ITEMS.containsKey(player)) {
				player.getInventory().setContents(INV_ITEMS.get(player));
				player.updateInventory();
			}
			getArmours(player);

		}
		/**
		 * Restaura as armaduras armazenado no Jogador
		 * 
		 * @param player
		 *            Jogador
		 */
		public static void getArmours(Player player) {
			if (INV_ARMOURS.containsKey(player)) {
				player.getInventory().setArmorContents(INV_ARMOURS.get(player));
				player.updateInventory();
			}
		}
		/**
		 * Pega a quantidade de itens do Invetario
		 * 
		 * @param inventory
		 *            Inventario
		 * @return Quantidade
		 */
		public static int getItemsAmount(Inventory inventory) {
			int amount = 0;
			for (ItemStack item : inventory.getContents()) {
				if (item != null) {
					amount++;
				}
			}

			return amount;
		}

		/**
		 * Modifca toda Hotbar para um Item
		 * 
		 * @param player
		 *            Jogador
		 * @param item
		 *            Item
		 */
		public static void setHotBar(Player player, ItemStack item) {
			PlayerInventory inv = player.getInventory();
			for (int i = 0; i < 8; i++) {
				inv.setItem(i, item);
			}
		}
		/**
		 * Modifica a Descri§§o do Item
		 * 
		 * @param item
		 *            Item
		 * @param lore
		 *            Descri§§o
		 * @return Item
		 */
		public static ItemStack setLore(ItemStack item, List<String> lore) {
			ItemMeta meta = item.getItemMeta();
			meta.setLore(lore);
			item.setItemMeta(meta);
			return item;
		}
		/**
		 * Pega a quantidade total dos itens do Inventario
		 * 
		 * @param inventory
		 *            Inventario
		 * @return quantidade
		 */
		public static int getTotalAmount(Inventory inventory) {
			int amount = 0;
			for (ItemStack item : inventory.getContents()) {
				if (item != null) {
					amount += item.getAmount();
				}
			}
			return amount;
		}
		/**
		 * Pega a quantidade total do Material do Inventario
		 * 
		 * @param inventory
		 *            Inventario
		 * @param material
		 *            Tipo do Material
		 * @return Quantidade
		 */
		public static int getTotalAmount(Inventory inventory, Material material) {
			int amount = 0;
			for (ItemStack id : inventory.all(material).values()) {
				amount += id.getAmount();
			}
			return amount;
		}
		/**
		 * Pega a quantidade total do Item do Inventario
		 * 
		 * @param inventory
		 *            Inventario
		 * @param item
		 *            Item
		 * @return Quantidade
		 */
		public static int getTotalAmount(Inventory inventory, ItemStack item) {
			int amount = 0;
			for (ItemStack id : inventory.all(item.getType()).values()) {
				if (id.isSimilar(item)) {
					amount += id.getAmount();
				}
			}
			return amount;
		}
		/**
		 * Remove itens se for igual a este<br>
		 * O inv.remove(...) tamb§m remove porem remove qualquer item n§o importanto
		 * nome, descri§§o, encantamentos
		 * 
		 * @param inventory
		 *            Inventario
		 * @param item
		 *            Item
		 */
		public static void remove(Inventory inventory, ItemStack item) {
			for (Entry<Integer, ? extends ItemStack> map : inventory
					.all(item.getType()).entrySet()) {
				if (map.getValue().isSimilar(item)) {
					inventory.clear(map.getKey());
				}
			}
		}
		/**
		 * Remove itens se for igual a este tipo de Material<br>
		 * O inv.remove(...) tamb§m remove porem remove qualquer item n§o importanto
		 * nome, descri§§o, encantamentos
		 * 
		 * @param inventory
		 *            Inventario
		 * @param material
		 *            Tipo do Material
		 */
		public static void remove(Inventory inventory, Material material,
				int amount) {
			remove(inventory, new ItemStack(material), amount);
		}
		/**
		 * Remove alguns itens se for igual a este Item<br>
		 * O inv.remove(...) tamb§m remove porem remove qualquer item n§o importanto
		 * nome, descri§§o, encantamentos
		 * 
		 * @param inventory
		 *            Inventario
		 * @param item
		 *            Item
		 * @param amount
		 *            Quantidade
		 */
		public static void remove(Inventory inventory, ItemStack item, int amount) {
			for (Entry<Integer, ? extends ItemStack> map : inventory
					.all(item.getType()).entrySet()) {
				if (map.getValue().isSimilar(item)) {
					ItemStack currentItem = map.getValue();
					if (currentItem.getAmount() <= amount) {
						amount -= currentItem.getAmount();
						inventory.clear(map.getKey());
					} else {
						currentItem.setAmount(currentItem.getAmount() - amount);
						amount = 0;
					}

				}
				if (amount == 0)
					break;
			}
		}
		/**
		 * Testa se o Inventario tem determinada quantidade do Item
		 * 
		 * @param inventory
		 *            Inventario
		 * @param item
		 *            Item
		 * @param amount
		 *            Quantidade
		 * @return net.eduard.api.Teste
		 */
		public static boolean contains(Inventory inventory, ItemStack item,
				int amount) {
			return getTotalAmount(inventory, item) >= amount;
		}
		/**
		 * Testa se o Inventario tem determinada quantidade do Tipo do Material
		 * 
		 * @param inventory
		 * @param item
		 * @param amount
		 * @return
		 */
		public static boolean contains(Inventory inventory, Material item,
				int amount) {
			return getTotalAmount(inventory, item) >= amount;
		}
		/**
		 * Adiciona um Encantamento no Item
		 * 
		 * @param item
		 *            Item
		 * @param type
		 *            Tipo do Material
		 * @param level
		 *            Nivel do Entamento
		 * @return Item
		 */
		public static ItemStack addEnchant(ItemStack item, Enchantment type,
				int level) {
			item.addUnsafeEnchantment(type, level);
			return item;
		}
		/**
		 * Adiciona itens na HotBar do Jogador
		 * 
		 * @param player
		 *            Jogador
		 * @param item
		 *            Item
		 */
		public static void addHotBar(Player player, ItemStack item) {
			PlayerInventory inv = player.getInventory();
			if (item == null)
				return;
			if (item.getType() == Material.AIR)
				return;
			int i;
			while ((i = inv.firstEmpty()) < 9) {
				inv.setItem(i, item);
			}
		}
		/**
		 * Cria um Inventario
		 * 
		 * @param name
		 *            Nome
		 * @param size
		 *            Tamanho do Inventario
		 * @return Inventario
		 */
		public static Inventory newInventory(String name, int size) {

			return Bukkit.createInventory(null, size, name);
		}
		/**
		 * Cria um Set de Couro para entidade viva
		 * 
		 * @param entity
		 *            Entidade viva
		 * @param color
		 *            Cor
		 * @param name
		 *            Nome
		 */
		public static void setEquip(LivingEntity entity, Color color, String name) {
			EntityEquipment inv = entity.getEquipment();
			inv.setBoots(setName(
					setColor(new ItemStack(Material.LEATHER_BOOTS), color), name));
			inv.setHelmet(setName(
					setColor(new ItemStack(Material.LEATHER_HELMET), color), name));
			inv.setChestplate(setName(
					setColor(new ItemStack(Material.LEATHER_CHESTPLATE), color),
					name));
			inv.setLeggings(setName(
					setColor(new ItemStack(Material.LEATHER_LEGGINGS), color),
					name));
		}
		/**
		 * Ganha todos os Itens do Inventario
		 * 
		 * @param items
		 *            Itens
		 * @param inventory
		 *            Inventario
		 */
		public static void give(Collection<ItemStack> items, Inventory inventory) {
			for (ItemStack item : items) {
				inventory.addItem(item);
			}
		}
		/**
		 * Pega o descri§§o do Item
		 * 
		 * @param item
		 *            Item
		 * @return Descri§§o
		 */
		public static List<String> getLore(ItemStack item) {
			if (item != null) {
				if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
					return item.getItemMeta().getLore();
				}
			}
			return new ArrayList<String>();
		}

		/**
		 * Testa se o Inventario esta cheio
		 * 
		 * @param inventory
		 *            Inventario
		 * @return net.eduard.api.Teste
		 */
		public static boolean isFull(Inventory inventory) {
			return inventory.firstEmpty() == -1;
		}
		/**
		 * Testa se a Entidade viva esta usando na mao o Tipo do Material
		 * 
		 * @param entity
		 *            Entitade
		 * @param material
		 *            Tipo de Material
		 * @return net.eduard.api.Teste
		 */
		public static boolean isUsing(LivingEntity entity, Material material) {
			return (getHandType(entity) == material);
		}
		/**
		 * Testa se a Entidade viva esta usando na mao um Tipo do Material com este
		 * nome
		 * 
		 * @param entity
		 *            Entidade
		 * @param material
		 *            Material
		 * @return
		 */
		public static boolean isUsing(LivingEntity entity, String material) {
			return getHandType(entity).name().toLowerCase()
					.contains(material.toLowerCase());
		}
		/**
		 * Dropa um item no Local da entidade
		 * 
		 * @param entity
		 *            Entidade
		 * @param item
		 *            Item
		 */
		public static void drop(Entity entity, ItemStack item) {
			drop(entity.getLocation(), item);
		}
		/**
		 * Pega o tipo do material da mao da Entidade viva
		 * 
		 * @param entity
		 *            Entidade viva
		 * @return Tipo do Material
		 */
		public static Material getHandType(LivingEntity entity) {
			EntityEquipment inv = entity.getEquipment();
			if (inv == null) {
				return Material.AIR;
			}
			ItemStack item = inv.getItemInHand();
			if (item == null) {
				return Material.AIR;
			}

			return item.getType();
		}

		/**
		 * Cria um Item da Cabe§a do Jogador
		 * 
		 * @param name
		 *            Nome de Jogador
		 * @return Item
		 */
		public static ItemStack getHead(String name) {
			ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			SkullMeta meta = (SkullMeta) item.getItemMeta();
			meta.setOwner(name);
			item.setItemMeta(meta);
			return item;
		}
		/**
		 * Testa se o Inventario esta vasio
		 * 
		 * @param inventory
		 * @return net.eduard.api.Teste
		 */
		public static boolean isEmpty(Inventory inventory) {

			for (ItemStack item : inventory.getContents()) {
				if (item != null) {
					if (item.getType()!=Material.AIR){
						return false;
					}

				}


			}
			return true;
		}
		/**
		 * Modifica a Cor do Item (Usado somente para Itens de Couro)
		 * 
		 * @param item
		 *            Item de Couro
		 * @param color
		 *            Cor
		 * @return Item
		 */
		public static ItemStack setColor(ItemStack item, Color color) {
			LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
			meta.setColor(color);
			item.setItemMeta(meta);
			return item;
		}
		/**
		 * Modifica a Descri§§o do Item
		 * 
		 * @param item
		 *            Item
		 * @param lore
		 *            Descri§§o
		 * @return Item
		 */
		public static ItemStack setLore(ItemStack item, String... lore) {

			ItemMeta meta = item.getItemMeta();
			if (meta != null) {
				meta.setLore(Arrays.asList(lore));
				item.setItemMeta(meta);
			}
			return item;
		}
		/**
		 * Modifica o Nome do Item
		 * 
		 * @param item
		 *            Item
		 * @param name
		 *            Novo Nome
		 * @return Item
		 */
		public static ItemStack setName(ItemStack item, String name) {
			ItemMeta meta = item.getItemMeta();

			if (meta != null) {
				meta.setDisplayName(name);
				item.setItemMeta(meta);
			}

			return item;
		}
		/**
		 * Restaura o Nome Original do Item
		 * 
		 * @param item
		 *            Item
		 * @return Nome
		 */
		public static ItemStack resetName(ItemStack item) {
			setName(item, "");
			return item;
		}
		/**
		 * Pega o Nome do Item
		 * 
		 * @param item
		 *            Item
		 * @return Nome
		 */
		public static String getName(ItemStack item) {

			return item.hasItemMeta()
					? item.getItemMeta().hasDisplayName()
							? item.getItemMeta().getDisplayName()
							: ""
					: "";
		}
		/**
		 * Dropa o Item no Local (Joga no Local)
		 * 
		 * @param location
		 *            Local
		 * @param item
		 *            --------- * Item
		 */
		public static void drop(Location location, ItemStack item) {
			location.getWorld().dropItemNaturally(location, item);
		}
		/**
		 * Enche o Invetario com o Item
		 * 
		 * @param inventory
		 *            Inventario
		 * @param item
		 *            Item
		 */
		public static void fill(Inventory inventory, ItemStack item) {
			int id;
			while ((id = inventory.firstEmpty()) != -1) {
				inventory.setItem(id, item);
			}
		}
		/**
		 * Pega a quantidade de dano causada pelo Item
		 * 
		 * @param item
		 *            Item
		 * @return Quantidade
		 */
		public static double getDamage(ItemStack item) {
			if (item == null)
				return 0;
			double damage = 0;
			String name = item.getType().name();
			for (int id = 0; id <= 4; id++) {
				String value = "";
				if (id == 0) {
					value = "DIAMOND_";
					damage += 3;
				}
				if (id == 1) {
					value = "IRON_";
					damage += 2;
				}
				if (id == 2) {
					value = "GOLD_";
				}
				if (id == 3) {
					value = "STONE_";
					damage++;
				}
				if (id == 4) {
					value = "WOOD_";
				}

				for (int x = 0; x <= 3; x++) {
					double newDamage = damage;
					if (x == 0) {
						value = "SWORD";
						newDamage += 4;
					}
					if (x == 1) {
						value = "AXE";
						newDamage += 3;
					}
					if (x == 2) {
						value = "PICKAXE";
						newDamage += 2;
					}
					if (x == 3) {
						value = "SPADE";
						newDamage++;
					}

					if (name.equals(value)) {
						return newDamage;
					}
				}
				damage = 0;
			}
			return damage;
		}
		/**
		 * Armazena os Itens do Jogador
		 * 
		 * @param player
		 */
		public static void saveItems(Player player) {
			saveArmours(player);
			INV_ITEMS.put(player, player.getInventory().getContents());
		}
		/**
		 * Armazena as armaduras do Jogador
		 * 
		 * @param player
		 */
		public static void saveArmours(Player player) {
			INV_ARMOURS.put(player, player.getInventory().getArmorContents());
		}
		/**
		 * Cria um Item
		 * 
		 * @param material
		 * @param name
		 * @return
		 */
		public static ItemStack newItem(Material material, String name) {
			ItemStack item = new ItemStack(material);
			setName(item, name);
			return item;
		}
		/**
		 * Cria um Item
		 * 
		 * @param material
		 *            Material
		 * @param name
		 *            Nome
		 * @param amount
		 *            Quantidade
		 * @return Item
		 */
		public static ItemStack newItem(Material material, String name,
				int amount) {
			return newItem(material, name, amount, 0);
		}

		/**
		 * Cria um Item
		 * 
		 * @param material
		 *            Material
		 * @param name
		 *            Nome
		 * @param amount
		 *            Quantidade
		 * @param data
		 *            MetaData
		 * @param lore
		 *            Descri§§o
		 * @return Item
		 */
		public static ItemStack newItem(Material material, String name, int amount,
				int data, String... lore) {

			ItemStack item = newItem(material, name);
			setLore(item, lore);
			item.setAmount(amount);
			item.setDurability((short) data);
			return item;
		}
		/**
		 * Cria um Item
		 * 
		 * @param id
		 *            ID do Material
		 * @param name
		 *            Nome
		 * @param amount
		 *            Quantidade
		 * @param data
		 *            MetaData
		 * @param lore
		 *            Descri§§o
		 * @return Item
		 */
		@SuppressWarnings("deprecation")
		public static ItemStack newItem(int id, String name, int amount,
				int data, String... lore) {

			ItemStack item = new ItemStack(id,amount,(short)data);
			ItemMeta meta = item.getItemMeta();
			if (meta != null)
			{
				meta.setDisplayName(name);
				meta.setLore(Arrays.asList(lore));
				item.setItemMeta(meta);
			}
			return item;
		}
		/**
		 * Cria um Item
		 * 
		 * @param id
		 *           ID do  Material
		 * @param name
		 *            Nome
		 * @param amount
		 *            Quantidade
		 * @param data
		 *            MetaData
		 * @param lore
		 *            Descri§§o
		 * @return Item
		 */
		@SuppressWarnings("deprecation")
		public static ItemStack newItem(int id, String name, int amount,
				int data, List<String> lore) {

			ItemStack item = new ItemStack(id,amount,(short)data);
			ItemMeta meta = item.getItemMeta();
			if (meta != null)
			{
				meta.setDisplayName(name);
				meta.setLore(lore);
				item.setItemMeta(meta);
			}
			return item;
		}
		/**
		 * Cria um Item
		 * 
		 * @param name
		 *            Nome
		 * @param material
		 *            Material
		 * @return
		 */
		public static ItemStack newItem(String name, Material material) {
			ItemStack item = new ItemStack(material);
			setName(item, name);
			return item;
		}
		/**
		 * Cria um Item
		 * 
		 * @param name
		 *            Nome
		 * @param material
		 *            Material
		 * @param data
		 *            MetaData
		 * @return Item
		 */
		public static ItemStack newItem(String name, Material material, int data) {
			return newItem(material, name, 1, data);
		}

		/**
		 * Cria um Item
		 * 
		 * @param name
		 *            Nome
		 * @param material
		 *            Material
		 * @param amount
		 *            Quantidade
		 * @param data
		 *            Data
		 * @param lore
		 *            Descri§§o
		 * @return Item
		 */
		public static ItemStack newItem(String name, Material material, int amount,
				int data, String... lore) {
			return newItem(material, name, amount, data, lore);
		}
	public static List<LivingEntity> getNearbyEntities(LivingEntity player,
			double x, double y, double z, EntityType... types) {
		List<LivingEntity> list = new ArrayList<>();
		for (Entity item : player.getNearbyEntities(x, y, z)) {
			if (item instanceof LivingEntity) {
				LivingEntity livingEntity = (LivingEntity) item;
				if (types != null) {
					for (EntityType entitie : types) {
						if (livingEntity.getType().equals(entitie)) {
							if (!list.contains(livingEntity))
								list.add(livingEntity);
						}
					}
				} else
					list.add(livingEntity);
			}
		}
		return list;

	}
	  @SuppressWarnings("unchecked")
	public static <T extends Player> T getTarget(Player entity, Iterable<T> entities)
	  {
	    if (entity == null)
	      return null;
	    Player target = null;
//	    double threshold = 1.0D;
	    for (Player other : entities) {
	      Vector n = other.getLocation().toVector().subtract(entity.getLocation().toVector());
	      if ((entity.getLocation().getDirection().normalize().crossProduct(n).lengthSquared() < 1.0D) && 
	        (n.normalize().dot(entity.getLocation().getDirection().normalize()) >= 0.0D)) {
	        if ((target == null) || 
	          (target.getLocation().distanceSquared(entity.getLocation()) > other.getLocation()
	          .distanceSquared(entity.getLocation()))) {
	          target = other;
	        }
	      }
	    }
	    return (T) target;
	  }

	public static List<LivingEntity> getNearbyEntities(LivingEntity entity,
			double radio, EntityType... entities) {

		return getNearbyEntities(entity, radio, radio, radio, entities);

	}
	public static List<Player> getPlayerAtRange(Location location,
			double range) {

		List<Player> players = new ArrayList<>();
		for (Player p : location.getWorld().getPlayers()) {
			if (p.getLocation().distance(location) <= range) {
				players.add(p);
			}
		}
		return players;
	}
	public static boolean hasLightOn(Entity entity) {
		return hasLightOn(entity.getLocation());
	}
	public static boolean hasLightOn(Location location) {
		return hasLightOn(location.getBlock());
	}
	public static boolean hasLightOn(Block block) {
		return block.getLightLevel() > 10;
	}
	public static Player getRandomPlayer(){
		return getRandomPlayer(getPlayers());
	}
	public static Player getRandomPlayer(List<Player> list){
		return Extra.getRandom(list);


	}
	
	public static void setDirection(Entity entity, Entity target) {
		entity.teleport(entity.getLocation()
				.setDirection(target.getLocation().getDirection()));
	}

	public static List<Player> getPlayers() {
		List<Player> lista = new ArrayList<>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			lista.add(p);
		}
		return lista;
	}
	
	


	public static void hide(Player player) {
		for (Player target :getPlayers()) { 
			if (target != player) {
				target.hidePlayer(player);
			}
		}
	}
	public static boolean isOnGround(Entity entity) {
		return entity.getLocation().getBlock().getRelative(BlockFace.DOWN)
				.getType() != Material.AIR;
	}




	public static void makeInvunerable(Player player, int seconds) {
		player.setNoDamageTicks(seconds * 20);

	}

	public static void makeVulnerable(Player player) {

		player.setNoDamageTicks(0);
	}

	public static void moveTo(Entity entity, Location target, double gravity) {
		Location location = entity.getLocation().clone();
		double distance = target.distance(location);
		double x = -(gravity - ((target.getX() - location.getX()) / distance));
		double y = -(gravity - ((target.getY() - location.getY()) / distance));
		double z = -(gravity - ((target.getZ() - location.getZ()) / distance));
		Vector vector = new Vector(x, y, z);
		entity.setVelocity(vector);
	}

	public static void moveTo(Entity entity, Location target, double staticX,
			double staticY, double staticZ, double addX, double addY,
			double addZ) {
		Location location = entity.getLocation();
		if (entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) entity;
			location = livingEntity.getEyeLocation();
		}
		entity.setVelocity(getVelocity(location, target, staticX, staticY,
				staticZ, addX, addY, addZ));
	}
	public static boolean isFlying(Entity entity) {
		return entity.getLocation().getBlock().getRelative(BlockFace.DOWN, 2)
				.getType() == Material.AIR;
	}
	
	public static boolean isInvulnerable(Player player) {
		return player.getNoDamageTicks() > 1;
	}
	
	public static LightningStrike strike(LivingEntity living, int maxDistance) {
		return strike(getTargetLoc(living, maxDistance));
	}

	public static LightningStrike strike(Location location) {
		return location.getWorld().strikeLightning(location);
	}

	public static void teleport(Entity entity, Location target) {
		entity.teleport(
				target.setDirection(entity.getLocation().getDirection()));
	}
	public static void removeEffects(Player player) {
		player.setFireTicks(0);
		for (PotionEffect effect : player.getActivePotionEffects()) {
			player.removePotionEffect(effect.getType());
		}
	}
	public static void resetLevel(Player player) {
		player.setLevel(0);
		player.setExp(0);
		player.setTotalExperience(0);
	}
	

	public static void setDirection(Entity entity, Location target) {
		Location location = entity.getLocation().clone();
		if (entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) entity;
			location = livingEntity.getEyeLocation().clone();

		}
		entity.teleport(entity.getLocation()
				.setDirection(getDiretion(location, target)));

	}
	public static Vector getDiretion(Location location, Location target) {
		double distance = target.distance(location);
		double x = ((target.getX() - location.getX()) / distance);
		double y = ((target.getY() - location.getY()) / distance);
		double z = ((target.getZ() - location.getZ()) / distance);
		return new Vector(x, y, z);
	}
	

	public static void changeTabName(Player player, String displayName) {
		player.setPlayerListName(displayName);
	}
	public static String toText(int size, String text) {

		return text.length() > size ? text.substring(0, size) : text;
	}
	
	public static Vector getVelocity(Location entity, Location target,
			double staticX, double staticY, double staticZ, double addX,
			double addY, double addZ) {
		double distance = target.distance(entity);
		double x = (staticX + (addX * distance))
				* ((target.getX() - entity.getX()) / distance);
		double y = (staticY + (addY * distance))
				* ((target.getY() - entity.getY()) / distance);
		double z = (staticZ + (addZ * distance))
				* ((target.getZ() - entity.getZ()) / distance);
		return new Vector(x, y, z);

	}


	public static void refreshAll(Player player) {
		clearInventory(player);
		removeEffects(player);
		refreshLife(player);
		refreshFood(player);
		makeVulnerable(player);
		resetLevel(player);
	}

	public static void refreshFood(Player player) {
		player.setFoodLevel(20);
		player.setSaturation(20);
		player.setExhaustion(0);
	}

	public static void refreshLife(Player p) {
		p.setHealth(p.getMaxHealth());
	}



	public static void setSpawn(Entity entity) {

		entity.getWorld().setSpawnLocation((int) entity.getLocation().getX(),
				(int) entity.getLocation().getY(),
				(int) entity.getLocation().getZ());
	}
	public static void show(Player player) {
		for (Player target : getPlayers()) {
			if (target != player) {
				target.showPlayer(player);
			}
		}
	}
	public static void teleport(LivingEntity entity, int range) {
		teleport(entity, getTargetLoc(entity, range));
	}

	public static void teleportToSpawn(Entity entity) {

		entity.teleport(entity.getWorld().getSpawnLocation()
				.setDirection(entity.getLocation().getDirection()));
	}
	public static boolean isFalling(Entity entity) {
		return entity.getVelocity().getY() < -0.08F;
	}
	public static List<Player> getOnlinePlayers() {
		return getPlayers();
	}
	public static Location getTargetLoc(LivingEntity entity, int distance) {
		@SuppressWarnings("deprecation")
		Block block = entity.getTargetBlock((HashSet<Byte>)null, distance);
		return block.getLocation();
	}
	
	public static Player getNearestPlayer(Player player) {
		double dis = 0.0D;
		Player target = null;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (dis == 0.0D) {
				dis = p.getLocation().distance(player.getLocation());
				target = p;
			} else {
				double newdis = p.getLocation().distance(player.getLocation());
				if (newdis < dis) {
					dis = newdis;
					target = p;
				}
			}
		}

		return target;
	}
	public static void fixDrops(List<ItemStack> drops) {
		HashMap<ItemStack, ItemStack> itens = new HashMap<>();
		for (ItemStack drop : drops) {
			Material type = drop.getType();
			if (type == Material.AIR | type == null) {
				continue;
			}
			boolean find = false;
			for (Entry<ItemStack, ItemStack> entry : itens.entrySet()) {
				if (drop.isSimilar(entry.getKey())) {
					ItemStack item = entry.getKey();
					item.setAmount(item.getAmount() + drop.getAmount());
					find = true;
					break;
				}
			}
			if (!find) {
				itens.put(drop, drop);
			}
			
		}
		drops.clear();
		drops.addAll(itens.values());
	}
	/**
	 * Descobre qual é a coluna baseada no numero
	 * 
	 * @param index Numero
	 * @return A coluna
	 */
	public static int getColumn(int index) {
		if (index < 9) {
			return index + 1;
		}
		return (index % 9) + 1;
	}
	
	public static int getLine(int index	) {
		return (index/9)+1;
	}



	/*

	--------------------------------------- WORLD API ABAIXO -------------------------------




	 */


	public static boolean equals(Location location1, Location location2) {

		return getBlockLocation1(location1).equals(getBlockLocation1(location2));
	}

	public static boolean equals2(Location location1, Location location2) {
		return location1.getBlock().getLocation().equals(location2.getBlock().getLocation());
	}

	public static List<Location> getLocations(Location location1, Location location2) {
		return getLocations(location1, location2, new LocationEffect() {

			@Override
			public boolean effect(Location location) {
				return true;
			}
		});
	}

	public static Location getHighLocation(Location loc, double high, double size) {

		loc.add(size, high, size);
		return loc;
	}


	public static void deleteWorld(String name) {
		World world = Bukkit.getWorld(name);
		if (world != null) {
			for (Player p : world.getPlayers()) {
				try {
					p.teleport(
							Bukkit.getWorlds().get(0).getSpawnLocation().setDirection(p.getLocation().getDirection()));

				} catch (Exception e) {
					p.kickPlayer("§cRestarting Server!");
				}
			}

		}
		Bukkit.unloadWorld(name, true);
		Extra.deleteFolder(new File(Bukkit.getWorldContainer(), name.toLowerCase()));
	}



	public static World copyWorld(String fromWorld, String toWorld) {
		unloadWorld(fromWorld);
		unloadWorld(toWorld);
		deleteWorld(toWorld);
		Extra.copyWorldFolder(getWorldFolder(fromWorld), getWorldFolder(toWorld));
		return loadWorld(toWorld);
	}

	public static void unloadWorld(String name) {
		World world = Bukkit.getWorld(name);
		if (world != null) {
			World mundoPadrao = Bukkit.getWorlds().get(0);
			for (Player p : world.getPlayers()) {
				p.teleport(mundoPadrao.getSpawnLocation());
			}

		}
		Bukkit.unloadWorld(name, true);
	}

	public static World loadWorld(String name) {
		return new WorldCreator(name).createWorld();
	}

	public static File getWorldFolder(String name) {
		return new File(Bukkit.getWorldContainer(), name.toLowerCase());
	}

	public static Location getHighLocation(Location loc1, Location loc2) {

		double x = Math.max(loc1.getX(), loc2.getX());
		double y = Math.max(loc1.getY(), loc2.getY());
		double z = Math.max(loc1.getZ(), loc2.getZ());
		return new Location(loc1.getWorld(), x, y, z);
	}

	public static List<Location> getLocations(Location location1, Location location2, LocationEffect effect) {

		Location min = getLowLocation(location1, location2);
		Location max = getHighLocation(location1, location2);
		List<Location> locations = new ArrayList<>();
		for (double x = min.getX(); x <= max.getX(); x++) {
			for (double y = min.getY(); y <= max.getY(); y++) {
				for (double z = min.getZ(); z <= max.getZ(); z++) {
					Location loc = new Location(min.getWorld(), x, y, z);
					try {
						boolean r = effect.effect(loc);
						if (r) {
							try {
								locations.add(loc);
							} catch (Exception ex) {
							}
						}
					} catch (Exception ex) {
					}

				}
			}
		}
		return locations;

	}

	public static Location getLowLocation(Location loc, double low, double size) {

		loc.subtract(size, low, size);
		return loc;
	}

	public static Location getLowLocation(Location location1, Location location2) {
		double x = Math.min(location1.getX(), location2.getX());
		double y = Math.min(location1.getY(), location2.getY());
		double z = Math.min(location1.getZ(), location2.getZ());
		return new Location(location1.getWorld(), x, y, z);
	}

	public static Location getBlockLocation1(Location location) {

		return new Location(location.getWorld(), (int) location.getX(), (int) location.getY(), (int) location.getZ());
	}

	public static Location getBlockLocation2(Location location) {

		return location.getBlock().getLocation();
	}

	public static List<Location> getBox(Location playerLocation, double higher, double lower, double size,
										LocationEffect effect) {
		Location high = getHighLocation(playerLocation.clone(), higher, size);
		Location low = getLowLocation(playerLocation.clone(), lower, size);
		return getLocations(low, high, effect);
	}

	public static List<Location> setBox(Location playerLocation, double higher, double lower, double size,
										Material wall, Material up, Material down, boolean clearInside) {
		return getBox(playerLocation, higher, lower, size, new LocationEffect() {

			@Override
			public boolean effect(Location location) {

				if (location.getBlockY() == playerLocation.getBlockY() + higher) {
					location.getBlock().setType(up);
					return true;
				}
				if (location.getBlockY() == playerLocation.getBlockY() - lower) {
					location.getBlock().setType(down);
					return true;
				}

				if (location.getBlockX() == playerLocation.getBlockX() + size
						|| location.getBlockZ() == playerLocation.getBlockZ() + size
						|| location.getBlockX() == playerLocation.getBlockX() - size
						|| location.getBlockZ() == playerLocation.getBlockZ() - size) {
					location.getBlock().setType(wall);
					return true;
				}
				if (clearInside) {
					if (location.getBlock().getType() != Material.AIR)
						location.getBlock().setType(Material.AIR);
				}
				return false;
			}
		});
	}

	public static List<Location> getBox(Location playerLocation, double higher, double lower, double size) {
		return getBox(playerLocation, higher, lower, size, new LocationEffect() {

			@Override
			public boolean effect(Location location) {
				return true;
			}
		});
	}

	public static List<Location> getBox(Location playerLocation, double xHigh, double xLow, double zHigh, double zLow,
										double yLow, double yHigh) {
		Location low = playerLocation.clone().subtract(xLow, yLow, zLow);
		Location high = playerLocation.clone().add(xHigh, yHigh, zHigh);
		return getLocations(low, high);
	}

	public static Location getRandomPosition(Location location, int xVar, int zVar) {
		return getHighPosition(getRandomLocation(location, xVar, 0, zVar));

	}

	public static double distanceX(Location loc1, Location loc2) {
		return loc1.getX() - loc2.getX();
	}

	public static double distanceZ(Location loc1, Location loc2) {
		return loc1.getZ() - loc2.getZ();
	}

	public static void unloadWorld(World world) {
		try {
			Bukkit.getServer().unloadWorld(world, false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}


	public static Location getRandomLocation(Location location, int xVar, int yVar, int zVar) {
		int x = location.getBlockX();
		int z = location.getBlockZ();
		int y = location.getBlockY();
		int xR = Extra.getRandomInt(x - xVar, x + xVar);
		int zR = Extra.getRandomInt(z - zVar, z + zVar);
		int yR = Extra.getRandomInt(y - yVar, y + zVar);
		return new Location(location.getWorld(), xR, yR, zR);
	}

	public static Location getHighPosition(Location location) {
		return location.getWorld().getHighestBlockAt(location).getLocation();
	}

	public static Location getSpawn() {
		return Bukkit.getWorlds().get(0).getSpawnLocation();
	}

	public static Point getCompassPointForDirection(double inDegrees) {
		double degrees = (inDegrees - 180.0D) % 360.0D;
		if (degrees < 0.0D) {
			degrees += 360.0D;
		}

		if ((0.0D <= degrees) && (degrees < 22.5D))
			return Point.N;
		if ((22.5D <= degrees) && (degrees < 67.5D))
			return Point.NE;
		if ((67.5D <= degrees) && (degrees < 112.5D))
			return Point.E;
		if ((112.5D <= degrees) && (degrees < 157.5D))
			return Point.SE;
		if ((157.5D <= degrees) && (degrees < 202.5D))
			return Point.S;
		if ((202.5D <= degrees) && (degrees < 247.5D))
			return Point.SW;
		if ((247.5D <= degrees) && (degrees < 292.5D))
			return Point.W;
		if ((292.5D <= degrees) && (degrees < 337.5D))
			return Point.NW;
		if ((337.5D <= degrees) && (degrees < 360.0D)) {
			return Point.N;
		}
		return null;
	}

	public static ArrayList<String> getAsciiCompass(Point point, ChatColor colorActive, String colorDefault) {
		ArrayList<String> ret = new ArrayList<>();

		String row = "";
		row = row + Point.NW.toString(Point.NW == point, colorActive, colorDefault);
		row = row + Point.N.toString(Point.N == point, colorActive, colorDefault);
		row = row + Point.NE.toString(Point.NE == point, colorActive, colorDefault);
		ret.add(row);

		row = "";
		row = row + Point.W.toString(Point.W == point, colorActive, colorDefault);
		row = row + colorDefault + "+";
		row = row + Point.E.toString(Point.E == point, colorActive, colorDefault);
		ret.add(row);

		row = "";
		row = row + Point.SW.toString(Point.SW == point, colorActive, colorDefault);
		row = row + Point.S.toString(Point.S == point, colorActive, colorDefault);
		row = row + Point.SE.toString(Point.SE == point, colorActive, colorDefault);
		ret.add(row);

		return ret;
	}

	public static ArrayList<String> getAsciiCompass(double inDegrees, ChatColor colorActive, String colorDefault) {
		return getAsciiCompass(getCompassPointForDirection(inDegrees), colorActive, colorDefault);
	}


}
