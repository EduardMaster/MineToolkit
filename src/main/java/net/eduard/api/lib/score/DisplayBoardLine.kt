package net.eduard.api.lib.score
interface DisplayBoardLine {
    var text : String
    var position : Int

    fun check()

    fun get(): String {
        check()
        return text
    }

}