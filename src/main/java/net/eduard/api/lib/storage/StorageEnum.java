package net.eduard.api.lib.storage;

public class StorageEnum extends StorageBase<Enum<?>,String> {

    @Override
    public Enum<?> restore(StorageInfo info, String data) {
        try {
            return (Enum<?>) info.getType().getDeclaredField(data.toUpperCase()).get(null);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String store(StorageInfo info,Enum<?> data) {
        return data.toString();
    }

}
