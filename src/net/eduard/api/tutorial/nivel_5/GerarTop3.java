package net.eduard.api.tutorial.nivel_5;

import java.util.HashMap;
import java.util.Map;

public class GerarTop3 {

	public static void main(String[] args) {

		Map<String, Integer> contas = new HashMap<>();

		contas.put("Eduard", 1000);
		contas.put("Edu", 2000);
		contas.put("Gabriel", 500);
		contas.put("Pedro", 200);
		contas.put("Caue", 100);
		contas.entrySet().stream().sorted((x, y) -> y.getValue().compareTo(x.getValue())).forEach(System.out::println);;
	}

}
