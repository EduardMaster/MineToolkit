package net.eduard.api.tutorial.nivel_6;

import org.bukkit.inventory.ItemStack;

public class ProdutoMercado {
	
	private String id;
	private ItemStack item;
	private String dono;
	private String categoria;
	private double preco;

	public ItemStack getIcone() {
		ItemStack newItem = item.clone();
		
		
		return newItem;
	}
	public ItemStack getIconeDevolucao() {
		ItemStack newItem = item.clone();
		
		
		return newItem;
	}
	public String getDono() {
		return dono;
	}
	public void setDono(String dono) {
		this.dono = dono;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public double getPreco() {
		return preco;
	}
	public void setPreco(double preco) {
		this.preco = preco;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
