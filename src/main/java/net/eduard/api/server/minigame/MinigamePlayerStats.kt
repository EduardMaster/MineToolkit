package net.eduard.api.server.minigame

class MinigamePlayerStats {
    var kills = 0
    var deaths = 0
    var streak = 0
    set(value) {
        if (maxStreak<field){
            maxStreak=value
        }
        field =value
    }
    var maxStreak = 0
    var assists = 0
    var firstKiller = false
    var points = 0.0
}