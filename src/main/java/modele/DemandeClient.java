package modele;

public class DemandeClient {

	private int id;
	private Product product;
	
	private int quantite;
	
	public DemandeClient (Product product, int quantite){
		this.setProduit(product);
		this.setQuantite(quantite);
	}

	public Product getProduit() {
		return product;
	}

	public void setProduit(Product product) {
		this.product = product;
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
