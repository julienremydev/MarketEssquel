package modele;

public class SuperMarche {
	private int id;
	private String nom;
	private float capital;
	private int stock;
	private static final int MAX_STOCK = 1000;
	
	public SuperMarche(){
		capital = 10000;
		stock = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public float getCapital() {
		return capital;
	}

	public void setCapital(float capital) {
		this.capital = capital;
	}

	public static int getMaxStock() {
		return MAX_STOCK;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
}
