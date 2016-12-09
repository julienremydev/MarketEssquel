package modele;

import fr.miage.agents.api.model.Produit;

public class Buy {
	private Produit product;
	private int quantite;
	private float prix;
	
	public Buy(Produit product, int quantite) {
		super();
		this.product = product;
		this.quantite = quantite;
	}
	
	public Produit getProduct() {
		return product;
	}
	
	public void setProduct(Produit product) {
		this.product = product;
	}
	public int getQuantite() {
		return quantite;
	}
	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}
	public float getPrix() {
		return prix;
	}
	public void setPrix(float prix) {
		this.prix = prix;
	}
	
	
}
