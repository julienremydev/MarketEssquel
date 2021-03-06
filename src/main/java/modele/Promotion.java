package modele;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import util.HibernateUtil;


public class Promotion {
	private int id;
	private Date dateDebut;
	private Date dateFin;
	private Categorie promosCategorie;
	private double promo;

	public Promotion(){

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public Categorie getPromosCategorie() {
		return promosCategorie;
	}

	public void setPromosCategorie(Categorie promosCategorie) {
		this.promosCategorie = promosCategorie;
	}

	public double getPromo() {
		return promo;
	}

	public void setPromo(double promo) {
		this.promo = promo;
	}

	public static float isPromo(Categorie c ) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		System.out.println("sysout de c : "+c);
		List promo = session.createQuery("select promo from Promotion where promosCategorie=? AND dateDebut < ? AND dateFin < ?")
				.setParameter(0, c)
				.setParameter(1, new Date())
				.setParameter(2, new Date())
				.list();
		session.close();
		if (promo.isEmpty()) {
			return (float)1.0;
		}
		else {
			return (float)promo.get(0);
		}
	}

	public static void ajoutPromo(Integer idCat, double promo){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Categorie cat = (Categorie)session.load(Categorie.class, idCat);

		Calendar date = Calendar.getInstance();

		Promotion pr = new Promotion();
		pr.setPromosCategorie(cat);
		pr.setPromo(promo);

		date.add(Calendar.DATE, 14);
		pr.setDateDebut(date.getTime());
		date.add(Calendar.DATE, 2);
		pr.setDateFin(date.getTime());
		date.add(Calendar.DATE, -16);

		session.save(pr);
		session.close();
	}


	public static void ajoutPromo(Integer idCat, double promo, Date dateDeb, Date dateFin){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Categorie cat = (Categorie)session.load(Categorie.class, idCat);

		Promotion pr = new Promotion();
		pr.setPromosCategorie(cat);
		pr.setPromo(promo);
		pr.setDateDebut(dateDeb);
		pr.setDateFin(dateFin);

		session.save(pr);
		session.getTransaction().commit();
	}
}
