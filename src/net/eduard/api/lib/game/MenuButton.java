package net.eduard.api.lib.game;

import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.manager.EffectManager;
import net.eduard.api.lib.modules.ClickEffect;
import net.eduard.api.lib.storage.Storable;

public class MenuButton implements Storable{
	
	private String name;
	private int page=1;
	private int positionX;
	private int positionY;
	
	private ItemStack icon;
	private EffectManager effects;
	private MenuShop shop;
	public boolean isCategory() {
		return shop != null;
	}

	public MenuShop getShop() {
		return shop;
	}

	public void setShop(MenuShop shop) {
		this.shop = shop;
	}
	private transient ClickEffect click;
	public ClickEffect getClick() {
		return click;
	}
	public void setClick(ClickEffect click) {
		this.click = click;
	}
	public ItemStack getIcon() {
		return icon;
	}
	public void setIcon(ItemStack icon) {
		this.icon = icon;
	}
	public int getIndex() {
		return Mine.getPosition(positionY, positionX);
	}
	public void setIndex(int index) {
		int x = Mine.getColumn(index);
		int y = (index/9)+1;
		setPositionX(x);
		setPositionY(y);
		
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public EffectManager getEffects() {
		return effects;
	}
	public void setEffects(EffectManager effects) {
		this.effects = effects;
	}

	public int getPositionX() {
		return positionX;
	}
	public void setPositionX(int positionX) {
		this.positionX = positionX;
		
	}
	public int getPositionY() {
		return positionY;
	}
	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
