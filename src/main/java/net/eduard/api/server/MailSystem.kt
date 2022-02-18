package net.eduard.api.server

import net.eduard.api.lib.modules.FakePlayer
import org.bukkit.inventory.ItemStack

interface MailSystem {

    fun sendItems(playerSender: FakePlayer, playerReceiver: FakePlayer, item: ItemStack)
    fun sendItems(playerSender: FakePlayer, playerReceiver: FakePlayer, items: List<ItemStack>)
    fun hasItems(player: FakePlayer): Boolean
    fun getItems(player : FakePlayer) : List<ItemStack>

}