package net.eduard.api.tutorial.nivel_5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class GerarTop {

	public static void main(String[] args) {

		HashMap<String, Double> map = new HashMap<>();
		map.put("Eduard", 2000D);
		map.put("Gabriel", 1000D);
		map.put("Caue", 500D);
//		String sql = "select * from tabela order by dinheiro desc;";
		

		ArrayList<Entry<String, Double>> listaCrescente = new ArrayList<>(
				map.entrySet());

		Collections.sort(listaCrescente, new Comparator<Entry<String, Double>>() {

			@Override
			public int compare(Entry<String, Double> entry1,
					Entry<String, Double> entry2) {
				return entry1.getValue().compareTo(entry2.getValue());
			}
		});
		List<Entry<String, Double>> listaDescrecente = new LinkedList<>(listaCrescente);
		Collections.reverse(listaDescrecente);
		
		for (Entry<String, Double> entry : listaDescrecente) {
			System.out.println(entry.getKey()+": "+entry.getValue());
		}
		System.out.println("---");
		for (Entry<String, Double> entry : listaCrescente) {
			
			System.out.println(entry.getKey()+": "+entry.getValue());
		}

	}
}
