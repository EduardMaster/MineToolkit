package net.eduard.eduardapi.test;

import java.util.Scanner;

public class PluginHost {
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner scanHost = new Scanner(System.in);
		System.out.println("Digite um Numero de Ip");
		String ip = scanHost.nextLine();
		System.out.println(ob(code(ip)));
	}
	private static String ob(String str) {
		String build = "";
		for (int i = 0; i < str.length(); i++) {
			build = build.equals("")
					? "" + (str.charAt(i) + str.length() * str.length())
					: build + ";"
							+ (str.charAt(i) + str.length() * str.length());
		}
		return build;
	}

	@SuppressWarnings("unused")
	private static String deob(String str) {
		final String[] split = str.split(";");
		final int[] parse = new int[split.length];
		for (int i = 0; i < split.length; i++) {
			parse[i] = Integer.parseInt(split[i]) - split.length * split.length;
		}
		String build = "";
		for (int i = 0; i < split.length; i++) {
			build = build + (char) parse[i];
		}
		return build;
	}

	private static String code(String str) {
		int i = str.length();
		char[] a = new char[i];
		int i0 = i - 1;
		while (true) {
			if (i0 >= 0) {
				int i1 = str.charAt(i0);
				int i2 = i0 + -1;
				int i3 = (char) (i1 ^ 56);
				a[i0] = (char) i3;
				if (i2 >= 0) {
					i0 = i2 + -1;
					int i4 = str.charAt(i2);
					int i5 = (char) (i4 ^ 70);
					a[i2] = (char) i5;
					continue;
				}
			}
			return new String(a);
		}
	}

}
