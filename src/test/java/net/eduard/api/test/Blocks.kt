package net.eduard.api.test


import java.io.*
import java.lang.Exception


class Blocks{

    var storage = true
    var posHigh = Vector(100, 100, 100)
    var posLow = Vector(-100, 0, -100)
    var posPlayer = Vector(0, 50, 0)
    var length = 100
    var width = 100
    var height = 100
    var list = mutableListOf<Block>()
    var arrayId = ByteArray(1000)
    var arrayData = ByteArray(1000)

    fun write(stream: DataOutputStream) {
        posPlayer.write(stream)
        posHigh.write(stream)
        posLow.write(stream)
        stream.writeInt(width)
        stream.writeInt(length)
        stream.writeInt(height)

        if (storage) {
            stream.writeInt(list.size)
            calcLag("For") {
                for (block in list) {
                    block.write(stream)
                }
            }
        }else {
            stream.writeInt(arrayId.size)
            stream.write(arrayId)
            stream.writeInt(arrayData.size)
            stream.write(arrayData)
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
        list.clear()
        if (storage) {
            val amount = stream.readInt()
            println("Quantidade: $amount")
            for (blockId in 0 until amount) {
                try {
                    val block = stream.readBlock()
                    list.add(block)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }else{
            arrayId = ByteArray(stream.readInt())
            stream.readFully(arrayId)
            arrayData = ByteArray(stream.readInt())
            stream.readFully(arrayData)
            var amount = 0
            for (index in arrayId.indices){
                val bloco = Block(
                    arrayId[index].toInt(),
                    arrayData[index].toInt()
                )
                list.add(bloco)
                if (bloco.data == 1){
                    amount++
                }
            }
            println("Blocos retornados realmente $amount")


        }
        stream.close()
    }

    fun write(file: File) {

        write(DataOutputStream(FileOutputStream(file)))
    }

    fun read(file: File) {
        read(DataInputStream(FileInputStream(file)))

    }

    fun getIndex(x: Int, y: Int, z: Int, width: Int, length: Int): Int {
        return y * width * length + z * width + x
    }

    fun listRecreate() {
        val size = width * height * length
        list.clear()
        arrayId = ByteArray(size)
        arrayData = ByteArray(size)
        for (x in 0 until width)
            for (y in 0 until height)
                for (z in 0 until length) {
                    val block = Block()
                    block.x = x
                    block.y = y
                    block.z = z
                    block.id = 0
                    arrayId[getIndex(x, y, z, width, length)] = block.id.toByte()
                    arrayData[getIndex(x, y, z, width, length)] = 1
                    list.add(block)
                }
    }


    class Block(
        var id: Int = 0,
        var data: Int = 0
    ) {
        var x = 0
        var y = 0
        var z = 0
        fun write(stream: DataOutputStream) {
            stream.writeShort(id)
            if (id != 0) {
                stream.write(data)
            }
        }
    }

    class Vector(
        var x: Int = 0,
        var y: Int = 0,
        var z: Int = 0
    )

    fun Vector.write(stream: DataOutput) {
        stream.writeInt(x)
        stream.writeInt(y)
        stream.writeInt(z)
    }

    fun DataInput.readVector(): Vector {
        return Vector(readInt(), readInt(), readInt())
    }

    fun DataInput.readBlock(): Block {
        val id = readByte()
        return Block(
            id.toInt(),
            if (id == 0.toByte()) 0 else readByte().toInt()
        )
    }
}