package net.eduard.api.command.api

import net.eduard.api.lib.manager.CommandManager

class ApiCommand : CommandManager("api") {
    init {
        register(ApiHelpCommand())
        register(ApiReloadCommand())
        register(ApiUnloadWorldCommand())
        register(ApiLoadWorldCommand())
        register(ApiWorldsCommand())
        register(ApiDeleteWorldCommand())
        register(ApiListCommand())
        register(ApiDisableCommand())
        register(ApiEnableCommand())
        register(ApiRestartEduardAPICommand())
        register(ApiListCommand())
        register(ApiSaveCommand())
        register(ApiReloadCommand())
        register(ApiUnloadCommand())
        register(ApiLoadCommand())
    }
}