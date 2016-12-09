package strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;

import fr.miage.agents.api.message.recherche.Rechercher;
import fr.miage.agents.api.message.recherche.ResultatRecherche;
import fr.miage.agents.api.message.relationclientsupermarche.Achat;
import fr.miage.agents.api.message.relationclientsupermarche.ResultatAchat;
import fr.miage.agents.api.model.Produit;
import modele.Buy;
import modele.Product;
import modele.Stock;
import modele.SuperMarche;
import util.HibernateUtil;

public class AppelBDD {
	
	public static ResultatRecherche search(Rechercher recherche) {
		ResultatRecherche rr = new ResultatRecherche();
		rr.produitList=new ArrayList<Produit>();
		//produit marque
		if (recherche.marque!=null) {
			List<Product> list = rechercheParMarque(recherche.marque);
			for (Product product : list) {
				rr.produitList.add(product.getCloneProduct());
			}
		}
		//produit ref
		if (recherche.idProduit>0) {
			Product product = Product.getProduct(recherche.idProduit);
			rr.produitList.add(product.getCloneProduct());
		}

		//list categorie
		if (recherche.nomCategorie!=null) {
			List<Product> list = rechercheParCategorie(recherche.nomCategorie);
			for (Product product : list) {
				rr.produitList.add(product.getCloneProduct());			
			}
		}

		return rr;
	}

	private static List<Product> rechercheParMarque(String marque) {
		List<Product> products = new ArrayList<Product>();
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

		products = ((Session) HibernateUtil.getSessionFactory()).createQuery("from Product where marque=?")
				.setParameter(0, marque).list();

		HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
		if (products.size() > 0) {
			return products;
		} else {
			return null;
		}
	}

	public static boolean isOkForBuying(float prix, int quantite, long idProduit) {
		boolean ok = false;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        SuperMarche supermarche = SuperMarche.getSuperMarche("MarketEssquel");
        if (supermarche.getCapital()>prix*quantite) {
        	Product product = (Product)session.load(Product.class, idProduit);
        	if (prix > product.getPrixUnitaire()*1.2) {
        		ok = true;
        	}
        }
        session.close();
		return ok;
	}

	public static List<Product> rechercheParCategorie(String categorie) {


		List<Product> products = new ArrayList<Product>();
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

		products = ((Session) HibernateUtil.getSessionFactory()).createQuery("from Product natural join Categorie where nomCategorie=?")
				.setParameter(0, categorie).list();

		HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
		if (products.size() > 0) {
			return products;
		} else {
			return null;
		}
	}

	public static ResultatAchat listeCourses(Achat achat) {
		ResultatAchat resultatAchat = new ResultatAchat();
		resultatAchat.courses = new HashMap<Produit,Integer>();
		for (int idProduit : achat.listeCourses.keySet()) {
			Buy buy = getAchat(Product.getProduct(idProduit));
			resultatAchat.courses.put(buy.getProduct(), buy.getQuantite());
		}
		return resultatAchat ;
	}

	public static Buy getAchat(Product p) {
		Buy buy = null;
		Produit produit = p.getCloneProduct();
		List<Stock> stocks = new ArrayList<Stock>();
		List<Buy> achats = new ArrayList<Buy>();
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

		stocks = ((Session) HibernateUtil.getSessionFactory()).createQuery("from Stock where product=?")
				.setParameter(0, p).list();

		HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

		if (stocks.size() > 0) {
			int quantite = 0;
			ArrayList<Float> prix = new ArrayList<Float>();
			float price=0;
			for (Stock s : stocks) {
				price+=s.getPrixUnitaire()*s.getQuantite();
				quantite+=s.getQuantite();
			}
			produit.prixProduit=price/quantite;
			buy = new Buy(produit,quantite);
			return buy;
		} else {
			return new Buy(produit,0);
		}
	}
}
