package strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import modele.Categorie;
import modele.Product;
import modele.SuperMarche;
import modele.Vendu;
import util.HibernateUtil;
/**
 * 
 * il faut que les products les plus rentables (qui font le plus de marge), et ceux qui sont à l’image de l’enseigne ( high-tech ), 
 * soient les plus disponibles. 
 * 
 * 
 * Stratégie de notre agent:
 * Notre supermarché est spécialisé dans les products High-Tech (prendre en compte la décroissance de désirabilité des products High-Tech)
 * Nous devons avoir en rayon tous les products High-Tech disponibles.
 * Nous avons une liste de products qui doivent toujours être présent en magasin (products high-tech, products qui se vendent le plus)
 * Pour ces products, quand le stock est inférieur à un certain seuil, nous effectuons un achat.
 * Au bout d'un certain nombre de demandes de products que nous n'avons pas, nous effectuons un achat, et le product peut passer dans la liste des products.
 * 
 * Nous effectuons des achats en grande quantité des products qui se vendent le plus et que nous sommes sûrs d'écouler.
 * 
 * Si c'est la première fois que nous achetons des products au fournisseur et que l'autre supermarché ne le vend pas,
 * nous acceptons la première offre du fournisseur.
 * Si c'est la première fois que nous achetons un product et que l'autre supermarché vend le product, nous nous basons
 * sur ce prix pour la négociation.
 * Sinon nous nous basons sur l'historique des achats et des ventes et sur le prix des concurrents.
 * 
 * La négociation doit absolument aboutir, sauf si le prix demandé est exorbitant.
 * 
 * Par exemple si les carottes sont vendues par les markets 2$ en moyenne, et si l'on souhaite en acheter 10,
 * on n'achète pas  le lot au dessus de 20$.
 * 
 * On vend tous les products qui existent.
 *
 */
public class AchatProduit {
	
	//Ajouter tous les products High-Tech et tous les autres products prioritaires
	//Liste de 5 produits
	private static List<Long> listeproductsStrategiques = Arrays.asList(Product.getProduct(24).getIdProduct(),Product.getProduct(25).getIdProduct(),Product.getProduct(26).getIdProduct(),Product.getProduct(27).getIdProduct());
	

	
	//private ArrayList<product> listeproductsStrategiques = new ArrayList<product>  ();
	private static int seuil_products_ht = 10;
	private static int seuil_products_prioritaires = 33;
	private static int seuil_products_min = 5;
	
	//Mise à jour des products prioritaires
	public void majListeproductsStrategiques (){
		//METTRE DaNS LA LISTE LEs 5 produits les + vendus !!!
		//Parcourir tous les products et vérifier si ils se sont correctement vendus récemment
		//Sinon les virer de la liste
		
	}
	public AchatProduit(){}

	public static int getNombreproductsDansStock(Product product) {
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
		String hql = "SELECT count(*) FROM Stock s WHERE s.product=:product";
        Query query =  HibernateUtil.getSessionFactory().getCurrentSession().createQuery(hql);
        query.setParameter("product", product);
		
        int result = ((Long) query.list().get(0)).intValue();
		HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
         
        return result;
        
	}
	@SuppressWarnings("unchecked")
	public static List<Product> getAllProduits() {
	List<Product> product = new ArrayList<Product>();
	HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
	
	product = HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Product").list();
	
	HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
	if (product.size() > 0) {
		return product;
	} else {
		return null;
	}
}
	
	public synchronized  static void  achatClient(Product produit, int quantite, float prix){
		SuperMarche s = SuperMarche.getSuperMarche("MarketEssquel");
		Session session = HibernateUtil.getSessionFactory().openSession();
		s.setCapital(s.getCapital()+prix);
		s.setStock(s.getStock()-quantite);
		//METTRE A JOUR LA TABLE STOCK
		session.update(s);
		session.getTransaction().commit();
		session.close();
	}
	public synchronized  static void  achatFournisseur(Product produit, int quantite, float prix){
		SuperMarche s = SuperMarche.getSuperMarche("MarketEssquel");
		Session session = HibernateUtil.getSessionFactory().openSession();
		s.setCapital(s.getCapital()-prix);
		s.setStock(s.getStock()+quantite);
		//METTRE A JOUR LA TABLE STOCK
		session.update(s);
		session.getTransaction().commit();
		session.close();
	}
	//Méthode appelée à intervalle de temps régulier
	// return une HASHMAP : clé=ref product, valeur=quantité demandée
	public static HashMap<Product, Integer> getWhatToBuy(){
		//Session session = HibernateUtil.getSessionFactory().openSession();
		SuperMarche s = SuperMarche.getSuperMarche("MarketEssquel");
		
		//PRENDRE EN COMPTE LES DEMANDES CLIENTS -> appel méthode de maj liste
		HashMap<Product, Integer> hash = new HashMap<Product, Integer> ();
		List<Product> lp = getAllProduits();
		
		int nb_prod_voulus = 0;
		//mettre a jour les stocks -> parcourir tous les products
		for (Product p : lp){
			//Categorie cat = (Categorie)session.load(Categorie.class, p.getCategorie());
			int nb_products_dans_stock = getNombreproductsDansStock(p);
			if(p.getCategorie().getNomCategorie().equals("High-Tech") && nb_products_dans_stock < seuil_products_ht){
				//On vérifie si le nombre total de products qu'on souhaite commander ne va pas engendrer le dépassement des stocks
				if (seuil_products_ht - nb_products_dans_stock + s.getStock() + nb_prod_voulus <= s.getMAX_STOCK() ){
					nb_prod_voulus+=seuil_products_ht - nb_products_dans_stock;
					hash.put(p, seuil_products_ht - nb_products_dans_stock);
				}	
			}else if (listeproductsStrategiques.contains(p.getIdProduct()) && nb_products_dans_stock < seuil_products_prioritaires){
				if (seuil_products_prioritaires - nb_products_dans_stock + s.getStock() + nb_prod_voulus <= s.getMAX_STOCK() ){
					nb_prod_voulus+=seuil_products_prioritaires - nb_products_dans_stock;
					hash.put(p, seuil_products_prioritaires - nb_products_dans_stock);
				}
			}else if (nb_products_dans_stock < seuil_products_min){
				if (seuil_products_min - nb_products_dans_stock + s.getStock() + nb_prod_voulus <= s.getMAX_STOCK() ){
					nb_prod_voulus+=seuil_products_min - nb_products_dans_stock;
					hash.put(p, seuil_products_min - nb_products_dans_stock);
				}
			}
		}
		if ( hash.size() != 0)
			return hash;
		else
			return null;
		
	}
	
	
	
	//Méthode appelée à chaque fois qu'un product est vendu pour renflouer les stocks au besoin
	private void achat(Vendu v){
		//selectionner compte total du product v.prdouit dans les stocks
		//Si product high tech ou prioritaire et inférieur au seuil : on achète
		
		//Sinon si on a plus le product ou si il n'est pas prioritaire on regarde si inférieur au seuil min si oui on achète
		
		
		
		//Si le fournisseur demande un prix trop élevé, on achète pas ! ( déterminer le prix en fonction du prix des ventes de la concurrence)
		
	}
	
	//Dernier prix auquel on a vendu le product
	//private float getPrixproductVendu(Product p){
		//return Prix.getPrixProduit(p);
	//}
	//Prix actuel du product de la concurrence
	private void getPrixproductVenduConcurrence(){
		//RETURN PRIX
	}
	//Dernier prix auquel on a acheté le product
	//private float getPrixproductAchete(Product p){
		//return Achat.getPrixProduitAchete(p).getPrix_unitaire();
	//}
	

	public static List<Long> getListeproductsStrategiques() {
		return listeproductsStrategiques;
	}

	public void setListeproductsStrategiques(List<Long> listeproductsStrategiques) {
		AchatProduit.listeproductsStrategiques = listeproductsStrategiques;
	}

	public static int getSeuil_products_ht() {
		return seuil_products_ht;
	}

	public void setSeuil_products_ht(int seuil_product_ht) {
		AchatProduit.seuil_products_ht = seuil_product_ht;
	}

	public static int getSeuil_product_prioritaires() {
		return seuil_products_prioritaires;
	}

	public void setSeuil_product_prioritaires(int seuil_product_prioritaires) {
		AchatProduit.seuil_products_prioritaires = seuil_product_prioritaires;
	}

	public static int getSeuil_product_min() {
		return seuil_products_min;
	}

	public void setSeuil_product_min(int seuil_product_min) {
		AchatProduit.seuil_products_min = seuil_product_min;
	}
}
