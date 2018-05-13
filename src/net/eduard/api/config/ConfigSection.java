package net.eduard.api.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.StorageAPI;
import net.eduard.api.lib.storage.game.Sounds;

/**
 * Sistema de PARSER YAML e YML
 * 
 * @author Eduard
 * @version 2.0
 * 
 * @see Config
 */
public class ConfigSection {

	private static String getComment(String line) {
		String[] split = line.split("#");
		if (split.length > 0)
			return line.replaceFirst(split[0] + "#", "").replaceFirst(" ", "");
		return line.replaceFirst("#", "").replaceFirst(" ", "");

	}

	private static String getKey(String line, String space) {
		line = line.replaceFirst(space, "");
		return line.split(":")[0];

	}

	private static String getList(String line) {
		String[] split = line.split("-");
		if (split.length > 0)
			return line.replaceFirst(split[0] + "-", "").replaceFirst(" ", "");
		return line.replaceFirst("-", "");
	}

	private static String getPath(String path) {
		if (path.startsWith("#")) {
			path.replaceFirst("#", "$");
		}
		if (path.startsWith("-")) {
			path.replaceFirst("-", "$");
		}
		if (path.contains(":")) {
			path.replace(":", "$");
		}
		return path;
	}

	/**
	 * Usar builder porque da menos lag
	 * 
	 * @param amount
	 * @return
	 */
	private static String getSpace(int amount) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < amount; i++) {
			builder.append("  ");
		}
		return builder.toString();
	}

	private static int getSpace(String line) {
		int value = 0;
		while (line.startsWith("  ")) {
			line = line.replaceFirst("  ", "");
			value++;
		}
		return value;
	}

	private static String getValue(String line, String space) {
		line = line.replaceFirst(space, "");
		if (line.endsWith(":")) {
			return "";
		}
		String[] split = line.split(":");
		String result = line.replaceFirst(split[0] + ":", "").replaceFirst(" ", "");
		return result;

	}

	private static String removeQuotes(String message) {
		if (message.startsWith("'")) {
			message = message.replaceFirst("'", "");
		}
		if (message.startsWith("\"")) {
			message = message.replaceFirst("\"", "");
		}
		if (message.endsWith("'")) {
			message = message.substring(0, message.length() - 1);
		}
		if (message.endsWith("\"")) {
			message = message.substring(0, message.length() - 1);
		}
		return message;
	}

	private static boolean isComment(String line) {
		return line.replace(" ", "").startsWith("#");
	}

	private static boolean isList(String line) {
		return line.replace(" ", "").startsWith("-");
	}

	private static boolean isSection(String line) {
		return !isList(line) & !isComment(line) & line.contains(":");
	}

	int lineSpaces;

	Object object;

	ConfigSection father;

	String key;

	// Object result;

	// Map<String, ConfigSection> sections = new LinkedHashMap<>();

	// List<Object> list = new ArrayList<>();

	List<String> comments = new ArrayList<>();

	public ConfigSection(ConfigSection father, String key, Object value) {
		this(key, value);
		this.father = father;
		father.getMap().put(key, this);

	}

	public Map<String, ConfigSection> getMap() {

		if (object != null) {
			if (!(object instanceof Map)) {
				this.object = new LinkedHashMap<String, ConfigSection>();
			}
		}

		@SuppressWarnings("unchecked")
		Map<String, ConfigSection> map = (Map<String, ConfigSection>) object;
		return map;
	}

	public ConfigSection(String key, Object value) {
		this.object = value;
		this.key = key;
	}

	public ConfigSection add(String path, Object value) {
		return add(path, value, new String[0]);
	}

	public ConfigSection add(String path, Object value, String... comments) {
		ConfigSection sec = getSection(path);
		if (!contains(path)) {
			set(path, value);
		}
		if (sec.comments.isEmpty())
			sec.setComments(comments);
		return sec;
	}

	public boolean contains(String path) {
		ConfigSection sec = getSection(path);
		boolean contains = sec.object != null && sec.object != "";
		if (!contains) {
			remove(path);
		}
		return contains;
	}

	public List<Integer> getIntList(String path) {
		return getSection(path).getIntList();
	}

	public Object get() {
		if (object.equals("[]")) {
			return getList();
		}
		if (object.equals("{}")) {
			return getMap();
		}
		return object;
	}

	public Object get(String path) {
		return getSection(path).getValue();
	}

	public boolean getBoolean() {
		return Mine.toBoolean(object);
	}

	public boolean getBoolean(String path) {
		return getSection(path).getBoolean();
	}

	public Double getDouble() {
		return Mine.toDouble(object);
	}

	public Double getDouble(String path) {
		return getSection(path).getDouble();
	}

	public Float getFloat() {
		return Mine.toFloat(object);
	}

	public Float getFloat(String path) {
		return getSection(path).getFloat();
	}

	public int getIndent() {
		return lineSpaces;
	}

	public Integer getInt() {
		return Mine.toInt(object);
	}

	public Integer getInt(String path) {
		return getSection(path).getInt();
	}

	public List<Integer> getIntList() {
		ArrayList<Integer> list = new ArrayList<>();
		for (Object item : getList()) {
			list.add(Mine.toInt(item));
		}
		return list;
	}

	public ItemStack getItem() {
		return (ItemStack) getValue();
	}

	public ItemStack getItem(String path) {
		return getSection(path).getItem();
	}

	public String getKey() {
		return key;
	}

	public Set<String> getKeys() {
		return getMap().keySet();
	}

	public Set<String> getKeys(String path) {
		return getSection(path).getKeys();
	}

	public List<Object> getList() {
		if (!(object instanceof List)) {
			object = new ArrayList<Object>();
		}
		@SuppressWarnings("unchecked")
		List<Object> list = (List<Object>) object;
		return list;
	}

	public Location getLocation() {
		return (Location) getValue();
	}

	public Location getLocation(String path) {
		return getSection(path).getLocation();
	}

	public Long getLong() {
		return Mine.toLong(object);
	}

	public Long getLong(String path) {
		return getSection(path).getLong();
	}

	public String getMessage() {

		return Mine.toChatMessage(getString());
	}

	public ArrayList<String> getMessages() {
		ArrayList<String> list = new ArrayList<>();
		for (String text : getStringList()) {
			list.add(Mine.toChatMessage(text));
		}
		return list;
	}

	public List<String> getMessages(String path) {
		return getSection(path).getMessages();
	}

	public ConfigSection getSection(String path) {
		path = getPath(path);
		if (path.contains(".")) {
			String[] split = path.replace(".", ",").split(",");
			String restPath = path.replaceFirst(split[0] + ".", "");
			return getSection(split[0]).getSection(restPath);
		} else {
			if (path.isEmpty())
				return null;
			if (getMap().containsKey(path)) {
				return getMap().get(path);
			}
			return new ConfigSection(this, path, "");
		}
	}

	public Set<Entry<String, ConfigSection>> getSet() {
		return getMap().entrySet();
	}

	public Sounds getSound() {
		return (Sounds) getValue();
	}

	public Sounds getSound(String path) {
		return getSection(path).getSound();
	}

	public String getString() {
		return removeQuotes(Mine.toString(object)).replace("/*", "\n").replace("\\n", "\n").replace("<br>", "\n")
				.replace("/n", "\n");
	}

	public String getString(String path) {
		return getSection(path).getString();
	}

	public List<String> getStringList() {
		ArrayList<String> list = new ArrayList<>();
		for (Object item : getList()) {
			list.add(removeQuotes(Mine.toString(item)));
		}
		return list;
	}

	public List<String> getStringList(String path) {
		return getSection(path).getStringList();
	}

	public Object getValue() {
		return StorageAPI.restoreData(isMap() ? toMap() : get(), null, null);

	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new LinkedHashMap<>();

		for (Entry<String, ConfigSection> entry : getSet()) {
			ConfigSection value = entry.getValue();
			String key = entry.getKey();
			if (value.isList()) {
				map.put(key, value.getList());
			} else if (value.isMap()) {
				map.put(key, value.toMap());
			} else {
				map.put(key, value.get());
			}

		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public void toSections(Map<Object, Object> map) {
		for (Entry<Object, Object> entry : map.entrySet()) {
			String key = entry.getKey().toString();
			Object value = entry.getValue();
			if (value instanceof Map) {
				ConfigSection sec = getSection(key);
				sec.getMap().clear();
				sec.toSections((Map<Object, Object>) value);
			} else if (value instanceof List) {
				getSection(key).set(value);
			} else {
				set(key, value);
			}
		}
	}

	public ConfigSection set(String path, Object value, String... comments) {
		ConfigSection sec = getSection(path);
		if (value == null) {
			sec.remove();
			return sec;
		}

		sec.set(StorageAPI.storeData(value, value.getClass(), null));
		sec.setComments(comments);
		return sec;
	}

	@SuppressWarnings("unchecked")
	public ConfigSection set(Object value) {
		if (value instanceof Map) {

			toSections((Map<Object, Object>) value);
		} else {
			this.object = value;
		}
		return this;
	}

	public Collection<ConfigSection> getValues() {
		return getMap().values();
	}

	public Collection<ConfigSection> getValues(String path) {
		return getSection(path).getValues();
	}

	public String message(String path) {
		return getSection(path).getMessage();
	}

	void save(Config config, int spaceId) {
		String space = getSpace(spaceId);
		for (String comment : comments) {
			config.lines.add(space + "# " + comment);
		}
		if (spaceId == -1) {
			config.lines.add("");
		}
		if (isList()) {
			config.lines.add(space + key + ": []");
			for (Object text : getList()) {
				config.lines.add(space + "- " + text);
			}
		} else if (isMap()) {
			if (spaceId != -1) {
				config.lines.add(space + key + ": {}");
			}
			for (ConfigSection section : getMap().values()) {
				section.save(config, spaceId + 1);
				for (int i = 0; i < lineSpaces; i++) {
					config.lines.add("");
				}
			}
		} else {
			if (spaceId == -1)
				return;
			config.lines.add(space + key + ": " + object);

		}

	}

	void reload(Config config) {
		int spaceId = 0;
		ConfigSection path = this;
		boolean headerSeted = false;
		List<String> currentComments = new ArrayList<>();
		// int index = 0;
		for (String line : config.lines) {
			// System.err.println("-> " + line);
			String space = getSpace(spaceId);
			if (!headerSeted
					&& (line.isEmpty() || (line.length() == 1 && !Character.isLetter(line.toCharArray()[0])))) {
				headerSeted = true;
				// System.out.println("index " + index);
				// index++;
				continue;
			}
			if (!headerSeted && isComment(line)) {
				comments.add(getComment(line));
			}
			if (headerSeted) {
				if (isList(line)) {
					path.getList().add(getList(line));
				} else if (isComment(line)) {
					currentComments.add(getComment(line));
				} else if (isSection(line)) {
					if (!line.startsWith("  ")) {
						spaceId = 0;
						path = this;
					} else if (!line.startsWith(space)) {
						int time = getSpace(line);
						while (time < spaceId) {
							path = path.father;
							spaceId--;
							// time = getSpace(line);
						}
					}
					space = getSpace(spaceId);
					path = path.getSection(getKey(line, space));
					path.set(getValue(line, space));
					path.comments.addAll(currentComments);
					currentComments.clear();
					spaceId++;
					// nao desencadeia apenas muda o percurso

				}
			}
			// index++;
		}

	}

	public void remove(String path) {
		getSection(path).remove();
	}

	public void remove() {
		father.getMap().remove(key);
	}

	public boolean isList() {
		return object instanceof List;
	}

	public boolean isMap() {
		return object instanceof Map;
	}

	public void setComments(List<String> list) {
		this.comments = list;
	}

	public void setComments(String... comments) {
		if (comments != null & comments.length > 0) {
			this.comments.clear();
			for (Object value : comments) {
				this.comments.add(Mine.toString(value));
			}
		}
	}

	public void setIndent(int amount) {
		lineSpaces = amount;
	}

}
