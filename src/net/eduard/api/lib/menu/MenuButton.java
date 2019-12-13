package net.eduard.api.lib.menu;

import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.modules.ClickEffect;

public class MenuButton  extends Slot{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private int page = 1;
	private Menu menu;

	public MenuButton() {
		// TODO Auto-generated constructor stub
	}


	public Menu getMenu() {
		return menu;
	}
	public Shop getShop() {
		return (Shop) menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	public MenuButton(String name) {
		setName(name);
		// TODO Auto-generated constructor stub
	}

	public MenuButton(String name, ItemStack icon) {
		setName(name);
		setIcon(icon);
		// TODO Auto-generated constructor stub
	}

	public MenuButton(ItemStack icon) {

		setIcon(icon);
		// TODO Auto-generated constructor stub
	}

	private transient ClickEffect click;

	public ClickEffect getClick() {
		return click;
	}

	public void setClick(ClickEffect click) {
		this.click = click;
	}

	/**
	 * Retorna getItem()
	 * @return
	 */
	public ItemStack getIcon() {
		return getItem();
	}
	/**
	 * Executa o setItem()
	 * @param icon
	 */
	public void setIcon(ItemStack icon) {
		setItem(icon);
	}

	public boolean isCategory() {
		return menu!=null;
	}

	

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}



	public String getName() {
		
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	
}
