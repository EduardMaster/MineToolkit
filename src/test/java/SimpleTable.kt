package net.eduard.api.test

import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.lang.Exception


class SimpleTable(var file : File) {

    fun insert(amount : Int){
        file.mkdirs()
        val fileMion = File(file,"data")
        val writer = FileOutputStream(fileMion)
        for(id in 1..amount){
          //  File(file,"$id.data").writeText("OI")
            writer.write("AU".toByteArray())
        }
        writer.close()
    }
    fun update(id : Int, str : String){
        val fileMion = File(file,"data")
        val writer = RandomAccessFile(fileMion, "rw")
        try {
            writer.seek((id-1)*2L)
            writer.write(str.toByteArray())
            writer.close()
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