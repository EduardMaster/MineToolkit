package net.eduard.api.lib.kotlin


val instances = mutableMapOf<Class<*>, Any>()
fun single(instance: Any) {
    instances[instance.javaClass] = instance
}

inline fun <reified T> resolve(): T {
    return instances[T::class.java] as T
}

fun deresolve(data: Any) {
    instances.remove(data.javaClass)
}
