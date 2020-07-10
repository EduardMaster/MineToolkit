package net.eduard.api.lib.command

interface ICommand {

    var name : String
    var aliases : MutableList<String>
    var permission : String
    var permissionMessage : String
    var playerOnly : Boolean
    var subCommands : MutableList<Command>
}