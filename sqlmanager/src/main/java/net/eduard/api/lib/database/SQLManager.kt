package net.eduard.api.lib.database

import java.sql.Connection
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * SQLManager é parecido com Hibernate porem com bem menas Features
 */
@Suppress("unused")
class SQLManager(var dbManager: DBManager) {


    enum class SQLAction {
        UPDATE, DELETE, INSERT, UPDATE_CACHE
    }

    class DataChanged(val data: Any, val action: SQLAction)

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
                updateData(dataChange.data)
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

    inline fun <reified T : Any> getAll(): List<T> {
        return getAllData(T::class.java)
    }


    fun <E : Any> getAllData(dataClass: Class<E>): List<E> {
        return if (hasConnection()) {
            dbManager.engineUsed.getTable(dataClass)
                .selectAll()
        } else ArrayList()
    }

    /**
     *
     * @param dataClass
     * @param where
     * @param orderBy
     * @param desc
     * @param limit
     * @param <E>
     * @return
    </E> */
    fun <E : Any> getAllData(
        dataClass: Class<E>,
        where: String,
        orderBy: String,
        desc: Boolean,
        limit: Int
    ): List<E> {
        return if (hasConnection()) {
            dbManager.engineUsed.getTable(dataClass)
                .select(where, orderBy, !desc, limit)
        } else ArrayList()
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
    fun <E : Any> updateDataQueue(data: E) {
        actions.offer(DataChanged(data, SQLAction.UPDATE))
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

    /**
     *
     * @param data
     * @param <T>
    </T> */
    fun <T : Any> updateData(data: T) {
        if (hasConnection()) {

            val dataClass = data.javaClass
            dbManager.engineUsed.getTable(dataClass)
                .update(data)

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

    inline fun <reified T : Any> deleteTable() {
        deleteTable(T::class.java)
    }

    fun <T : Any> deleteTable(dataClass: Class<T>) {
        if (hasConnection()) {
            dbManager.engineUsed.deleteTable(dataClass)
        }
    }

    inline fun <reified T : Any> clearTable() {
        clearTable(T::class.java)
    }


    fun <T : Any> clearTable(dataClass: Class<T>) {
        if (hasConnection()) {
            dbManager.engineUsed.clearTable(dataClass)
        }
    }


    inline fun <reified T : Any> createReferences() {
        createReferences(T::class.java)
    }


    fun <T : Any> createReferences(dataClass: Class<T>) {
        if (hasConnection()) {
            dbManager.engineUsed.getTable(dataClass)
                .createReferences()
        }
    }

    fun updateReferences() {
        if (hasConnection()) {
            dbManager.engineUsed.updateReferences()
        }
    }

    fun updateCache(data: Any) {
        if (hasConnection()) {
            dbManager.engineUsed.updateCache(data)
        }
    }
}