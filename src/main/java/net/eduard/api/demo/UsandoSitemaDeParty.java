package net.eduard.api.demo;

import net.eduard.api.lib.command.PlayerBukkit;
import net.eduard.api.server.Systems;
import net.eduard.api.server.party.Party;
import net.eduard.api.server.party.PartyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class UsandoSitemaDeParty {

    public void preencherTimes(int timeLimite,
                               List<Player> timeA,
                               List<Player> timeB, List<Player> jogadores) {
        boolean enchendoTimeA = true;
        Set<Party> parties = new HashSet();
        List<Player> semParty = new ArrayList<>();
        for (Player jogador : jogadores) {
            PartyPlayer player = Systems.getPartySystem().getPlayer(new PlayerBukkit(jogador).getOffline());
            if (player.hasParty()) {
                parties.add(player.getParty());
            } else {
                semParty.add(jogador);
            }
        }
        for (Party party : parties) {

            List<Player> partyJogadores = party.getMembers().stream()
                    .map(player -> Bukkit.getPlayer(player.getOffline().getName())).
                            filter(Objects::nonNull).collect(Collectors.toList());
            if (enchendoTimeA) {
                if (timeLimite - timeA.size() >= partyJogadores.size()) {

                    timeA.addAll(partyJogadores);

                } else {
                    enchendoTimeA = false;
                }
            } else {
                if (timeLimite - timeB.size() >= partyJogadores.size()) {

                    timeB.addAll(partyJogadores);

                } else {

                    for (Player jogador : partyJogadores) {


                        if (timeLimite != timeB.size()) {
                            timeB.add(jogador);
                        } else {
                            semParty.add(jogador);

                        }
                    }
                }

            }

            for (Player jogador : semParty){
                if (timeLimite == timeA.size()) {
                    timeB.add(jogador);
                } else {
                    timeA.add(jogador);
                }
            }
        }
    }
}
