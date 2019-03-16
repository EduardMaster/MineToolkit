package net.eduard.api.lib.old;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Sistema de criar configuração usando PARSER próprio semelhante {@link Properties}<br>
 * Versão nova {@link SimplesConfig} 2.0
 * @since 0.7
 * @author Eduard
 * @version 1.0
 * @deprecated Use {@link Properties}
 */
class ConfigProperties {
	private List<String> list = new ArrayList<>();

	private Path file;

	public File getFile() {
		return this.file.toFile();
	}

	public Path getPath() {
		return this.file;
	}

	public static ConfigProperties copy(ConfigProperties oldSave, ConfigProperties newSave) {
		newSave.list = oldSave.list;
		return newSave;
	}

	public ConfigProperties reloadConfig(ConfigProperties copy) {
		this.list = copy.list;
		return this;
	}

	public ConfigProperties(String pathName) {
		this.file = Paths.get(pathName, new String[0]);
		if (getFile().exists()) {
			reloadConfig();
		} else {
			saveConfig();
		}
	}

	public ConfigProperties saveConfig() {
		try {
			Files.write(this.file, this.list, new OpenOption[0]);
		} catch (Exception e) {
			System.err.println("Could not save");
		}

		return this;
	}

	public ConfigProperties reloadConfig() {
		try {
			List<String> lines = Files.readAllLines(this.file);
			this.list.clear();
			for (String line : lines) {
				if (line.startsWith("# ")) {
					this.list.add(line);
				} else if (line.contains(" = ")) {
					String[] a = line.split(" = ");
					setSection(a[0], a[1]);
				}
			}
		} catch (Exception e) {
			System.err.println("Could not reload");
		}
		return this;
	}

	public ConfigProperties setSection(String name, Object obj) {
		if (name.isEmpty())
			return this;
		String line = name + " = " + obj.toString();
		int index = getIndex(name);
		if (index != -1) {
			this.list.set(index, line);
		} else {
			this.list.add(line);
		}
		return this;
	}

	public Object getSection(String name) {
		int index = getIndex(name);
		if (index != -1)
			return ((String) this.list.get(index)).split(" = ")[1];
		return null;
	}

	public String getString(String name) {
		return getSection(name).toString();
	}

	public int getInteger(String name) {
		return ObjectConverter.toInt(getSection(name));
	}

	public double getDouble(String name) {
		return ObjectConverter.toDouble(getSection(name));
	}

	public float getFloat(String name) {
		return ObjectConverter.toFloat(getSection(name));
	}

	public byte getByte(String name) {
		return ObjectConverter.toByte(getSection(name));
	}

	public short getShort(String name) {
		return ObjectConverter.toShort(getSection(name));
	}

	public long getLong(String name) {
		return ObjectConverter.toLong(getSection(name));
	}

	public ConfigProperties removeLine(int lineIndex) {
		if (lineIndex > this.list.size())
			return this;
		this.list.remove(lineIndex);
		return this;
	}

	public boolean hasSection(String name) {
		for (String text : this.list) {
			if (text.startsWith(name))
				return true;
		}
		return false;
	}

	public boolean removeSection(String name) {
		int index = getIndex(name);
		if (index != -1) {
			removeLine(index);
			return true;
		}
		return false;
	}

	public boolean removeComment(int index) {
		String line = (String) this.list.get(index);
		if (line.startsWith("# ")) {
			this.list.remove(index);
			return true;
		}
		return false;
	}

	public ConfigProperties addComment(int place, String comment) {
		if (place > this.list.size()) {
			this.list.add("# " + comment.replace("#", ""));
		} else {
			this.list.add(place, comment);
		}

		return this;
	}

	public ConfigProperties setHeader(String header) {
		if (getLine(0).startsWith("# ")) {
			this.list.set(0, "# " + header.replace("#", ""));
		} else {
			this.list.add(0, "# " + header.replace("#", ""));
		}

		return this;
	}

	public String getLine(int lineIndex) {
		if (lineIndex > this.list.size()) {
			return "";
		}
		return (String) this.list.get(lineIndex);
	}

	private int getIndex(String name) {
		int index = 0;
		for (String text : this.list) {
			if (text.startsWith(name))
				return index;
			index++;
		}
		return -1;
	}

	public ConfigProperties showLines() {
		for (String line : this.list) {
			System.out.println(line);
		}
		return this;
	}
}
