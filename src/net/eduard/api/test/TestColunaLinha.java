package net.eduard.api.test;

import net.eduard.api.lib.modules.Extra;

public class TestColunaLinha {

	public static void main(String[] args) {
		System.out.println(Extra.getColumn(10));
		System.out.println(Extra.getColumn(0));
		System.out.println(Extra.getLine(9*2-1));
		System.out.println(Extra.getLine(9*2));
		System.out.println(Extra.getIndex(9, 2));
	}
}
