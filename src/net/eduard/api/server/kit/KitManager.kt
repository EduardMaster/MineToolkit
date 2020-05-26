package net.eduard.api.server.kit

import java.util.ArrayList
import java.util.HashMap

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.manager.EventsManager
import net.eduard.api.lib.menu.Menu
import net.eduard.api.lib.menu.Slot
import net.eduard.api.lib.inventory.ClickEffect
import net.eduard.api.lib.modules.VaultAPI

class KitManager : EventsManager() {

    @Transient
    var playersKits: MutableMap<Player, KitAbility> = HashMap()
    var isKitsEnabled = true
    var defaultKitPrice = 0.0
    var isGiveSoups: Boolean = false
    var isFillHotBar = true
    var isOnSelectGainKit = true
    var msgNoneKit = "§8Nenhum"
    var msgKitsDisabled = "§cOs kits foram desabilitados!"
    var msgKitSelected = "§6Voce escolheu o Kit §e\$kit"
    var msgKitGived = "§6Voce ganhou o Kit §e\$kit"
    var msgKitBuyed = "§aVoce comprou o Kit §2\$kit"
    var msgNoKitBuyed = "§§Voce nao tem dinheiro para comprar o kit §e\$kit"
    var msgShopTitle = "§cKit §4§l\$kit §cseu pre§o: §a§l\$price"
    var msgKitDisabled = "§cHabilidade \$kit desativada temporariamente!"
    var itemSoup = Mine.newItem( Material.MUSHROOM_SOUP,"§6Sopa")
    var itemEmptySlot = Mine.newItem( Material.STAINED_GLASS_PANE," ", 15)
    var itemHotBar = Mine.newItem( Material.STAINED_GLASS_PANE,"§6§lKit§f§lPvP", 10)
    var globalItems: MutableList<ItemStack> = ArrayList()
    var openKits = Slot(Mine.newItem(Material.CHEST, "§6§lSelecionar Kit"), 0)
    var openShop = Slot(Mine.newItem(Material.EMERALD, "§6§lComprar Kit"), 8)
    var kitsShop = Menu("§8Loja de Kits", 6)
    var kitsSelection = Menu("§8Seus  Kits", 6)

    var kits = ArrayList<KitAbility>()

    init {
        globalItems.add(ItemStack(Material.STONE_SWORD))
        kitsSelection.openWithItem = null
        kitsShop.openWithItem = null
        kitsSelection.effect = ClickEffect { event, page ->
            if (event.whoClicked is Player) {
                val player = event.whoClicked as Player
                val item = event.currentItem

                for (kit in kits) {
                    if (kit.icon == item) {
                        selectKit(player, kit)
                        break
                    }
                }

            }
        }
        kitsShop.effect = ClickEffect { event, page ->
            if (event.whoClicked is Player) {
                val player = event.whoClicked as Player

                val item = event.currentItem

                for (kit in kits) {
                    if (kit.icon == item) {
                        buyKit(player, kit)
                        break
                    }
                }

            }
        }

    }


    fun openShop(player: Player) {

        val menu = kitsSelection.open(player)

        var posicao = 0
        for (kit in kits) {
            if (!kit.isShowOnGui) continue
            if (!player.hasPermission(kit.REQUIRE_PERMISSION)) {

                menu.setItem(posicao, kit.icon)
                posicao++
            }
            if (posicao>=menu.size)break

        }
        player.updateInventory()

    }

    fun openKitSelector(player: Player) {
        val menu = kitsSelection.open(player)

        var posicao = 0
        for (kit in kits) {
            if (!kit.isShowOnGui) continue

            if (player.hasPermission(kit.REQUIRE_PERMISSION)) {
               // player.sendMessage("§cAbrindo menu com kit " + kit.name)
                menu.setItem(posicao, kit.icon)
                posicao++
            }
            if (posicao>=menu.size)break
        }
        player.updateInventory()
    }


    fun giveItems(player: Player) {
        val inv = player.inventory
        openShop.give(inv)
        openKits.give(inv)
        if (isFillHotBar) {
            Mine.addHotBar(player, itemHotBar)
        }

    }


    fun getKit(type: KitType): KitAbility? {
        for (kit in kits) {
            if (kit.name.equals(type.name, ignoreCase = true)) {
                return kit
            }
        }
        return null
    }

    fun gainKit(player: Player) {
        if (!isKitsEnabled)
            return
        val kit = playersKits[player]!!
        removeKits(player)
        Mine.refreshAll(player)
        val inv = player.inventory
        for (item in kit.ITEMS_TO_GIVE) {
            inv.addItem(item)
        }
        for (subkit in kit.kits) {
            for (item in subkit.ITEMS_TO_GIVE) {
                inv.addItem(item)
            }
        }
        for (item in globalItems) {
            inv.addItem(item)
        }
        if (isGiveSoups) {
            Mine.fill(inv, itemSoup)
            Mine.setEquip(player, Color.GREEN, "§4§lINSANE")
        }
        player.sendMessage(msgKitGived.replace("\$kit", kit.name))
    }

    override fun register(plugin: Plugin) {
        for (kit in kits) {
            kit.register(plugin)
            if (kit.click != null) {
                kit.click!!.register(plugin)
            }
            if (kit.price == 0.0) {
                kit.price = defaultKitPrice
            }
        }
        this.kitsShop.register(plugin)
        this.kitsSelection.register(plugin)
        super.register(plugin)

    }

    fun removeKits(player: Player) {
        playersKits.remove(player)
    }

    fun selectKit(player: Player, kit: KitAbility?) {
        player.closeInventory()
        playersKits[player] = kit!!
        player.sendMessage(msgKitSelected.replace("\$kit", kit!!.name))
        if (isOnSelectGainKit) {
            gainKit(player)
        }

    }

    fun selectKit(player: Player, type: KitType) {
        val kit = getKit(type)
        selectKit(player, kit)
    }


    //	public void setKitsGui(Menu kitsSelection) {
    //		this.kitsSelection = kitsSelection;
    //	}



    fun buyKit(player: Player, kit: KitAbility) {
        if (VaultAPI.hasVault()) {
            if (VaultAPI.getEconomy().has(player, kit.price)) {
                VaultAPI.getEconomy().withdrawPlayer(player, kit.price)
                VaultAPI.getPermission().playerAdd(player, kit.REQUIRE_PERMISSION)
                player.sendMessage(msgKitBuyed.replace("\$kit", kit.name))
            } else {
                player.sendMessage(msgNoKitBuyed.replace("\$kit", kit.name))

            }
        }
    }

    @EventHandler
    fun abrir(event: PlayerInteractEvent) {
        val p = event.player
        val item = event.item ?: return
        if (event.action.name.contains("RIGHT")) {
            if (item.isSimilar(openKits.item)) {
                event.isCancelled = true
                openKitSelector(p)
            } else if (item.isSimilar(openShop.item)) {
                event.isCancelled = true
                openShop(p)
            }
        }

    }

    fun getKit(player: Player): KitAbility? {
        if (hasKit(player)) {
            return playersKits[player]
        }
        val def = getKit(KitType.DEFAULT)
        playersKits[player] = def!!
        return def
    }

    fun hasKit(player: Player): Boolean {
        return playersKits.containsKey(player)
    }



}
