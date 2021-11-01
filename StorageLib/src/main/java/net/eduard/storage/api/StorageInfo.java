package net.eduard.storage.api;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.StorageAPI;
import net.eduard.api.lib.storage.annotations.StorageAttributes;
import net.eduard.api.lib.storage.annotations.StorageIndex;
import net.eduard.api.lib.storage.annotations.StorageReference;

import java.lang.reflect.Field;

final public class StorageInfo implements Cloneable {

    public StorageInfo(Class<?> claz) {
        setType(claz);
    }

    public StorageInfo(Field field) {
        setField(field);
        setType(field.getType());


    }

    private Class<?> type;
    private boolean reference;
    private boolean inline;
    private boolean indentifiable;
    private Field field;


    public StorageClassInfo getClassInfo() {
        return StorageAPI.getClassInfo(getType());
    }


    public Class<?> getMapKey() {
        return Extra.getTypeKey(field.getGenericType());
    }

    public Class<?> getMapValue() {
        return Extra.getTypeValue(field.getGenericType());
    }

    public boolean isList() {
        return Extra.isList(type);
    }

    public boolean isMap() {
        return Extra.isMap(type);
    }

    public boolean isEnum() {
        return type.isEnum();
    }

    public Class<?> getListType() {
        if (field != null) {
            return Extra.getTypeKey(field.getGenericType());
        }
        return null;
    }

    public Class<?> getArrayType() {
        return getType().getComponentType();
    }

    public Storable<?> getStore() {
        return StorageAPI.getStore(getType());
    }

    public String getAlias() {
        return getAlias(getType());
    }


    public boolean isStorable() {
        return getStore() != null;
    }

    public StorageInfo clone() {
        try {
            return (StorageInfo) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void updateByType() {
        if (getType() != null) {
            if (getType().isAnnotationPresent(StorageAttributes.class)) {
                StorageAttributes attributes = getType().getAnnotation(StorageAttributes.class);
                update(attributes);
            }
        }
    }

    public void updateByField() {
        if (getField() == null) return;
        if (getField().isAnnotationPresent(StorageAttributes.class)) {
            StorageAttributes attributes = getField().getAnnotation(StorageAttributes.class);
            update(attributes);
        }
        if (getField().isAnnotationPresent(StorageReference.class)) {
            setReference(true);
        }
        if (getField().isAnnotationPresent(StorageIndex.class)) {
            setIndentifiable(true);
        }

    }

    public void updateByStorable() {
        Storable<?> store = getStore(getType());
        if (store == null) return;
        if (store.getClass().isAnnotationPresent(StorageAttributes.class)) {
            StorageAttributes attributes = store.getClass().getAnnotation(StorageAttributes.class);
            update(attributes);
        }


    }

    public void update(StorageAttributes attributes) {
        if (!reference)
            setReference(attributes.reference());
        if (!inline)
            setInline(attributes.inline());
        if (!indentifiable)
            setIndentifiable(attributes.indentificate());
    }


    public Class<?> getType() {
        return type;
    }

    public Storable<?> getStore(Class<?> claz) {
        return StorageAPI.getStore(claz);
    }

    public String getAlias(Class<?> claz) {
        return StorageAPI.getAlias(claz);
    }

    public boolean isReference() {
        return reference;
    }

    public void setReference(boolean reference) {
        this.reference = reference;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public boolean isInline() {
        return inline;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }

    public boolean isIndentifiable() {
        return indentifiable;
    }

    public void setIndentifiable(boolean indentifiable) {
        this.indentifiable = indentifiable;
    }


}
