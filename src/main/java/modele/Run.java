package modele;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;

import strategy.AchatProduit;
import util.HibernateUtil;

public class Run {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Session session = HibernateUtil.getSessionFactory().openSession();
		 
		//session.beginTransaction();
		
		//SuperMarche s = SuperMarche.getSuperMarche("MarketEssquel");
		//AchatProduit ap = new AchatProduit(s);
		
		
		System.out.println(AchatProduit.getAllProduits());
		System.out.println(AchatProduit.getListeproductsStrategiques().get(0));
		HashMap<Product, Integer> gwtb = AchatProduit.getWhatToBuy();
		for (Map.Entry<Product, Integer> entry : gwtb.entrySet()) {
		    Product key = entry.getKey();
		    int value = entry.getValue();
		    System.out.println("Produit-> "+key.getNomProduct() + " Quantité: "+value + "\n");
		}
		//System.out.println(AchatProduit.getWhatToBuy());
		//session.save(user);
		//session.getTransaction().commit();

	}

}
