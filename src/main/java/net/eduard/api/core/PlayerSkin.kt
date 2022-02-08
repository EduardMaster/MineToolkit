package net.eduard.api.core

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.MineReflect
import net.eduard.api.lib.abstraction.Minecraft
import java.util.HashMap
import net.eduard.api.EduardAPI
import net.eduard.api.lib.config.Config
import org.bukkit.entity.Player
import java.lang.Exception

class PlayerSkin {
    constructor() {}

    fun exists(): Boolean {
        return playerUUID != null
    }

    var playerName: String? = null
    var playerUUID: String? = null
    var name: String? = null
    var signature: String? = null
    var value: String? = null

    constructor(playerName: String?) {
        this.playerName = playerName
        playerUUID = Extra.getPlayerUUIDByName(playerName)
        if (playerUUID != null) {
            update()
        }
    }

    fun update(player: Player) {
        if (playerUUID == null) return
        try {
            val handle = MineReflect.getPlayerHandle(player)
            val profile = Extra.getMethodInvoke(handle, "getProfile") as GameProfile
            profile.properties.clear()
            try {
                profile.properties.put("textures", Property(name, value, signature))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        println("Atualizando skin do " + player.name)
        Minecraft.instance.respawnPlayer(player)
        Minecraft.instance.reloadPlayer(player)

    }

    fun update() {
        val skinProperty = Extra.getSkinProperty(playerUUID)
        if (skinProperty != null) {
            name = skinProperty["name"].asString
            value = skinProperty["value"].asString
            signature = skinProperty["signature"].asString
        }
    }

    companion object {
        private val skins = HashMap<String, PlayerSkin>()
        private val skinUsed: MutableMap<Player, PlayerSkin> = HashMap()
        private val config = Config(EduardAPI.instance, "skins.yml")
        fun saveSkins() {
            for (skin in skins.values) {
                config[skin.playerName!!.toLowerCase()] = skin
            }
            config.saveConfig()
        }

        fun reloadSkins() {
            skins.clear()
            for (subSecao in config.keys) {
                val skin = config.get(subSecao, PlayerSkin::class.java)
                skins[skin.playerName!!.toLowerCase()] = skin
            }
        }

        fun getSkin(playerName: String): PlayerSkin {
            var skin = skins[playerName.toLowerCase()]
            if (skin == null) {
                skin = PlayerSkin(playerName)
                if (skin.exists()) skins[playerName.toLowerCase()] = skin
            }
            return skin
        }

        fun change(player: Player, skinName: String) {
            var skin = getSkin(skinName)
            var defName = EduardAPI.instance.getString("features.custom-skin")
            if (defName.isEmpty()) {
                defName = "Eduard"
            }
            if (!skin.exists()) {
                skin = getSkin(defName)
            }
            skin.update(player)
            skinUsed[player] = skin
        }
        /*
        private const val LINK_MOJANG_UUID = "https://api.mojang.com/users/rofiles/minecraft/"
        private const val LINK_MOJANG_SKIN = "https://sessionserver.mojang.com/session/minecraft/profile/"
        private const val LINK_MCAPI_UUID = "https://mcapi.ca/name/uuid/"
        private const val LINK_MCAPI_SKIN = "http://mcapi.ca/uuid/player/"
        */
    }
}