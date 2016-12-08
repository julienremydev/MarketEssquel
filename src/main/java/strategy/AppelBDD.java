package strategy;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import fr.miage.agents.api.message.recherche.Rechercher;
import fr.miage.agents.api.message.recherche.ResultatRecherche;
import fr.miage.agents.api.model.Produit;
import modele.Prix;
import modele.Product;
import util.HibernateUtil;

public class AppelBDD {

	public static ResultatRecherche search(Rechercher recherche) {
		ResultatRecherche rr = new ResultatRecherche();
		//rechercher produit par nom categorie via Hibernate
		if (recherche.idProduit<0) {
			Product p = Product.getProduct(recherche.idProduit);
			//setPrixVente
			p.getCloneProduit();
			//rr.produitList.add(product.getCloneProduit(prix)(recherche.idProduit));
		}
		List<Product> list = rechercheParCategorie(recherche.nomCategorie);
		List<Produit> returnList = new ArrayList<Produit>();
		for (Product p : list) {
			float prix = Prix.getPrixProduit(p);
			Produit prod = new Produit();
			//set produit
			prod.prixProduit=prix;
		}
		
		rr.produitList=returnList;
		return rr;

	}

	public static List<Product> rechercheParCategorie(String categorie) {


		List<Product> products = new ArrayList<Product>();
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

		products = ((Session) HibernateUtil.getSessionFactory()).createQuery("from Produit natural join Categorie where nomCategorie=?")
				.setParameter(0, categorie).list();

		HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
		if (products.size() > 0) {
			return products;
		} else {
			return null;
		}
	}
}
