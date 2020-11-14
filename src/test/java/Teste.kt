package net.eduard.api.test;

import net.eduard.api.server.minigame.Blocks
import org.junit.Test
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream

class Teste {
    fun getIndex(x: Int, y: Int, z: Int, width: Int, length: Int): Int {
        return y * width * length + z * width + x
    }
    @Test
    fun simpleDB(){
        val file = File("E:\\Tudo\\Minecraft - Server\\Servidor Teste\\Testes Schematics\\teste\\")
        file.mkdirs()
        val t = SimpleTable(file)
        calcLag("100k de save") {
            t.insert(100000)
        }
        calcLag("Update 50k") {
           t.update(50000,"Caramba")
        }
    }

    @Test
    fun testandoIndex(){
/*
        val width = 10
        val length = 10

        for (x in 0..5) {
            for (z in 0..5) {
                println("x=$x z=$z result="+getIndex(x, 5, z, width, length))
            }
        }
*/

        val blocks = Blocks()
        blocks.height = 100
        blocks.length = 100
        blocks.width = 100
        blocks.listRecreate()


        val file = File("E:\\Tudo\\Minecraft - Server\\Servidor Teste\\Testes Schematics\\teste.map")

        calcLag("Escrever") {
            val writer = ObjectOutputStream(FileOutputStream(file))
            blocks.write(writer)
        }
        calcLag("Ler") {
           // blocks.read(file)
        }

    }
    inline fun calcLag(msg : String , body : () -> Unit){
        println("Executando: $msg")
        val start = System.currentTimeMillis()
        body.invoke()
        val end = System.currentTimeMillis()
        val dif = end-start
        println("Executado levou: ${dif}ms")
    }

}