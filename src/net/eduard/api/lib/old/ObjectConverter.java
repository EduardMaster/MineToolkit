package net.eduard.api.lib.old;

import net.eduard.api.lib.modules.Extra;

/**
 * API de conversão de tipos de objetos primitivos<br>
 * Versão anterior {@link EditSetup} 1.0
 * @since 0.9
 * @author Eduard
 * @version 2.0
 * @deprecated Métodos foram adicionado na {@link Extra}
 * 
 */
public class ObjectConverter {
	public static int toInt(Object object) {
		if ((object instanceof Number))
			return ((Number) object).intValue();
		try {
			return Integer.valueOf(object.toString()).intValue();
		} catch (NumberFormatException localNumberFormatException) {
		} catch (NullPointerException localNullPointerException) {
		}

		return 0;
	}

	public static float toFloat(Object object) {
		if ((object instanceof Number))
			return ((Number) object).floatValue();
		try {
			return Float.valueOf(object.toString()).floatValue();
		} catch (NumberFormatException localNumberFormatException) {
		} catch (NullPointerException localNullPointerException) {
		}

		return 0.0F;
	}

	public static double toDouble(Object object) {
		if ((object instanceof Number))
			return ((Number) object).doubleValue();
		try {
			return Double.valueOf(object.toString()).doubleValue();
		} catch (NumberFormatException localNumberFormatException) {
		} catch (NullPointerException localNullPointerException) {
		}

		return 0.0D;
	}

	public static long toLong(Object object) {
		if ((object instanceof Number))
			return ((Number) object).longValue();
		try {
			return Long.valueOf(object.toString()).longValue();
		} catch (NumberFormatException localNumberFormatException) {
		} catch (NullPointerException localNullPointerException) {
		}

		return 0L;
	}

	public static short toShort(Object object) {
		if ((object instanceof Number))
			return ((Number) object).shortValue();
		try {
			return Short.valueOf(object.toString()).shortValue();
		} catch (NumberFormatException localNumberFormatException) {
		} catch (NullPointerException localNullPointerException) {
		}

		return 0;
	}

	public static byte toByte(Object object) {
		if ((object instanceof Number))
			return ((Number) object).byteValue();
		try {
			return Byte.valueOf(object.toString()).byteValue();
		} catch (NumberFormatException localNumberFormatException) {
		} catch (NullPointerException localNullPointerException) {
		}

		return 0;
	}
}
