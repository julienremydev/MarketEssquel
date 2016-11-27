package modele;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import util.HibernateUtil;

public class Prix {
	private int id;
	private float prix;
	private Date date;
	private Produit produit;
	
	public Prix(){}

	
	public Prix(float prix, Date date, Produit produit) {
		super();
		this.prix = prix;
		this.date = date;
		this.produit = produit;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getPrix() {
		return prix;
	}

	public void setPrix(float prix) {
		this.prix = prix;
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
	
	public static Prix getPrixProduit(Produit produit){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        String hql = "SELECT p FROM Prix p WHERE p.produit=:produit ORDER BY p.date DESC";
        Query query = session.createQuery(hql);
        query.setParameter("produit", produit);
        
        Prix prix = null;
        
        if(!query.list().isEmpty()){
        	prix = (Prix)query.list().get(0);
        }
        
        session.close();
        
        return prix;
	}
	
	public static void ajoutPrix(float prix, Date date, Produit produit){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Prix newP = new Prix();
        newP.setPrix(prix);
        newP.setDate(date);
        newP.setProduit(produit);
        
        session.save(newP);
        session.close();
	}
}
