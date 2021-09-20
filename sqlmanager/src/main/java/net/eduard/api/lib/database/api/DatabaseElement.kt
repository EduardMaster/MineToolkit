package net.eduard.api.lib.database.api

import net.eduard.api.lib.database.SQLManager

interface DatabaseElement {

    val sqlManager : SQLManager

    fun insert(){
        sqlManager.insertData(this )
    }
    fun insertQueue(){
        sqlManager.insertDataQueue(this )
    }
    fun delete(){
        sqlManager.deleteData(this )
    }
    fun deleteQueue(){
        sqlManager.deleteDataQueue(this )
    }
    fun update(){
        sqlManager.updateData(this,*arrayOf() )
    }

    fun updateOnly(vararg collumnsNames : String){
        sqlManager.updateData(this,*collumnsNames )
    }

    fun updateQueue(){
        sqlManager.updateDataQueue(this, *arrayOf())
    }

    fun updateOnlyQueue(vararg columnsNames: String){
        sqlManager.updateDataQueue(this, *columnsNames)
    }

    fun updateCache(){
        sqlManager.updateCache(this)
    }


}