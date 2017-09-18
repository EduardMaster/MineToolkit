package net.eduard.api.game;

import java.util.Map;

import org.bukkit.entity.Player;

import net.eduard.api.setup.RexAPI;
import net.eduard.api.setup.StorageAPI.Copyable;
import net.eduard.api.setup.StorageAPI.Storable;

public class Title implements Storable , Copyable {

	private int fadeIn;

	private int stay;

	private int fadeOut;

	private String title;
	private String subTitle;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public Title() {
	}

	public Title(int fadeIn, int stay, int fadeOut, String title,
			String subTitle) {
		super();
		this.fadeIn = fadeIn;
		this.stay = stay;
		this.fadeOut = fadeOut;
		this.title = title;
		this.subTitle = subTitle;
	}

	public Title create(Player player) {
		RexAPI.sendTitle(player, title, subTitle, fadeIn, stay, fadeOut);
		return this;
	}

	public int getFadeIn() {

		return fadeIn;
	}

	public int getFadeOut() {

		return fadeOut;
	}

	public int getStay() {

		return stay;
	}

	public void setFadeIn(int fadeIn) {

		this.fadeIn = fadeIn;
	}

	public void setFadeOut(int fadeOut) {

		this.fadeOut = fadeOut;
	}

	public void setStay(int stay) {

		this.stay = stay;
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
