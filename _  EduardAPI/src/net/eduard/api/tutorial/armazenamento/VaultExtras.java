package net.eduard.api.tutorial.armazenamento;

import net.eduard.api.setup.VaultAPI;

public class VaultExtras {
	public VaultExtras() {
		
		// pegar valor da config
		// tem bugs
		VaultAPI.getChat().getGroupInfoDouble("null", "Membro", "price", 20);
	}
}
