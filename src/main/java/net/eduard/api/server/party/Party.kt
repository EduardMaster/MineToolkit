package net.eduard.api.server.party


interface Party {

    var leader : PartyPlayer
    var members : MutableList<PartyPlayer>

}