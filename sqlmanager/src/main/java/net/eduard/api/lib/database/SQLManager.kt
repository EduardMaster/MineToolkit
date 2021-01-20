package net.eduard.api.lib.database

import java.sql.Connection
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * SQLManager é parecido com Hibernate porem com bem menas Features
 */
@Suppress("unused")
class SQLManager(var dbManager: DBManager) {

    val queueRunsLimit = 100
    private val updatesQueue: Queue<Any> = ConcurrentLinkedQueue()
    private val deletesQueue: Queue<Any> = ConcurrentLinkedQueue()

    fun runUpdatesQueue(): Int {
        var amount = 0
        for (i in 0 until queueRunsLimit) {
            val data = updatesQueue.poll() ?: break
            updateData(data)
            amount++
        }
        return amount
    }

    fun runDeletesQueue(): Int {
        var amount = 0
        for (i in 0 until queueRunsLimit) {
            val data = deletesQueue.poll() ?: break
            deleteData(data)
            amount++
        }
        return amount
    }

    fun hasConnection(): Boolean {
        return dbManager.hasConnection()
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
        if (updatesQueue.contains(data)) {
            return
        }
        updatesQueue.add(data)
    }
    /**
     *
     * @param data
     */
    fun <E : Any> deleteDataQueue(data: E) {
        deletesQueue.add(data)
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

    fun <T : Any> createTable(dataClass: Class<T>) {
        if (hasConnection()) {
            dbManager.engineUsed.createTable(dataClass)
        }
    }

    fun <T : Any> deleteTable(dataClass: Class<T>) {
        if (hasConnection()) {
            dbManager.engineUsed.deleteTable(dataClass)
        }
    }

    fun <T : Any> clearTable(dataClass: Class<T>) {
        if (hasConnection()) {
            dbManager.engineUsed.clearTable(dataClass)
        }
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