package net.eduard.api.tutorial.nivel_5;

public class SobreAvancadoNumeros {
	public static int teste1(int x, int y) {
		int numero = 1;
		if (x == 0) {
			return 0;
		}
		numero=+x;
		for (int i = 0; i < y; i++) {
			numero = numero *2;
		}
		return numero;
	}
	public static void main(String[] args) {
	
			for (int x = 0; x < 10; x++) {
				for (int y = 0; y < 5; y++) {
					System.out.printf("\nteste1(%s,%s) = %s",x,y,teste1(x,y));
					System.out.printf("		%s << %s = %s",x,y,x <<y);
					System.out.printf("	%s >> %s = %s",x,y,x >>y);
				}
			}
	}

}
