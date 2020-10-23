package net.eduard.api.lib.database.api

interface DatabaseElement {

    var table : DatabaseTable<DatabaseElement>
    fun insert(){
        table.insert(this )
    }
    fun delete(){
        table.delete(this )
    }
    fun update(){
        table.update(this )
    }


}