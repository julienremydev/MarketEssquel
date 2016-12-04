package modele;

public class DemandeClient {

	private int id;
	private Produit produit;
	
	private int quantite;
	
	public DemandeClient (Produit produit, int quantite){
		this.setProduit(produit);
		this.setQuantite(quantite);
	}

	public Produit getProduit() {
		return produit;
	}

	public void setProduit(Produit produit) {
		this.produit = produit;
	}

	public int getQuantite() {
		return quantite;
	}

	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
