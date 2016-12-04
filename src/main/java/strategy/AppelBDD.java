package strategy;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import fr.miage.agents.api.message.recherche.Rechercher;
import fr.miage.agents.api.message.recherche.ResultatRecherche;
import fr.miage.agents.api.model.Categorie;
import modele.Prix;
import modele.Produit;
import util.HibernateUtil;

public class AppelBDD {

	public static ResultatRecherche search(Rechercher recherche) {
		List<Produit> list = rechercheParCategorie(recherche.categorie.nomCategorie);
		List<fr.miage.agents.api.model.Produit> returnList = new ArrayList<fr.miage.agents.api.model.Produit>();
		for (Produit p : list) {
			float prix = Prix.getPrixProduit(p);
		}
		ResultatRecherche rr = new ResultatRecherche();
		rr.produitList=returnList;
		return rr;
		//rechercher produit par nom categorie via Hibernate
	}

	public static List<Produit> rechercheParCategorie(String categorie) {


		List<Produit> produits = new ArrayList<Produit>();
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

		produits = ((Session) HibernateUtil.getSessionFactory()).createQuery("from Produit where nomCategorie=?")
				.setParameter(0, categorie).list();

		HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
		if (produits.size() > 0) {
			return produits;
		} else {
			return null;
		}
	}
}
