package net.eduard.api.lib.database;

import net.eduard.api.lib.database.annotations.ColumnName;
import net.eduard.api.lib.database.annotations.ColumnSize;
import net.eduard.api.lib.database.annotations.TableName;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.StorageAPI;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class TestAutoBase {
    public static void main(String[] args) {

        //DBConnector c = new DBConnector("E:\\Tudo\\kotlin\\teste.db");
        StorageAPI.register(Rank.class);
        DBManager c = new DBManager();
        c.openConnection();

        AutoBaseEngine en = new AutoBaseEngine(c.getConnection());
        en.deleteTable(Jogador.class);

        en.createTable(Jogador.class);

        en.clearTable(Jogador.class);
        Jogador j = new Jogador();
        j.nome = "Edu";
        j.cash = 15;

        en.insertInto(j);
        en.delete(j);
        int idCreated = en.insertInto(j);
        Jogador j2 = en.fetchById(idCreated, Jogador.class);
        System.out.println(j2);
        j2.nome = "Gabriel";

        en.update(j2);


        c.closeConnection();

    }

    public static class Rank implements Storable {

        int id;
        String name;
        String tag;
    }

    @TableName("tabela teste")
    public static class Jogador {
        int id;

        @ColumnName("saldo de cash")
        int cash;
        @ColumnSize(unique = true, size = 16)
        String nome;
        UUID playerID = UUID.randomUUID();
        Date dia = new Date(System.currentTimeMillis());
        Timestamp stampa = new Timestamp(System.currentTimeMillis());
        Calendar calendario = Calendar.getInstance();
        long longo = 1000L;
        double decimal = 15.0D;
        float flutuante = 20.5F;
        boolean forte = false;


        Rank r = new Rank();

        @Override
        public String toString() {

            return "Jogador{" +
                    "id=" + id +
                    ", cash=" + cash +
                    ", nome='" + nome + '\'' +
                    ", playerID=" + playerID +
                    ", dia=" + dia +
                    ", longo=" + longo +
                    ", decimal=" + decimal +
                    ", flutuante=" + flutuante +
                    '}';
        }
    }
}
