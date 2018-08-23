package net.eduard.api.test;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import net.eduard.api.lib.AutoBase;
import net.eduard.api.lib.AutoBase.Info;
import sun.net.www.http.HttpClient;

@Info(name="tabela")
public class MeuObjeto implements AutoBase<String> {
	@Info(primary = true)
	int id;
	@Info(secondary = true)
	String nome = "Boracha";
	UUID uuid = UUID.randomUUID();
	double duplo;
	float flutuante;
	char carro = 'A';
	long longo;
	byte bit;
	short shorte;
	Date dia = new Date(System.currentTimeMillis());
	Timestamp datetime = new Timestamp(System.currentTimeMillis());
	Time time  = new Time(System.currentTimeMillis());
	Calendar calendario = Calendar.getInstance();
	java.util.Date diautil = Calendar.getInstance().getTime();
	@Override
	public String toString() {
		
		return "MeuObjeto [id=" + id + ", nome=" + nome + ", uuid=" + uuid + ", duplo=" + duplo + ", flutuante="
				+ flutuante + ", carro=" + carro + ", longo=" + longo + ", bit=" + bit + ", shorte=" + shorte + ", dia="
				+ dia + ", datetime=" + datetime + ", time=" + time + ", calendario=" + calendario + ", diautil="
				+ diautil + "]";
	}

}
