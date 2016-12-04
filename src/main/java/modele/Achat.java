package modele;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Query;

import util.HibernateUtil;

public class Achat {
	private int id;
	private int quantite;
	private Date date;
	//private Fournisseur fournisseur;
	private Produit produit;
	private int prix_unitaire;
	
	public Achat(){}

	public static Achat getPrixProduitAchete(Produit produit){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        String hql = "SELECT a FROM Achat a WHERE p.produit=:produit ORDER BY p.date DESC";
        Query query = session.createQuery(hql);
        query.setParameter("produit", produit);
        
        Achat achat = null;
        
        if(!query.list().isEmpty()){
        	achat = (Achat)query.list().get(0);
        }
        
        session.close();
        
        return achat;
	}
	
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Produit getProduit() {
		return produit;
	}

	public void setProduit(Produit produit) {
		this.produit = produit;
	}

	public int getPrix_unitaire() {
		return prix_unitaire;
	}

	public void setPrix_unitaire(int prix_unitaire) {
		this.prix_unitaire = prix_unitaire;
	}
}
