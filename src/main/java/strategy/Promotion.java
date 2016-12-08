package strategy;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.miage.agents.api.message.recherche.Rechercher;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.util.leap.ArrayList;
import modele.Product;
import modele.Stock;
import modele.SuperMarche;

public class Promotion {

	public static HashMap<String, Double> checkForSales() {
		// TODO regarder les vieux stocks et la trésorerie pour établir une période de soldes de 2 jours dans 2 semaines
		HashMap<String, Double> sales = new HashMap();
		
		/*
		 * Calcul du ratio par rapport au capital du super marché
		 */
		SuperMarche market = SuperMarche.getSuperMarche("MarketEssquel");
		float capital = market.getCapital();
		double ratioCapital = calculRatioCapital(capital);
		
		/*
		 * Calcul du ratio par rapport au vieux stock
		 */
		HashMap<String, Double> ratiosVieuxStocks = new HashMap();
		String[] categories = {"cosmétique", "High-tech", "Produit d'entretien"};
		for(int i=0; i<categories.length; i++){
			List<Product> products = Product.getProduits(categories[0]);
			ratiosVieuxStocks.put(categories[0], calculRationStockCategorie(products));
		}
		
		
		
		Iterator it = ratiosVieuxStocks.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();

			pair.getKey();
			double ratio = (double)pair.getValue()*ratioCapital;
			
			if(ratio > 0.40){
				sales.put((String)pair.getKey(), 10.0);
			}else if(ratio > 0.55){
				sales.put((String)pair.getKey(), 20.0);
			}else if(ratio > 0.70){
				sales.put((String)pair.getKey(), 30.0);
			}else if(ratio > 0.85){
				sales.put((String)pair.getKey(), 40.0);
			}

			it.remove();
		}
			
		return sales;
	}
	

	public static double calculRatioCapital(float capital){
		double ratio = 0;
		
		if(capital<500){
			ratio = 1;
		}else if(capital < 1000){
			ratio = 0.75;
		}else if(capital < 1500){
			ratio = 0.5;
		}else if(capital < 2000){
			ratio = 0.25;
		}
		
		return ratio;
	}	
	
	private static Double calculRationStockCategorie(List<Product> products) {
		double cumulRatio = 0;
		int nbProduitsCategorie = products.size();
		
		for(Product product : products){
			Stock stock = Stock.getVieuxStock(product);
			Date date = stock.getDateAchat();
			int quantite = stock.getQuantite();
			double ratioProduit = 1;
			
			long dateDiff = new Date().getTime() - date.getTime();
			double nbJourDiff= dateDiff / (1000*60*60*24);
			if(nbJourDiff > 90){
				ratioProduit *= 0.95*(1-1/nbProduitsCategorie)*calculRatioQuantite(quantite);
			}else if(nbJourDiff > 60){
				ratioProduit *= 0.85*(1-1/nbProduitsCategorie)*calculRatioQuantite(quantite);
			}else if(nbJourDiff > 30){
				ratioProduit *= 0.75*(1-1/nbProduitsCategorie)*calculRatioQuantite(quantite);
			}
			
			cumulRatio += ratioProduit;
		}
		
		return cumulRatio/nbProduitsCategorie;
	}

	private static double calculRatioQuantite(int quantite) {
		double ratio = 1;
		
		if(quantite > 100){
			ratio = 0.95;
		}else if(quantite > 75){
			ratio = 0.85;
		}else if(quantite > 50){
			ratio = 0.75;
		}
		
		return ratio;
	}
}