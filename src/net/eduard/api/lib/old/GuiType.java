package net.eduard.api.lib.old;
/**
 * Tipo do tamnho do Menu<br>
 * Versão anterior tinha menas Opções
 * @since 0.7
 * @version 2.0
 * @author Eduard
 * @deprecated O tamanho do Menu é definido em numeros de 1 a 6
 *
 */
public enum GuiType {
	ONE_LINE(9), TWO_LINE(18), THREE_LINE(27), FOUR_LINE(36), FIVE_LINE(45), SIX_LINE(54), LINE_1(9), LINE_2(9 * 2),
	LINE_3(9 * 3), LINE_4(9 * 4), LINE_5(9 * 5), LINE_6(9 * 6);

	private int size;

	private GuiType(int size) {
		setSize(size);
	}

	public int getSize() {
		return this.size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
