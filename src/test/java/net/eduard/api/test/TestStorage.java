package net.eduard.api.test;

import net.eduard.api.lib.storage.StorageAPI;
import org.junit.Test;

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


    @Test
    public void testStorage(){

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
