package net.eduard.api.test;

import com.sun.org.apache.bcel.internal.generic.ALOAD;

public class JavaBasico {
	
	public static void main(String[] args) {
		
		String texto = "textao ";
		System.out.println(texto);
		
		System.out.println("Hello world");
		int inteiro = 10;
		double decimalgrande = 100.150;
		System.out.println(inteiro);
		System.out.println(decimalgrande);
		float decimalmenor = 50.50f;
		System.out.println(decimalmenor);
		long inteirogrande = 10000000000000000L;
		System.out.println(inteirogrande);
		short inteirocurto =  10000;
		System.out.println(inteirocurto);
		
		byte inteiropequeno = 127;
		System.out.println(inteiropequeno);
		
		boolean verdadeiro = true;
		boolean falsiano = false;
		System.out.println(verdadeiro);
		System.out.println(falsiano);
		
//		boolean allowFlight = true;
//		if ( allowFlight!= true  &&  10 > 5) {
//			System.out.println("foi");
//		}else if (4 > 5) {
//			System.out.println( "foi 2");
//		}else {
//			System.out.println("foi 3");
//		}
		
		enviarMensagem();
		enviarMensagem("Olá tudo bem");
		System.out.println(cacule());
		enviarMensagem("O resultado é "+cacule());
		System.out.println(cacule(300, 400));
	}
	static void enviarMensagem(String msg) {
		System.out.println(msg);
	}
	
	static void enviarMensagem() {
		System.out.println("mensagem enviada");
	}
	static int cacule() {
		return 50 +50;
	}
	static int cacule(int numero1 , int numero2) {
		return numero1 + numero2;
	}
	
	
	
	
	
}
