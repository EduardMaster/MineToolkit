package net.eduard.api.lib.database.test;

import net.eduard.api.lib.database.DBManager;
import net.eduard.api.lib.database.SQLEngineType;
import net.eduard.api.lib.database.SQLManager;
import net.eduard.api.lib.database.annotations.*;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.StorageAPI;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TestSQLManager {
    public static void main(String[] args) {
        StorageAPI.startGson();
        DBManager dbManager = new DBManager("root", "", "localhost");
        dbManager.openConnection();
        SQLManager manager = new SQLManager(dbManager);
        testComplexEntityTable(manager);
        dbManager.closeConnection();
    }

    public static void testComplexEntityTable(SQLManager manager) {
        manager.deleteTable(ComplexEntity.class);
        manager.createTable(ComplexEntity.class);
        ComplexEntity dado = new ComplexEntity();
        manager.insertData(dado);
        ComplexEntity result = manager.getData(ComplexEntity.class, 1);
        System.out.println(result);
    }

    @SuppressWarnings("unused")
    @TableName("tabela_complexa")
    public static class ComplexEntity {
        @ColumnPrimary
        int id;
        @ColumnUnique
        @ColumnSize(16)
        String nome = "Eduard";

        UUID playerID = UUID.randomUUID();
        @ColumnName("saldo de cash")
        int cash;
        Date dia = new Date(System.currentTimeMillis());
        Timestamp stampa = new Timestamp(System.currentTimeMillis());
        Calendar calendario = Calendar.getInstance();
        Time horario = new Time(System.currentTimeMillis());
        long longo = 1000L;
        double decimal = 15.0D;
        float flutuante = 20.5F;
        boolean forte = false;

        @ColumnJson
        SimpleLocation location = new SimpleLocation();

        public static class SimpleLocation {
            String world = "world";
            int x;
            int y;
            int z;
            @Override
            public String toString() {
                return "SimpleLocation{" +
                        "world='" + world + '\'' +
                        ", x=" + x +
                        ", y=" + y +
                        ", z=" + z +
                        '}';
            }
        }
        @Override
        public String toString() {
            return "ComplexEntity{" + location +
                    "location=" +
                    '}';
        }
    }

}
