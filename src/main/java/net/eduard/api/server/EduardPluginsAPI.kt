package net.eduard.api.server

private val EDUARD_PLUGINS_APIS = mutableMapOf<Class<*>, EduardPluginsAPI>()

inline fun <reified T : EduardPluginsAPI> useAPI(): T {
    return getAPI(T::class.java)!!
}

fun <T : EduardPluginsAPI> getAPI(classAPI: Class<T>): T? {
    return EDUARD_PLUGINS_APIS[classAPI] as T?
}

inline fun <reified T : EduardPluginsAPI> hasAPI(): Boolean {
    return getAPI(T::class.java) != null
}

fun EduardPluginsAPI.childrenAPIClass(): Class<*>? {
    for (interfaceClass in javaClass.interfaces) {
        if (EduardPluginsAPI::class.java.isAssignableFrom(interfaceClass)
            && EduardPluginsAPI::class.java != interfaceClass
        ) return interfaceClass
    }
    return null
}

fun EduardPluginsAPI.registerAPI() {
    val currentAPI = childrenAPIClass()!!
    EDUARD_PLUGINS_APIS[currentAPI] = this
}

fun EduardPluginsAPI.unregisterAPI() {
    val currentAPI = childrenAPIClass()!!
    EDUARD_PLUGINS_APIS.remove(currentAPI)
}

interface EduardPluginsAPI