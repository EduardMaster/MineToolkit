package net.eduard.api.lib.old;

import net.eduard.api.lib.modules.Extra;

/**
 * API de controlar texto
 * 
 * @version 1.0
 * @since EduardAPI 0.7
 * @author Eduard
 * @deprecated Métodos adicionados na {@link Extra}
 */
public abstract interface TextSetup {
	public default String getText(String text) {
		return getText(16, text);
	}

	public default String getText(int size, String text) {
		return text.length() > size ? text.substring(0, size) : text;
	}
}
