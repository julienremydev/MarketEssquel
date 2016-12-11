package modele;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Query;

import util.HibernateUtil;

public class SuperMarche {
	private static String nom="MarketEssquel";
	private static float capital=100000;
	private static int stock=0;
	private static int MAX_STOCK=1000;
	private static int nb_jours_promo_restants=10;
	public final static ArrayList<EnsembleProduit> produitsPromo = new ArrayList<EnsembleProduit>();
	public static SuperMarche supermarhe = new SuperMarche();

	public static SuperMarche getInstance() {
		return supermarhe;
	}

	static {
		produitsPromo.add(new EnsembleProduit("Courgette", "Tomate"));
		produitsPromo.add(new EnsembleProduit("Eponge", "Serpillière"));
		produitsPromo.add(new EnsembleProduit("Four microonde", "Grille pain"));
		produitsPromo.add(new EnsembleProduit("Montre connectée", "Smartphone"));
		produitsPromo.add(new EnsembleProduit("Bière", "Mirabelle"));

	}

	public SuperMarche() {
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public float getCapital() {
		return capital;
	}

	public void setCapital(float capital) {
		this.capital = capital;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public static void ajoutSuperMarche(String nom) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		SuperMarche newSM = new SuperMarche();
		newSM.setNom(nom);

		session.save(newSM);
		session.close();
	}

	public int getMAX_STOCK() {
		return MAX_STOCK;
	}

	public void setMAX_STOCK(int mAX_STOCK) {
		MAX_STOCK = mAX_STOCK;
	}

	public int getNb_jours_promo_restants() {
		return nb_jours_promo_restants;
	}

	public void setNb_jours_promo_restants(int nb_jours_promo_restants) {
		this.nb_jours_promo_restants = nb_jours_promo_restants;
	}

	public void promoPrevue() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		this.setNb_jours_promo_restants(this.getNb_jours_promo_restants() - 2);

		session.update(this);
		session.close();
	}

	public void nouvelAn() {
		this.setNb_jours_promo_restants(10);	}
}
