package modele;

import java.util.Date;

import org.hibernate.Query;
import org.hibernate.Session;

import fr.miage.agents.api.model.Produit;
import util.HibernateUtil;

public class Stock {
	//@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private int quantite;
	private Date dateAchat;
	private Product product;
	
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

	public Product getProduit() {
		return product;
	}

	public void setProduit(Product product) {
		this.product = product;
	}

	public static long getDateAchat(Product product) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        String hql = "SELECT s.dateAchat FROM Stock s WHERE s.produit=:produit AND s.quantite > 0 ORDER BY s.dateAchat ASC";
        Query query = session.createQuery(hql);
        query.setParameter("produit", product);
        
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

	public static Stock getVieuxStock(Product product) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        String hql = "SELECT s FROM Stock s WHERE s.produit=:produit AND s.quantite > 0 ORDER BY s.dateAchat ASC LIMIT 1";
        Query query = session.createQuery(hql);
        query.setParameter("produit", product);
        Stock s = null;
        
        if(!query.list().isEmpty()){
        	s = (Stock)query.list().get(0);
        }
        session.close();
		return s;
	}
}
