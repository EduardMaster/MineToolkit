package net.eduard.api.tutorial.nivel_5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;

public class GerarTop4 {

	private static Map<String, Double> contas = new HashMap<>();
	
	public static void main(String[] args) {

		contas.put("teste", 5D);
		contas.put("affs", 10D);
		double lastValue = 0;
		List<Result> resultados = new ArrayList<>();
		for (Entry<String, Double> entry : contas.entrySet()) {
			String name = entry.getKey();
			Double value = entry.getValue();
			if (value>=lastValue) {
				lastValue = value;
				resultados.add(new Result(name,value));
			}
			
		}
		resultados=Lists.reverse(resultados);
		for (Result line : resultados) {
			System.out.println(line.getOwner()+": "+line.getValue());
		}
		
		
		
	}
	public static class Result{
		private String owner;
		private double value;
		public Result(String name, Double value) {
			setOwner(name);
			setValue(value);
		}
		public String getOwner() {
			return owner;
		}
		public void setOwner(String owner) {
			this.owner = owner;
		}
		public double getValue() {
			return value;
		}
		public void setValue(double value) {
			this.value = value;
		}
		
	}
}
