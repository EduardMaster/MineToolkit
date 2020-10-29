package net.eduard.api.lib.database.api

import net.eduard.api.lib.database.annotations.*
import net.eduard.api.lib.database.dataSize
import net.eduard.api.lib.modules.Extra
import java.lang.reflect.Field

class DatabaseColumn<T>(val table: DatabaseTable<T>, val field: Field, val engine: DatabaseEngine) {

    val isConstraint = field.isAnnotationPresent(ColumnRelation::class.java)
    val javaType = Extra.getWrapperOrReturn(field.type)
    var sqlType = engine.types.getOrDefault(javaType, if (isConstraint)"INT" else "TEXT")
    val isNumber = Number::class.java.isAssignableFrom(javaType)
    val isPrimary = field.isAnnotationPresent(ColumnPrimary::class.java)
    val isNullable = field.isAnnotationPresent(ColumnNullable::class.java)

    val isUnique = field.isAnnotationPresent(ColumnUnique::class.java)

    val name = if (field.isAnnotationPresent(ColumnName::class.java))
        field.getAnnotation(ColumnName::class.java).value else field.name


    val size = if (field.isAnnotationPresent(ColumnSize::class.java))
        field.getAnnotation(ColumnSize::class.java).value else dataSize[javaClass]?:11

    val customType: String = if (field.isAnnotationPresent(
            ColumnType::
            class.java
        )
    ) field.getAnnotation(ColumnType::class.java).value else sqlType

    val customDefaultValue: String? = if (field.isAnnotationPresent(
            ColumnValue::
            class.java
        )
    ) field.getAnnotation(ColumnValue::class.java).value else null


    val isJson = field.isAnnotationPresent(ColumnJson::class.java)
    val referenceTable get() = engine.getTable(javaType)
    val foreignKeyName =
        "${name}_x_" + referenceTable.name + "_" + referenceTable.primaryName

/*

         ResultSet rs = connection.prepareStatement("SELECT * FROM " + tableName + where).executeQuery();
         ResultSetMetaData meta = rs.getMetaData();

 */

}