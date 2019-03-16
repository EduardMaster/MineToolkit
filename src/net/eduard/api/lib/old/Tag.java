package net.eduard.api.lib.old;

/**
 * Tag do jogador<br>
 * Versão anterior {@link TagSetup} 1.0
 * 
 * @version 1.0
 * @since 0.7
 * @author Eduard
 * @deprecated Versão Atual {@link net.eduard.api.lib.game.Tag}
 *
 */

public class Tag {
	private String prefix;
	private String suffix;

	public Tag(String prefix, String suffix) {
		setPrefix(prefix);
		setSuffix(suffix);
	}

	public String getPrefix() {
		return this.prefix;
	}

	public String getSuffix() {
		return this.suffix;
	}

	public void setPrefix(String prefix) {
		this.prefix = Principal.getText(prefix);
	}

	public void setSuffix(String suffix) {
		this.suffix = Principal.getText(suffix);
	}
}
