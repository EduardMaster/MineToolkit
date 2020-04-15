package net.eduard.api.lib.storage;

import net.eduard.api.lib.modules.Extra;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.*;
import java.util.*;

public class Storation {

    public static void main(String[] args) {
        System.out.println("Novo storage");
        Map<String, Object> mapa = store(new ObjetoNovo());
        System.out.println(mapa);
        System.out.println("-----");
        ObjetoNovo objetoRetornado = restore(mapa, ObjetoNovo.class);
        System.out.println("" + objetoRetornado.nome);

    }

    static class ObjetoNovo extends ObjetoNovo2 {
        static int variavelEstatica = 15;
        String nome = "Edu";
        double cash = 5;
        String[] arrayZada = new String[]{"lorenzo", "bundao"};
        ObjectSecundario sec = new ObjectSecundario();
    }

    static class ObjectSecundario {

        String text = "AAAA";
        int level = 10;
    }

    static class ObjetoNovo2 {

        String sobrenome = "Master";
        double dinheiro = 15;
    }

    public static void log(String msg) {
        System.out.println("[Storation] " + msg);
    }

    public static <E> E restore(Map<String, Object> data, Class<E> classeDeRetorno) {

        E newIntance = null;
        for (Constructor<?> cons : classeDeRetorno.getDeclaredConstructors()) {
            if (cons.getParameterCount() == 0) {
                try {
                    newIntance = (E) cons.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

        }
        if (newIntance == null) {
            log("Cannot return object if do not have a constructor without parameters");

        } else {

            Class<?> claz = classeDeRetorno;
            while (claz != Object.class) {
                restoreFields(claz, data, newIntance);
                claz = claz.getSuperclass();
            }

        }
        return newIntance;
    }

    public static <E> E restore(Map<String, Object> data) {
        String dataTypeName = (String) data.get("object");


        try {
            return (E) restore(data, Class.forName(dataTypeName));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object storeObject(Object value) {
        Class<?> valueType = value.getClass();

        if (valueType.isEnum()) {
            return ((Enum) value).name();
        } else if (valueType.isAnonymousClass() || valueType.isInterface()) {

        } else if (valueType.isArray()) {
            Class<?> arrayType = valueType.getComponentType();
            int arraySize = Array.getLength(value);
            List<Object> newList = new ArrayList<>();
//
            for (int index = 0; index < arraySize; index++) {
                Object arrayLoopValue = Array.get(value, index);

                newList.add(storeObject(arrayLoopValue));

            }
            return newList;
        } else if (Extra.isWrapper(valueType)) {

            if (value instanceof String) {
                return value.toString().replace('§', '&');
            }
            return value;
        } else {
            return store(value);
        }

        return null;
    }


    public static Map<String, Object> store(Object value) {

        Class<?> clz = value.getClass();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("object", clz.getSimpleName());
        List<Class<?>> listClasses = new LinkedList<>();
        Class<?> whileClaz = clz;
        while (whileClaz != Object.class) {
            listClasses.add(whileClaz);
            whileClaz = whileClaz.getSuperclass();
        }
        Collections.reverse(listClasses);
        for (Class<?> forClaz : listClasses) {
            data.putAll(storeFields(forClaz, value));
        }


        return data;
    }

    public static Object storeList(List<?> list, Class<?> listType) {
        List<Object> newList = new ArrayList<>();

        ArrayList<?> listaCopia = new ArrayList<>(list);
        for (Object item : listaCopia) {
            newList.add(restoreObject(item, listType));

        }

        return newList;

    }

    public static Map<String, Object> storeFields(Class<?> claz, Object instanced) {
        Map<String, Object> data = new LinkedHashMap<>();
        for (Field field : claz.getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            if (fieldType == claz) continue;
            if (Modifier.isStatic(field.getModifiers())) continue;
            if (Modifier.isFinal(field.getModifiers())) continue;
            try {

                Object toSave = null;
                Object fieldValue = field.get(instanced);
                if (fieldValue == null) continue;

                if (Extra.isList(fieldType)) {
                    Class<?> listType = Extra.getTypeKey(field.getGenericType());
                    toSave = storeList((List<?>) data, listType);
                } else if (Extra.isMap(fieldType)) {

                } else {
                    toSave = storeObject(fieldValue);
                }
                if (toSave != null)
                    data.put(field.getName(), toSave);

            } catch (Exception ex) {
                log("Field " + field.getName() + " cannnot be saved, error: " + ex.getMessage());
            }


        }
        return data;
    }

    public static Object restoreObject(Object value, Class<?> type) {
        Object toReturn = null;
        try {
            if (type.isEnum()) {

                toReturn = type.getField(value.toString().toUpperCase()).get(0);

            } else if (type.isAnonymousClass() || type.isInterface()) {

            } else if (type.isArray()) {
                Class<?> arrayType = type.getComponentType();
                if (value instanceof List) {
                    List<?> list = (List<?>) value;
                    Object array = Array.newInstance(arrayType, list.size());
                    int index = 0;

                    for (Object item : list) {
                        Array.set(array, index, restoreObject(item, arrayType));
                        index++;
                    }
                    toReturn = array;
                } else if (value instanceof Map) {
                    Map<?, ?> mapa = (Map<?, ?>) value;
                    Object array = Array.newInstance(arrayType, mapa.size());
                    int index = 0;
                    for (Object item : mapa.values()) {

                        Array.set(array, index, restore((Map<String, Object>) item, arrayType));
                        index++;
                    }
                }
            } else if (Extra.isWrapper(type)) {

                Class<?> wrapper = Extra.getWrapper(type);

                Object result = Extra.getResult(Extra.class, "to" + wrapper.getSimpleName(),
                        new Object[]{Object.class}, value);
                if (result instanceof String) {
                    result = ((String) result).replace("&", "§");
                }
                toReturn = result;
            } else {
                if (value instanceof Map) {
                    Map valueMap = (Map) value;
                    toReturn = restore(valueMap, type);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public static List<?> restoreList(Object data, Class<?> listType) {

        List<Object> newList = new ArrayList<>();
        if (data instanceof List) {
            List<?> listOld = (List<?>) data;
            for (Object item : listOld) {
                newList.add(restoreObject(item, listType));
            }
        } else if (data instanceof Map) {
            Map<?, ?> mapOld = (Map<?, ?>) data;
            for (Object item : mapOld.values()) {
                Object objeto = restoreObject(item, listType);
                newList.add(objeto);
            }

        }
        return newList;
    }


    public static class StorationConfig {

    }

    @Target({java.lang.annotation.ElementType.FIELD, ElementType.TYPE,})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface StorationMeta {
        String customName() default "";


    }

    public static void restoreFields(Class<?> claz, Map<String, Object> data, Object instanced) {

        for (Field field : claz.getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            if (fieldType == claz) continue;
            if (Modifier.isStatic(field.getModifiers())) continue;
            if (Modifier.isFinal(field.getModifiers())) continue;
            try {

                String name = field.getName();
                Object mapValue = data.get(name);

                Object toReturn = null;
                if (Extra.isList(fieldType)) {
                    Class<?> listType = Extra.getTypeKey(field.getGenericType());

                } else if (Extra.isMap(fieldType)) {

                } else {
                    toReturn = restoreObject(mapValue, fieldType);
                }


                field.set(instanced, toReturn);

            } catch (Exception ex) {

                log("Field " + field.getName() + " cannot be returned, error: " + ex.getMessage());
                ex.printStackTrace();
            }


        }

    }

}
