package net.eduard.api.server


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
    var clanSystem: ClanSystem? = null

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
