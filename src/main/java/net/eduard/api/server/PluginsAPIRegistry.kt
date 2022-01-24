package net.eduard.api.server


class PluginsAPIRegistry {
    companion object{
        @JvmStatic
        val PLUGINS_APIS = mutableMapOf<Class<*>, PluginSystem>()
    }
}

inline fun <reified T : PluginSystem> useAPI(): T {
    return getAPI(T::class.java)!!
}

fun <T : PluginSystem> getAPI(classAPI: Class<T>): T? {
    return PluginsAPIRegistry.PLUGINS_APIS[classAPI] as T?
}

inline fun <reified T : PluginSystem> hasAPI(): Boolean {
    return getAPI(T::class.java) != null
}

fun PluginSystem.getChildrenAPIClass(): Class<*>? {
    for (interfaceClass in javaClass.interfaces) {
        if (PluginSystem::class.java.isAssignableFrom(interfaceClass)
            && PluginSystem::class.java != interfaceClass
        ) return interfaceClass
    }
    return null
}

fun PluginSystem.registerAPI() {
    val currentAPI = getChildrenAPIClass()!!
    PluginsAPIRegistry.PLUGINS_APIS[currentAPI] = this
}

fun PluginSystem.unregisterAPI() {
    val currentAPI = getChildrenAPIClass()!!
    PluginsAPIRegistry.PLUGINS_APIS.remove(currentAPI)
}

