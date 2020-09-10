package net.eduard.api.lib.storage.references;

import java.lang.reflect.Field;

public abstract class ReferenceBase<T> {
    private Field field;
    private Object instance;
    private T restore;

    public T getRestore() {
        return restore;
    }

    public void setRestore(T restore) {
        this.restore = restore;
    }


    public ReferenceBase(T restore, Field field, Object instance) {
        super();
        this.field = field;
        this.instance = instance;
        setRestore(restore);
    }

    public ReferenceBase(Field field, Object instance) {
        super();
        this.field = field;
        this.instance = instance;
    }


    public abstract void update();


    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }


    public Object getInstance() {
        return instance;
    }


    public void setInstance(Object instance) {
        this.instance = instance;
    }


}
