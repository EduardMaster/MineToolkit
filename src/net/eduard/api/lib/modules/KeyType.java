package net.eduard.api.lib.modules;

/**
 * Tipo de geração de Key
 *
 * @author Eduard-PC
 *
 */
public  enum KeyType {
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
