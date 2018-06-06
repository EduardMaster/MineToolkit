package net.eduard.api.lib.click;
/**
 * Tipo de Compara§§o do Clique que o jogador faz
 * @since 1.0
 * @version 1.0
 * @author Eduard
 *
 */
public enum ClickComparationType {
	ON_BLOCK,
	WITH_LEFT_ON_AIR, WITH_RIGHT_ON_AIR, ON_AIR, WITH_RIGHT, WITH_LEFT, WITH_RIGHT_ON_BLOCK, WITH_LEFT_ON_BLOCK;
	
	public boolean compare(String action) {
		switch (this) {
		
			case ON_AIR :
				return action.contains("AIR");
			case ON_BLOCK :
				return action.contains("BLOCK");
			case WITH_LEFT :
				return action.contains("LEFT");
			case WITH_RIGHT :
				return action.contains("RIGHT");
			case WITH_LEFT_ON_AIR :
				return action.equals("LEFT_CLICK_AIR");
			case WITH_LEFT_ON_BLOCK :
				return action.equals("LEFT_CLICK_BLOCK");
			case WITH_RIGHT_ON_AIR :
				return action.equals("RIGHT_CLICK_AIR");
			case WITH_RIGHT_ON_BLOCK :
				return action.equals("RIGHT_CLICK_BLOCK");

		}
		return false;
	}

}