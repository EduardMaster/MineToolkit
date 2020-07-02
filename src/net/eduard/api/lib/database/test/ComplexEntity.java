package net.eduard.api.lib.database.test;

import net.eduard.api.lib.database.annotations.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.Vector;

@TableName("tabela_complexa")
public class ComplexEntity {


    @ColumnPrimary
    int id;

    @ColumnName("saldo de cash")
    int cash;
    @ColumnUnique
    @ColumnSize(16)
    String nome;
    UUID playerID = UUID.randomUUID();
    Date dia = new Date(System.currentTimeMillis());

    Timestamp stampa = new Timestamp(System.currentTimeMillis());

    Calendar calendario = Calendar.getInstance();
    Time horario = new Time(System.currentTimeMillis());
    long longo = 1000L;
    double decimal = 15.0D;
    float flutuante = 20.5F;
    boolean forte = false;
    SimpleLocation location = new SimpleLocation();

    public static class SimpleLocation{
        String world="world";
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
        return "ComplexEntity{" +
                "location=" + location +
                '}';
    }
}
