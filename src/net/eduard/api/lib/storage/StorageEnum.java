package net.eduard.api.lib.storage;

public class StorageEnum extends StorageBase {

    public StorageEnum(StorageInfo info) {
        super(info);
    }

    @Override
    public Object restore(Object data) {
        try {
            return getType().getDeclaredField(data.toString().toUpperCase()).get(null);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Object store(Object data) {
        return data.toString();
    }

}
