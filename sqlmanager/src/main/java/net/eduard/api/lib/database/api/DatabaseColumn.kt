package net.eduard.api.lib.database.api

import net.eduard.api.lib.database.annotations.*
import net.eduard.api.lib.database.customTypes
import net.eduard.api.lib.modules.Extra
import java.lang.reflect.Field

class DatabaseColumn<T : Any>(val table: DatabaseTable<T>, val field: Field, val engine: DatabaseEngine) {

    val tableData get() = field.getAnnotation(TableData::class.java)
    val isReference get() = isConstraint && !isPrimary
    val javaType = field.type
    val wrapperType = Extra.getWrapperOrReturn(field.type)
    val isWrapper = Extra.isWrapper(javaType)
    val isEnum = javaType.isEnum
    val isBoolean = (wrapperType == java.lang.Boolean::class.java)
    val isNumber
        get() = Number::class.java.isAssignableFrom(wrapperType)
                || isBoolean
    val isPrimary = field.isAnnotationPresent(ColumnPrimary::class.java)
            || tableData?.primary ?: false
    val isNullable = field.isAnnotationPresent(ColumnNullable::class.java)
            || tableData?.nullable ?: false
    val isUnique = field.isAnnotationPresent(ColumnUnique::class.java)
            || tableData?.unique ?: false

    val isConstraint = field.isAnnotationPresent(ColumnRelation::class.java)
            || tableData?.reference ?: false
    val isString = javaType == String::class.java || isEnum

    val name = field.getAnnotation(ColumnName::class.java)?.value ?: tableData?.name ?: field.name

    val size = field.getAnnotation(ColumnSize::class.java)?.value ?: customTypes[javaType]?.sqlSize
    ?: if (isString) 100 else -1
    var sqlType = engine.types.getOrDefault(
        javaType, if (isConstraint) "INT"
        else customTypes[javaType]?.sqlType ?: "VARCHAR"
    )

    val customType
        get() = field.getAnnotation(ColumnType::class.java)?.value
            ?: tableData?.sqlType.takeIf { it?.isNotEmpty() ?: false }
            ?: sqlType
    val customDefaultValue
        get() = field.getAnnotation(ColumnValue::class.java)?.value
            ?: tableData?.defaultValue.takeIf { it?.isNotEmpty() ?: false }


    val isJson
        get() = field.isAnnotationPresent(ColumnJson::class.java)
                || tableData?.json ?: false


    val referenceTable get() = engine.getTable(javaType)
    val foreignKeyName
        get() =
            "${table.name}_${name}_x_" + referenceTable.name + "_" + referenceTable.primaryName

/*

         ResultSet rs = connection.prepareStatement("SELECT * FROM " + tableName + where).executeQuery();
         ResultSetMetaData meta = rs.getMetaData();

 */

}