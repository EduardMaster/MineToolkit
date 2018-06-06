package net.eduard.api.server.vips;

import net.eduard.api.lib.storage.Storable;

public class VipKey implements Storable{
	private String vipName;
	private String vipKey;
	private long duration;
	public String getVipKey() {
		return vipKey;
	}
	public void setVipKey(String vipKey) {
		this.vipKey = vipKey;
	}
	public String getVipName() {
		return vipName;
	}
	public void setVipName(String vipName) {
		this.vipName = vipName;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}

}
