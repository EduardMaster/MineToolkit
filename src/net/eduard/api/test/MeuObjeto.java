package net.eduard.api.test;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;


public class MeuObjeto {

	@Column(primary=true)
	int id;
	String nome;
	UUID uuid;
	double duplo;
	float flutuante;
	char carro;
	long longo;
	byte bit;
	short shorte;
	Date dia;
	Timestamp datetime;
	Time time;
	
	
}
