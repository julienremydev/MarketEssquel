package strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;

import fr.miage.agents.api.message.recherche.Rechercher;
import fr.miage.agents.api.message.recherche.ResultatRecherche;
import fr.miage.agents.api.message.relationclientsupermarche.Achat;
import fr.miage.agents.api.message.relationclientsupermarche.ResultatAchat;
import fr.miage.agents.api.model.Produit;
import modele.Prix;
import modele.Product;
import modele.Stock;
import util.HibernateUtil;

public class AppelBDD {
	/*
	public static float getPrixAchat(Product product) {
		List<Stock> stocks = new ArrayList<Stock>();
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

		stocks= ((Session) HibernateUtil.getSessionFactory()).createQuery("from Stock where product=?")
				.setParameter(0, product).list();

		HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
		if (stocks.size() > 0) {
			float moyenne = 0;
			for (Stock stock : stocks) {
				moyenne+=
			}
			return achats;
		} else {
			//check return
			return 0;
		}
		return 0;
	}

	 */

	//right price ?
	public static ResultatRecherche search(Rechercher recherche) {
		ResultatRecherche rr = new ResultatRecherche();
		rr.produitList=new ArrayList<Produit>();
		//produit marque
		if (recherche.marque!=null) {
			List<Product> list = rechercheParMarque(recherche.marque);
			for (Product p : list) {
				float prix = Prix.getPrixProduit(p);
				Produit produit = p.getCloneProduct();
				produit.prixProduit=prix;
				rr.produitList.add(produit);
			}
		}
		//produit ref
		if (recherche.idProduit>0) {
			Product product = Product.getProduct(recherche.idProduit);
			Produit p = product.getCloneProduct();
			//p.prixProduit=PrixVente.setPrixVente(product, (float)0, new Date().getDate());
			rr.produitList.add(p);
		}

		//list categorie
		if (recherche.nomCategorie!=null) {
			List<Product> list = rechercheParCategorie(recherche.nomCategorie);
			for (Product p : list) {
				float prix = Prix.getPrixProduit(p);
				Produit produit = p.getCloneProduct();
				produit.prixProduit=prix;
				rr.produitList.add(produit);			
			}
		}

		//list prix
		if (recherche.prixMin>0) {
			//get all produits prixmin

			/*
			for (Product p : list) {
				float prix = Prix.getPrixProduit(p);
				Produit produit = p.getCloneProduct();
				produit.prixProduit=prix;
				rr.produitList.add(produit);			
			}
			 */
		}
		return rr;
	}

	private static List<Product> rechercheParMarque(String marque) {
		List<Product> products = new ArrayList<Product>();
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

		products = ((Session) HibernateUtil.getSessionFactory()).createQuery("from Product where marque=?")
				.setParameter(0, marque).list();

		HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
		if (products.size() > 0) {
			return products;
		} else {
			return null;
		}
	}


	public static List<Product> rechercheParCategorie(String categorie) {


		List<Product> products = new ArrayList<Product>();
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

		products = ((Session) HibernateUtil.getSessionFactory()).createQuery("from Product natural join Categorie where nomCategorie=?")
				.setParameter(0, categorie).list();

		HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
		if (products.size() > 0) {
			return products;
		} else {
			return null;
		}
	}

	public static ResultatAchat listeCourses(Achat achat) {
		ResultatAchat resultatAchat = new ResultatAchat();
		resultatAchat.courses = new HashMap<Integer,Integer>();
		for (int idProduit : achat.listeCourses.keySet()) {
			Product p = Product.getProduct(idProduit);
			Produit produit = p.getCloneProduct();
			produit.prixProduit=Prix.getPrixProduit(p);
			int quantite = getQuantiteStock(p);
			resultatAchat.courses.put(produit, quantite);
		}
		return resultatAchat ;
	}

	public static int getQuantiteStock(Product p) {
		List<Stock> stocks = new ArrayList<Stock>();
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

		stocks = ((Session) HibernateUtil.getSessionFactory()).createQuery("from Stock where product=?")
				.setParameter(0, p).list();

		HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
		if (stocks.size() > 0) {
			return stocks.get(0).getQuantite();
		} else {
			return 0;
		}
	}
}
