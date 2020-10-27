package net.eduard.api.lib.database.api

class TableReference<T>(val column : DatabaseColumn<*>,
                     val instance : T,
                     val foreignKey : Any) {

    fun update() {
        val value = column.referenceTable.elements[foreignKey]!!
        column.field.set(instance, value )

    }
}