package net.eduard.api.server.minigame

import net.eduard.api.lib.config.Config
import net.eduard.api.lib.game.DisplayBoard
import net.eduard.api.lib.game.Kit
import net.eduard.api.lib.kotlin.reload
import net.eduard.api.lib.kotlin.reloadListFromFolder
import net.eduard.api.lib.kotlin.save
import net.eduard.api.lib.kotlin.saveListInFolder
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
    val modeName get() = name.replace(" ","_").toLowerCase();
    var timeIntoStart = 20
    var timeIntoRestart = 6
    var timeIntoGameOver = 10 * 60
    var timeIntoPlay = 5
    var timeOnStartTimer = 0
    var timeOnRestartTimer = 20
    var timeOnForceTimer = 10
    var timeOnStartingToBroadcast = 15
    var timeOnEquipingToBroadcast = 1
    var scoreboardStarting : DisplayBoard? = null
    var scoreboardPlaying :  DisplayBoard? = null
    val plugin get() = minigame.plugin
    val folder get() = File(plugin.dataFolder, "modes/$modeName/")

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
        Config(folder, "chests-normal.yml")
            .save(chests)
        Config(folder, "chests-feast.yml")
            .save(chestsFeast)
        Config(folder, "chests-mini-feast.yml")
            .save(chestMiniFeast)
    }

    fun saveKits() {
        File(folder,"kits").saveListInFolder(kits){
            name
        }
    }


    fun reloadKits() {
        kits.clear()
        kits.addAll(File(folder,"kits").reloadListFromFolder())

    }

    fun reloadChests() {
        chests = Config(folder, "chests-normal.yml").reload()
        chestsFeast = Config(folder, "chests-feast.yml").reload()
        chestMiniFeast = Config(folder, "chests-mini-feast.yml").reload()

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
