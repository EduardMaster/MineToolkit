package net.eduard.api.lib.storage;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.Map.Entry;

import net.eduard.api.lib.modules.Extra;
@SuppressWarnings({"deprecated"})
public final class StorageInline extends StorageBase<Object, String> {

    StorageInline(){}

    public Object restore(StorageInfo info, String data) {
        if (data == null)
            return null;
        Object resultadoFinal = null;
        try {
            resultadoFinal = info.getStore().newInstance();

        } catch (Exception ignored) {
        }
        if (resultadoFinal == null) {
            try {


                resultadoFinal = Extra.getEmptyConstructor(info.getType()).newInstance();
            } catch (Exception ignored) {
            }
        }
        if (resultadoFinal == null) {
            debug(">> INLINE INSTANCE INVALID");
            return null;
        }


        String[] split = ((String) data).split(";");
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

            try {
                Storable<?> store = StorageAPI.getStore(field.getType());

                Object fieldFinalValue = split[index];
                debug("SPLIT PART " + fieldFinalValue + " - " + index);
                if (fieldFinalValue.equals("-")) {
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
                } else if (store != null) {

                    StorageInfo fieldInfo = info.clone();
                    fieldInfo.setField(field);
                    fieldInfo.setType(field.getType());
                    fieldInfo.updateByType();
                    fieldInfo.updateByStoreClass();
                    fieldInfo.updateByField();
                    fieldInfo.setInline(true);
                    StringBuilder b = new StringBuilder();
                    String currentText = fieldFinalValue.toString();


                    boolean ended = false;
                    while (true) {
                        currentText = split[index];
                        ended = currentText.endsWith("}");
                        currentText = currentText.replace("{", "");
                        if (ended) {
                            currentText = currentText.replace("}", "");
                        }
                        b.append(currentText).append(";");


                        if (ended) break;
                        else index++;
                    }
                    b.deleteCharAt(b.length() - 1);
                    debug("TRYING TO RESTORE A INLINE INSIDE INLINE");
                    debug("FOR THIS TEXT: " + b.toString());
                    fieldFinalValue = StorageAPI.STORE_OBJECT.restore(fieldInfo, b.toString());


                } else if (Extra.isWrapper(field.getType())) {
                    fieldFinalValue = StorageAPI.transform(fieldFinalValue, Objects.requireNonNull(Extra.getWrapper(field.getType())));
                } else {
                    fieldFinalValue = null;
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
            Class<?> clz = field.getType();
            try {
                Object fieldValue = field.get(data);
                if (fieldValue == null) {
                    builder.append("-;");

                } else {


                    if (Extra.isWrapper(clz)) {
                        builder.append(fieldValue);
                    } else if (Extra.isList(field.getType())) {
                        int index = 0;
                        for (Object object : (List<Object>) fieldValue) {
                            if (index > 0) {
                                builder.append(",");
                            } else
                                index++;
                            builder.append(object);
                        }

                    } else if (Extra.isMap(field.getType())) {
                        int index = 0;
                        for (Entry<Object, Object> entrada : ((Map<Object, Object>) fieldValue).entrySet()) {
                            if (index > 0) {
                                builder.append(",");
                            } else
                                index++;
                            builder.append(entrada.getKey()).append("=").append(entrada.getValue());
                        }

                    } else {
                        builder.append("{");
                        StorageInfo fieldInfo = info.clone();
                        fieldInfo.setField(field);
                        fieldInfo.setType(clz);
                        fieldInfo.updateByType();
                        fieldInfo.updateByStoreClass();
                        fieldInfo.updateByField();
                        fieldInfo.setInline(true);

                        Object result = StorageAPI.STORE_OBJECT.store(fieldInfo, fieldValue);
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
