package strategy;

import java.util.ArrayList;
import java.util.HashMap;

import fr.miage.agents.api.model.Categorie;
import fr.miage.agents.api.model.Produit;
import modele.Prix;
import modele.Product;
import modele.Stock;

public class PrixVente {
	
	//evaluation
	//doubler la méthode sans la concurrence
	
	public static float setPrixVente(Product product, float prixAchat, int date) {
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
		ArrayList<HashMap<Categorie,Double>> list = new ArrayList<HashMap<Categorie,Double>>();
		HashMap<Categorie,Double> map0 = new HashMap<Categorie,Double>();
		HashMap<Categorie,Double> map1 = new HashMap<Categorie,Double>();
		HashMap<Categorie,Double> map2 = new HashMap<Categorie,Double>();
		HashMap<Categorie,Double> map3 = new HashMap<Categorie,Double>();
		Categorie consommable= new Categorie();
		consommable.nomCategorie="Consommable";
		Categorie hightech= new Categorie();
		hightech.nomCategorie="High-tech";
		Categorie cosmetique= new Categorie();
		cosmetique.nomCategorie="Cosmétique";
		Categorie entretien = new Categorie();
		entretien.nomCategorie="Produit entretien";
		map0.put(consommable, 1.2);
		map0.put(consommable, 1.1);
		map0.put(consommable, 1.1);
		map0.put(consommable, -1.0);
		map1.put(hightech, 1.4);
		map1.put(hightech, 1.3);
		map1.put(hightech, 1.2);
		map1.put(hightech, 1.1);
		map2.put(cosmetique, 1.15);
		map2.put(cosmetique, 1.05);
		map2.put(cosmetique, 1.02);
		map2.put(cosmetique, 1.01);
		map3.put(entretien, 1.15);
		map3.put(entretien, 1.045);
		map3.put(entretien, 1.025);
		map3.put(entretien, -1.015);
		list.add(map0);
		list.add(map1);
		list.add(map2);
		list.add(map3);
		long nbJours = Stock.getDateAchat(product);
		Categorie categorie = product.idCategorie;
		int coeffNbJours = (int) (nbJours % 7);
		return list.get(coeffNbJours).get(categorie);

	}

	public static String realCategorie(Categorie categorie, boolean autres) {

		if (categorie.nomCategorie.equals("Légume") || categorie.nomCategorie.equals("Produit laitier") || categorie.nomCategorie.equals("Boisson")) {
			return "Consommable";
		}
		else {
			if (autres) {
				return "Autres";

			}
			else {
				return categorie.nomCategorie;
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
}
