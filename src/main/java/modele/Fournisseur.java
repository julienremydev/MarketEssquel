package modele;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Query;

import util.HibernateUtil;

public class Fournisseur {
	private int id;
	private String nom;
	
	public Fournisseur(){}

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
	
	public static void ajoutFournisseur(String nom){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Fournisseur newF = new Fournisseur();
        newF.setNom(nom);
        
        session.save(newF);
        session.close();
	}
	
	public static Fournisseur getFournisseur(String nom){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        String hql = "SELECT f FROM Fournisseur f WHERE f.nom=:nom";
        Query query = session.createQuery(hql);
        query.setParameter("nom", nom);
        
        Fournisseur fournisseur = null;
        
        if(!query.list().isEmpty()){
        	fournisseur = (Fournisseur)query.list().get(0);
        }
        
        session.close();
        
        return fournisseur;
	}
	
	public static List<Fournisseur> getFournisseurs(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        String hql = "SELECT f FROM Fournisseur f";
        Query query = session.createQuery(hql);
        
        List fournisseurs = query.list();
        
        session.close();
        
        return fournisseurs;
	}
}
