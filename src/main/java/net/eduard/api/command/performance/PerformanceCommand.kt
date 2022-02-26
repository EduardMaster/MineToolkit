package net.eduard.api.command.performance

import net.eduard.api.lib.manager.CommandManager

class PerformanceCommand : CommandManager("performance", "desempenho","checklag") {

    init {
        description = "Verifica Desempenho do Servidor e derivados"
        usage = "/desempenho "
        register(PerformanceGarbageColectorCommand())
        register(PerformanceCPUCommand())
        register(PerformanceMemoryCommand())
        register(PerformanceEntitiesCommand())
    }
}