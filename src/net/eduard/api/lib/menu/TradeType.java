package net.eduard.api.lib.menu;

public enum TradeType {

	SELABLE("Venda","Vendivel") ,BUYABLE("Compra","Compravel"), BOTH("Troca","Trocavel");
	private String name;
	private TradeType(String name, String description) {
		setName(name);
		setDescription(description);
		
	}
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
