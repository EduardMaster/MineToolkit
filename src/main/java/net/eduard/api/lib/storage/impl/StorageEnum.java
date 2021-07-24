package net.eduard.api.lib.storage.impl;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.api.StorageBase;
import net.eduard.api.lib.storage.api.StorageInfo;

public class StorageEnum extends StorageBase<Enum<?>,String> {

    @Override
    public Enum<?> restore(StorageInfo info, String data) {
        try {
            return (Enum<?>) info.getType().getDeclaredField(data.toUpperCase()
                    .replace(" ","_")).get(null);
        } catch (Exception ex) {
            try {
                Enum<?>[] valores = (Enum<?>[]) Extra.getMethodInvoke(
                        info.getType() , "values");
                 return valores[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String store(StorageInfo info,Enum<?> data) {
        return data.toString();
    }

}
