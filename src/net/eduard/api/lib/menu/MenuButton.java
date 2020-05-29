package net.eduard.api.lib.menu;

import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.game.ClickEffect;

public class MenuButton  extends Slot{

	private String name="Botao";
	private int page = 1;
	private Menu menu;
	private transient Menu parentMenu;





	public MenuButton() {

	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	public MenuButton(Menu parent) {
		setParentMenu(parent);
		getParentMenu().addButton(this);

	}
	public MenuButton(String name) {
		setName(name);

	}
	public MenuButton(ItemStack icon) {

		setIcon(icon);

	}

	public MenuButton(String name, ItemStack icon) {
		setName(name);
		setIcon(icon);

	}

	public Menu getMenu() {
		return menu;
	}
	public Shop getShop() {
		return (Shop) menu;
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


	public Menu getParentMenu() {
		return parentMenu;
	}

	public void setParentMenu(Menu parentMenu) {
		this.parentMenu = parentMenu;
	}
}
