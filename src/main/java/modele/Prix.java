package modele;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Query;

import util.HibernateUtil;

public class Prix {
	private int id;
	private float prix;
	private Date date;
	private Product product;
	
	public Prix(){}

	
	public Prix(float prix, Date date, Product product) {
		super();
		this.prix = prix;
		this.date = date;
		this.product = product;
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

	public Product getProduit() {
		return product;
	}

	public void setProduit(Product product) {
		this.product = product;
	}
	
	public static float getPrixProduit(Product product){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        String hql = "SELECT p FROM Prix p WHERE p.produit=:produit ORDER BY p.date DESC";
        Query query = session.createQuery(hql);
        query.setParameter("produit", product);
        
        float prix = (Float) null;
        
        //no check
        if(!query.list().isEmpty()){
        	prix = (float)query.list().get(0);
        }
        
        session.close();
        
        return prix;
	}
	
	public static void ajoutPrix(float prix, Date date, Product product){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Prix newP = new Prix();
        newP.setPrix(prix);
        newP.setDate(date);
        newP.setProduit(product);
        
        session.save(newP);
        session.close();
	}
}
