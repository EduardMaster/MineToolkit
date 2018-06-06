package net.eduard.api.test.bungee_messager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class MoneyTop {
	public static void main(String[] args) {
		gerarMoneyTop();
	}
	public static void gerarMoneyTop() {
		Map<String,Double> map = new HashMap<>();
		map.put("Edu", 100d);
		map.put("Pediatra", 200d);
		List<Entry<String, Double>> lista = 
				map.entrySet().stream().sorted((x,z)->z.getValue().compareTo(x.getValue()))
				.limit(10).collect(Collectors.toList());
		for (Entry<String,Double> entrada  : lista) {
			String dono = entrada.getKey();
			Double valoremconta = entrada.getValue();
			System.out.println(" "+dono+" : "+valoremconta);
		}
		
			
		
		
		
		
	}

}
