package net.eduard.api.lib.database.api

import net.eduard.api.lib.database.annotations.*
import net.eduard.api.lib.modules.Extra
import java.lang.reflect.Field

class DatabaseColumn(val field: Field, val engine: DatabaseEngine) {
    var sqlType = engine.types.getOrDefault(field.type, "TEXT")
    val javaType = Extra.getWrapperOrReturn(field.type)
    val isNumber = Number::class.java.isAssignableFrom(javaType)
    val isPrimary = field.isAnnotationPresent(ColumnPrimary::class.java)
    val isNullable = field.isAnnotationPresent(ColumnNullable::class.java)
    val isUnique = field.isAnnotationPresent(ColumnUnique::class.java)

    val name = if (field.isAnnotationPresent(ColumnName::class.java))
        field.getAnnotation(ColumnName::class.java).value else field.name

    val size = if (field.isAnnotationPresent(ColumnSize::class.java))
        field.getAnnotation(ColumnSize::class.java).value else 11

    val customType: String? = if (field.isAnnotationPresent(
            ColumnType::
            class.java
        )
    ) field.getAnnotation(ColumnType::class.java).value else null

    val customDefaultValue: String? = if (field.isAnnotationPresent(
            ColumnValue::
            class.java
        )
    ) field.getAnnotation(ColumnValue::class.java).value else null


    val isJson = field.isAnnotationPresent(ColumnJson::class.java)


/*
public List<Map<String, ColumnInfo>> getVariablesFrom(String tableName, String where) {
 List<Map<String, ColumnInfo>> lista = new LinkedList<>();

 if (!where.isEmpty()) {
     where = " WHERE " + where;
 }
     try {
         ResultSet rs = connection.prepareStatement("SELECT * FROM " + tableName + where).executeQuery();
         ResultSetMetaData meta = rs.getMetaData();

     while (rs.next()) {
         Map<String, ColumnInfo> mapa = new LinkedHashMap<>();
         lista.add(mapa);
         for (int colunaID = 1; colunaID <= meta.getColumnCount(); colunaID++) {
             String coluna = meta.getColumnName(colunaID);
             String classe = meta.getColumnClassName(colunaID);
             int type = meta.getColumnType(colunaID);
             String typeName = meta.getColumnTypeName(colunaID);
             Object valor = rs.getObject(colunaID);
             String texto = rs.getString(colunaID);
             // String calalog = meta.getCatalogName(colunaID);
             // String label = meta.getColumnLabel(colunaID);
             // int displaySize = meta.getColumnDisplaySize(colunaID);
             // int precision = meta.getPrecision(colunaID);
             // int scale = meta.getScale(colunaID);
             ColumnInfo campo = new ColumnInfo();
             campo.setValueString(texto);
             campo.setValue( valor);
             campo.setTypeName( typeName);
             campo.setType( type);
             campo.setClassName( classe);
             campo.setName(coluna);
             campo.setId(colunaID);
             mapa.put(coluna, campo);
         }
     }
     rs.close();

     } catch (Exception e) {
         e.printStackTrace();
     }

    return lista;

}

 */
}