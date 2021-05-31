package net.eduard.api.lib.kotlin

import kotlin.reflect.KClass

private val instances = mutableMapOf<Class<*>, Any>()

/**
 * registra um Module ligado
 */
inline fun <reified T : Any> single(instance: T): T {
    return single(T::class, instance)
}

/**
 * registra um Module ligado
 */
fun <T : Any, E : Any> single(clz : KClass<E>, instance: T): T {
    instances[clz::class.java] = instance
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
