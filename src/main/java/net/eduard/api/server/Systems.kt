package net.eduard.api.server

import java.util.function.Consumer

/**
 * Classe onde possui Métodos de Acesso as APIs do Servidor<br>
 * e também possui as Variaveis de algumas APIs definidas na classe
 */
object Systems {

    @JvmStatic
    fun <T : PluginSystem> getAPI(classAPI: Class<T>): T? {
        return net.eduard.api.server.getAPI(classAPI)
    }

    @JvmStatic
    fun <T : PluginSystem> useAPI(classAPI: Class<T>, action: Consumer<T>): T? {
        val api = net.eduard.api.server.getAPI(classAPI) as T?
        if (api != null) {
            action.accept(api)
        }
        return api
    }

    @JvmStatic
    fun registerAPI(api: PluginSystem) {
        api.registerAPI()
    }

    @JvmStatic
    fun <T : PluginSystem> registerAPI(classAPI: Class<T>, api: T) {
        api.registerAPI(classAPI)
    }

    @JvmStatic
    fun unregisterAPI(api: PluginSystem) {
        api.unregisterAPI()
    }

    @JvmStatic
    fun <T : PluginSystem> unregisterAPI(classAPI: Class<T>, api: T) {
        api.unregisterAPI(classAPI)
    }

    @JvmStatic
    var clanSystem: ClanSystem? = null

    @JvmStatic
    var playTimeSystem: PlayTimeSystem? = null

    @JvmStatic
    var arenaSystem: ArenaSystem? = null

    @JvmStatic
    var cashSystem: CashSystem? = null

    @JvmStatic
    var soulSystem: SoulSystem? = null

    @JvmStatic
    var scoreSystem: ScoreSystem? = null

    @JvmStatic
    var tagSystem: TagSystem? = null

    @JvmStatic
    var dropsSystem: DropSystem? = null

    @JvmStatic
    var mineSystem: MineSystem? = null


}
