package net.eduard.api.lib.storage;

import java.util.Map;

import net.eduard.api.lib.modules.Extra;

/**
 * Sistema de armazenamento automatizado baseado na reflexão das classes
 * 
 * 
 * 
 * @author Eduard
 * @see Extra
 * @see StorageAPI
 * @version 3.0
 */
public interface Storable {

	/**
	 * Cria um Objeto pelo Mapa
	 * 
	 * @param map Mapa
	 * @return Objeto
	 */
	public default Object restore(Map<String, Object> map) {

		return null;
	}

	/**
	 * Salva o Objeto no Mapa
	 * 
	 * @param map    Mapa
	 * @param object Objeto
	 */
	public default void store(Map<String, Object> map, Object object) {

	}

	/**
	 * Gera uma nova instancia do objeto
	 * 
	 *
	 * @return Nova Instancia
	 */
	public default Object newInstance() {
		try {
			return Extra.getNew(getClass());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 *  Cria um novo Objeto pelo Objeto String
	 * @param object Objeto String
	 * @return Objeto
	 */
	public default Object restore(Object object) {

		return null;
	}
	/**
	 * Gera um objeto armazenal em STRING apartir do Objeto 1
	 * @param object Objeto
	 * @return String Objeto
	 */
	public default Object store(Object object) {
		return null;
	}

}
