package net.eduard.api.server.minigame


import java.io.*
import java.lang.Exception
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

class Blocks : Serializable {

    var posHigh = Vector()
    var posLow = Vector()
    var posPlayer = Vector()
    var length = 5
    var width = 5
    var height = 5
    var list = mutableListOf<Block>()
    fun write(stream : ObjectOutputStream){
        stream.writeObject(this)
        stream.flush()
        stream.close()
    }
    fun write(stream: DataOutputStream) {
        posPlayer.write(stream)
        posHigh.write(stream)
        posLow.write(stream)
        stream.writeInt(width)
        stream.writeInt(length)
        stream.writeInt(height)
        stream.writeInt(list.size)
        for (block in list) {
            block.write(stream)
        }
        stream.flush()
        stream.close()
    }

    fun read(stream: DataInputStream) {
        posPlayer = stream.readVector()
        posHigh = stream.readVector()
        posLow = stream.readVector()
        width = stream.readInt()
        length = stream.readInt()
        height = stream.readInt()
        val amount = stream.readInt()
        list.clear()
        println("Quantidade: $amount")
        for (blockId in 0..amount) {
            try {
                val block = stream.readBlock()
                list.add(block)
            }catch (ex:Exception){
                ex.printStackTrace()
            }
        }
        stream.close()
    }

    fun write(file: File) {

        write(DataOutputStream(GZIPOutputStream(FileOutputStream(file))))
    }
    fun read(file : File){
        read(DataInputStream(GZIPInputStream(FileInputStream(file))))

    }


    fun listRecreate() {
        list.clear()
        for (x in 0..width)
            for (y in 0..height)
                for (z in 0..length) {
                    val block = Block()
                    list.add(block)
                }
    }


    class Block(
        var id: Byte = 0 ,
        @Transient var data: Byte = 0
    )  : Serializable {
        @Transient var x = 0
        @Transient var y = 0
        @Transient var z = 0
        fun write(stream: DataOutput) {
            /*
            stream.writeByte(id)
            if (id != 0) {
                stream.writeByte(data)
            }*/
        }
    }

    class Vector (
        var x: Int = 0,
        var y: Int = 0,
        var z: Int = 0
    ): Serializable

    fun Vector.write(stream: DataOutput) {
        stream.writeInt(x)
        stream.writeInt(y)
        stream.writeInt(z)
    }

    fun DataInput.readVector(): Vector {
        return Vector(readInt(), readInt(), readInt())
    }

    fun DataInput.readBlock(): Block {
        val id =readByte()
        return Block(id, if (id==0.toByte())0 else readByte())
    }
}