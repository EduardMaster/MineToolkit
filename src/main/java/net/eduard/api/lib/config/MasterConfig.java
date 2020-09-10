package net.eduard.api.lib.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Sistema Interpretador de YAML e JSON simultaneo
 * 
 * @author Eduard
 *
 */
public class MasterConfig {


	private static String getSpaces(int size) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < size; i++) {
			builder.append(SPACE);
		}
		return builder.toString();
	}

//	private static List<String> toLines(String line) {
//		List<String> lines = new ArrayList<>();
//		if (line.contains("\n")) {
//			String[] split = line.split("\n");
//			for (String current : split) {
//				lines.add(current);
//			}
//		} else
//			lines.add(line);
//		return lines;
//	}

	static char COMMENT = '#';
//	private static char SAVE = '!';
	private static char STRING_START = '\'';
	private static char TEXT_START = '\"';
	private static char STRING_END = '\'';
	private static char TEXT_END = '\"';
	private static char MAP_START = '{';
	private static char MAP_END = '}';
	private static char LIST_START = '[';
	private static char LIST_END = ']';
	private static char LIST_ITEM = '-';
	private static char ENTRY_DELIMITER = ':';
	private static char ITEM_DELIMITER = ',';
	private static char SPACE = ' ';
	private static char NEW_LINE = '\n';
	private static int TAB_SIZE = 2;

	protected Object value = null;
	protected StringBuilder text = new StringBuilder();
	private MasterSection root = new MasterSection(this, null, null);

	protected File folder;
	protected File file;
	protected String name = "config.yml";
	protected String section = ".";
	protected char[] textChars;
	protected int currentIndex;
	protected int currentLine;
	protected int lastLine;
	protected int currentLineIndex;
	protected int tabSize = TAB_SIZE;
	protected int currentTab;
	protected int spacesSkipped;

	public MasterConfig() {
		restartRead();
	}

	public MasterConfig(File folder, String name) {
		restartRead();
		this.folder = folder;
		this.file = new File(folder, name);
		this.name = name;
		reloadConfig();
	}

	public void saveResourceOf(Class<?> clz) {
		try {
			Files.deleteIfExists(file.toPath());
			Files.copy(clz.getResourceAsStream(name), file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveResource(boolean force) {

		if (!force && file.exists())
			return;
		saveResourceOf(getClass());

	}

	public void saveDefaultConfig() {
		saveResource(false);
		reloadConfig();
	}

	public boolean isFolder() {

		return name.endsWith("/");
	}

	public void debug() {
		System.out.println(" -------- ");
		System.out.println(text);

	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void restartRead() {
		this.textChars = text.toString().toCharArray();
		this.currentIndex = 0;
		this.currentLine = 0;
		this.lastLine = 0;
		this.currentLineIndex = 0;
		this.currentTab = 0;
		this.spacesSkipped = 0;
		// this.charsSkippedAmount = 0;
		// this.lastSpacesSkipped = 0;
	}

	protected void readContentFromFile() {
		file.getParentFile().mkdirs();

		BufferedReader reader;
		try {
			reader = Files.newBufferedReader(file.toPath(), Charset.forName("UTF-8"));
			text = new StringBuilder();
			int first = 0;
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (first != 0) {
					add(NEW_LINE);
				}
				add(line);
				first++;
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void writeContentToFile() {
		file.getParentFile().mkdirs();
		try {
			BufferedWriter writer = Files.newBufferedWriter(file.toPath(), Charset.forName("UTF-8"));
			writer.write(text.toString());
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected void add(Object value) {
		text.append(value);
	}

	protected void saveTab() {
		add(getSpaces(tabSize * currentTab));
	}

	protected void saveNewLine() {
		add(NEW_LINE);
		saveTab();
	}

	private void resetText() {
		this.text = new StringBuilder();
	}

	public void saveJson() {
		resetText();

		if (value == null) {
			add("{}");
		} else {
			saveAsJson(value);
		}
	}

	protected void saveAsJson(Object value) {
		if (value instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) value;
			add(MAP_START);
			currentTab++;
			saveNewLine();
			int first = 0;
			for (Entry<String, Object> entry : map.entrySet()) {
				if (first != 0) {
					add(ITEM_DELIMITER);
					add(NEW_LINE);
					saveTab();
				}
				if (entry.getKey().toCharArray()[0] == COMMENT) {
					add(COMMENT);
					add(SPACE);
					add(entry.getValue());
					add(SPACE);
					add(COMMENT);

				} else {
					saveAsJson(entry.getKey());
					add(ENTRY_DELIMITER);
					saveAsJson(entry.getValue());
					first++;
				}

			}
			add(NEW_LINE);
			currentTab--;
			saveTab();
			add(MAP_END);
		} else if (value instanceof List) {
			@SuppressWarnings("unchecked")
			List<Object> list = (List<Object>) value;
			add(LIST_START);
			currentTab++;
			saveNewLine();
			int first = 0;
			for (Object object : list) {
				if (first != 0) {
					add(ITEM_DELIMITER);
					add(NEW_LINE);
					saveTab();
				}
				saveAsJson(object);
				first++;
			}
			currentTab--;
			saveNewLine();
			add(LIST_END);
		} else if (value instanceof String) {
			add(STRING_START);
			add(value.toString());
			add(STRING_END);
		} else {
			add(TEXT_START);
			add(value.toString());
			add(TEXT_END);
		}

	}

	public void saveFast() {
		resetText();
		if (value == null) {
			add("{}");
		} else {
			saveOneLine(value);
		}
		// writeContentToFile();
	}

	protected void saveOneLine(Object value) {
		if (value instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) value;
			add(MAP_START);
			int first = 0;
			for (Entry<String, Object> entry : map.entrySet()) {
				if (first != 0) {
					add(ITEM_DELIMITER);
				}
				if (entry.getKey().toCharArray()[0] == COMMENT) {
					add(COMMENT);
					add(entry.getValue());
					add(COMMENT);
				} else {
					saveOneLine(entry.getKey());
					add(ENTRY_DELIMITER);
					saveOneLine(entry.getValue());
				}
				first++;
			}
			add(MAP_END);

		} else if (value instanceof List) {

			@SuppressWarnings("unchecked")
			List<Object> list = (List<Object>) value;
			add(LIST_START);
			int first = 0;
			for (Object object : list) {
				if (first != 0) {
					add(ITEM_DELIMITER);
				}
				saveOneLine(object);
				first++;
			}
			add(LIST_END);
		} else if (value instanceof String) {
			add(STRING_START);
			add(value.toString());
			add(STRING_END);
		} else {
			add(TEXT_START);
			add(value.toString());
			add(TEXT_END);
		}
	}

	public void saveConfig() {
		saveYaml();
		writeContentToFile();
		
	}

	protected void saveAsYaml(Object value) {
		if (value instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) value;
			currentTab++;
			saveNewLine();
			int first = 0;
			for (Entry<String, Object> entry : map.entrySet()) {
				if (first != 0) {
					add(NEW_LINE);
					saveTab();
				}
				if (entry.getKey().toCharArray()[0] == COMMENT) {
					add(COMMENT);
					add(SPACE);
					add(entry.getValue());
				} else {
					saveAsYaml(entry.getKey());
					add(ENTRY_DELIMITER);
					add(SPACE);
					saveAsYaml(entry.getValue());
				}
				first++;
			}
			currentTab--;
		} else if (value instanceof List) {
			@SuppressWarnings("unchecked")
			List<Object> list = (List<Object>) value;
			currentTab++;
			saveNewLine();
			int first = 0;
			for (Object object : list) {
				if (first != 0) {
					saveNewLine();
				}

				add(LIST_ITEM);
				add(SPACE);
				saveAsYaml(object);
				first++;

			}
			currentTab--;
		} else {
			add(value.toString());
		}
	}

	public void saveYaml() {
		resetText();
		if (value == null) {
			text.append("{}");
		} else {
			currentTab = -1;
			saveAsYaml(value);
		}
	}

	@SuppressWarnings("rawtypes")
	public MasterSection getConfig() {
		if (value instanceof Map) {
			root.setValue((Map) value);
		}
		return root;
	}

	public Object getValue() {
		return value;
	}

	public boolean contains(String path) {
		return root.contains(path);
	}

	public void set(String path, Object value) {
		root.set(path, value);
	}

	public Object get(String path, Object value) {
		return root.get(path);
	}

	protected void expected(char value) {
		expected(String.valueOf(value));
	}

	protected void expected(String value) {
		System.out.println("\nErro na linha: " + (currentLine + 1));
		System.out.println("->  " + String.copyValueOf(textChars, lastLine, currentLineIndex));
		System.out.println("->  " + getSpaces(currentLineIndex) + "^");
		System.out.println("Esperava-se: ( " + value + " )");
	}

	protected char getCurrentChar() {
		if (textChars.length == 0) {
			return '\n';
		}
		if (findEnd()) {
			return '?';
		}
		return textChars[currentIndex];
	}

	protected void toNextChar() {

		if (getCurrentChar() == NEW_LINE) {
			currentLine++;
			lastLine += currentLineIndex + 1;
			currentLineIndex = -1;
		}
		currentLineIndex++;
		currentIndex++;
	}

	protected char getNextChar() {
		currentIndex++;
		char value = getCurrentChar();
		currentIndex--;
		return value;
	}

	protected int ignoreSpaces() {
		return skipChars(SPACE);
	}

	protected int ignoreSpacesAndNewLines() {
		return skipChars(SPACE, NEW_LINE);
	}

	protected boolean findEnd() {
		return currentIndex >= textChars.length;
	}

	public boolean hasRoot() {
		return value instanceof Map;
	}

	public void reloadConfig() {
		if (!isFolder()) {
			if (file.exists()) {
				readContentFromFile();
				restartRead();
				this.value = readObject();
			}
		}
	}

	public Object readEntry(Map<String, Object> map) {
		try {
			if (getCurrentChar() == COMMENT) {
				map.put("#" + map.size() + 1, (String) readObject());
			} else {
				String key = (String) readObject();
				Object value = readObject();
				map.put(key, value);
			}

		} catch (Exception e) {
			expected("STR -> MAP KEY");
		}
		return map;
	}

	public Object readObject() {

		if (getCurrentChar() == MAP_START) {
			toNextChar();
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

			while (!findEnd()) {
				ignoreSpacesAndNewLines();
				if (getCurrentChar() == ITEM_DELIMITER) {
					toNextChar();
					continue;
				} else if (getCurrentChar() == MAP_END) {
					toNextChar();
					break;
				}
				readEntry(map);

			}
			return map;
		} else if (getCurrentChar() == LIST_START) {
			toNextChar();
			ArrayList<Object> list = new ArrayList<Object>();
			while (!findEnd()) {
				ignoreSpacesAndNewLines();
				if (getCurrentChar() == ITEM_DELIMITER) {
					toNextChar();
					continue;
				} else if (getCurrentChar() == LIST_END) {
					toNextChar();
					break;
				}
				list.add(readObject());

			}
			return list;
		} else if (getCurrentChar() == STRING_START) {
			toNextChar();
			StringBuilder str = new StringBuilder();
			while (!findEnd()) {
				if (getCurrentChar() == STRING_END) {
					toNextChar();
					break;
				}
				str.append(getCurrentChar());
				toNextChar();

			}
			return str.toString();
		} else if (getCurrentChar() == TEXT_START) {

			toNextChar();
			StringBuilder str = new StringBuilder();
			while (!findEnd()) {
				if (getCurrentChar() == TEXT_END) {
					toNextChar();
					break;
				}
				str.append(getCurrentChar());
				toNextChar();

			}
			return str.toString();
		} else if (getCurrentChar() == NEW_LINE) {
			// se§§o
			toNextChar();
			if (findEnd()) {
				return "";
			}

			// int last = spacesSkipped;
			ignoreSpacesAndNewLines();
			int init = spacesSkipped;
			if (getCurrentChar() == LIST_ITEM) {

				List<Object> list = new ArrayList<>();
				while (!findEnd() && getCurrentChar() == LIST_ITEM && spacesSkipped == init) {
					list.add(readObject());
					ignoreSpacesAndNewLines();
				}
				return list;

			} else {
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
				while (!findEnd() && spacesSkipped == init) {
					ignoreSpacesAndNewLines();
					readEntry(map);

				}
				return map;
			}

		} else if (getCurrentChar() == LIST_ITEM) {
			toNextChar();
			if (getCurrentChar() == SPACE) {
				toNextChar();
			}
			return readObject();
			// -
		} else if (getCurrentChar() == ENTRY_DELIMITER) {
			toNextChar();
			if (getCurrentChar() == SPACE) {
				toNextChar();
			}
			return readObject();

		} else if (getCurrentChar() == COMMENT) {
			toNextChar();
			StringBuilder str = new StringBuilder();
			while (!(findEnd())) {
				if (getCurrentChar() == NEW_LINE | getCurrentChar() == COMMENT) {
					toNextChar();
					break;
				}
				str.append(getCurrentChar());
				toNextChar();
			}
			return str.toString();
		} else {

			StringBuilder str = new StringBuilder();
			while (!findEnd()) {
				if (getCurrentChar() == ENTRY_DELIMITER | getCurrentChar() == COMMENT | getCurrentChar() == LIST_END
						| getCurrentChar() == MAP_END | getCurrentChar() == NEW_LINE) {
					// talves precise por
					// toNextChar();
					break;
				}
				str.append(getCurrentChar());
				toNextChar();
			}
			return str.toString();
		}

	}

	protected int skipChars(char... charsToSkip) {
		// lastSpacesSkipped = spacesSkipped;
		spacesSkipped = 0;
		boolean can = true;
		int amount = 0;
		while (can) {
			can = false;
			for (char var : charsToSkip) {

				if (var == getCurrentChar()) {
					can = true;

				}
			}
			if (getCurrentChar() == SPACE) {
				spacesSkipped++;
			}
			if (getCurrentChar() == NEW_LINE) {
				spacesSkipped = 0;
			}
			if (can) {
				toNextChar();
				// charsSkippedAmount++;
				amount++;
			}
		}
		return amount;
	}

}
