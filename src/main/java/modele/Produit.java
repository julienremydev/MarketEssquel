package modele;

import org.hibernate.Session;
import org.hibernate.query.Query;

import util.HibernateUtil;

public class Produit {
	private int id;
	private String reference;
	private String nom;

	public Produit(){}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean equals( String nom) {
		if (nom.equals(this.getNom())) {
			return true;
		}
		else {
			return false;
		}

	}
	
	public static void ajoutProduit(String reference, String nom){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Produit newP = new Produit();
        newP.setReference(reference);
        newP.setNom(nom);
        
        session.save(newP);
        session.close();
	}
	
	public static Produit getProduit(String reference){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        String hql = "SELECT p FROM Produit p WHERE p.reference=:reference";
        Query query = session.createQuery(hql);
        query.setParameter("reference", reference);
        
        Produit produit = null;
        
        if(!query.list().isEmpty()){
            produit = (Produit)query.list().get(0);
        }
        
        session.close();
        
        return produit;
	}
}
