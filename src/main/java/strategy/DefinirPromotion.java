package strategy;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import modele.Categorie;
import modele.Product;
import modele.Promotion;
import modele.Stock;
import modele.SuperMarche;

public class DefinirPromotion {

	public static HashMap<String, Double> checkForSales() {
		// TODO regarder les vieux stocks et la trésorerie pour établir une période de soldes de 2 jours dans 2 semaines
		HashMap<Integer, Double> salesToInsert = new HashMap();
		HashMap<String, Double> salesToReturn = new HashMap();
		/*
		 * Calcul du ratio par rapport au capital du super marché
		 */
		SuperMarche market = SuperMarche.getSuperMarches().get(0);
		int nbJoursPromos = market.getNb_jours_promo_restants();
		
		if(nbJoursPromos <= 0){
			return salesToReturn;
		}
		
		float capital = market.getCapital();
		double ratioCapital = calculRatioCapital(capital);
		
		/*
		 * Calcul du ratio par rapport au vieux stock
		 */
		HashMap<Integer, Double> ratiosVieuxStocks = new HashMap();
		int[] categories = {4, 5, 6};
		for(int i=0; i<categories.length; i++){
			List<Product> products = Product.getCategorieProduct(categories[i]);
			ratiosVieuxStocks.put(categories[i], calculRationStockCategorie(products));
		}
		
		
		
		Iterator it = ratiosVieuxStocks.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();

			pair.getKey();
			double ratio = (double)pair.getValue()*ratioCapital;
			if(ratio > 0.40){
				salesToInsert.put((Integer)pair.getKey(), 10.0);
				salesToReturn.put((Categorie.getCategorie((Integer)pair.getKey())).getNomCategorie(), 10.0);
			}else if(ratio > 0.55){
				salesToInsert.put((Integer)pair.getKey(), 20.0);
				salesToReturn.put((Categorie.getCategorie((Integer)pair.getKey())).getNomCategorie(), 20.0);
			}else if(ratio > 0.70){
				salesToInsert.put((Integer)pair.getKey(), 30.0);
				salesToReturn.put((Categorie.getCategorie((Integer)pair.getKey())).getNomCategorie(), 30.0);
			}else if(ratio > 0.85){
				salesToInsert.put((Integer)pair.getKey(), 40.0);
				salesToReturn.put((Categorie.getCategorie((Integer)pair.getKey())).getNomCategorie(), 40.0);
			}

			it.remove();
		}
			
		if(!salesToInsert.isEmpty()){
			Iterator it2 = salesToInsert.entrySet().iterator();
			while (it2.hasNext()) {
				Map.Entry pair = (Map.Entry)it2.next();
				Promotion.ajoutPromo((Integer)pair.getKey(), (double)pair.getValue());
				market.promoPrevue();
				it2.remove();
			}
		}
		return salesToReturn;
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
			if(stock != null) {
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

	/**
	 * Stratégie pour le calcul des rabais de noel
	 * Nous actualisons les rabais chaque dimanche pour la semaine suivante pendant les 2 mois de noel
	 * On prend un random entre 0 et 30 pour chaque categories
	 * 
	 */
	public static void calculRabaisNoel(Date debutSemaine) {
		/*
		 * Calcul de la date de fin
		 */
		Calendar calDeb = Calendar.getInstance();
		calDeb.setTime(debutSemaine);
		calDeb.add(Calendar.DATE, 6);
		
		Date finSemaine = calDeb.getTime();
		
		calDeb.add(Calendar.DATE, -6);
		
		
		List<Categorie> categories = Categorie.getCategories();
		
		for(Categorie cat : categories){
			Random r = new Random();
			int promo = r.nextInt(30);
			Promotion.ajoutPromo(cat.getIdCategorie(), promo, debutSemaine, finSemaine);
		}
	}
}
























