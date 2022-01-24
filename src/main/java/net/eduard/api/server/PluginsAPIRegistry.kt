package net.eduard.api.server


class PluginsAPIRegistry {
    companion object{
        val PLUGINS_APIS = mutableMapOf<Class<*>, PluginSystem>()
        fun <T : PluginSystem> getAPI(classAPI: Class<T>): T? {
            return PLUGINS_APIS[classAPI] as T?
        }
        private fun PluginSystem.getChildrenAPIClass(): Class<*>? {
            for (interfaceClass in javaClass.interfaces) {
                if (PluginSystem::class.java.isAssignableFrom(interfaceClass)
                    && PluginSystem::class.java != interfaceClass
                ) return interfaceClass
            }
            return null
        }
        fun registerAPI(api: PluginSystem) {
            val currentAPI = api.getChildrenAPIClass()!!
            PLUGINS_APIS[currentAPI] = api
        }

        fun unregisterAPI(api: PluginSystem) {
            val currentAPI = api.getChildrenAPIClass()!!
            PLUGINS_APIS.remove(currentAPI)
        }
    }
}

inline fun <reified T : PluginSystem> useAPI(): T {
    return getAPI(T::class.java)!!
}

fun <T : PluginSystem> getAPI(classAPI: Class<T>): T? {
    return PluginsAPIRegistry.getAPI(classAPI)
}

inline fun <reified T : PluginSystem> hasAPI(): Boolean {
    return getAPI(T::class.java) != null
}

fun PluginSystem.registerAPI() {
    PluginsAPIRegistry.registerAPI(this)
}

fun PluginSystem.unregisterAPI() {
    PluginsAPIRegistry.unregisterAPI(this)
}

