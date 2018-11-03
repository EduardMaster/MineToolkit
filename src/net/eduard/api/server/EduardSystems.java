package net.eduard.api.server;

public class EduardSystems {

	private static SoulsSystem souls;
	private static VipsSystem vips;

	public static VipsSystem getVips() {
		return vips;
	}

	public static void setVips(VipsSystem vips) {
		EduardSystems.vips = vips;
	}

	public static SoulsSystem getSouls() {
		return souls;
	}

	public static void setSouls(SoulsSystem souls) {
		EduardSystems.souls = souls;
	}

}
