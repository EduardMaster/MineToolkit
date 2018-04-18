package net.eduard.api.tutorial.nivel_5;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class GerarTop2 {
	public static void main(String[] args) {
		Map<String, Integer> dinheiros = new HashMap<>();
		dinheiros.put("Edu", 10000);
		dinheiros.put("Caue", 2500);
		dinheiros.put("Gabriel", 5000);

		Set<Entry<String, Integer>> contas = dinheiros.entrySet();
		// contas.removeIf(x -> x.getValue()<5001);
		List<Entry<String, Integer>> novascontas = contas.stream().
		sorted((x, y) -> x.getValue().compareTo(y.getValue()))
		.collect(Collectors.toList());
		Collections.reverse(novascontas);
		System.out.println(novascontas);

		// List<Integer> nums = new ArrayList<>();
		// nums.add(1);
		// nums.add(5);
		// nums.add(10);
		//// nums.forEach((x)-> System.out.println(x));
		// nums.sort((x,y)-> x + y);
		// Collections.reverse(nums);
		// System.out.println(nums);
	}
}
