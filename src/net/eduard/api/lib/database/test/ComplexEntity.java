package net.eduard.api.lib.database.test;

import net.eduard.api.lib.database.annotations.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@TableName("tabela")
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
    long longo = 1000L;
    double decimal = 15.0D;
    float flutuante = 20.5F;
    boolean forte = false;


}
