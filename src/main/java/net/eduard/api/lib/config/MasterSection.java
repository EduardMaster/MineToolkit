package net.eduard.api.lib.config;

import java.util.LinkedHashMap;
import java.util.Map;
/**
 * Secao do {@link MasterConfig}
 * @author Eduard
 *
 */
public class MasterSection {
	private MasterConfig config;
	private Object value = new LinkedHashMap<>();
	private MasterSection parent;
	private String key;

	public MasterSection getSection(String path) {
		if (path.isEmpty()) {
			return parent;
		}

		if (path.contains(config.section)) {
			String[] split = path.split(config.section);
			String first = split[0];
			String last = path.replace(first + config.section, "");
			MasterSection section = new MasterSection(config, this, first);
			return section.getSection(last);
		} else {
			return new MasterSection(config, this, path.replace(config.section, ""));
		}
	}

	public MasterSection add(String path, Object value, String comments) {
		if (!contains(path)) {
			return set(path, value, comments);
		}
		return getSection(path);
	}

	public MasterSection set(String path, Object value, String... comments) {
		MasterSection sec = getSection(path);
		if (!sec.getParent().getMap().containsKey(sec.getKey())) {
			for (String comment : comments) {
				sec.getParent().getMap().put(String.valueOf(MasterConfig.COMMENT) + sec.getParent().getMap().size(),
						comment);
			}

		}
		sec.getParent().getMap().put(sec.getKey(), value);
		sec.setValue(value);
		return sec;
	}

	public MasterSection remove(String path) {
		MasterSection sec = getSection(path);
		sec.getParent().remove(sec.getKey());
		sec.setValue(null);
		return sec;
	}

	public Object get(String path) {
		return getSection(path).getValue();
	}

	public boolean contains(String path) {
		return get(path) != null;
	}

	public MasterSection(MasterConfig config, MasterSection parent, String key) {
		this.config = config;
		this.setParent(parent);
		this.setKey(key);
		if (parent != null) {
			this.value = parent.getMap().get(key);
		}

	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getMap() {
		return (Map<String, Object>) value;
	}

	public boolean isRoot() {
		return parent == null;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public MasterSection getParent() {
		return parent;
	}

	public void setParent(MasterSection parent) {
		this.parent = parent;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
