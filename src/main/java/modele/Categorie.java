package modele;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import util.HibernateUtil;

public class Categorie {

	private int idCategorie;
	private String nomCategorie;
	
	public Categorie(){}

	public int getIdCategorie() {
		return idCategorie;
	}

	public void setIdCategorie(int idCategorie) {
		this.idCategorie = idCategorie;
	}

	public String getNomCategorie() {
		return nomCategorie;
	}

	public void setNomCategorie(String nomCategorie) {
		this.nomCategorie = nomCategorie;
	}

	public static Categorie getCategorie(Integer idCat) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        Categorie cat = (Categorie)session.load(Categorie.class, idCat);
        
        session.close();
        return cat;
	}
	
	public static List<Categorie> getCategories(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		String hql = "SELECT ca FROM Categorie ca";
		Query query = session.createQuery(hql);

		List categories = null;

		if (!query.list().isEmpty()) {
			categories = query.list();
		}

		session.close();

		return categories;
	}
}
