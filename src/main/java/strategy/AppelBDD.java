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
		List<Product> list = rechercheParCategorie(recherche.categorie.nomCategorie);
		List<fr.miage.agents.api.model.Produit> returnList = new ArrayList<fr.miage.agents.api.model.Produit>();
		for (Product p : list) {
			Produit produit = new Produit();
			float prix = Prix.getPrixProduit(p);
		}
		ResultatRecherche rr = new ResultatRecherche();
		rr.produitList=returnList;
		return rr;
		//rechercher produit par nom categorie via Hibernate
	}

	public static List<Product> rechercheParCategorie(String categorie) {


		List<Product> products = new ArrayList<Product>();
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

		products = ((Session) HibernateUtil.getSessionFactory()).createQuery("from Produit where nomCategorie=?")
				.setParameter(0, categorie).list();

		HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
		if (products.size() > 0) {
			return products;
		} else {
			return null;
		}
	}
}
