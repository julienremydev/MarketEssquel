package strategy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;

import fr.miage.agents.api.model.Produit;
import modele.Buy;
import modele.Categorie;
import modele.Product;
import modele.Stock;
import util.HibernateUtil;

public class PrixVente {
	
	//evaluation
	//doubler la méthode sans la concurrence
	//internaliser le prixAchat
	public static float setPrixVente(Product product, float prixAchat) {
		float prixVente = prixAchat;

		//traitement priorite
		prixVente *= PrixVente.getTop(product);

		if (product.getCategorie().equals("High-tech")) {
			prixVente *= 1.1;
		}
		else {
			prixVente *= 1.05;
		}


		//traitement conccurence
		float prixConccurence = PrixVente.getPrixConccurence(product);
		
		while (prixConccurence>prixVente*1.2) {
			prixVente*=0.98;
		}
		prixVente=prixVente*(float) getCoeffDate(product);
		return prixVente;
	} 

	public static float getPrixConccurence(Product product) {
		//appel bdd via agent au supermarche2
		return 0;
	}

	public static double getTop (Product product) {
		//appel bdd pour connaitre le classement de vente du produit
		int top = 0;
		switch (top) {
		case 1 :
			return 1.15;
		case 2 :
			return 1.1;
		case 3 :
			return 1.05;
		default : 
			return 1;
		}
	}

	public static double getCoeffDate(Product product) {
		ArrayList<HashMap<String,Double>> list = new ArrayList<HashMap<String,Double>>();
		HashMap<String,Double> map0 = new HashMap<String,Double>();
		HashMap<String,Double> map1 = new HashMap<String,Double>();
		HashMap<String,Double> map2 = new HashMap<String,Double>();
		HashMap<String,Double> map3 = new HashMap<String,Double>();
		
		map0.put("Consommable", 1.2);
		map0.put("High-tech", 1.4);
		map0.put("Cosmétique", 1.15);
		map0.put("Produit entretien", 1.15);
		
		map1.put("Consommable", 1.1);
		map1.put("High-tech", 1.3);
		map1.put("Cosmétique", 1.05);
		map1.put("Produit entretien", 1.045);
		
		map2.put("Consommable", 1.0);
		map2.put("High-tech", 1.2);
		map2.put("Cosmétique", 1.02);
		map2.put("Produit entretien", 1.025);
		
		map3.put("Consommable", 0.9);
		map3.put("High-tech", 1.1);
		map3.put("Cosmétique", 1.01);
		map3.put("Produit entretien", 1.015);
		
		list.add(map0);
		list.add(map1);
		list.add(map2);
		list.add(map3);
		long nbJours = Stock.getDateAchat(product);
		Categorie categorie = product.getCategorie();
		
		int coeffNbJours = (int) (nbJours % 7);
		return list.get(coeffNbJours).get(realCategorie(categorie,false));
	}

	public static String realCategorie(Categorie categorie, boolean autres) {

		if (categorie.getNomCategorie().equals("Légume") || categorie.getNomCategorie().equals("Produit laitier") || categorie.getNomCategorie().equals("Boisson")) {
			return "Consommable";
		}
		else {
			if (autres) {
				return "Autres";

			}
			else {
				return categorie.getNomCategorie();
			}
		}
	}

	private static double getStockCoeff(Produit produit, Categorie categorie) {

		//getStock
		Stock stock = null;
		switch (realCategorie(categorie,true)) {
		case "Consommable" :
			if (stock.getQuantite()<10) {
				return 0.9;
			}
			if (stock.getQuantite()<20) {
				return 1.11;
			}
			if (stock.getQuantite()<40) {
				return 1.09;
			}
			if (stock.getQuantite()<50) {
				return 1.06;
			}
			if (stock.getQuantite()<80) {
				return 1.04;
			}
			else {
				return 1.02;
			}
		case "Autres" :
			if (stock.getQuantite()<10) {
				return 0.9;
			}
			if (stock.getQuantite()<20) {
				return 1.115;
			}
			if (stock.getQuantite()<40) {
				return 1.105;
			}
			if (stock.getQuantite()<50) {
				return 1.1;
			}
			if (stock.getQuantite()<80) {
				return 1.09;
			}
			else {
				return 1.1;
			}
		default :
			return 1;
		}
	}

	public static void updatePrice() {
		List<Product> products = new ArrayList<Product>();
		Categorie c1 = Categorie.getCategorie(1);
		Categorie c2 = Categorie.getCategorie(2);
		Categorie c3 = Categorie.getCategorie(3);
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.createQuery("DELETE FROM Stock as s WHERE s.product IN (SELECT idProduct FROM Product WHERE categorie=? OR categorie=? OR categorie=?) AND DATEDIFF(?,dateachat) > 27").setParameter(0, c1).setParameter(1, c2).setParameter(2, c3).setParameter(3, new Date()).executeUpdate();
		
		products = session.createQuery("SELECT s.product FROM Stock s").list();
		session.getTransaction().commit();
		for (Product p : products) {
			Buy buy = AppelBDD.getAchat(p);
			System.out.println(buy);
			p.setPrixUnitaire(setPrixVente(p, buy.getProduct().prixProduit));
			HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
			HibernateUtil.getSessionFactory().getCurrentSession().update(p);
			HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
			HibernateUtil.getSessionFactory().getCurrentSession().close();
		}
	}
}
