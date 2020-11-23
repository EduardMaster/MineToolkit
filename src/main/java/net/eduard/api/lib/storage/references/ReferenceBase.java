package net.eduard.api.lib.storage.references;

import net.eduard.api.lib.storage.api.StorageInfo;


public abstract class ReferenceBase<T> {
    private StorageInfo info;
    private Object instance;
    private T restore;


    public ReferenceBase(StorageInfo info, Object instance) {
        setInstance(instance);
        setInfo(info);
    }

    public T getRestore() {
        return restore;
    }

    public void setRestore(T restore) {
        this.restore = restore;
    }

    public abstract void update();

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public StorageInfo getInfo() {
        return info;
    }

    public void setInfo(StorageInfo info) {
        this.info = info;
    }
}
