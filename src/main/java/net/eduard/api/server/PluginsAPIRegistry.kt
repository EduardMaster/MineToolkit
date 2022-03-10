package net.eduard.api.server

private val pluginsAPI = mutableMapOf<Class<*>, PluginSystem>()

private fun PluginSystem.getChildrenAPIClass(): Class<*> {
    for (interfaceClass in javaClass.interfaces) {
        if (PluginSystem::class.java.isAssignableFrom(interfaceClass)
            && PluginSystem::class.java != interfaceClass
        ) return interfaceClass as Class<*>
    }
    throw IllegalArgumentException("Precisa ser uma classe que Implementa PluginSystem")
}

inline fun <reified T : PluginSystem> useAPI(): T {
    return getAPI(T::class.java)!!
}
inline fun <reified T : PluginSystem> useAPI(apply : T.() -> Unit): T? {
    return getAPI(T::class.java)?.apply(apply)
}
fun <T : PluginSystem> getAPI(classAPI: Class<T>): T? {
    return  pluginsAPI[classAPI] as T?
}

inline fun <reified T : PluginSystem> hasAPI(): Boolean {
    return getAPI(T::class.java) != null
}
fun PluginSystem.registerAPI() {
    val classAPI = getChildrenAPIClass()
    pluginsAPI[classAPI] = this
}
fun <E: PluginSystem> PluginSystem.registerAPI(apiClass : Class<E>) {
    pluginsAPI[apiClass] = this
}
fun <E: PluginSystem> PluginSystem.unregisterAPI(apiClass : Class<E>) {
    pluginsAPI[apiClass] = this
}
fun PluginSystem.unregisterAPI() {
    val classAPI = getChildrenAPIClass()
    pluginsAPI.remove(classAPI)
}

