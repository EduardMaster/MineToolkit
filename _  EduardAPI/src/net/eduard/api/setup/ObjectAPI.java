package net.eduard.api.setup;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * API de 
 * 
 * @author Eduard-PC
 *
 */
public final class ObjectAPI {
	public static String toChatMessage(String text) {
		return text.replace("&", "§");
	}
	public static List<String> toMessages(List<Object> list) {
		List<String> lines = new ArrayList<String>();
		for (Object line : list) {
			lines.add(toChatMessage(line.toString()));
		}
		return lines;
	}
	public static Random RANDOM = new Random();
	private static List<String> lineBreakers = new ArrayList<>();
	public static final float TNT = 4F;
	public static final float CREEPER = 3F;
	public static final float WALKING_VELOCITY = -0.08F;
	public static final int DAY_IN_HOUR = 24;
	public static final int DAY_IN_MINUTES = DAY_IN_HOUR * 60;
	public static final int DAY_IN_SECONDS = DAY_IN_MINUTES * 60;
	public static final long DAY_IN_TICKS = DAY_IN_SECONDS * 20;
	public static final long DAY_IN_LONG = DAY_IN_TICKS * 50;

	public static String formatTime(long time) {
		if (time == 0L) {
			return "never";
		}
		long day = TimeUnit.MILLISECONDS.toDays(time);
		long hours = TimeUnit.MILLISECONDS.toHours(time) - day * 24L;
		long minutes = TimeUnit.MILLISECONDS.toMinutes(time)
				- TimeUnit.MILLISECONDS.toHours(time) * 60L;
		long seconds = TimeUnit.MILLISECONDS.toSeconds(time)
				- TimeUnit.MILLISECONDS.toMinutes(time) * 60L;
		StringBuilder sb = new StringBuilder();
		if (day > 0L) {
			sb.append(day).append(" ").append(day == 1L ? "dia" : "dias")
					.append(" ");
		}
		if (hours > 0L) {
			sb.append(hours).append(" ").append(hours == 1L ? "hora" : "horas")
					.append(" ");
		}
		if (minutes > 0L) {
			sb.append(minutes).append(" ")
					.append(minutes == 1L ? "minuto" : "minutos").append(" ");
		}
		if (seconds > 0L) {
			sb.append(seconds).append(" ")
					.append(seconds == 1L ? "segundo" : "segundos");
		}
		String diff = sb.toString();
		return diff.isEmpty() ? "agora" : diff;
	}

	public static String formatDiference(long timestamp) {
		return formatTime(timestamp - System.currentTimeMillis());
	}

	public static long parseDateDiff(String time, boolean future)
			throws Exception {
		Pattern timePattern = Pattern.compile(
				"(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?",
				2);
		Matcher m = timePattern.matcher(time);
		int years = 0;
		int months = 0;
		int weeks = 0;
		int days = 0;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		boolean found = false;
		while (m.find())
			if ((m.group() != null) && (!m.group().isEmpty())) {
				for (int i = 0; i < m.groupCount(); i++) {
					if ((m.group(i) != null) && (!m.group(i).isEmpty())) {
						found = true;
						break;
					}
				}
				if (found) {
					if ((m.group(1) != null) && (!m.group(1).isEmpty())) {
						years = Integer.parseInt(m.group(1));
					}
					if ((m.group(2) != null) && (!m.group(2).isEmpty())) {
						months = Integer.parseInt(m.group(2));
					}
					if ((m.group(3) != null) && (!m.group(3).isEmpty())) {
						weeks = Integer.parseInt(m.group(3));
					}
					if ((m.group(4) != null) && (!m.group(4).isEmpty())) {
						days = Integer.parseInt(m.group(4));
					}
					if ((m.group(5) != null) && (!m.group(5).isEmpty())) {
						hours = Integer.parseInt(m.group(5));
					}
					if ((m.group(6) != null) && (!m.group(6).isEmpty())) {
						minutes = Integer.parseInt(m.group(6));
					}
					if ((m.group(7) == null) || (m.group(7).isEmpty()))
						break;
					seconds = Integer.parseInt(m.group(7));

					break;
				}
			}
		if (!found) {
			throw new Exception("Illegal Date");
		}
		if (years > 20) {
			throw new Exception("Illegal Date");
		}
		Calendar c = new GregorianCalendar();
		if (years > 0) {
			c.add(1, years * (future ? 1 : -1));
		}
		if (months > 0) {
			c.add(2, months * (future ? 1 : -1));
		}
		if (weeks > 0) {
			c.add(3, weeks * (future ? 1 : -1));
		}
		if (days > 0) {
			c.add(5, days * (future ? 1 : -1));
		}
		if (hours > 0) {
			c.add(11, hours * (future ? 1 : -1));
		}
		if (minutes > 0) {
			c.add(12, minutes * (future ? 1 : -1));
		}
		if (seconds > 0) {
			c.add(13, seconds * (future ? 1 : -1));
		}
		return c.getTimeInMillis();
	}

	public static boolean getChance(double chance) {

		return Math.random()<=chance;
	}

	public static boolean commandEquals(String message, String cmd) {
		String command = message;
		if (message.contains(" "))
			command = message.split(" ")[0];
		return command.equalsIgnoreCase(cmd);
	}
	public static boolean commandStartWith(String message, String cmd) {
		return startWith(message, "/" + cmd);
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

		long now = System.currentTimeMillis();
		long cooldown = seconds * 1000;
		return now <= (cooldown + before);

	}
	public static long getCooldown(long before, long seconds) {

		long now = System.currentTimeMillis();
		long cooldown = seconds * 1000;

		// +5 - 19 + 15

		return +cooldown - now + before;

	}

	public static long getNow() {
		return System.currentTimeMillis();
	}
	@SafeVarargs
	public static <E> E getRandom(E... objects) {
		if (objects.length >= 1)
			return objects[getRandomInt(1, objects.length) - 1];
		return null;
	}
	public static <E> E getRandom(List<E> objects) {
		if (objects.size() >= 1)
			return objects.get(getRandomInt(1, objects.size()) - 1);
		return null;
	}
	public static boolean isMultBy(int number1, int numer2) {

		return number1 % numer2 == 0;
	}

	public static double getRandomDouble(double minValue, double maxValue) {

		double min = Math.min(minValue, maxValue),
				max = Math.max(minValue, maxValue);
		return min + (max - min) * RANDOM.nextDouble();
	}
	public static int getRandomInt(int minValue, int maxValue) {

		int min = Math.min(minValue, maxValue),
				max = Math.max(minValue, maxValue);
		return min + RANDOM.nextInt(max - min + 1);
	}

	public static boolean isIpProxy(String ip) {
		try {
			String url = "http://botscout.com/test/?ip=" + ip;
			Scanner scanner = new Scanner(new URL(url).openStream());
			if (scanner.findInLine("Y") != null) {
				scanner.close();
				return true;
			}
			scanner.close();

		} catch (Exception e) {
		}
		return false;
	}

	public static String getTime(int time) {

		return getTime(time, " segundo(s)", " minuto(s) ");

	}

	public static String getTime(int time, String second, String minute) {
		if (time >= 60) {
			int min = time / 60;
			int sec = time % 60;
			if (sec == 0) {
				return min + minute;
			} else {
				return min + minute + sec + second;
			}

		}
		return time + second;
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

	public static String toConfigMessage(String text) {
		return text.replace("§", "&");
	}

	public static String toDecimal(Object number) {
		return toDecimal(number, 2);
	}

	public static String toDecimal(Object number, int max) {
		String text = "" + number;
		if (text.contains(".")) {
			String[] split = text.replace(".", ",").split(",");
			if (split[1].length() >= max) {
				return split[0] + "." + split[1].substring(0, max);
			}
			return text;
		}
		return text;
	}

	public static String toText(Collection<String> message) {
		return message.toString().replace("[", "").replace("]", "");
	}

	public static String toText(int size, String text) {

		return text.length() > size ? text.substring(0, size) : text;
	}

	public static String toText(String... message) {

		return message.toString().replace("[", "").replace("]", "");
	}

	public static String toText(String text) {

		return toText(16, text);
	}

	public static String toTitle(String name) {
		if (name == null)
			return "";
		char first = name.toUpperCase().charAt(0);
		name = name.toLowerCase();
		return first + name.substring(1, name.length());

	}

	public static String toTitle(String name, String replacer) {
		if (name.contains("_")) {
			String customName = "";
			int id = 0;
			for (String newName : name.split("_")) {
				if (id != 0) {
					customName += replacer;
				}
				id++;
				customName += toTitle(newName);
			}
			return customName;
		}
		return toTitle(name);
	}
	public static boolean contains(String message, String text) {
		return message.toLowerCase().contains(text.toLowerCase());
	}

	public static Double toDouble(Object object) {

		if (object == null) {
			return 0D;
		}
		if (object instanceof Double) {
			return (Double) object;
		}
		if (object instanceof Number) {
			Number number = (Number) object;
			return number.doubleValue();
		}
		try {
			return Double.valueOf(object.toString());
		} catch (Exception e) {
			return 0D;
		}

	}

	public static Float toFloat(Object object) {

		if (object == null) {
			return 0F;
		}
		if (object instanceof Float) {
			return (Float) object;
		}
		if (object instanceof Number) {
			Number number = (Number) object;
			return number.floatValue();
		}
		try {
			return Float.valueOf(object.toString());
		} catch (Exception e) {
			return 0F;
		}

	}

	public static Integer toInt(Object object) {

		if (object == null) {
			return 0;
		}
		if (object instanceof Integer) {
			return (Integer) object;
		}
		if (object instanceof Number) {
			Number number = (Number) object;
			return number.intValue();
		}
		try {
			return Integer.valueOf(object.toString());
		} catch (Exception e) {
			return 0;
		}

	}

	public static Integer toInteger(Object object) {
		return toInt(object);
	}

	public static Long toLong(Object object) {

		if (object == null) {
			return 0L;
		}
		if (object instanceof Long) {
			return (Long) object;
		}
		if (object instanceof Number) {
			Number number = (Number) object;
			return number.longValue();
		}
		try {
			return Long.valueOf(object.toString());
		} catch (Exception e) {
			return 0L;
		}
	}

	public static Short toShort(Object object) {

		if (object == null) {
			return 0;
		}
		if (object instanceof Short) {
			return (Short) object;
		}
		if (object instanceof Number) {
			Number number = (Number) object;
			return number.shortValue();
		}
		try {
			return Short.valueOf(object.toString());
		} catch (Exception e) {
			return 0;
		}

	}
	public static Boolean toBoolean(Object obj) {

		if (obj == null) {
			return false;
		}
		if (obj instanceof Boolean) {
			return (Boolean) obj;
		}
		try {
			return Boolean.valueOf(obj.toString());
		} catch (Exception e) {
			return false;
		}
	}

	public static Byte toByte(Object object) {

		if (object == null) {
			return 0;
		}
		if (object instanceof Byte) {
			return (Byte) object;
		}
		if (object instanceof Number) {
			Number number = (Number) object;
			return number.byteValue();
		}
		try {
			return Byte.valueOf(object.toString());
		} catch (Exception e) {
			return 0;
		}

	}

	public static String toString(Object object) {

		return object == null ? "" : object.toString();
	}

	public static String getWraps(String value) {
		for (String wrap : lineBreakers) {
			value = value.replaceAll(wrap, "\n");
		}
		return value;
	}

	public static String getMessage(Object... objects) {
		String message = getText(objects);
		message = message.replace("$>", ChatAPI.getArrowRight());
		return getWraps(message);
	}

	public static String getText(Object... objects) {
		StringBuilder builder = new StringBuilder();
		for (Object object : objects) {
			builder.append(object);

		}
		return builder.toString();
	}

	public static void newWrapper(String wrap) {
		if (!lineBreakers.contains(wrap))
			lineBreakers.add(wrap);

	}

	public static List<String> toLines(String text, int size) {

		List<String> lista = new ArrayList<>();

		String x = text;

		int id = 1;
		while (x.length() >= size) {
			String cut = x.substring(0, size);
			x = text.substring(id * size);
			id++;
			lista.add(cut);
		}
		lista.add(x);
		return lista;

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
	/**
	 * Tipo de geração de Key
	 * @author Eduard-PC
	 *
	 */
	public static enum KeyType {
		/**
		 * ID UNICO
		 */
		UUID, 
		/**
		 * LETRAS
		 */
		LETTER, 
		/**
		 * NUMEROS
		 */
		NUMERIC, 
		/**
		 * NUMEROS E LETRAS
		 */
		ALPHANUMERIC;
	}

	/**
	 * Gera uma nova Key
	 * @param type Tipo da Key
	 * @param maxSize Tamanho da Key
  	 * @return Key em forma de STRING
	 */
	public static String newKey(KeyType type, int maxSize) {

		String key = "";
		if (type == KeyType.UUID) {
			key = UUID.randomUUID().toString();
		} else if (type == KeyType.LETTER) {
			final StringBuffer buffer = new StringBuffer();
			String characters = "";
			characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
			final int charactersLength = characters.length();
			for (int i = 0; i < maxSize; ++i) {
				final double index = Math.random() * charactersLength;
				buffer.append(characters.charAt((int) index));
			}
			key = buffer.toString();
		} else if (type == KeyType.NUMERIC) {
			final StringBuffer buffer = new StringBuffer();
			String characters = "";
			characters = "0123456789";
			final int charactersLength = characters.length();
			for (int i = 0; i < maxSize; ++i) {
				final double index = Math.random() * charactersLength;
				buffer.append(characters.charAt((int) index));
			}
			key = buffer.toString();
		} else if (type == KeyType.ALPHANUMERIC) {
			final StringBuffer buffer = new StringBuffer();
			String characters = "";
			characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			final int charactersLength = characters.length();
			for (int i = 0; i < maxSize; ++i) {
				final double index = Math.random() * charactersLength;
				buffer.append(characters.charAt((int) index));
			}
			key = buffer.toString();
		}
		return key;

	}
	/**
	 * Pega o Ip do Coneção do Servidor
	 * @return Ip do Servidor
	 */
	public static String getServerIp() {
		try {
			URLConnection connect = new URL("http://checkip.amazonaws.com/")
					.openConnection();
			connect.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
			Scanner scan = new Scanner(connect.getInputStream());
			StringBuilder sb = new StringBuilder();
			while (scan.hasNext()) {
				sb.append(scan.next());
			}
			scan.close();
			return sb.toString();

		} catch (Exception ex) {

			String ip = null;
			return ip;
		}
	}
	
}
