package net.eduard.api.server.vips;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.Storable;

public class VipUsed implements Storable {

	private long since;
	private long duration;
	private long expiration;
	private boolean active;
	private String vipName;

	public String getVipName() {
		return vipName;
	}
	public long getTimeLeft() {
		return getExpirationTime() - Extra.getNow();
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

	public long getExpire() {
		if (expiration ==0) {
			expiration = getExpirationTime();
		}
		return expiration;
	}

	public long getExpirationTime() {
		return duration + since;
	}

	public long getSince() {
		return since;
	}

	public void setSince(long since) {
		this.since = since;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getExpiration() {
		return expiration;
	}

	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

}
