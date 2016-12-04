package modele;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import util.HibernateUtil;


public class Vendu {
	private int id;
	private int quantite;
	private Date date;
	private Produit produit;
	
	public Vendu(){}

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
	
	public static List<Produit> getTopProduit(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        String hql = "SELECT v.produit, sum(v.quantite) FROM Vendu v GROUP BY v.produit ORDER BY sum(v.quantite) DESC LIMIT 3";
        Query query = session.createQuery(hql);
        List result = query.list();
        
        session.close();
        
        return result;
	}
}
