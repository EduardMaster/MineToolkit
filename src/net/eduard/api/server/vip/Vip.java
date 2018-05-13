package net.eduard.api.server.vip;

import java.util.Map;

import net.eduard.api.lib.Extra;
import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.StorageAPI.Storable;

public class Vip implements Storable{

	private int days;

	private long vipStart;

	private String name;

	public boolean hasVip() {
		return days > 0;
	}
	public void addDays(int days) {
		this.days += days;
	}
	public void removeDays(int days) {
		this.days -= days;
	}
	public void removeVip() {
		this.days = 0;
	}
	public boolean isVip() {
		return Mine.inCooldown(vipStart, days * Extra.DAY_IN_SECONDS);
	}
	public long getTimeLeft() {
		return Mine.getCooldown(vipStart, days * Extra.DAY_IN_SECONDS);
	}
	public void setVipEternal() {
		this.days = -1;
	}
	public boolean isVipEternal() {
		return days == -1;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getVipStart() {
		return vipStart;
	}
	public void setVipStart(long vipStart) {
		this.vipStart = vipStart;
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
