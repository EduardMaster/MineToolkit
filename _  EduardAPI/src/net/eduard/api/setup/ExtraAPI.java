package net.eduard.api.setup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
/**
 * API extra cheia de funcionalidades que sempre será preciso de uma forma
 * simplificada
 * 
 * @author Eduard
 *
 */
public final class ExtraAPI {
	private static Map<String, Replacer> replacers = new HashMap<>();
	public static interface Replacer {

		Object getText(Player p);
	}
	public static void sendMessage(Object... objects) {
		consoleMessage(objects);
		broadcastMessage(objects);
	}


	public static void resetScoreboards() {

		for (Team teams : getMainScoreboard().getTeams()) {
			teams.unregister();
		}
		for (Objective objective : getMainScoreboard().getObjectives()) {
			objective.unregister();
		}
		for (Player player : GameAPI.getPlayers()) {
			player.setScoreboard(getMainScoreboard());
			player.setMaxHealth(20);
			player.setHealth(20);
			player.setHealthScaled(false);
		}
	}

	/**
	 * Pega um Som baseado num Objeto (Texto)
	 * 
	 * @param object
	 *            Objeto
	 * @return Som
	 */
	public static Sound getSound(Object object) {
		String str = object.toString().replace("_", "").trim();
		for (Sound sound : Sound.values()) {
			if (str.equals("" + sound.ordinal())) {
				return sound;
			}
			if (str.equalsIgnoreCase(sound.name().replace("_", ""))) {
				return sound;
			}

		}
		return null;
	}

	public static String formatTime(long time) {
		return ObjectAPI.formatTime(time);
	}

	public static String formatDiference(long timestamp) {
		return formatTime(timestamp - System.currentTimeMillis());
	}

	public static long parseDateDiff(String time, boolean future)
			throws Exception {
		return ObjectAPI.parseDateDiff(time, future);
	}
	

	/**
	 * Pega uma lista de Entidades baseada em um Argumento (Texto)
	 * 
	 * @param argument
	 *            Texto
	 * @return Lista de Entidades
	 */
	public static List<String> getLivingEntities(String argument) {
		List<String> list = new ArrayList<>();
		argument = argument.trim().replace("_", "");
		for (EntityType type : EntityType.values()) {
			if (type == EntityType.PLAYER)
				continue;
			if (type.isAlive() & type.isSpawnable()) {
				String text = ExtraAPI.toTitle(type.name(), "");
				String line = type.name().trim().replace("_", "");
				if (startWith(line, argument)) {
					list.add(text);
				}
			}

		}
		return list;
	}
	@SuppressWarnings("deprecation")
	public static EntityType getEntity(Object object) {
		String str = object.toString().replace("_", "").trim();
		for (EntityType type : EntityType.values()) {
			if (str.equals("" + type.getTypeId())) {
				return type;
			}
			if (str.equalsIgnoreCase("" + type.getName())) {
				return type;
			}
			if (str.equalsIgnoreCase(type.name().replace("_", ""))) {
				return type;
			}

		}
		return null;
	}

	public static List<String> getEnchants(String argument) {
		if (argument == null) {
			argument = "";
		}
		argument = argument.trim().replace("_", "");
		List<String> list = new ArrayList<>();

		for (Enchantment enchant : Enchantment.values()) {
			String text = ExtraAPI.toTitle(enchant.getName(), "");
			String line = enchant.getName().trim().replace("_", "");
			if (startWith(line, argument)) {
				list.add(text);
			}
		}
		return list;

	}
	public static List<String> getSounds(String argument) {
		if (argument == null) {
			argument = "";
		}
		argument = argument.trim().replace("_", "");
		List<String> list = new ArrayList<>();

		for (Sound enchant : Sound.values()) {
			String text = ExtraAPI.toTitle(enchant.name(), "");
			String line = enchant.name().trim().replace("_", "");
			if (startWith(line, argument)) {
				list.add(text);
			}
		}
		return list;

	}
	@SuppressWarnings("deprecation")
	public static Enchantment getEnchant(Object object) {
		String str = object.toString().replace("_", "").trim();
		for (Enchantment enchant : Enchantment.values()) {
			if (str.equals("" + enchant.getId())) {
				return enchant;
			}
			if (str.equalsIgnoreCase(enchant.getName().replace("_", ""))) {
				return enchant;
			}
		}
		return null;
	}

	public static boolean getChance(double chance) {

		return ObjectAPI.getChance(chance);
	}
	public static boolean hasPerm(CommandSender sender, String permission,
			int max, int min) {

		boolean has = false;
		for (int i = max; i >= min; i--) {
			if (sender.hasPermission(permission + "." + i)) {
				has = true;
			}
		}
		return has;

	}

	public static PluginCommand command(String commandName,
			CommandExecutor command, String permission,
			String permissionMessage) {

		PluginCommand cmd = Bukkit.getPluginCommand(commandName);
		cmd.setExecutor(command);
		cmd.setPermission(permission);
		cmd.setPermissionMessage(permissionMessage);
		return cmd;
	}
	public static boolean commandEquals(String message, String cmd) {
		return ObjectAPI.commandEquals(message, cmd);
	}
	public static boolean commandStartWith(String message, String cmd) {
		return ObjectAPI.startWith(message, "/" + cmd);
	}
	public static boolean hasPlugin(String plugin) {
		return Bukkit.getPluginManager().getPlugin(plugin) != null;
	}

	/**
	 * Retorna se (now < (seconds + before));
	 * 
	 * @param before
	 *            (Antes)
	 * @param seconds
	 *            (Cooldown)
	 * @return
	 */
	public static boolean inCooldown(long before, long seconds) {
		return ObjectAPI.inCooldown(before, seconds);

	}
	public static long getCooldown(long before, long seconds) {
		return ObjectAPI.getCooldown(before, seconds);

	}

	public static boolean hasAPI() {
		return hasPlugin("EduardAPI");
	}
	public static Scoreboard getMainScoreboard() {
		return Bukkit.getScoreboardManager().getMainScoreboard();
	}
	public static long getNow() {
		return ObjectAPI.getNow();
	}
	@SafeVarargs
	public static <E> E getRandom(E... objects) {
		return ObjectAPI.getRandom(objects);
	}
	public static <E> E getRandom(List<E> objects) {
		return ObjectAPI.getRandom(objects);
	}
	public static boolean isMultBy(int number1, int numer2) {

		return ObjectAPI.isMultBy(number1, numer2);
	}
	public static void addPermission(String permission) {
		Bukkit.getPluginManager().addPermission(new Permission(permission));
	}
	public static boolean newExplosion(Location location, float power,
			boolean breakBlocks, boolean makeFire) {
		return location.getWorld().createExplosion(location.getX(),
				location.getY(), location.getZ(), power, breakBlocks, makeFire);
	}
	public static BukkitTask timer(Plugin plugin, long ticks, Runnable run) {
		if (run instanceof BukkitRunnable) {
			BukkitRunnable bukkitRunnable = (BukkitRunnable) run;
			return bukkitRunnable.runTaskTimer(plugin, ticks, ticks);
		}
		return Bukkit.getScheduler().runTaskTimer(plugin, run, ticks, ticks);
	}
	public static BukkitTask timers(Plugin plugin, long ticks, Runnable run) {
		if (run instanceof BukkitRunnable) {
			BukkitRunnable bukkitRunnable = (BukkitRunnable) run;
			return bukkitRunnable.runTaskTimerAsynchronously(plugin, ticks,
					ticks);
		}
		return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, run,
				ticks, ticks);
	}

	public static double getRandomDouble(double minValue, double maxValue) {
		return ObjectAPI.getRandomDouble(minValue, maxValue);
	}
	public static Firework newFirework(Location location, int high, Color color,
			Color fade, boolean trail, boolean flicker) {
		Firework firework = location.getWorld().spawn(location, Firework.class);
		FireworkMeta meta = firework.getFireworkMeta();
		meta.setPower(high);
		meta.addEffect(FireworkEffect.builder().trail(trail).flicker(flicker)
				.withColor(color).withFade(fade).build());
		firework.setFireworkMeta(meta);
		return firework;
	}
	public static int getRandomInt(int minValue, int maxValue) {
		return ObjectAPI.getRandomInt(minValue, maxValue);
	}

	public static void runCommand(String command) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
	}
	public static void makeCommand(String command) {
		runCommand(command);
	}

	public static boolean isIpProxy(String ip) {
		return ObjectAPI.isIpProxy(ip);
	}

	public static void callEvent(Event event) {

		Bukkit.getPluginManager().callEvent(event);
	}
	public static void event(Listener event, Plugin plugin) {
		registerEvents(event, plugin);
	}
	public static void registerEvents(Listener event, Plugin plugin) {
		Bukkit.getPluginManager().registerEvents(event, plugin);
	}

	public static BukkitTask delay(Plugin plugin, long ticks, Runnable run) {
		return Bukkit.getScheduler().runTaskLater(plugin, run, ticks);
	}
	public static BukkitTask delays(Plugin plugin, long ticks, Runnable run) {
		return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, run,
				ticks);
	}

	public static String getTime(int time) {

		return getTime(time, " segundo(s)", " minuto(s) ");

	}

	public static String getTime(int time, String second, String minute) {
		return ObjectAPI.getTime(time, second, minute);
	}

	public static String getTimeMid(int time) {

		return getTime(time, " seg", " min ");

	}

	public static String getTimeSmall(int time) {

		return getTime(time, "s", "m");

	}

	public static boolean startWith(String message, String text) {
		return message.toLowerCase().startsWith(text.toLowerCase());
	}

	public static String toChatMessage(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

	public static String toConfigMessage(String text) {
		return text.replace("§", "&");
	}

	public static String toDecimal(Object number) {
		return toDecimal(number, 2);
	}

	public static String toDecimal(Object number, int max) {
		return ExtraAPI.toDecimal(number, max);
	}

	public static String toText(Collection<String> message) {
		return ObjectAPI.toText(message);
	}

	public static String toText(int size, String text) {
		return ObjectAPI.toText(size, text);
	}

	public static String toText(String... message) {
		return ObjectAPI.toText(message);
	}

	public static String toText(String text) {

		return toText(16, text);
	}

	public static String toTitle(String name) {
		return ObjectAPI.toTitle(name);

	}

	public static String toTitle(String name, String replacer) {
		return ObjectAPI.toTitle(name, replacer);
	}
	public static boolean contains(String message, String text) {
		return message.toLowerCase().contains(text.toLowerCase());
	}

	public static String getText(int init, String... args) {
		StringBuilder text = new StringBuilder();
		int id = 0;
		for (String arg : args) {
			if (id < init) {
				id++;
				continue;
			}
			text.append(" " + toChatMessage(arg));
			id++;
		}
		return text.toString();
	}
	public static Double toDouble(Object object) {
		
		return ObjectAPI.toDouble(object);


	}

	public static Float toFloat(Object object) {
		return ObjectAPI.toFloat(object);

	}

	public static Integer toInt(Object object) {
		
		return ObjectAPI.toInt(object);

	}

	public static Integer toInteger(Object object) {
		return toInt(object);
	}

	public static Long toLong(Object object) {
		return ObjectAPI.toLong(object);
	}

	public static Short toShort(Object object) {
		return ObjectAPI.toShort(object);

	}
	public static Boolean toBoolean(Object obj) {
		return ObjectAPI.toBoolean(obj);
	}

	public static Byte toByte(Object object) {
		return ObjectAPI.toByte(object);

	}

	public static String toString(Object object) {

		return object == null ? "" : object.toString();
	}
	public static void sendActionBar(String message) {
		for (Player player : GameAPI.getPlayers()) {
			RexAPI.sendActionBar(player, message);
		}
	}
	public static void addReplacer(String key, Replacer value) {
		replacers.put(key, value);
	}
	public static Replacer getReplacer(String key) {
		return replacers.get(key);
	}

	public static String getWraps(String value) {
		return ObjectAPI.getWraps(value);
	}

	public static void chatMessage(CommandSender sender, Object... objects) {
		sender.sendMessage(getMessage(objects));
	}
	public static String getMessage(Object... objects) {
		String message = getText(objects);
		message = message.replace("$>", ChatAPI.getArrowRight());
		return getWraps(message);
	}

	public static void consoleMessage(Object... objects) {
		chatMessage(Bukkit.getConsoleSender(), objects);
	}

	public static void broadcastMessage(Object... objects) {

		for (Player p : GameAPI.getPlayers()) {
			chatMessage(p, objects);
		}
	}

	public static String getText(Object... objects) {
		return ObjectAPI.getText(objects);
	}

	public static void newWrapper(String wrap) {
		ObjectAPI.newWrapper(wrap);

	}

	public static String getReplacers(String text, Player player) {
		for (Entry<String, Replacer> value : replacers.entrySet()) {
			if (text.contains(value.getKey())) {
				try {
					text = text.replace(value.getKey(),
							"" + value.getValue().getText(player));

				} catch (Exception e) {
					consoleMessage(e.getMessage());
				}

			}

		}
		return text;
	}
	public static List<String> toLines(String text, int size) {
		return ObjectAPI.toLines(text, size);

	}
	public static String[] wordWrap(String rawString, int lineLength) {
		if (rawString == null) {
			return new String[]{""};
		}

		if ((rawString.length() <= lineLength)
				&& (!(rawString.contains("\n")))) {
			return new String[]{rawString};
		}

		char[] rawChars = new StringBuilder().append(rawString).append(' ')
				.toString().toCharArray();
		StringBuilder word = new StringBuilder();
		StringBuilder line = new StringBuilder();
		List<String> lines = new LinkedList<>();
		int lineColorChars = 0;

		for (int i = 0; i < rawChars.length; ++i) {
			char c = rawChars[i];

			if (c == 167) {
				word.append(ChatColor.getByChar(rawChars[(i + 1)]));
				lineColorChars += 2;
				++i;
			} else if ((c == ' ') || (c == '\n')) {
				if ((line.length() == 0) && (word.length() > lineLength)) {
					for (String partialWord : word.toString()
							.split(new StringBuilder().append("(?<=\\G.{")
									.append(lineLength).append("})")
									.toString()))
						lines.add(partialWord);
				} else if (line.length() + word.length()
						- lineColorChars == lineLength) {
					line.append(word);
					lines.add(line.toString());
					line = new StringBuilder();
					lineColorChars = 0;
				} else if (line.length() + 1 + word.length()
						- lineColorChars > lineLength) {
					for (String partialWord : word.toString()
							.split(new StringBuilder().append("(?<=\\G.{")
									.append(lineLength).append("})")
									.toString())) {
						lines.add(line.toString());
						line = new StringBuilder(partialWord);
					}
					lineColorChars = 0;
				} else {
					if (line.length() > 0) {
						line.append(' ');
					}
					line.append(word);
				}
				word = new StringBuilder();

				if (c == '\n') {
					lines.add(line.toString());
					line = new StringBuilder();
				}
			} else {
				word.append(c);
			}
		}

		if (line.length() > 0) {
			lines.add(line.toString());
		}

		if ((lines.get(0).length() == 0) || (lines.get(0).charAt(0) != 167)) {
			lines.set(0, new StringBuilder().append(ChatColor.WHITE)
					.append(lines.get(0)).toString());
		}
		for (int i = 1; i < lines.size(); ++i) {
			String pLine = lines.get(i - 1);
			String subLine = lines.get(i);

			char color = pLine.charAt(pLine.lastIndexOf(167) + 1);
			if ((subLine.length() == 0) || (subLine.charAt(0) != 167)) {
				lines.set(i,
						new StringBuilder().append(ChatColor.getByChar(color))
								.append(subLine).toString());
			}
		}

		return (lines.toArray(new String[lines.size()]));
	}
	public static String formatColors(String str) {
		char[] chars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a',
				'b', 'c', 'd', 'e', 'f', 'n', 'r', 'l', 'k', 'o', 'm'};
		char[] array = str.toCharArray();
		for (int t = 0; t < array.length - 1; t++) {
			if (array[t] == '&') {
				for (char c : chars) {
					if (c == array[(t + 1)]) {
						array[t] = '§';
					}
				}
			}
		}
		return new String(array);
	}
	public static void box(String[] paragraph, String title) {
		ArrayList<String> buffer = new ArrayList<String>();
		String at = "";

		int side1 = (int) Math.round(25.0D - (title.length() + 4) / 2.0D);
		int side2 = (int) (26.0D - (title.length() + 4) / 2.0D);
		at = at + '+';
		for (int t = 0; t < side1; t++) {
			at = at + '-';
		}
		at = at + "{ ";
		at = at + title;
		at = at + " }";
		for (int t = 0; t < side2; t++) {
			at = at + '-';
		}
		at = at + '+';
		buffer.add(at);
		at = "";
		buffer.add("|                                                   |");
		String[] arrayOfString = paragraph;
		int j = paragraph.length;
		for (int i = 0; i < j; i++) {
			String s = arrayOfString[i];
			at = at + "| ";
			int left = 49;
			for (int t = 0; t < s.length(); t++) {
				at = at + s.charAt(t);
				left--;
				if (left == 0) {
					at = at + " |";
					buffer.add(at);
					at = "";
					at = at + "| ";
					left = 49;
				}
			}
			while (left-- > 0) {
				at = at + ' ';
			}
			at = at + " |";
			buffer.add(at);
			at = "";
		}
		buffer.add("|                                                   |");
		buffer.add("+---------------------------------------------------+");

		System.out.println(" ");
		for (String line : buffer.toArray(new String[buffer.size()])) {
			System.out.println(line);
		}
		System.out.println(" ");
	}
	public static List<String> toMessages(List<Object> list) {
		List<String> lines = new ArrayList<String>();
		for (Object line : list) {
			lines.add(toChatMessage(line.toString()));
		}
		return lines;
	}
}
