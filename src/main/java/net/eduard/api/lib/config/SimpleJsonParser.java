package net.eduard.api.lib.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sistema de parseamento de JSON simples
 * 
 * @author Eduard
 * @version 0.1
 * 
 *
 */
public class SimpleJsonParser {

	public static final char OBJECT_START = '{';
	public static final char OBJECT_END = '}';
	public static final char ARRAY_START = '[';
	public static final char ARRAY_END = ']';
	public static final char STRING_START = '\"';
	public static final char STRING_END = '\"';
	public static final char ENTRY_SEPARATOR = ':';
	public static final char ITEM_SEPARATOR = ',';
	public static final char SPACE = ' ';
	public static final char ENTER = '\n';
	private char[] chars;
	private int index;
	private String jsonOriginal;

	public SimpleJsonParser(String json) {
		this.jsonOriginal = json;
		
		this.chars = json.toCharArray();
		debug("STARTING: " + json);
	}

	public String parseString() {
		debug("READING A NORMAL STRING");
		int letraStart = index;
		int count = 0;
		while (true) {
			char letra = read();
			if (letra == STRING_END) {
				break;
			}
			count++;
			if (index == jsonOriginal.length()) {
				break;
			}
		}
		String finalStr = String.copyValueOf(chars, letraStart, count);
		debug("READED STR: " + finalStr);
		return finalStr;
	}

	public char read() {

		char letra = chars[index++];
		debug("->  " + letra + "  <-");
		return letra;
	}

	public Map<String, Object> parseObject() {
		debug("READING A MAP OR OBJECT");
		Map<String, Object> mapa = new HashMap<>();
		String key = null;
		Object value = null;
		while (true) {
			char letra = read();
			if (key == null) {
				if (letra == STRING_START) {
					key = parseString();
				}
			} else if (value == null) {
				value = parse();
				mapa.put(key, value);
			}
			if (letra == ITEM_SEPARATOR) {
				key = null;
				value = null;
				continue;
			}
			if (letra == OBJECT_END) {
				break;
			}
			if (index == jsonOriginal.length()) {
				break;
			}
		}
		return mapa;

	}

	public String readAnormalString() {
		debug("READING ANORMAL STRING");
		StringBuilder b = new StringBuilder();
		while (true) {
			char letra = read();
			if (letra == ITEM_SEPARATOR) {
				index--;
				break;
			}
			if (letra == ARRAY_END) {
				index--;
				break;
			}
			b.append(letra);
		}
		debug("STR: " + b.toString() + "|");
		return b.toString();
	}

	public Object parse() {
		debug("TRYING TO PARSE");

		while (index != jsonOriginal.length()) {
			char letra = read();
			if (letra == OBJECT_START) {
				return parseObject();
			} else if (letra == ARRAY_START) {
				return parseList();
			} else if (letra == STRING_START) {
				return parseString();
			} else if (letra == ENTRY_SEPARATOR) {
			} else if (letra == SPACE) {
			} else if (letra == ENTER) {
			} else {
				index--;
				return readAnormalString();
			}

		}
		return null;
	}

	public static void debug(String msg) {
		System.out.println("[JSON PARSER] " + msg);
	}

	public char getChar() {
		return chars[index];
	}

	public List<Object> parseList() {
		debug("READING LIST");
		List<Object> list = new ArrayList<>();
		while (true) {
		

			if (index == jsonOriginal.length()) {
				break;
			}
			char letra = read();
			if (letra== ITEM_SEPARATOR) {
				continue;
			}
			if (letra == ARRAY_END) {
				debug("LIST ENDED");
				break;
			}
			index--;
			list.add(parse());

		}
		return list;
	}

	public String getJsonOriginal() {
		return jsonOriginal;
	}

	public void setJsonOriginal(String jsonOriginal) {
		this.jsonOriginal = jsonOriginal;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public char[] getChars() {
		return chars;
	}

	public void setChars(char[] chars) {
		this.chars = chars;
	}

}
