package modele;

import java.util.Date;
import org.hibernate.Session;

import strategy.AchatProduit;
import util.HibernateUtil;

public class Run {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		 
		//session.beginTransaction();
		
		SuperMarche s = SuperMarche.getSuperMarche("MarketEssquel");
		AchatProduit ap = new AchatProduit(s);
		
		
		System.out.println(ap.getAllProduits());
		System.out.println(ap.getListeproductsStrategiques().get(0).getNomProduct());
		//session.save(user);
		//session.getTransaction().commit();

	}

}
