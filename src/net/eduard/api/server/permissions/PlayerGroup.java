package net.eduard.api.server.permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.eduard.api.setup.StorageAPI.Reference;
import net.eduard.api.setup.StorageAPI.Storable;

public class PlayerGroup implements  Storable {

	private String name;
	
	@Reference
	private List<PlayerGroup> childrens = new ArrayList<>();
	private List<String> permissions = new ArrayList<>();
	
	private String prefix;
	private String suffix;
	private boolean isDefault;
	
	public List<String> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public Object restore(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void store(Map<String, Object> map, Object object) {
		// TODO Auto-generated method stub
		
	}
	public List<PlayerGroup> getChildrens() {
		return childrens;
	}
	public void setChildrens(List<PlayerGroup> childrens) {
		this.childrens = childrens;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
}
