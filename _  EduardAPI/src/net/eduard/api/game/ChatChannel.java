package net.eduard.api.game;

import java.util.Map;

import net.eduard.api.setup.StorageAPI.Storable;

/**
 * Canal de Chat
 * @author Eduard-PC
 *
 */
public class ChatChannel implements Storable{
	
	private String name;
	private String format;
	private String prefix = "";
	private String suffix = "";
	private String command;
	public ChatChannel() {
	}
	
	public ChatChannel(String name,String format, String prefix, String suffix,
			String command) {
		this.format = format;
		this.name = name;
		this.prefix = prefix;
		this.suffix = suffix;
		this.command = command;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
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

}
