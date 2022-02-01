package net.eduard.storage.impl;

import net.eduard.api.lib.modules.Extra;
import net.eduard.storage.api.StorageBase;
import net.eduard.storage.api.StorageInfo;


import java.lang.reflect.Method;

public class StorageEnum extends StorageBase<Enum<?>, String> {

    public Enum<?>[] getEnums(Class<?> enumClass) throws Exception {
        return (Enum<?>[]) Extra.getMethodInvoke(enumClass, "values");
    }

    public Enum<?> findEnum(Class<?> enumClass, String enumName) throws Exception {
        Method method = enumClass.getMethod("valueOf", String.class);
        return (Enum<?>) method.invoke(enumClass, enumName);
    }

    public Enum<?> reflectEnum(Class<?> enumClass, String enumName) throws Exception {
        return (Enum<?>) enumClass.getDeclaredField(enumName).get(enumClass);
    }

    @Override
    public Enum<?> restore(StorageInfo info, String data) {
        String enumName = data.toUpperCase().replace(" ", "_");
        Enum<?> enumFinded = null;
        try {
            enumFinded = findEnum(info.getType(), enumName);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        try {
            if (enumFinded == null) {
                enumFinded = reflectEnum(info.getType(), enumName);
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
            try {
               enumFinded = getEnums(info.getType())[0];

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Dia 31/01/2022 Erro grave porque modificou as Enums para o Tipo Default
         */
        /*
        try {
            if (enumFinded == null) {
                enumFinded = getEnums(info.getType())[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

         */
        if (enumFinded == null) {
            System.out.println("Enum nao encontrada retornando null para: " + enumName);
        }
        return enumFinded;
    }

    @Override
    public String store(StorageInfo info, Enum<?> data) {
        return data.toString();
    }

}
