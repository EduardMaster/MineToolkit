package net.eduard.api.lib.config;

import java.util.*;
import java.util.Map.Entry;


import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.StorageAPI;

/**
 * Interpretador de YAML proprio, Secao da {@link Config}
 *
 * @author Eduard
 */
public class ConfigSection {

    int lineSpaces;

    Object object;

    ConfigSection father;

    String key;
    List<String> comments = new ArrayList<>();

    public ConfigSection(ConfigSection father, String key, Object value) {
        this(key, value);
        this.father = father;
        father.getMap().put(key, this);

    }

    public Map<String, ConfigSection> getMap() {

        if (object != null) {
            if (!(object instanceof Map)) {
                this.object = new LinkedHashMap<String, ConfigSection>();
            }
        }

        @SuppressWarnings("unchecked")
        Map<String, ConfigSection> map = (Map<String, ConfigSection>) object;
        return map;
    }

    public ConfigSection(String key, Object value) {
        this.object = value;
        this.key = key;
    }

    public ConfigSection add(String path, Object value) {
        return add(path, value, new String[0]);
    }

    public ConfigSection add(String path, Object value, String... comments) {
        ConfigSection sec = getSection(path);
        List<String> comentarios = sec.comments;
        if (value != null)
            StorageAPI.autoRegisterClass(value.getClass());
        if (!contains(path)) {
            set(path, value);
        }

        if (comentarios.isEmpty())
            sec.setComments(comments);
        return sec;
    }

    public boolean contains(String path) {
        ConfigSection sec = getSection(path);
        boolean contains = sec.object != null && sec.object != "";
        if (!contains) {
            remove(path);
        }
        return contains;
    }

    public List<Integer> getIntList(String path) {
        return getSection(path).getIntList();
    }

    private Object get() {
        if (object.equals(ConfigUtil.EMPTY_LIST)) {
            return getList();
        }
        if (object.equals(ConfigUtil.EMPTY_SECTION)) {
            return getMap();
        }
        if (object instanceof String) {
            String string = (String) object;
            string = Extra.toChatMessage(string);
            return ConfigUtil.INSTANCE.removeQuotes(string);
        }
        return object;
    }

    public Object get(String path) {
        return getSection(path).getValue();
    }

    public boolean getBoolean() {
        return Extra.toBoolean(object);
    }

    public boolean getBoolean(String path) {
        return getSection(path).getBoolean();
    }

    public Double getDouble() {
        return Extra.toDouble(object);
    }

    public Double getDouble(String path) {
        return getSection(path).getDouble();
    }

    public Float getFloat() {
        return Extra.toFloat(object);
    }

    public Float getFloat(String path) {
        return getSection(path).getFloat();
    }

    public int getIndent() {
        return lineSpaces;
    }

    public Integer getInt() {
        return Extra.toInt(object);
    }

    public Integer getInt(String path) {
        return getSection(path).getInt();
    }

    public List<Integer> getIntList() {
        ArrayList<Integer> list = new ArrayList<>();
        for (Object item : getList()) {
            list.add(Extra.toInt(item));
        }
        return list;
    }


    public <T> T get(String path, Class<T> claz) {

        return (T) getSection(path).getValue(claz);
    }

    public String getKey() {
        return key;
    }

    public Set<String> getKeys() {
        return getMap().keySet();
    }

    public Set<String> getKeys(String path) {
        return getSection(path).getKeys();
    }

    public List<Object> getList() {
        if (!(object instanceof List)) {
            object = new ArrayList<>();
        }
        @SuppressWarnings("unchecked")
        List<Object> list = (List<Object>) object;
        return list;
    }

    public Long getLong() {
        return Extra.toLong(object);
    }

    public Long getLong(String path) {
        return getSection(path).getLong();
    }

    public String getMessage() {

        return Extra.toChatMessage(getString());
    }

    public ArrayList<String> getMessages() {
        ArrayList<String> list = new ArrayList<>();
        for (String text : getStringList()) {
            list.add(Extra.toChatMessage(text));
        }
        return list;
    }

    public List<String> getMessages(String path) {
        return getSection(path).getMessages();
    }

    public ConfigSection getSection(String path) {
        if (path.isEmpty()) {
            return this;
        }
        path = ConfigUtil.INSTANCE.getPath(path);
        if (path.contains(".")) {
            String[] split = path.split("\\.");
            String restPath = path.replaceFirst(split[0] + ".", "");
            return getSection(split[0]).getSection(restPath);
        } else {
            if (getMap().containsKey(path)) {
                return getMap().get(path);
            }
            return new ConfigSection(this, path, "");
        }
    }

    public Set<Entry<String, ConfigSection>> getSet() {
        return getMap().entrySet();
    }

    public String getString() {
        return ConfigUtil.INSTANCE.removeQuotes(Extra.toString(object))
                .replace("\\n", ConfigUtil.LINE_SEPARATOR)
                .replace("<br>", ConfigUtil.LINE_SEPARATOR)
                .replace("/n", ConfigUtil.LINE_SEPARATOR);
    }

    public String getString(String path) {
        return getSection(path).getString();
    }

    public List<String> getStringList() {
        ArrayList<String> list = new ArrayList<>();
        for (Object item : getList()) {
            list.add(ConfigUtil.INSTANCE.removeQuotes(Extra.toString(item)));
        }
        return list;
    }

    public List<String> getStringList(String path) {
        return getSection(path).getStringList();
    }

    public Object getValue() {

        return getValue(null);
    }

    public <T> Object getValue(Class<T> claz) {

        Object data = isMap() ? toMap() : get();

        return StorageAPI.restore(claz, data);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();

        for (Entry<String, ConfigSection> entry : getSet()) {
            ConfigSection value = entry.getValue();
            String key = entry.getKey();
            if (value.isList()) {
                map.put(key, value.getList());
            } else if (value.isMap()) {
                map.put(key, value.toMap());
            } else {
                map.put(key, value.get());
            }

        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public void toSections(Map<Object, Object> map) {
        for (Entry<Object, Object> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            if (value instanceof Map) {
                ConfigSection sec = getSection(key);
                sec.getMap().clear();
                sec.toSections((Map<Object, Object>) value);
            } else if (value instanceof List) {
                getSection(key).set(value);
            } else {
                set(key, value);
            }
        }
    }

    public ConfigSection set(String path, Object value, String... comments) {
        ConfigSection sec = getSection(path);
        if (value == null) {
            sec.remove();
            return sec;
        }

        Object dataSalved = StorageAPI.store(value.getClass(), value);
        sec.set(dataSalved);
        sec.setComments(comments);
        return sec;
    }

    @SuppressWarnings("unchecked")
    public ConfigSection set(Object value) {
        if (value instanceof List) {
            List<?> list = (List<?>) value;
            if (!list.isEmpty()) {
                Object first = list.get(0);
                if (first instanceof Map) {
                    int id = 1;
                    for (Object item : list) {
                        getSection("" + id).set(item);
                        id++;
                    }
                } else {
                    this.object = list;
                }
            } else {
                this.object = list;
            }
        } else if (value instanceof Map) {

            toSections((Map<Object, Object>) value);
        } else {
            this.object = value;
        }
        return this;
    }

    public Collection<ConfigSection> getValues() {
        return getMap().values();
    }

    public Collection<ConfigSection> getValues(String path) {
        return getSection(path).getValues();
    }

    public String message(String path) {
        return getSection(path).getMessage();
    }

    void save(List<String> lines, int spaceId) {
        String space = ConfigUtil.INSTANCE.getSpace(spaceId);
        for (String comment : comments) {
            lines.add(space + "# " + comment);
        }
        if (isList()) {
            lines.add(space + key + ":");
            for (Object text : getList()) {
                lines.add(space + "  - " + text);
            }
        } else if (isMap()) {
            if (spaceId != -1) {
                lines.add(space + key + ":");
            }
            for (ConfigSection section : getMap().values()) {
                section.save(lines, spaceId + 1);
                for (int spaceCreatorIndex = 0; spaceCreatorIndex < lineSpaces; spaceCreatorIndex++) {
                    lines.add("");
                }
            }
        } else {
            if (spaceId == -1)
                return;
            if (object instanceof String){
                object = ((String) object).replace("§","&");
                object = "\""+ object+"\"";
            }
            lines.add(space + key + ": " + object);

        }

    }

    void reload(List<String> lines) {

        int spaceId = 0;
        ConfigSection path = this;

        List<String> currentComments = new ArrayList<>();
        // int index = 0;
        for (String line : lines) {
            // System.err.println("-> " + line);
            String space = ConfigUtil.INSTANCE.getSpace(spaceId);

            if (ConfigUtil.INSTANCE.isList(line)) {
                path.getList().add(ConfigUtil.INSTANCE.getList(line));
            } else if (ConfigUtil.INSTANCE.isComment(line)) {
                currentComments.add(ConfigUtil.INSTANCE.getComment(line));
            } else if (ConfigUtil.INSTANCE.isSection(line)) {
                if (!line.startsWith("  ")) {
                    spaceId = 0;
                    path = this;
                } else if (!line.startsWith(space)) {
                    int time = ConfigUtil.INSTANCE.getSpaceAmount(line);
                    while (time < spaceId) {
                        path = path.father;
                        spaceId--;
                        // time = getSpace(line);
                    }
                }
                space = ConfigUtil.INSTANCE.getSpace(spaceId);
                path = path.getSection(ConfigUtil.INSTANCE.getKey(line, space));
                path.set(ConfigUtil.INSTANCE.getValue(line, space));
                path.comments.addAll(currentComments);
                currentComments.clear();
                spaceId++;
                // nao desencadeia apenas muda o percurso

            }

            // index++;
        }

    }

    public void remove(String path) {
        getSection(path).remove();
    }

    public void remove() {
        father.getMap().remove(key);
    }

    public boolean isList() {
        return object instanceof List || object.toString().equals(ConfigUtil.EMPTY_LIST);
    }

    public boolean isMap() {
        return object instanceof Map;
    }

    public void setComments(List<String> list) {
        this.comments = list;
    }

    public void setComments(String... comments) {
        if (comments != null && comments.length > 0) {
            this.comments.clear();
            for (Object value : comments) {
                this.comments.add(Extra.toString(value));
            }
        }
    }

    public void setIndent(int amount) {
        lineSpaces = amount;
    }

}
