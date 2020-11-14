package net.eduard.api.test;

import org.junit.Test
import java.io.File
import java.io.RandomAccessFile

class Teste {
    fun getIndex(x: Int, y: Int, z: Int, width: Int, length: Int): Int {
        return y * width * length + z * width + x
    }

    @Test
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
    @Test
    fun testandoIndex2() {
        /*
        val width = 10
        val length = 10

        for (x in 0..5) {
            for (z in 0..5) {
                println("x=$x z=$z result="+getIndex(x, 5, z, width, length))
            }
        }
        */
    }
    @Test
    fun testandoIndex() {
        val blocks = Blocks()
        blocks.listRecreate()


        val file = File("E:\\Tudo\\Minecraft - Server\\Servidor Teste\\Testes Schematics\\teste.map")
        val array = ByteArray(200000000)
        val writer = RandomAccessFile(file,"rw")
        calcLag("Escrever") {


            for(index in 0 until 1000000){
                writer.write(index)
                array[index] = 1
            }
           // blocks.write(file)
        }
        calcLag("Escrever2") {
            writer.write(array)

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
