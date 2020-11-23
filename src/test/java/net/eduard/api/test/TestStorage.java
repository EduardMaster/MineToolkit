package net.eduard.api.test;

import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import net.eduard.api.lib.storage.StorageAPI;
import net.eduard.api.lib.storage.annotations.StorageIndex;
import net.eduard.api.lib.storage.annotations.StorageReference;
import org.junit.Test;

import java.util.ArrayList;

public class TestStorage {

    public static class Schematic {
        BlockLoc pos1;
        BlockLoc pos2;

        @Override
        public String toString() {
            return "Schematic{" +
                    "pos1=" + pos1 +
                    ", pos2=" + pos2 +
                    '}';
        }
    }


    public static class BlockLoc {
        String world="world";
        int id=1;
        int data;
        int x=5;
        int y=5;
        int z=5;

        public BlockLoc(){

        }
        public BlockLoc(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public String toString() {
            return "BlockTeste{" +
                    "id=" + id +
                    ", data=" + data +
                    ", x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }
    }


    public static class Jogador {

        @StorageIndex
        String nome;

        @StorageReference
        Servidor server;

        @Override
        public String toString() {
            return "Jogador{" +
                    "nome='" + nome + '\'' +
                    ", server=" + server +
                    '}';
        }
    }

    public static class Servidor{

        @StorageIndex
        int id;

        @StorageReference
        ArrayList<Jogador> players = new ArrayList<>();

        @Override
        public String toString() {
            return "Servidor{" +
                    "id=" + id +
                    ", players=" + players +
                    '}';
        }
    }


    @Test
    public void testReference(){

        Jogador jogador1 = new Jogador();
        jogador1.nome="Eduard";
        Servidor server = new Servidor();
        server.id = 15;
        jogador1.server = server;
        System.out.println(StorageAPI.store(Jogador.class, jogador1));
    //    System.out.println(StorageAPI.store(Servidor.class, server));


    }




    @Test
    public void testInline(){

        {
            Schematic sc = new Schematic();
            sc.pos1 = new BlockLoc(10,10,10);
            sc.pos2 = new BlockLoc(20,20,20);

            Object data = StorageAPI.storeInline(Schematic.class,sc);
            System.out.println(data);
            System.out.println(StorageAPI.restoreInline(Schematic.class, data));

        }
    }

}
