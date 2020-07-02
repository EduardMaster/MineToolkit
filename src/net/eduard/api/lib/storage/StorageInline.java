package net.eduard.api.lib.storage;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.references.ReferenceValue;

public class StorageInline extends StorageBase {

    public StorageInline(StorageInfo info) {
        super(info);
    }

    @Override
    public Object restore(Object data) {
        if (data == null)
            return null;
        Object resultadoFinal = null;
        try {
            resultadoFinal = getStore().newInstance();

        } catch (Exception ex) {
        }
        if (resultadoFinal == null) {
            try {
                resultadoFinal = getType().newInstance();
            } catch (Exception ex) {
            }
        }
        if (resultadoFinal == null) {
            debug(">> INLINE INSTANCE INVALID");
            return null;
        }

        if (data instanceof String) {
            String line = (String) data;
            String[] split = line.split(";");
            int index = 0;
            for (Field field : getType().getDeclaredFields()) {
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
                    Storable store = StorageAPI.getStore(field.getType());

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

                        StorageObject storage = new StorageObject(getInfo().clone());
                        storage.setField(field);
                        storage.setType(field.getType());
                        storage.updateByType();
                        storage.updateByStoreClass();
                        storage.updateByField();
                        storage.setInline(true);
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
                            b.append((currentText) + ";");


                            if (ended) break;
                            else index++;
                        }
                        b.deleteCharAt(b.length() - 1);
                        debug("TRYING TO RESTORE A INLINE INSIDE INLINE");
                        debug("FOR THIS TEXT: " + b.toString());
                        fieldFinalValue = storage.restore(b.toString());


                    } else if (Extra.isWrapper(field.getType())) {
                        fieldFinalValue = StorageAPI.transform(fieldFinalValue, Extra.getWrapper(field.getType()));
                    } else {
                        fieldFinalValue = null;
                    }
                    field.set(resultadoFinal, fieldFinalValue);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                index++;
            }
        }
        return resultadoFinal;

    }

    @SuppressWarnings("unchecked")
    @Override
    public Object store(Object data) {
        Class<? extends Object> c = getType();
        StringBuilder b = new StringBuilder();

        for (Field field : c.getDeclaredFields()) {

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
                    b.append("-;");

                } else {

                    Storable store = StorageAPI.getStore(fieldValue.getClass());
                    if (Extra.isWrapper(clz)) {
                        b.append(fieldValue);
                    } else if (Extra.isList(field.getType())) {
                        int index = 0;
                        for (Object object : (List<Object>) fieldValue) {
                            if (index > 0) {
                                b.append(",");
                            } else
                                index++;
                            b.append(object);
                        }

                    } else if (Extra.isMap(field.getType())) {
                        int index = 0;
                        for (Entry<Object, Object> entrada : ((Map<Object, Object>) fieldValue).entrySet()) {
                            if (index > 0) {
                                b.append(",");
                            } else
                                index++;
                            b.append(entrada.getKey() + "=" + entrada.getValue());
                        }

                    } else {
                        b.append("{");
                        StorageObject storage = new StorageObject(getInfo().clone());
                        storage.setField(field);
                        storage.setType(clz);
                        storage.updateByType();
                        storage.updateByStoreClass();
                        storage.updateByField();
                        storage.setInline(true);

                        Object result = storage.store(fieldValue);
                        b.append(result);
                        b.append("}");

                    }
                    b.append(";");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        return b.toString();
    }

}
