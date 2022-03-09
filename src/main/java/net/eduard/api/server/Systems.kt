package net.eduard.api.server

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
    fun registerAPI(api: PluginSystem) {
        api.registerAPI()
    }
    @JvmStatic
    fun unregisterAPI(api: PluginSystem) {
        api.unregisterAPI()
    }

    @JvmStatic
    var mailSystem : MailSystem? = null

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
    var spawnerSystem : SpawnerSystem? = null

    @JvmStatic
    var mineSystem: MineSystem? = null


}
