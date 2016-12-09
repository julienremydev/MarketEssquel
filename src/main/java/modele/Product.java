package modele;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import fr.miage.agents.api.model.Produit;
import util.HibernateUtil;

public class Product {

	private long idProduct;

	private String nomProduct;

	private String descriptionProduct;

	private Categorie categorie;

	private String marque;

	public Product(){}

/*
 * 

	public boolean equals( String nom) {
		if (nom.equals(this.getNomProduct())) {
			return true;
		}
		else {
			return false;
		}

	}
 */
	public static void ajoutproduct(long ref, String nom){
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

		Product Product = null;

		if(!query.list().isEmpty()){
			Product = (Product)query.list().get(0);
		}

		session.close();

		return Product;
	}

	public static List<Product> getCategorieProduct(int categories){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		String hql = "SELECT p FROM Product p WHERE p.idCategorie=:categorie";
		Query query = session.createQuery(hql);
		query.setParameter("categorie", categories);

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


	public void setIdProduct(long idproduct) {
		this.idProduct = idproduct;
	}


	public String getNomProduct() {
		return nomProduct;
	}


	public void setNomProduct(String nomproduct) {
		this.nomProduct = nomproduct;
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


	public void setDescriptionProduct(String descriptionproduct) {
		this.descriptionProduct = descriptionproduct;
	}


	public Categorie getCategorie() {
		return categorie;
	}


	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	//TODO categorie
	public Produit getCloneProduct() {
		Produit p = new Produit();
		p.descriptionProduit=this.descriptionProduct;
		p.marque=this.marque;
		p.idProduit=this.idProduct;
		p.nomProduit=this.nomProduct;
		
		return p;
	}
}
