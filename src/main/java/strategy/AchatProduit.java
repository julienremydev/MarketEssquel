package strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import modele.Achat;
import modele.Prix;
import modele.Product;
import modele.SuperMarche;
import modele.Vendu;
import util.HibernateUtil;
/**
 * 
 * il faut que les products les plus rentables (qui font le plus de marge), et ceux qui sont � l�image de l�enseigne ( high-tech ), 
 * soient les plus disponibles. 
 * 
 * 
 * Strat�gie de notre agent:
 * Notre supermarch� est sp�cialis� dans les products High-Tech (prendre en compte la d�croissance de d�sirabilit� des products High-Tech)
 * Nous devons avoir en rayon tous les products High-Tech disponibles.
 * Nous avons une liste de products qui doivent toujours �tre pr�sent en magasin (products high-tech, products qui se vendent le plus)
 * Pour ces products, quand le stock est inf�rieur � un certain seuil, nous effectuons un achat.
 * Au bout d'un certain nombre de demandes de products que nous n'avons pas, nous effectuons un achat, et le product peut passer dans la liste des products.
 * 
 * Nous effectuons des achats en grande quantit� des products qui se vendent le plus et que nous sommes s�rs d'�couler.
 * 
 * Si c'est la premi�re fois que nous achetons des products au fournisseur et que l'autre supermarch� ne le vend pas,
 * nous acceptons la premi�re offre du fournisseur.
 * Si c'est la premi�re fois que nous achetons un product et que l'autre supermarch� vend le product, nous nous basons
 * sur ce prix pour la n�gociation.
 * Sinon nous nous basons sur l'historique des achats et des ventes et sur le prix des concurrents.
 * 
 * La n�gociation doit absolument aboutir, sauf si le prix demand� est exorbitant.
 * 
 * Par exemple si les carottes sont vendues par les markets 2$ en moyenne, et si l'on souhaite en acheter 10,
 * on n'ach�te pas  le lot au dessus de 20$.
 * 
 * On vend tous les products qui existent.
 *
 */
public class AchatProduit {
	private SuperMarche s;
	List<Product> listeproductsStrategiques;
	//private ArrayList<product> listeproductsStrategiques = new ArrayList<product>  ();
	private int seuil_products_ht = 20;
	private int seuil_products_prioritaires = 30;
	private int seuil_products_min = 5;
	
	//Mise � jour des products prioritaires
	public void majListeproductsStrategiques (){
		//Parcourir tous les products et v�rifier si ils se sont correctement vendus r�cemment
		//Sinon les virer de la liste
		
	}
	public AchatProduit(){}
	public AchatProduit ( SuperMarche s ){
		this.setS(s);
		//Ajouter tous les products High-Tech et tous les autres products 
		listeproductsStrategiques = Arrays.asList(Product.getProduct(24),Product.getProduct(25),Product.getProduct(26),Product.getProduct(27));
		
	}

	public int getNombreproductsDansStock(Product product) {
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
		String hql = "SELECT count(*) FROM Stock s WHERE s.product=:product";
        Query query =  HibernateUtil.getSessionFactory().getCurrentSession().createQuery(hql);
        query.setParameter("product", product);
		
		HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
         
        return (int) query.list().get(0);
        
	}
	@SuppressWarnings("unchecked")
	public List getAllProduits() {
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
	
	//M�thode appel�e � intervalle de temps r�gulier
	// return une HASHMAP : cl�=ref product, valeur=quantit� demand�e
	public static HashMap<Product, Integer> getWhatToBuy(){
		//PRENDRE EN COMPTE LES DEMANDES CLIENTS -> appel m�thode de maj liste
		HashMap<Product, Integer> hash = new HashMap<Product, Integer> ();
		List<Product> lp = getAllProduits();
		
		int nb_prod_voulus = 0;
		//mettre a jour les stocks -> parcourir tous les products
		for (Product p : lp){
			int nb_products_dans_stock = getNombreproductsDansStock(p);
			if(p.getCategorie().equals("High-tech") && nb_products_dans_stock < seuil_products_ht){
				//On v�rifie si le nombre total de products qu'on souhaite commander ne va pas engendrer le d�passement des stocks
				if (seuil_products_ht - nb_products_dans_stock + s.getStock() + nb_prod_voulus <= s.getMAX_STOCK() ){
					nb_prod_voulus+=seuil_products_ht - nb_products_dans_stock;
					hash.put(p, seuil_products_ht - nb_products_dans_stock);
				}	
			}else if (listeproductsStrategiques.contains(p) && nb_products_dans_stock < seuil_products_prioritaires){
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
	
	
	
	//M�thode appel�e � chaque fois qu'un product est vendu pour renflouer les stocks au besoin
	private void achat(Vendu v){
		//selectionner compte total du product v.prdouit dans les stocks
		//Si product high tech ou prioritaire et inf�rieur au seuil : on ach�te
		
		//Sinon si on a plus le product ou si il n'est pas prioritaire on regarde si inf�rieur au seuil min si oui on ach�te
		
		
		
		//Si le fournisseur demande un prix trop �lev�, on ach�te pas ! ( d�terminer le prix en fonction du prix des ventes de la concurrence)
		
	}
	
	//Dernier prix auquel on a vendu le product
	private float getPrixproductVendu(Product p){
		return Prix.getPrixProduit(p);
	}
	//Prix actuel du product de la concurrence
	private void getPrixproductVenduConcurrence(){
		//RETURN PRIX
	}
	//Dernier prix auquel on a achet� le product
	private float getPrixproductAchete(Product p){
		return Achat.getPrixProduitAchete(p).getPrix_unitaire();
	}
	
	

	public SuperMarche getS() {
		return s;
	}
	public void setS(SuperMarche s) {
		this.s = s;
	}

	public List<Product> getListeproductsStrategiques() {
		return listeproductsStrategiques;
	}

	public void setListeproductsStrategiques(ArrayList<Product> listeproductsStrategiques) {
		this.listeproductsStrategiques = listeproductsStrategiques;
	}

	public int getSeuil_products_ht() {
		return seuil_products_ht;
	}

	public void setSeuil_products_ht(int seuil_product_ht) {
		this.seuil_products_ht = seuil_product_ht;
	}

	public int getSeuil_product_prioritaires() {
		return seuil_products_prioritaires;
	}

	public void setSeuil_product_prioritaires(int seuil_product_prioritaires) {
		this.seuil_products_prioritaires = seuil_product_prioritaires;
	}

	public int getSeuil_product_min() {
		return seuil_products_min;
	}

	public void setSeuil_product_min(int seuil_product_min) {
		this.seuil_products_min = seuil_product_min;
	}
}
