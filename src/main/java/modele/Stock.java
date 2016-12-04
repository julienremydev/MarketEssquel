package modele;

import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;

import util.HibernateUtil;

public class Stock {
	//@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
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

	public static long getDateAchat(Produit produit) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        String hql = "SELECT s.dateAchat FROM Stock s WHERE s.produit=:produit AND s.quantite > 0 ORDER BY s.dateAchat ASC";
        Query query = session.createQuery(hql);
        query.setParameter("produit", produit);
        
        Date dateAchat = null;
        long dateDiff = -1;
        
        if(!query.list().isEmpty()){
        	Date today = new Date();
        	dateAchat = (Date)query.list().get(0);
        	
        	dateDiff = (today.getTime() - dateAchat.getTime()) / (1000*60*60*24);
        }
        
        session.close();
        
        return dateDiff;
	}
	
	public static void ajoutStock(int quantite, Date dateAchat){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Stock newS = new Stock();
        newS.setQuantite(quantite);
        newS.setDateAchat(dateAchat);
        
        session.save(newS);
        session.close();
	}
}
