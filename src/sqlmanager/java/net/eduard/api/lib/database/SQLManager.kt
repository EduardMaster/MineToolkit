package net.eduard.api.lib.database

import java.sql.Connection
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 *
 */
class SQLManager {
    lateinit var dbManager: DBManager
    private val updatesQueue: Queue<Any> = ConcurrentLinkedQueue()


    constructor() {}
    constructor(dbManager: DBManager) {
       this.dbManager = dbManager
    }

    fun runUpdatesQueue(): Int {
        var amount = 0
        val queueRunsLimit = 100
        for (i in 0 until queueRunsLimit) {
            val data = updatesQueue.poll() ?: break
            updateData(data)
            amount++
        }
        return amount
    }

    fun hasConnection(): Boolean {
        return this::dbManager.isInitialized && dbManager.hasConnection()
    }

    /**
     *
     * @param dataClass
     * @param fieldName
     * @param fieldValue
     * @param <E>
     * @return
    </E> */
    fun <E> getData(dataClass: Class<E>, fieldName: String, fieldValue: Any): E? {
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
    fun <E> getData(dataClass: Class<E>, primaryKeyValue: Any): E? {
        return if (hasConnection()) {
            dbManager!!.engineUsed.getTable(dataClass)
                .findByPrimary(primaryKeyValue)
        } else null
    }

    fun <E> getAllData(dataClass: Class<E>): List<E> {
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
    fun <E> getAllData(
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

    fun <T : Any> insertData(data: T) {
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
    fun updateDataQueue(data: Any) {
        if (updatesQueue.contains(data)) {
            return
        }
        updatesQueue.add(data)
    }

    /**
     *
     * @param data
     * @param <T>
    </T> */
    fun <T : Any> updateData(data: T) {
        if (hasConnection()) {
            if (hasConnection()) {
                val dataClass = data.javaClass
                dbManager.engineUsed.getTable(dataClass)
                    .update(data)
            }
        }
    }

    fun <T : Any> deleteData(data: T) {
        if (hasConnection()) {
            if (hasConnection()) {

                dbManager.engineUsed.getTable(data.javaClass)
                    .delete(data)
            }
        }
    }

    fun <T> createTable(dataClass: Class<T>) {
        if (hasConnection()) {
            dbManager.engineUsed.createTable(dataClass)
        }
    }

    fun deleteTable(dataClass: Class<*>) {
        if (hasConnection()) {
            dbManager.engineUsed.deleteTable(dataClass)
        }
    }

    fun clearTable(dataClass: Class<*>) {
        if (hasConnection()) {
            dbManager.engineUsed.clearTable(dataClass)
        }
    }
}