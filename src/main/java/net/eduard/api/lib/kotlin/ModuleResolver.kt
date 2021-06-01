package net.eduard.api.lib.kotlin

import kotlin.reflect.KClass

private val instances = mutableMapOf<Class<*>, Any>()

/**
 * registra um Module ligado
 */
fun <T : Any> single(instance: T): T {
    instances[instance.javaClass] = instance
    return instance
}

/**
 * registra um Module ligado
 */
fun <E : Any, T : Any> singleClass(classUsed : KClass<E>, instance: T): T {
    instances[classUsed::class.java] = instance
    return instance
}

/**
 * Recupera o Module instanciado
 */
inline fun <reified T : Any> resolve(): T {
    return resolve(T::class)
}

/**
 * Recupera o Module instanciado pela Classe
 */
fun <T : Any> resolve(claz : KClass<T>): T {
    return instances[claz.java] as T
}
/**
 * Desregistra um Module
 */
fun <T : Any> deresolve(data: T): Boolean {
    return instances.remove(data.javaClass) != null
}
