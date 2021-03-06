package net.eduard.api.server.minigame

import net.eduard.api.lib.config.Config
import net.eduard.api.lib.game.DisplayBoard
import net.eduard.api.lib.game.Kit
import net.eduard.api.lib.storage.annotations.StorageIndex
import java.io.File
import java.util.*

/**
 * Modo do Minigame
 * @author Eduard
 */
open class MinigameMode() {
    @Transient
    lateinit var minigame: Minigame

    @StorageIndex
    var uuid = UUID.randomUUID()
    var name = "Normal"
    var modeName get() = name
            set(newName){
                name = newName
            }
    var timeIntoStart = 20
    var timeIntoRestart = 6
    var timeIntoGameOver = 10 * 60
    var timeIntoPlay = 5
    var timeOnStartTimer = 0
    var timeOnRestartTimer = 20
    var timeOnForceTimer = 10
    var timeOnStartingToBroadcast = 15
    var timeOnEquipingToBroadcast = 1
    var scoreboardStarting = DisplayBoard("Minigame iniciando")
    var scoreboardPlaying = DisplayBoard("Minigame em jogo")
    val plugin get() = minigame.plugin
    val folder get() = File(plugin.dataFolder, "$modeName/")

    @Transient
    var chests = MinigameChest()

    @Transient
    var chestsFeast = MinigameChest()

    @Transient
    var chestMiniFeast = MinigameChest()

    @Transient
    var kits: MutableList<Kit> = ArrayList()

    @Transient
    var timers = mutableListOf<MinigameTimer>()

    fun register(timer : MinigameTimer){
        timers.add(timer)
        timer.mode = this


    }

    fun saveChests() {
        val configChest = Config(folder, "chests/normal.yml")
        configChest.set(chests)
        configChest.saveConfig()

        val configFeast = Config(folder, "chests/feast.yml")
        configFeast.set(chestsFeast)
        configFeast.saveConfig()

        val configMiniFeast = Config(folder, "chests/mini-feast.yml")
        configMiniFeast.set(chestMiniFeast)
        configMiniFeast.saveConfig()
    }

    fun saveKits() {
        val kitsConfig = Config(folder, "kits/kits.yml");
        for (kit in kits) {
            kitsConfig.set(kit.name, kit)
        }
        kitsConfig.saveConfig()
    }


    fun reloadKits() {
        kits.clear()
        val kitsConfig = Config(folder, "kits/kits.yml");
        for (id in kitsConfig.keys) {
            val kit = kitsConfig.get(id, Kit::class.java)
            kits.add(kit)
        }
    }

    fun reloadChests() {
        chests = Config(folder, "chests/normal.yml")
            .get(MinigameChest::class.java)
        chestsFeast = Config(folder, "chests/feast.yml")
            .get(MinigameChest::class.java)
        chestMiniFeast = Config(folder, "chests/mini-feast.yml")
            .get(MinigameChest::class.java)

    }


    /**
     * Timer do Minigame define oque acontece a cada segundo que se passa do
     * Minigame em cada Sala
     *
     * @param room Sala
     */
    open fun event(room: MinigameRoom) {
        for (timer in timers){
            if (timer.state == room.state)
                timer.event(room)
        }
    }
}
