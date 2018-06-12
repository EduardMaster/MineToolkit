package net.eduard.api.test;

public class PunicaoBolada {
	
	
	
	private String tipo;
	
	private long[] times;
	
	

	public PunicaoBolada(String tipo, long... times) {
		super();
		this.tipo = tipo;
		this.times = times;
	}
	public long getTime(int id) {
		id--;
		if (id>times.length) {
			id = times.length-1;
		}
		return times[id];
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public long[] getTimes() {
		return times;
	}

	public void setTimes(long[] times) {
		this.times = times;
	}

}
