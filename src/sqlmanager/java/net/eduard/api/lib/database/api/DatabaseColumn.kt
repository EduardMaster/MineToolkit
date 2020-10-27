package net.eduard.api.lib.database.api

import net.eduard.api.lib.database.annotations.*
import net.eduard.api.lib.modules.Extra
import java.lang.reflect.Field

class DatabaseColumn(val field: Field, val engine: DatabaseEngine) {

    val javaType = Extra.getWrapperOrReturn(field.type)
    var sqlType = engine.types.getOrDefault(javaType, "TEXT")
    val isNumber = Number::class.java.isAssignableFrom(javaType)
    val isPrimary = field.isAnnotationPresent(ColumnPrimary::class.java)
    val isNullable = field.isAnnotationPresent(ColumnNullable::class.java)
    val isConstraint = field.isAnnotationPresent(ColumnRelation::class.java)
    val isUnique = field.isAnnotationPresent(ColumnUnique::class.java)

    val name = if (field.isAnnotationPresent(ColumnName::class.java))
        field.getAnnotation(ColumnName::class.java).value else field.name


    val table get() = engine.getTable(javaType)
    val foreignKeyName =
        "${name}_x_" + table.name + "_" + table.primaryName
    val size = if (field.isAnnotationPresent(ColumnSize::class.java))
        field.getAnnotation(ColumnSize::class.java).value else 25

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


/*

         ResultSet rs = connection.prepareStatement("SELECT * FROM " + tableName + where).executeQuery();
         ResultSetMetaData meta = rs.getMetaData();

 */

}