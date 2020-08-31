package net.eduard.api.lib.storage;

import net.eduard.api.lib.modules.Extra;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * Sistema de armazenamento automatizado baseado na reflexão das classes
 *
 * @author Eduard
 * @version 3.0
 * @see Extra
 */
public interface Storable<T> {


    @Target({java.lang.annotation.ElementType.FIELD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface StorageAttributes {

        boolean reference() default false;

        boolean indentificate() default false;

        boolean inline() default false;

    }

    /**
     * Cria um Objeto pelo Mapa
     *
     * @param map Mapa
     * @return Objeto
     */
    default T restore(Map<String, Object> map) {

        return null;
    }

    /**
     * Salva o Objeto no Mapa
     *
     * @param map    Mapa
     * @param object Objeto
     */
    default void store(Map<String, Object> map, T object) {

    }


    /**
     * Gera uma nova instancia do objeto
     *
     * @return Nova Instancia
     */
    default T newInstance() {
        try {
            return (T) Extra.getNew(getClass());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deserializa o Objeto apartir de uma String
     *
     * @param string String
     * @return Objeto
     */
    default T restore(String string) {

        return null;
    }

    /**
     * Serializa o Objeto em uma String
     *
     * @param object Objeto
     * @return String
     */
    default String store(T object) {
        return null;
    }

}
