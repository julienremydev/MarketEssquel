package modele;

import java.util.Date;

import fr.miage.agents.api.model.Produit;

public class Stock {
	private int id;
	private int quantite;
	private Date dateAchat;
	private Produit produit;
	
	public Stock(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantite() {
		return quantite;
	}

	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}

	public Date getDateAchat() {
		return dateAchat;
	}

	public void setDateAchat(Date dateAchat) {
		this.dateAchat = dateAchat;
	}

	public Produit getProduit() {
		return produit;
	}

	public void setProduit(Produit produit) {
		this.produit = produit;
	}

	public static int getDateAchat(Produit produit) {
		//TODO
		//recuperer bdd
		//retourner date now - nb jours du plus ancien achat
		return 0;
	}
}
