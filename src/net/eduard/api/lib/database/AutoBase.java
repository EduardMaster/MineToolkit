package net.eduard.api.lib.database;


/**
 * Banco de dados SQL automatizado
 *
 * @author Eduard
 */
public interface AutoBase {




    default AutoBaseEngine engine() {

        return null;
    }


    default void deleteTable() {

        engine().deleteTable(getClass());
    }

    default void createTable() {

        engine().createTable(getClass());
    }

    default void insert() {
        engine().insertInto(this);

    }
    default void delete() {
        engine().delete(this);

    }
    default void update() {
        engine().update(this);
    }









}