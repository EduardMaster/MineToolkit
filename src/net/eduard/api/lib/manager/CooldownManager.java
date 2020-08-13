package net.eduard.api.lib.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.eduard.api.lib.storage.Storable;
import org.bukkit.entity.Player;

import net.eduard.api.lib.modules.Mine;

public class CooldownManager {
    private long duration;
    private String msgOnCooldown;
    private String msgOverCooldown;
    private String msgStartCooldown;
    @Storable.StorageAttributes(reference = true)
    private final Map<UUID, TimeManager> cooldowns = new HashMap<>();

    public CooldownManager() {

    }

    public CooldownManager(int time) {
        setDuration(time);
        msgOnCooldown = "§6Voce esta em Cooldown!";
        msgOverCooldown = "§6Voce saiu do Cooldown!";
        msgStartCooldown = "§6Voce usou a Habilidade!";
    }


    public boolean cooldown(Player player) {
        if (onCooldown(player)) {
            sendOnCooldown(player);
            return false;
        }
        setOnCooldown(player);
        sendStartCooldown(player);
        return true;
    }

    public void stopCooldown(Player player) {

        TimeManager cd = cooldowns.get(player.getUniqueId());
        cd.stopTask();
        cooldowns.remove(player.getUniqueId());
    }

    public boolean onCooldown(Player player) {
        return getResult(player) > 0;
    }


    public CooldownManager setOnCooldown(Player player) {
        if (onCooldown(player)) {
            stopCooldown(player);
        }

        TimeManager cd = new TimeManager() {

            public void run() {
                sendOverCooldown(player);
            }
        };
        cd.setTime(getDuration());
        cd.asyncDelay();
        cooldowns.put(player.getUniqueId(), cd);
        return this;

    }

    public void sendOverCooldown(Player player) {
        if (msgOnCooldown != null)
            player.sendMessage(msgOverCooldown);

    }

    public void sendOnCooldown(Player player) {
        if (msgOnCooldown != null)
            player.sendMessage(msgOnCooldown);

    }

    public void sendStartCooldown(Player player) {
        if (msgStartCooldown != null)
            player.sendMessage(msgStartCooldown);

    }

    public long getResult(Player player) {

        if (cooldowns.containsKey(player.getUniqueId())) {
            long now = Mine.getNow();
            TimeManager cd = cooldowns.get(player.getUniqueId());
            long before = cd.getStartedTime();
            long cooldown = cd.getTime() * 50;
            long calc = before + cooldown;
            long result = calc - now;
            if (result <= 0) {
                return 0;
            }
            return result / 50;
        }
        return 0;
    }

    public int getCooldown(Player player) {

        return (int) ((getResult(player) / 20));
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getOnCooldownMessage() {
        return msgOnCooldown;
    }

    public void setOnCooldownMessage(String onCooldownMessage) {
        this.msgOnCooldown = onCooldownMessage;
    }

    public String getStartCooldownMessage() {
        return msgStartCooldown;
    }

    public void setStartCooldownMessage(String startCooldownMessage) {
        this.msgStartCooldown = startCooldownMessage;
    }

}
