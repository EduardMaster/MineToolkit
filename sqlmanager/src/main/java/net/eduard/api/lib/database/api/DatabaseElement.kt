package net.eduard.api.lib.database.api

import net.eduard.api.lib.database.SQLManager

interface DatabaseElement {

    val sqlManager : SQLManager

    fun insert(){
        sqlManager.insertData(this )
    }
    fun delete(){
        sqlManager.deleteData(this )
    }
    fun update(){
        sqlManager.updateData(this )
    }
    fun updateQueue(){
        sqlManager.updateDataQueue(this)
    }
    fun updateCache(){
        sqlManager.updateCache(this)
    }


}