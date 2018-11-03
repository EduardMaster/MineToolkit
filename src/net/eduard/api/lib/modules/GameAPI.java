package net.eduard.api.lib.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import net.eduard.api.lib.Mine;





/**
 * API de controle de Entidades, Localizacoes e muito mais
 * @author Eduard
 * @version 1.0
 * @since Lib v1.0
 * @see Mine
 */

public final class GameAPI {
	
	/**
	 * API relacionada a cria§§o e manipula§§o de Itens do Minecraft
	 * 
	 * @version 1.0
	 * @since Lib v1.0 <br> EduardAPI 5.2
	 * @author Eduard
	 * @see Mine
	 */


		/**
		 * Mapa que armazena as Armaduras dos jogadores
		 */
		public static Map<Player, ItemStack[]> INV_ARMOURS = new HashMap<>();
		/**
		 * Mapa que armazena os Itens dos jogadores tirando as Armaduras
		 */
		public static Map<Player, ItemStack[]> INV_ITEMS = new HashMap<>();

		/**
		 * Cria um item da Cabe§a do Jogador
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

			return getRandom(items);
		}
		public static <E> E getRandom(List<E> objects) {
			if (objects.size() >= 1)
				return objects.get(getRandomInt(1, objects.size()) - 1);
			return null;
		}
		
		
		public static int getRandomInt(int minValue, int maxValue) {

			int min = Math.min(minValue, maxValue), max = Math.max(minValue, maxValue);
			return min + new Random().nextInt(max - min + 1);
		}
		/**
		 * Pega um Item aleatorio baseado no vetor
		 * 
		 * @param items
		 *            Vetor de Itens
		 * @return O item aletario
		 */
		public static ItemStack getRandomItem(ItemStack... items) {

			return getRandom(items);
		}
		@SafeVarargs
		public static <E> E getRandom(E... objects) {
			if (objects.length >= 1)
				return objects[getRandomInt(1, objects.length) - 1];
			return null;
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
		 * @param material
		 *            Tipo do Material
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
		 * @return Teste
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
		 * @return Teste
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
		 * @return Teste
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
		 * @return Teste
		 */
		public static boolean isEmpty(Inventory inventory) {

			for (ItemStack item : inventory.getContents()) {
				if (item != null) {
					return false;
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
		return list.get(getRandomInt(1, list.size())-1);
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
}
