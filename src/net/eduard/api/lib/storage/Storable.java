package net.eduard.api.lib.storage;

import java.util.Map;

import net.eduard.api.lib.modules.Extra;

/**
 * Sistema de armazenamento automatizado baseado na refle��o das classes
 * 
 * 
 * 
 * @author Eduard
 * @see Extra
 * @see StorageAPI
 */
public interface Storable {

	/**
	 * Cria um Objeto pelo Mapa
	 * 
	 * @param map
	 *            Mapa
	 * @return Objeto
	 */
	public default Object restore(Map<String, Object> map) {
		
		return null;
	}

	/**
	 * Salva o Objeto no Mapa
	 * 
	 * @param map
	 *            Mapa
	 * @param object
	 *            Objeto
	 */
	public default void store(Map<String, Object> map, Object object) {

	}

	/**
	 * Gera uma nova instancia do objeto
	 * 
	 * @param map
	 *            Mapa
	 * @return Nova Instancia
	 */
	public default Object newInstance() {
		try {
			return Extra.getNew(type());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Nickname para salvar o Objecto
	 * 
	 * @return NickName
	 */
	public default String alias() {
		return type().getSimpleName();
	}

	public default Object restore(Object object) {
		return StorageAPI.restoreInline(object.toString(), type());
	}

	public default Class<?> type() {
		return getClass();
	}

	public default boolean saveInline() {
		return false;
	}

	public default Object store(Object object) {
		return StorageAPI.storeInline(object);
	}

}
