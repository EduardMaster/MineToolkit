package net.eduard.api.command.api

import net.eduard.api.lib.manager.CommandManager

class ApiCommand : CommandManager("api") {
    init {
        register(ApiHelpCommand())
        register(ApiSQLDebugCommand())
        register(ApiReloadCommand())
        register(ApiUnloadWorldCommand())
        register(ApiLoadWorldCommand())
        register(ApiWorldsCommand())
        register(ApiDeleteWorldCommand())
        register(ApiListPluginsCommand())
        register(ApiDisablePluginCommand())
        register(ApiEnablePluginCommand())
        register(ApiRestartEduardAPICommand())
        register(ApiListPluginsCommand())
        register(ApiSaveCommand())
        register(ApiReloadCommand())
        register(ApiUnloadPluginCommand())
        register(ApiLoadPluginCommand())
    }
}