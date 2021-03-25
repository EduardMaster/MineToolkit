package net.eduard.api.lib.kotlin


val instances = mutableMapOf<Class<*>, Any>()

/**
 * registra um Module ligado
 */
fun single(instance: Any): Any {
    instances[instance.javaClass] = instance
    return instance
}

/**
 * Recupera o Module instanciado
 */
inline fun <reified T> resolve(): T {
    return instances[T::class.java] as T
}

/**
 * Desregistra um Module
 */
fun deresolve(data: Any) {
    instances.remove(data.javaClass)
}
