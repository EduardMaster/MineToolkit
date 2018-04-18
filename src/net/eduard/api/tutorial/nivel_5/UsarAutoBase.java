package net.eduard.api.tutorial.nivel_5;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import net.eduard.api.setup.autobase.AutoBase;
import net.eduard.api.setup.autobase.Info;
import net.eduard.api.setup.manager.DBManager;

public class UsarAutoBase {
	public static HashMap<String, Double> contas = new HashMap<>();
	@Info(name = "Tabelita")
	public static class Exemplo implements AutoBase {

		public Connection connect() {

			return UsarAutoBase.db.getConnection();
		}

		@Info(primaryKey = true)
		private int id;

		@Info()
		private int idade;

		@Info
		private int forca;

		@Info(size = 16, update = true)
		private String nome;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getIdade() {
			return idade;
		}

		@Override
		public String toString() {
			return "Exemplo [id=" + id + ", idade=" + idade + ", forca=" + forca + ", nome=" + nome + "]";
		}

		public void setIdade(int idade) {
			this.idade = idade;
		}

		public int getForca() {
			return forca;
		}

		public void setForca(int forca) {
			this.forca = forca;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

	}

	public static DBManager db;

	public static void main(String[] args) {
		db = new DBManager();
		db.setDatabase("mine");
		db.openConnection();
		Exemplo exemplo = new Exemplo();
		exemplo.setNome("teste");
		exemplo.setForca(10);
		exemplo.setIdade(15);
		exemplo.createTable();
//		 exemplo.insert();
		exemplo.restorePrimaryKey();
//		exemplo.delete();
		// exemplo.setNome( "Avestruz");
		// exemplo.update();
		// System.out.println(exemplo.getId());
		List<Exemplo> lista = exemplo.getAll(exemplo);
		for (Exemplo item : lista) {
			item.delete();
		}
		System.out.println(lista);
		db.closeAll();
		
		// ----------------------------------
		
		db = new DBManager();
		db.openConnection();
		
		Conta conta = new Conta();
		
		
		
		
		conta.createTable();
		conta.setDinheiro(14);
		conta.setNome("gabriel");
//		conta.insert();
		List<Conta> contas = conta.getAll(conta);
		Conta conta1 = contas.get(0);
		conta1.setNome("teste");
		conta1.change();
//		conta.update();
//		System.out.println(conta.getNome());
		System.out.println(contas);
//		conta.change();
//		conta.insert();
		
		
		db.closeAll();
		
//		System.out.println("a");
		
		
		
	}
	
	
	
	
	@Info(name="contas")
	public static class Conta implements AutoBase {
		
	
		@Override
		public Connection connect() {
			return db.getConnection();
		}
		
		public boolean autoCloseable() {
			return true;
		}
		@Info(primaryKey=true)
		private int id;
		
		@Info(size=16,name="Jogador",update=true)
		private String nome;
		
		@Info(name="Banco")
		private int dinheiro;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public String toString() {
			return "Conta [id=" + id + ", nome=" + nome + ", dinheiro=" + dinheiro + "]";
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public double getDinheiro() {
			return dinheiro;
		}

		public void setDinheiro(int dinheiro) {
			this.dinheiro = dinheiro;
		}
		
		
		
		

	}

}
