package modele;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import util.HibernateUtil;

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
	
	public static void ajoutSuperMarche(String nom){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        SuperMarche newSM = new SuperMarche();
        newSM.setNom(nom);
        
        session.save(newSM);
        session.close();
	}
	
	public static SuperMarche getSuperMarche(String nom){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        String hql = "SELECT sm FROM SuperMarche sm WHERE sm.nom=:nom";
        Query query = session.createQuery(hql);
        query.setParameter("nom", nom);
        
        SuperMarche supermarche = null;
        
        if(!query.list().isEmpty()){
        	supermarche = (SuperMarche)query.list().get(0);
        }
        
        session.close();
        
        return supermarche;
	}
	
	public static List<SuperMarche> getSuperMarches(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        String hql = "SELECT sm FROM SuperMarche sm WHERE";
        Query query = session.createQuery(hql);
        
        List supermarches = null;
        
        if(!query.list().isEmpty()){
        	supermarches = query.list();
        }
        
        session.close();
        
        return supermarches;
	}
}
