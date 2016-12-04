package strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import modele.Achat;
import modele.Prix;
import modele.Produit;
import modele.SuperMarche;
import modele.Vendu;
import util.HibernateUtil;
/**
 * 
 * il faut que les produits les plus rentables (qui font le plus de marge), et ceux qui sont � l�image de l�enseigne ( high-tech ), 
 * soient les plus disponibles. 
 * 
 * 
 * Strat�gie de notre agent:
 * Notre supermarch� est sp�cialis� dans les produits High-Tech (prendre en compte la d�croissance de d�sirabilit� des produits High-Tech)
 * Nous devons avoir en rayon tous les produits High-Tech disponibles.
 * Nous avons une liste de produits qui doivent toujours �tre pr�sent en magasin (produits high-tech, produits qui se vendent le plus)
 * Pour ces produits, quand le stock est inf�rieur � un certain seuil, nous effectuons un achat.
 * Au bout d'un certain nombre de demandes de produits que nous n'avons pas, nous effectuons un achat, et le produit peut passer dans la liste des produits.
 * 
 * Nous effectuons des achats en grande quantit� des produits qui se vendent le plus et que nous sommes s�rs d'�couler.
 * 
 * Si c'est la premi�re fois que nous achetons des produits au fournisseur et que l'autre supermarch� ne le vend pas,
 * nous acceptons la premi�re offre du fournisseur.
 * Si c'est la premi�re fois que nous achetons un produit et que l'autre supermarch� vend le produit, nous nous basons
 * sur ce prix pour la n�gociation.
 * Sinon nous nous basons sur l'historique des achats et des ventes et sur le prix des concurrents.
 * 
 * La n�gociation doit absolument aboutir, sauf si le prix demand� est exorbitant.
 * 
 * Par exemple si les carottes sont vendues par les markets 2$ en moyenne, et si l'on souhaite en acheter 10,
 * on n'ach�te pas  le lot au dessus de 20$.
 * 
 * On vend tous les produits qui existent.
 *
 */
public class AchatProduit {
	private SuperMarche s;
	List<Produit> listeProduitsStrategiques = Arrays.asList(Produit.getProduit(24),Produit.getProduit(25),Produit.getProduit(26),Produit.getProduit(27));
	//private ArrayList<Produit> listeProduitsStrategiques = new ArrayList<Produit>  ();
	private int seuil_produits_ht = 20;
	private int seuil_produits_prioritaires = 30;
	private int seuil_produits_min = 5;
	
	//Mise � jour des produits prioritaires
	public void majListeProduitsStrategiques (){
		//Parcourir tous les produits et v�rifier si ils se sont correctement vendus r�cemment
		//Sinon les virer de la liste
	}
	
	public AchatProduit ( SuperMarche s ){
		this.setS(s);
		//Ajouter tous les produits High-Tech et tous les autres produits 
	}

	public int getNombreProduitsDansStock(Produit produit) {
		HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		
		String hql = "SELECT count(*) FROM Stock s WHERE s.produit=:produit";
        Query query =  HibernateUtil.getSessionFactory().getCurrentSession().createQuery(hql);
        query.setParameter("produit", produit);
		
		HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
         
        return (int) query.list().get(0);
        
	}
	@SuppressWarnings("unchecked")
	public List getAllProduits() {
	List<Produit> produit = new ArrayList<Produit>();
	HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
	
	produit = HibernateUtil.getSessionFactory().getCurrentSession().createQuery("from Produit").list();
	
	HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
	if (produit.size() > 0) {
		return produit;
	} else {
		return null;
	}
}
	
	//M�thode appel�e � intervalle de temps r�gulier
	// return une HASHMAP : cl�=ref produit, valeur=quantit� demand�e
	private HashMap<String, Integer> getWhatToBuy(){
		//PRENDRE EN COMPTE LES DEMANDES CLIENTS -> appel m�thode de maj liste
		HashMap<String, Integer> hash = new HashMap<String, Integer> ();
		List<Produit> lp = getAllProduits();
		//mettre a jour les stocks -> parcourir tous les produits
		for (Produit p : lp){
			if(p.getCategorie().equals("High-tech") && getNombreProduitsDansStock(p) < seuil_produits_ht){
					hash.put(p.getNomProduit(), seuil_produits_ht - getNombreProduitsDansStock(p));
			}else if (listeProduitsStrategiques.contains(p) && getNombreProduitsDansStock(p) < seuil_produits_prioritaires){
				hash.put(p.getNomProduit(), seuil_produits_prioritaires - getNombreProduitsDansStock(p));
			}else if (getNombreProduitsDansStock(p) < seuil_produits_min){
				hash.put(p.getNomProduit(), seuil_produits_min - getNombreProduitsDansStock(p));
			}
		}
		if ( hash.size() != 0)
			return hash;
		else
			return null;
		
	}
	
	
	
	//M�thode appel�e � chaque fois qu'un produit est vendu pour renflouer les stocks au besoin
	private void achat(Vendu v){
		//selectionner compte total du produit v.prdouit dans les stocks
		//Si produit high tech ou prioritaire et inf�rieur au seuil : on ach�te
		
		//Sinon si on a plus le produit ou si il n'est pas prioritaire on regarde si inf�rieur au seuil min si oui on ach�te
		
		
		
		//Si le fournisseur demande un prix trop �lev�, on ach�te pas ! ( d�terminer le prix en fonction du prix des ventes de la concurrence)
		
	}
	
	//Dernier prix auquel on a vendu le produit
	private float getPrixProduitVendu(Produit p){
		return Prix.getPrixProduit(p).getPrix();
	}
	//Prix actuel du produit de la concurrence
	private void getPrixProduitVenduConcurrence(){
		//RETURN PRIX
	}
	//Dernier prix auquel on a achet� le produit
	private float getPrixProduitAchete(Produit p){
		return Achat.getPrixProduitAchete(p).getPrix_unitaire();
	}
	
	

	public SuperMarche getS() {
		return s;
	}
	public void setS(SuperMarche s) {
		this.s = s;
	}

	public List<Produit> getListeProduitsStrategiques() {
		return listeProduitsStrategiques;
	}

	public void setListeProduitsStrategiques(ArrayList<Produit> listeProduitsStrategiques) {
		this.listeProduitsStrategiques = listeProduitsStrategiques;
	}

	public int getSeuil_produits_ht() {
		return seuil_produits_ht;
	}

	public void setSeuil_produits_ht(int seuil_produit_ht) {
		this.seuil_produits_ht = seuil_produit_ht;
	}

	public int getSeuil_produit_prioritaires() {
		return seuil_produits_prioritaires;
	}

	public void setSeuil_produit_prioritaires(int seuil_produit_prioritaires) {
		this.seuil_produits_prioritaires = seuil_produit_prioritaires;
	}

	public int getSeuil_produit_min() {
		return seuil_produits_min;
	}

	public void setSeuil_produit_min(int seuil_produit_min) {
		this.seuil_produits_min = seuil_produit_min;
	}
}
