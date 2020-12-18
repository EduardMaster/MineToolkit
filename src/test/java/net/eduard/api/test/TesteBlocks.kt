package net.eduard.api.test;

import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.File
import java.io.RandomAccessFile

class Teste {



    fun simpleDB() {
        val file = File("E:\\Tudo\\Minecraft - Server\\Servidor Teste\\Testes Schematics\\teste\\")
        file.mkdirs()
        val t = SimpleTable(file)
        val inserir = 100000
        calcLag("$inserir de save") {
            t.insert(inserir)
        }
        val atualizar = 99990
        calcLag("Update o $atualizar") {
            t.update(atualizar, "AE")
        }


    }



    fun testandoIndex() {
        val blocks = Blocks()
        blocks.listRecreate()

        val size = 1000000
        val file = File("E:\\Tudo\\Minecraft - Server\\Servidor Teste\\Testes Schematics\\teste.map")

        val writer = RandomAccessFile(file,"rw")
        val byteWrite = ByteArrayOutputStream(size*2)
        val dataWriter = DataOutputStream(byteWrite)
        calcLag("Escrever") {
            for (index in 0 until size) {
                writer.writeShort(1)
            }
        }
        calcLag("Escrever2") {
            for (index in 0 until size) {
                dataWriter.writeShort(1)
            }
            writer.write(byteWrite.toByteArray())
        }
        writer.close()
        calcLag("Ler") {
            //  blocks.read(file)
        }

    }


}
inline fun calcLag(msg: String, body: () -> Unit) {
    println("Executando: $msg")
    val start = System.currentTimeMillis()
    body.invoke()
    val end = System.currentTimeMillis()
    val dif = end - start
    println("Executado $msg - levou: ${dif}ms")
}
