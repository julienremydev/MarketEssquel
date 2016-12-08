package modele;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import fr.miage.agents.api.model.Categorie;
import fr.miage.agents.api.model.Produit;
import util.HibernateUtil;

public class Product {

	private long idProduct;

	private String nomProduct;

	private String descriptionProduct;

	private String categorie;

	private String marque;

	public Product(){}


	public boolean equals( String nom) {
		if (nom.equals(this.getNomProduct())) {
			return true;
		}
		else {
			return false;
		}

	}

	public static void ajoutProduct(long ref, String nom){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Product newP = new Product();
		newP.setIdProduct(ref);
		newP.setNomProduct(nom);

		session.save(newP);
		session.close();
	}

	public static Product getProduct(long ref){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		String hql = "SELECT p FROM Product p WHERE p.idProduct=:ref";
		Query query = session.createQuery(hql);
		query.setParameter("ref", ref);

		Product product = null;

		if(!query.list().isEmpty()){
			product = (Product)query.list().get(0);
		}

		session.close();

		return product;
	}

	public static List<Product> getCategorieProduct(String categorie){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		String hql = "SELECT p FROM Product p WHERE p.idCategorie=:categorie";
		Query query = session.createQuery(hql);
		query.setParameter("categorie", categorie);

		List<Product> products = null;

		if(!query.list().isEmpty()){
			products = query.list();
		}
		session.close();
		return products;

	}


	public long getIdProduct() {
		return idProduct;
	}


	public void setIdProduct(long idProduct) {
		this.idProduct = idProduct;
	}


	public String getNomProduct() {
		return nomProduct;
	}


	public void setNomProduct(String nomProduct) {
		this.nomProduct = nomProduct;
	}


	public String getMarque() {
		return marque;
	}


	public void setMarque(String marque) {
		this.marque = marque;
	}


	public String getDescriptionProduct() {
		return descriptionProduct;
	}


	public void setDescriptionProduct(String descriptionProduct) {
		this.descriptionProduct = descriptionProduct;
	}


	public String getCategorie() {
		return categorie;
	}


	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	//TODO
	public Produit getCloneProduit(float prix) {
		Produit p = new Produit();
		//p.idCategorie=this.categorie;
		return null;
	}
}
