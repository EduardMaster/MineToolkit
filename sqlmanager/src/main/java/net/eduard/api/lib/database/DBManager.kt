package net.eduard.api.lib.database

import net.eduard.api.lib.database.api.DatabaseEngine
import net.eduard.api.lib.database.api.SQLEngineType
import net.eduard.api.lib.database.impl.MySQLEngine
import net.eduard.api.lib.modules.Extra
import java.sql.*
import javax.persistence.ColumnResult
import kotlin.jvm.internal.Intrinsics

/**
 * API de Controle de MySQL ou SQLite com apenas 1 conexão

 * Construtor pedindo Usuario, Senha, Host sem conectar com nenhum database
 * apenas no Driver
 *
 * @param user Usuario
 * @param pass Senha
 * @param host Host

 * @author Eduard
 */
@Suppress("unchecked", "unused")
class DBManager(
    var user: String,
    var pass: String,
    var database: String,
    var host: String

) {
    constructor() : this("root", "", "mine", "localhost")

    var autoReconnect = true
    var isEnabled = false
    var port = "3306"

    var engine =
        SQLEngineType.MYSQL

    @Transient
    lateinit var engineUsed: DatabaseEngine

    /**
     * Volta a conexao da variavel
     *
     * @return conexao atual
     */
    @Transient
    lateinit var connection: Connection


    /**
     * Fecha a conexao do Banco
     */
    fun closeConnection() {
        if (hasConnectionInMemory()) {
            try {
                engineUsed.clearCache()
                connection.close()

            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Cria uma connexão com o Driver
     *
     * @return Conexão nova criada
     * @throws Exception Erro
     */
    @Throws(Exception::class)
    fun connect(): Connection {
        return DriverManager.getConnection(
            engine.getUrl(
                host,
                Extra.toInt(port),
                user,
                pass,
                database
            ), user, pass
        )
    }

    /**
     * Abre a conexao com o banco de dados caso nao exista ainda
     *
     * @return Mesma instacia da classe DBManager
     */
    fun openConnection(): DBManager {
        try {
            connection = connect()
            if (engine === SQLEngineType.MYSQL) {
                engineUsed = MySQLEngine(connection)
                createDatabase(database)
                useDatabase(database)
            } else if (engine === SQLEngineType.SQLITE) {
                engineUsed = MySQLEngine(connection)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()

        }

        return this
    }


    /**
     * Ve se a conexao nao esta nula
     *
     * @return Se a conexao existe
     */
    fun hasConnectionInFact(time: Int = 500): Boolean {
        try {

            return hasConnectionInMemory()
                    && connection.isValid(time)
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return false
    }

    fun hasConnectionInMemory(): Boolean {
        return this::connection.isInitialized && !(connection.isClosed)
    }

    /**
     * Ve se a conexao nao esta nula
     *
     * @return Se a conexao existe
     */
    fun hasConnection(): Boolean {
        try {
            var have = hasConnectionInFact()
            if (!have && autoReconnect && isEnabled) {
                openConnection()
                have = hasConnectionInMemory()
            }
            return have
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        return false
    }


    /**
     * Criar uma database
     *
     * @param database Database
     */
    fun createDatabase(database: String) {
        update(
            "create database if not exists " + database
                    + " default character set utf8 default collate utf8_general_ci"
        )
        // update("create database if not exists " + database + " default character set utf8");
    }

    /**
     * Conecta com a database
     *
     * @param database Database
     */
    fun useDatabase(database: String) {
        update("USE $database")
    }

    /**
     * Cria uma tabela
     *
     * @param table  Tabela
     * @param values Valores
     */
    fun createTable(table: String, values: String) {
        val builder = StringBuilder(
            "CREATE TABLE IF NOT EXISTS $table (" +

                    " ID INT AUTO_INCREMENT PRIMARY KEY,"
        )

        builder.append(values)
        builder.append(")")

        update(builder.toString())
    }

    /**
     * Deleta todas tabelas da database
     *
     * @param database Database
     */
    fun clearDatabase(database: String) {
        update("TRUNCATE DATABASE $database")
    }

    /**
     * Deleta database
     *
     * @param database Database
     */
    fun deleteDatabase(database: String) {
        update("DROP DATABASE $database")
    }

    /**
     * Insere um registro
     *
     * @param table   Tabela
     * @param objects Objetos
     * @return Id gerado pelo Insert
     */
    fun insert(table: String, vararg objects: Any?): Int {
        val builder = StringBuilder()
        builder.append("INSERT INTO $table VALUES (")
        for (index in objects.indices) {
            builder.append("? ,")
        }
        builder.deleteCharAt(builder.length - 1)
        builder.append(")")
        return update(
            builder.toString(),
            *objects
        )
    }

    /**
     * Deleta um registro
     *
     * @param table Tabela
     * @param index Index (ID)
     */
    fun deleteData(table: String, index: Int) {
        update("DROP TABLE $table WHERE ID = ?", index)
    }

    /**
     * Deleta um registro
     *
     * @param table  Tablea
     * @param where  Como
     * @param values Valores
     */
    fun deleteData(table: String, where: String, vararg values: Any?) {
        update("DELETE FROM $table WHERE $where", *values)
    }

    /**
     * Deleta uma coluna
     *
     * @param table  Tale
     * @param column Coluna
     */
    fun deleteColumn(table: String, column: String) {
        alter(table, "drop column $column")
    }

    /**
     * Adiciona no Inicio §o da tabela uma coluna
     *
     * @param table          Tabela
     * @param columnComplete Coluna
     */
    fun addFirst(table: String, columnComplete: String) {
        alter(table, "add column $columnComplete first")
    }

    fun addReference(table: String, key: String, references: String) {
        update("ALTER TABLE $table ADD FOREIGN KEY ($key) REFERENCES $references ON DELETE SET NULL ON UPDATE SET NULL")
    }

    /**
     * Cria uma view com um select
     *
     * @param view   View
     * @param select Select query
     */
    fun createView(view: String, select: String) {
        update("CREATE OR REPLACE VIEW $view AS $select")
    }

    /**
     * Deleta a view
     *
     * @param view View
     */
    fun deleteView(view: String) {
        update("DROP VIEW $view")
    }

    /**
     * Renomeia a Tabela para uma Nova Tabela
     *
     * @param table    Tabela
     * @param newTable Nova tabela
     */
    fun renameTable(table: String, newTable: String) {
        alter(table, "rename to $newTable")
    }

    /**
     * Modifica a Coluna da Tabela
     *
     * @param table        Tabela
     * @param column       Coluna
     * @param modification Modificar
     */
    fun modify(table: String, column: String, modification: String) {
        alter(table, "modify column $column $modification")
    }

    /**
     * Adiciona chave primaria na tabela
     *
     * @param table Tabela
     * @param key   Chave
     */
    fun addKey(table: String, key: String) {
        alter(table, "add primary key ($key)")
    }

    /**
     * Altera uma tabala
     *
     * @param table Tabela
     * @param alter Alteração
     */
    fun alter(table: String, alter: String) {
        if (hasConnection()) update("alter table $table $alter")
    }

    /**
     * Modifica alguns registros da tabela
     *
     * @param table  Tabela
     * @param where  Como
     * @param edit   Modificar
     * @param values Valores
     */
    fun change(
        table: String,
        edit: String,
        where: String,
        vararg values: Any?
    ) {
        update("UPDATE $table SET $edit WHERE $where", *values)
    }

    /**
     * Cria um join entre as tabelas
     *
     * @param table     Tabela
     * @param joinTable Tabela de Junção
     * @param onClause  Comparador
     * @param select    Select completo
     * @return ResultSet
     */
    fun join(
        table: String,
        joinTable: String,
        onClause: String,
        select: String
    ): ResultSet? {
        return select("$select FROM $table JOIN $joinTable ON $onClause")
    }

    /**
     * Deleta a tabela
     *
     * @param table Tabela
     */
    fun deleteTable(table: String) {
        update("DROP TABLE $table")
    }

    /**
     * Limpa a tabela removendo todos registros
     *
     * @param table Tabela
     */
    fun clearTable(table: String) {
        update("TRUNCATE TABLE $table")
    }

    /**
     * Verifica se contem algo na tabela
     *
     * @param table  Tabela
     * @param where  Verificação
     * @param values Valores da verificação
     * @return Se contem
     */
    fun contains(table: String, where: String, vararg values: Any?): Boolean {
        return contains("select * from $table where $where limit 1", *values)
    }

    /**
     * Executa um Select e volta se tem algum registro
     *
     * @param query   Query
     * @param objects Objetos
     * @return Se tem ou nao registro com esta Query
     */
    fun contains(
        query: String, vararg objects: Any?
    ): Boolean {
        var has = false
        if (hasConnection()) try {
            val resultSet = select(
                query, *objects
            )
            has = resultSet!!.next()
            resultSet.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return has
    }

    /**
     * Executa uma Atualização com um Query
     *
     * @param query   Query Pesquisa
     * @param objects Objetos
     * @return -1 se o não ocorreu update em nada, e retorna o numero do update ou insert caso tenha feito pelo menos 1
     */
    fun update(
        query: String, vararg objects: Any?
    ): Int {
        var resultado = -1
        if (hasConnection()) {
            var state: PreparedStatement? = null
            var keys: ResultSet? = null
            try {
                state = query(
                    query, *objects
                )
                resultado = state!!.executeUpdate()
                keys = state.generatedKeys
                if (keys != null) {
                    if (keys.next()) {
                        resultado = keys.getInt(1)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                keys?.close()
                state?.close()
            }
        }
        return resultado
    }

    fun getDouble(
        table: String,
        column: String,
        where: String,
        vararg objects: Any?
    ): Double {
        return getData(Double::class.java, table, column, where, *objects) ?: 1.0
    }

    fun getInt(
        table: String,
        column: String,
        where: String,
        vararg objects: Any?
    ): Int {
        return getData(Int::class.java, table, column, where, *objects) ?: 1
    }

    fun <T> getData(
        type: Class<T>,
        table: String,
        column: String,
        where: String,
        vararg objects: Any?
    ): T? {
        var result: T? = null
        var queryResult: ResultSet? = null
        if (hasConnection()) try {
            queryResult = selectAll(table, where, *objects) ?: return null
            if (queryResult.next()) {
                result = queryResult.getObject(column, type) as T
            }
            queryResult.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            queryResult?.close()
            queryResult?.statement?.close()
        }
        return result
    }

    /**
     * Cria um PreparedStatement com uma Query dada, e aplica os objects
     *
     * @param query   Query
     * @param objects Objetos
     * @return PreparedStatement (Estado da Query)
     */
    fun query(
        queryStr: String, vararg objects: Any?
    ): PreparedStatement? {

        var query = queryStr
        var state: PreparedStatement? = null
        try {
            if (!query.endsWith(";")) {
                query += ";"
            }
            state =
                connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            var id = 1
            for (replacer in objects) {
                try {
                    if (replacer == null) continue
                    val data: Any = engineUsed.convertToSQL(replacer)
                    state.setObject(id, data)
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
                id++
            }
            debug("[MySQL] $query")
        } catch (e: Exception) {
            e.printStackTrace()
            state?.close()
        }
        return state
    }

    fun selectAll(
        table: String, where: String, vararg objects: Any?
    ): ResultSet? {
        return select(
            "SELECT * FROM $table WHERE $where", *objects
        )
    }

    /**
     * Executa um Query e volta um ResultSet
     *
     * @param query   Pesquisa
     * @param objects Objetos
     * @return ResultSet (Resultado da Query)
     */
    fun select(
        query: String, vararg objects: Any?
    ): ResultSet? {
        return try {
            query(
                query, *objects
            )?.executeQuery()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun toString(): String {
        return ("DBManager [user=" + user + ", pass=" + pass + ", host=" + host + ", port=" + port + ", database="
                + database + ", engine=" + engine + "]")
    }

    fun useSQLite(): Boolean {
        return engine === SQLEngineType.SQLITE
    }

    fun setUseSQLite(useSQLite: Boolean) {
        if (useSQLite) engine = SQLEngineType.SQLITE
    }

    companion object {
        var isDebugging = true

        fun setDebug(flag: Boolean) {
            isDebugging = flag
        }

        fun debug(msg: String) {
            if (isDebugging) println("[DB] $msg")
        }

        init {
            javaTypes()
        }
    }
}