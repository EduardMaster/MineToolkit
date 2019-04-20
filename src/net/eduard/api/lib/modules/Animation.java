package net.eduard.api.lib.modules;

import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;

/**
 * Sistema de animar {@link ArmorStand}
 * 
 * @author Eduard
 * @version 1.0
 * @since 2.5
 */
public class Animation {

	private double one_degree = (Math.PI / 180);

	private ArmorStand stand;

	public ArmorStand getStand() {
		return stand;
	}

	public Animation(ArmorStand stand) {
		setStand(stand);
	}

	public void setStand(ArmorStand stand) {
		this.stand = stand;
	}

	public void moveHead(double x, double y, double z) {
		EulerAngle headAngule = stand.getHeadPose().add(x, y, z);
		stand.setHeadPose(headAngule);

	}

	public void moveHeadFront(int degrees) {
		moveHead(one_degree * degrees, 0, 0);
	}

	public void moveHeadBack(int degrees) {
		moveHead(-one_degree * degrees, 0, 0);
	}

	public void moveHeadUp(int degrees) {
		moveHead(0, 0, one_degree * degrees);
	}

	public void moveHeadDown(int degrees) {
		moveHead(0, 0, -one_degree * degrees);
	}

	public void moveHeadLeft(int degrees) {
		moveHead(0, one_degree * degrees, 0);
	}

	public void moveHeadRight(int degrees) {
		moveHead(0, -one_degree * degrees, 0);
	}

}
