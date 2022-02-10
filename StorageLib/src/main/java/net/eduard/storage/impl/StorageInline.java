package net.eduard.storage.impl;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.StorageAPI;
import net.eduard.storage.api.StorageBase;
import net.eduard.storage.api.StorageInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.Map.Entry;

@SuppressWarnings({"deprecated"})
public final class StorageInline extends StorageBase<Object, String> {

    public StorageInline() {
    }

    public Object restore(StorageInfo info, String data) {
        if (data == null)
            return null;
        debug(">> INLINE TYPE: " + StorageAPI.getClassName(info.getType()));
        Object resultadoFinal = null;
        try {
            resultadoFinal = info.getStore().newInstance();
        } catch (Exception ignored) {
        }
        if (resultadoFinal == null) {
            try {
                resultadoFinal = Extra.getConstructor(info.getType()).newInstance();
            } catch (Exception ignored) {
            }
        }
        if (resultadoFinal == null) {
            debug(">> INLINE INSTANCE INVALID");
            return null;
        }


        String[] split = data.split(";");
        debug("~~ SPLIT SIZE: " +split.length);
        int index = 0;
        for (Field field : info.getType().getDeclaredFields()) {
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            if (Modifier.isNative(field.getModifiers())) {
                continue;
            }
            if (Modifier.isStatic(field.getModifiers()))
                continue;
            if (Modifier.isFinal(field.getModifiers()))
                continue;
            field.setAccessible(true);
            debug(">> INLINE VARIABLE " + field.getName());
            try {
                Storable<?> store = StorageAPI.getStore(field.getType());

                Object fieldFinalValue = split[index];
                debug("~~ SPLIT PART[" + index + "] " + fieldFinalValue + "!");
                if (fieldFinalValue.equals("null")) {
                    fieldFinalValue = null;
                } else if (fieldFinalValue.toString().isEmpty()) {

                    fieldFinalValue = new ArrayList<>();
                } else if (Extra.isList(field.getType())) {

                    Class<?> typeKey = Extra.getTypeKey(field.getGenericType());
                    String[] subSplit = fieldFinalValue.toString().split(",");
                    List<Object> list = new ArrayList<>();
                    for (String pedaco : subSplit) {
                        if (pedaco.isEmpty())
                            continue;
                        list.add(StorageAPI.transform(pedaco, typeKey));
                    }
                    fieldFinalValue = list;
                } else if (Extra.isMap(field.getType())) {

                    Class<?> typeKey = Extra.getTypeKey(field.getGenericType());
                    Class<?> typeValue = Extra.getTypeValue(field.getGenericType());
                    String[] subSplit = fieldFinalValue.toString().split(",");
                    Map<Object, Object> mapa = new HashMap<>();
                    for (String pedaco : subSplit) {
                        String[] corteNoPedaco = pedaco.split("=");
                        Object chave = StorageAPI.transform(corteNoPedaco[0], typeKey);
                        Object value = StorageAPI.transform(corteNoPedaco[1], typeValue);
                        mapa.put(chave, value);
                    }
                    fieldFinalValue = mapa;
                } else if (Extra.isWrapper(field.getType())) {
                    fieldFinalValue = StorageAPI.transform(fieldFinalValue, Objects.requireNonNull(Extra.getWrapper(field.getType())));
                } else {

                    StorageInfo fieldInfo = info.clone();
                    fieldInfo.setField(field);
                    fieldInfo.setType(field.getType());
                    fieldInfo.updateByType();
                    fieldInfo.updateByStorable();
                    fieldInfo.updateByField();
                    fieldInfo.setInline(true);
                    StringBuilder builder = new StringBuilder();
                    String currentText;


                    int findEnd = 0;
                    while (true) {
                        if (index >= split.length) {
                            break;
                        }
                        currentText = split[index];

                        char[] chars = currentText.toCharArray();
                        for (char currentChar : chars) {
                            if (currentChar == '{') {
                                if (findEnd > 0) {
                                    builder.append(currentChar);
                                }
                                findEnd++;

                            }
                            else if (currentChar == '}') {
                                findEnd--;
                                if (findEnd != 0) {
                                    builder.append(currentChar);
                                }
                            }else{
                                builder.append(currentChar);
                            }
                        }
                        builder.append(";");

                        if (findEnd == 0) {
                            break;
                        } else {
                            index++;
                        }

                    }
                    builder.deleteCharAt(builder.length() - 1);
                    debug(">> INLINE INSIDE INLINE: " + builder.toString());

                    fieldFinalValue = StorageAPI.STORE_OBJECT.restore(fieldInfo, builder.toString());


                }
                field.set(resultadoFinal, fieldFinalValue);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            index++;
        }

        return resultadoFinal;

    }

    @SuppressWarnings("unchecked")
    @Override
    public String store(StorageInfo info, Object data) {

        debug("<< INLINE TYPE: " + StorageAPI.getClassName(data.getClass()));
        StringBuilder builder = new StringBuilder();

        for (Field field : info.getType().getDeclaredFields()) {

            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            if (Modifier.isStatic(field.getModifiers()))
                continue;
            if (Modifier.isNative(field.getModifiers()))
                continue;
            if (Modifier.isFinal(field.getModifiers()))
                continue;
            field.setAccessible(true);
            debug("<< INLINE VARIABLE " + field.getName());


            Class<?> fieldType = field.getType();
            try {
                Object fieldValue = field.get(data);
                if (fieldValue == null) {
                    builder.append("null;");

                } else {
                    debug("<< INLINE VARIABLE TYPE: " + StorageAPI.getClassName(fieldValue.getClass()));

                    if (Extra.isWrapper(fieldType)) {
                        builder.append(fieldValue);
                    } else if (Extra.isList(fieldType)) {
                        int index = 0;
                        for (Object object : (List<Object>) fieldValue) {
                            if (index > 0) {
                                builder.append(",");
                            } else
                                index++;
                            builder.append(object);
                        }

                    } else if (Extra.isMap(fieldType)) {
                        int index = 0;
                        for (Entry<Object, Object> entrada : ((Map<Object, Object>) fieldValue).entrySet()) {
                            if (index > 0) {
                                builder.append(",");
                            } else
                                index++;
                            builder.append(entrada.getKey()).append("=").append(entrada.getValue());
                        }

                    } else {

                        StorageInfo fieldInfo = info.clone();
                        fieldInfo.setField(field);
                        fieldInfo.setType(fieldType);
                        fieldInfo.updateByType();
                        fieldInfo.updateByField();
                        fieldInfo.updateByStorable();
                        fieldInfo.setInline(true);

                        builder.append("{");
                        Object result = StorageAPI.STORE_OBJECT.store(fieldInfo, fieldValue);
                        debug("<< INLINE INSIDE INLINE: " +result);
                        builder.append(result);
                        builder.append("}");

                    }
                    builder.append(";");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        return builder.toString();
    }

}
