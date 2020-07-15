package net.eduard.api.server

abstract class PluginRook(val pluginName : String)
{
    init{
        rooks.add(this)
    }

    companion object{
        private var rooks = mutableListOf<PluginRook>()
        fun getRooks(plugin : String): MutableList<PluginRook> {
            var list = mutableListOf<PluginRook>()
            for (rook in  rooks){
                if (rook.pluginName.equals(plugin,true))
                    list.add(rook)
            }
            return list
        }
    }


    abstract fun onPluginActive()

}