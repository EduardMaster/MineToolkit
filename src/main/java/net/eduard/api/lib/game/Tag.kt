package net.eduard.api.lib.game

import lib.modules.Copyable

/**
 * Representa uma Tag geralmente usado para Cargos de Permissão
 * @author Eduard
 * @version 2.0
 * @since EduardAPI 1.0
 */
class Tag(
        var name: String = "Tag",
        var prefix: String = "",
        var suffix: String = "",
        var rank: Int = 0
) {
    fun copy(): Tag {
        return lib.modules.Copyable.copyObject(this)
    }

    override fun toString(): String {
        return "Tag [prefix=$prefix, suffix=$suffix, name=$name, rank=$rank]"
    }
}