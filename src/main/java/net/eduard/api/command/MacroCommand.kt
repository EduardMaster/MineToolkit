package net.eduard.api.command

import net.eduard.api.EduardAPI
import net.eduard.api.lib.manager.CommandManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class MacroCommand : CommandManager("macro","autoclick") {

    init{
        EduardAPI.instance.syncTimer(5,5){
            update()
        }

    }
    companion object{
        private val using = mutableSetOf<Player>()
        private fun update(){
            for (player in using){
                doMacro(player)
            }

        }
        fun doMacro(player : Player){
            // e.player.sendMessage("§aVocê esta clicando")
            val list = player.getNearbyEntities(3.5/2, 3.5/2, 3.5/2)
            //val realTarget = Mine.getTarget(e.player , list)
            for (entity in list) {
                if (entity == null) continue
                if (entity !is LivingEntity) continue
                if (entity is ArmorStand) continue
                if (entity == player) continue
                val distancia = player.location.distanceSquared(entity.location)
                if (distancia <= 3.5*3.5) {
                    val directionEntrePlayerEAlvo = entity.location.toVector().subtract(player.location.toVector())
                    val result1 = entity.location.direction.normalize().crossProduct(directionEntrePlayerEAlvo).lengthSquared() < 1.0
                    val result2 = directionEntrePlayerEAlvo.normalize().dot(player.location.direction.normalize()) >= 0.0
                    if (result1 && result2) {
                        entity.damage(1.0, player)
                        // player.sendMessage(
                        //    "§cCausando dano na entidade " + entity
                        //         .type
                        //)
                        break;
                    }
                }
            }
        }
    }



    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player){
            if (sender in using){
                sender.sendMessage("§cMacro desativada.")
                using.remove(sender)
            }else{
                sender.sendMessage("§aMacro ativada.")
                using.add(sender)
            }
        }

        return true
    }
}