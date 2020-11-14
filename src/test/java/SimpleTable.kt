package net.eduard.api.test

import java.io.File
import java.io.RandomAccessFile
import java.lang.Exception


class SimpleTable(var file : File) {
    val writer : RandomAccessFile
        init{
        val fileMion = File(file,"data")
            fileMion.delete()
            fileMion.createNewFile()
       writer = RandomAccessFile(fileMion, "rw")
    }
    fun insert(amount : Int){
        file.mkdirs()
        val fileMion = File(file,"data")
        for(id in 1..amount){
            writer.write("AU".toByteArray())

        }


    }
    fun update(id : Int, str : String){

        try {
            writer.seek((id-1)*2L)
            writer.write(str.toByteArray())

        }catch (ex : Exception){
            ex.printStackTrace()
        }
        /*
        val reader = File(file,"data").bufferedReader()
        var byteAmount = 0
        for (a in 1..100000){
           val lido=  reader.read()
            if (lido == -1)break
            byteAmount++
        }
          println("Bytes $byteAmount")

        */

    }




}