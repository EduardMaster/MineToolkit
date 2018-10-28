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
		Object resultadoFinal = null;
//		if (data == null) {
//			try {
//				resultadoFinal = (Object) getType().newInstance();
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//		} else {
//			resultadoFinal = data;
//		}
//
//		String line = (String) data;
//		String[] split = line.split(";");
//		int index = 0;
//		for (Field field : getType().getDeclaredFields()) {
//			if (Modifier.isTransient(field.getModifiers())) {
//				continue;
//			}
//			if (Modifier.isStatic(field.getModifiers()))
//				continue;
//			if (Modifier.isFinal(field.getModifiers()))
//				continue;
//			field.setAccessible(true);
//
//			try {
//				Storable store = StorageAPI.getStore(field.getType());
//				Object fieldFinalValue = split[index];
//
//				if (fieldFinalValue.equals("-")) {
//					fieldFinalValue = null;
//				} else if (fieldFinalValue.toString().isEmpty()) {
//
//					fieldFinalValue = new ArrayList<>();
//				}
//
//				else if (Extra.isList(field.getType())) {
//
//					Class<?> typeKey = Extra.getTypeKey(field.getGenericType());
//					String[] subSplit = fieldFinalValue.toString().split(",");
//					List<Object> list = new ArrayList<>();
//					for (String pedaco : subSplit) {
//						if (pedaco.isEmpty())
//							continue;
//						list.add(StorageAPI.transform(pedaco, typeKey));
//					}
//					fieldFinalValue = list;
//				} else if (Extra.isMap(field.getType())) {
//
//					Class<?> typeKey = Extra.getTypeKey(field.getGenericType());
//					Class<?> typeValue = Extra.getTypeValue(field.getGenericType());
//					String[] subSplit = fieldFinalValue.toString().split(",");
//					Map<Object, Object> mapa = new HashMap<>();
//					for (String pedaco : subSplit) {
//						String[] corteNoPedaco = pedaco.split("=");
//						Object chave = StorageAPI.transform(corteNoPedaco[0], typeKey);
//						Object value = StorageAPI.transform(corteNoPedaco[1], typeValue);
//						mapa.put(chave, value);
//					}
//					fieldFinalValue = mapa;
//				} else if (store != null) {
//					if (field.isAnnotationPresent(StorageAttributes.class)) {
//						StorageAttributes atr = field.getAnnotation(StorageAttributes.class);
//
//						if (atr.inline()) {
//							int length = index + field.getType().getDeclaredFields().length;
//							StringBuilder b = new StringBuilder();
//							while (index < length) {
//								b.append(split[index] + ";");
//								index++;
//							}
//							fieldFinalValue = store.restore(b.toString());
//						} else if (atr.reference()) {
//							if (fieldFinalValue.toString().contains(StorageAPI.REFER_KEY)) {
//								StorageAPI.newReference(new ReferenceValue(
//										(int) Extra.toInt(fieldFinalValue.toString().split(StorageAPI.REFER_KEY)[1]), field, data));
////							b.append(getAlias(fieldValue.getClass()) + StorageAPI.REFER_KEY
////									+ StorageAPI.getIdByObject(fieldValue));
////							StorageAPI.newReference(new ReferenceValue(id, field, resultadoFinal));
//								fieldFinalValue = null;
//							}
//						}
//					}
////						System.out.println("[Tenso antes] "+fieldFinalValue);
////						int id = (int) new StorageObject(field.getType(), true).restore(fieldFinalValue);
////						
////						System.out.println("[Tenso] "+id+" class "+field.getType());
//
//				} else if (Extra.getWrapper(field.getType()) != null) {
//					fieldFinalValue = StorageAPI.transform(fieldFinalValue, Extra.getWrapper(field.getType()));
//				} else {
//					fieldFinalValue = null;
//				}
//				field.set(resultadoFinal, fieldFinalValue);
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//			index++;
//		}

		return resultadoFinal;

	}

	@Override
	public Object store(Object data) {
//		Class<? extends Object> c = getType();
//		StringBuilder b = new StringBuilder();
//		for (Field field : c.getDeclaredFields()) {
//			field.setAccessible(true);
//			if (Modifier.isTransient(field.getModifiers())) {
//				continue;
//			}
//			if (Modifier.isStatic(field.getModifiers()))
//				continue;
//			if (Modifier.isFinal(field.getModifiers()))
//				continue;
//			try {
//				Object fieldValue = field.get(data);
//				if (fieldValue == null) {
//					b.append("-;");
//
//				} else {
//					Storable store = StorageAPI.getStore(fieldValue.getClass());
//					if (Extra.isList(field.getType())) {
//						int index = 0;
//						for (Object object : (List<Object>) fieldValue) {
//							if (index > 0) {
//								b.append(",");
//							} else
//								index++;
//							b.append(object);
//						}
//
//					} else if (Extra.isMap(field.getType())) {
//						int index = 0;
//						for (Entry<Object, Object> entrada : ((Map<Object, Object>) fieldValue).entrySet()) {
//							if (index > 0) {
//								b.append(",");
//							} else
//								index++;
//							b.append(entrada.getKey() + "=" + entrada.getValue());
//						}
//
//					} else if (store != null) {
//
//						if (field.isAnnotationPresent(StorageAttributes.class)) {
//							StorageAttributes atr = field.getAnnotation(StorageAttributes.class);
//
//							if (atr.inline()) {
//								b.append(store.store(fieldValue));
//							} else if (atr.reference()) {
//								b.append(getAlias(fieldValue.getClass()) + StorageAPI.REFER_KEY
//										+ StorageAPI.getIdByObject(fieldValue));
//							}
//						} else {
//							b.append("-;");
//						}
//					} else {
//						b.append(fieldValue);
//					}
//					b.append(";");
//				}
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//
//		}
//		return b.toString();
		return data;
	}

}
