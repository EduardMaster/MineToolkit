package net.eduard.api.lib.database

import java.sql.Connection
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * SQLManager é parecido com Hibernate porem com bem menos Features
 */
@Suppress("unused")
class SQLManager(var dbManager: DBManager) {


    enum class SQLAction {
        UPDATE, DELETE, INSERT, UPDATE_CACHE
    }

    class DataChanged(val data: Any,
                      val action: SQLAction,
                      vararg val collumnsNames : String)

    val actions: Queue<DataChanged> = ConcurrentLinkedQueue()

    val queueRunsLimit = 100


    fun runChanges(): Int {
        var amount = 0
        val updatesDone = mutableListOf<Any>()
        val deletesDone = mutableListOf<Any>()
        for (i in 0 until queueRunsLimit) {
            val dataChange = actions.poll() ?: break
            if (dataChange.action == SQLAction.UPDATE) {
                if (updatesDone.contains(dataChange.data)) {
                    continue
                }
                updatesDone.add(dataChange.data)
                updateData(dataChange.data,*dataChange.collumnsNames)
            } else if (dataChange.action == SQLAction.DELETE) {
                if (deletesDone.contains(dataChange.data)) {
                    continue
                }
                deletesDone.add(dataChange.data)
                deleteData(dataChange.data)
            } else if (dataChange.action == SQLAction.INSERT) {
                insertData(dataChange.data)
            } else if (dataChange.action == SQLAction.UPDATE_CACHE) {
                updateCache(dataChange.data)
            }
            amount++
        }
        return amount
    }

    fun runUpdatesQueue(): Int {
        return runChanges()
    }

    fun runDeletesQueue(): Int {
        return 0
    }

    fun hasConnection(): Boolean {
        return dbManager.hasConnection()
    }

    /**
     *
     * @param primaryKeyValue
     * @param <E> dataType
     * @return
     */
    inline fun <reified E : Any> getData(primaryKeyValue: Any): E? {
        return getData(E::class.java, primaryKeyValue)
    }

    inline fun <reified E : Any> getDataOf(reference: Any): E? {
        return getDataOf(E::class.java, reference)
    }
    inline fun <reified E : Any> getDatasOf(reference: Any): List<E> {
        return getDatasOf(E::class.java, reference)
    }
    /**
     *
     * @param dataClass
     * @param fieldName
     * @param fieldValue
     * @param <E>
     * @return
    </E> */
    fun <E : Any> getData(dataClass: Class<E>, fieldName: String, fieldValue: Any): E? {
        return if (hasConnection()) {
            dbManager.engineUsed.getTable(dataClass)
                .findByColumn(fieldName, fieldValue)
        } else null
    }



    fun <E : Any> getDataOf(dataClass: Class<E>, reference: Any): E? {
        return if (hasConnection()) {
            dbManager.engineUsed.getTable(dataClass)
                .findByReference(reference)
        } else null
    }



    fun <E : Any> getDatasOf(dataClass: Class<E>, reference: Any): List<E> {
        return if (hasConnection()) {
            dbManager.engineUsed.getTable(dataClass)
                .selectByReference(reference)
        } else listOf()
    }

    /**
     *
     * @param dataClass
     * @param primaryKeyValue
     * @param <E>
     * @return
    </E> */
    fun <E : Any> getData(dataClass: Class<E>, primaryKeyValue: Any): E? {
        return if (hasConnection()) {
            dbManager.engineUsed.getTable(dataClass)
                .findByPrimary(primaryKeyValue)
        } else null
    }

    inline fun <reified E : Any> getAll(): List<E> {
        return getAllData(E::class.java)
    }

    fun <E : Any> getAllData(dataClass: Class<E>): List<E> {
        return if (hasConnection()) {
            dbManager.engineUsed.getTable(dataClass)
                .selectAll()
        } else ArrayList()
    }

    inline fun <reified E : Any> getSome(
        collums: String = "*",
        where: String = "",
        orderBy: String = "id",
        desc: Boolean = true,
        limit: Int = 10
    ): List<E> {
        return getSome(E::class.java, collums, where, orderBy, desc, limit)
    }

    /**
     * Alias para getSome()
     */
    fun <E : Any> getAllData(
        dataClass: Class<E>,
        where: String,
        orderBy: String,
        desc: Boolean,
        limit: Int
    ): List<E> {
        return getSome(dataClass, "*", where, orderBy, !desc, limit)
    }

    /**
     * Retorna alguns dados da tabela pesquisa mais complexa do que getAll()
     * @param dataClass Classe
     * @param where Where
     * @param orderBy Coluna de Ordenar
     * @param desc Se é Decrescente
     * @param limit Numero Limite
     * @param <E>
     * @return
    </E> */
    fun <E : Any> getSome(
        dataClass: Class<E>,
        collums: String,
        where: String,
        orderBy: String,
        desc: Boolean,
        limit: Int
    ): List<E> {
        return if (hasConnection()) {
            dbManager.engineUsed.getTable(dataClass)
                .select(collums, where, orderBy, !desc, limit)
        } else listOf()
    }

    fun <E : Any> insertData(data: E) {
        if (hasConnection()) {
            val dataClass = data.javaClass
            dbManager.engineUsed.getTable(dataClass)
                .insert(data)
        }
    }

    val connection: Connection
        get() = dbManager.connection

    /**
     *
     * @param data
     */
    fun <E : Any> updateDataQueue(data: E,vararg columnsNames: String) {
        actions.offer(DataChanged(data, SQLAction.UPDATE,*columnsNames))
    }

    /**
     *
     * @param data
     */
    fun <E : Any> deleteDataQueue(data: E) {
        actions.offer(DataChanged(data, SQLAction.DELETE))
    }

    /**
     *
     * @param data
     */
    fun <E : Any> insertDataQueue(data: E) {
        actions.offer(DataChanged(data, SQLAction.INSERT))
    }
    fun <T : Any> updateData(data: T) {
        return updateData(data, *arrayOf())
    }
    /**
     *
     * @param data
     * @param <T>
    </T> */
    fun <T : Any> updateData(data: T, vararg columnsNames : String) {
        if (hasConnection()) {

            val dataClass = data.javaClass
            dbManager.engineUsed.getTable(dataClass)
                .update(data,*columnsNames)

        }
    }

    fun <T : Any> deleteData(data: T) {
        if (hasConnection()) {
            dbManager.engineUsed.getTable(data.javaClass)
                .delete(data)

        }
    }

    inline fun <reified T : Any> createTable() {
        createTable(T::class.java)
    }

    fun <T : Any> createTable(dataClass: Class<T>) {
        if (hasConnection()) {
            dbManager.engineUsed.createTable(dataClass)
        }
    }

    inline fun <reified E : Any> deleteTable() {
        deleteTable(E::class.java)
    }

    fun <E : Any> deleteTable(dataClass: Class<E>) {
        if (hasConnection()) {
            dbManager.engineUsed.deleteTable(dataClass)
        }
    }

    inline fun <reified E : Any> clearTable() {
        clearTable(E::class.java)
    }


    fun <E : Any> clearTable(dataClass: Class<E>) {
        if (hasConnection()) {
            dbManager.engineUsed.clearTable(dataClass)
        }
    }


    inline fun <reified E : Any> createReferences() {
        createReferences(E::class.java)
    }


    fun <T : Any> createReferences(dataClass: Class<T>) {
        if (hasConnection()) {
            dbManager.engineUsed.getTable(dataClass)
                .createReferences()
        }
    }

    fun updateAllReferences() {
        if (hasConnection()) {
            dbManager.engineUsed.updateReferences()
        }
    }

    inline fun <reified E : Any> updateReferences() {
        if (hasConnection()) {
            dbManager.engineUsed.getTable(E::class.java)
                .updateReferences()
        }
    }

    fun updateCache(data: Any) {
        if (hasConnection()) {
            dbManager.engineUsed.updateCache(data)
        }
    }

    fun cacheInfo() {
        if (hasConnection()) {
            dbManager.engineUsed.cacheInfo()
        }

    }
}