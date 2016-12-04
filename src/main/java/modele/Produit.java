package modele;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.hibernate.Session;
import org.hibernate.Query;

import util.HibernateUtil;

public class Produit {

	private long idProduit;

	private String nomProduit;

	private String descriptionProduit;

	private String categorie;

	private String marque;

	public Produit(){}


	public boolean equals( String nom) {
		if (nom.equals(this.getNomProduit())) {
			return true;
		}
		else {
			return false;
		}

	}
	
	public static void ajoutProduit(long ref, String nom){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Produit newP = new Produit();
        newP.setIdProduit(ref);
        newP.setNomProduit(nom);
        
        session.save(newP);
        session.close();
	}
	
	public static Produit getProduit(long ref){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        String hql = "SELECT p FROM Produit p WHERE p.idProduit=:ref";
        Query query = session.createQuery(hql);
        query.setParameter("ref", ref);
        
        Produit produit = null;
        
        if(!query.list().isEmpty()){
            produit = (Produit)query.list().get(0);
        }
        
        session.close();
        
        return produit;
	}

 
	public long getIdProduit() {
		return idProduit;
	}


	public void setIdProduit(long idProduit) {
		this.idProduit = idProduit;
	}


	public String getNomProduit() {
		return nomProduit;
	}


	public void setNomProduit(String nomProduit) {
		this.nomProduit = nomProduit;
	}


	public String getMarque() {
		return marque;
	}


	public void setMarque(String marque) {
		this.marque = marque;
	}


	public String getDescriptionProduit() {
		return descriptionProduit;
	}


	public void setDescriptionProduit(String descriptionProduit) {
		this.descriptionProduit = descriptionProduit;
	}


	public String getCategorie() {
		return categorie;
	}


	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}
}
