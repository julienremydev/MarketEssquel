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
		
		//SuperMarche s = getSuperMarche();
		AchatProduit ap = new AchatProduit();
 
		System.out.println(ap.getAllProduits());
 
		//session.save(user);
		//session.getTransaction().commit();

	}

}
