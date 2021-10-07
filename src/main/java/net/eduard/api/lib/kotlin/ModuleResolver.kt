package net.eduard.api.lib.kotlin


private val instances = mutableMapOf<Class<*>, Any>()

/**
 * registra um Module ligado
 */
fun <T : Any> resolvePut(instance: T): T {
    instances[instance.javaClass] = instance
    return instance
}

/**
 * Recupera o Module instanciado
 */
inline fun <reified T : Any> resolve(): T {
    return resolve(T::class.java)
}

/**
 * Recupera o Module instanciado pela Classe
 */
fun <T : Any> resolve(claz : Class<T>): T {
    return instances[claz] as T
}
/**
 * Desregistra um Module
 */
fun <T : Any> resolveTake(data: T): Boolean {
    return instances.remove(data.javaClass) != null
}
