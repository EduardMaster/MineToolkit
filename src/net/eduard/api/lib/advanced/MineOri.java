package net.eduard.api.lib.advanced;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class MineOri {

	public static class PlayerSkin {
		private String uuid;
		private String nickname;
		private String signature;
		private String texture;

		public String getTexture() {
			return texture;
		}

		public void setTexture(String texture) {
			this.texture = texture;
		}

		public String getSignature() {
			return signature;
		}

		public void setSignature(String signature) {
			this.signature = signature;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}

	}

	public static PlayerSkin getPlayerSkinByUUID(UUID id) {
		PlayerSkin playerskin = new PlayerSkin();
		playerskin.setUuid(id.toString());
		String link = "https://sessionserver.mojang.com/session/minecraft/profile/" + playerskin.getUuid()
				+ "?unsigned=false";
		try {
			URL url = new URL(link);
			URLConnection conexao = url.openConnection();
			InputStream lendo = conexao.getInputStream();
			BufferedInputStream lendo2 = new BufferedInputStream(lendo);
			byte[] array = new byte[lendo2.available()];
			while (lendo2.read() != -1) {
				lendo2.read(array);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return playerskin;
	}

}
