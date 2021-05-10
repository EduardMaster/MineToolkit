package net.eduard.api.lib.database.api

class TableReference<T>(
    val column: DatabaseColumn<*>,
    val instance: T,
    val foreignKey: Any
) {

    fun update() {
        var value = column.referenceTable.elements[foreignKey]
        if (value == null) {
            value = column.referenceTable.findByPrimary(foreignKey, null)
        }
        column.field.set(instance, value)
    }

}