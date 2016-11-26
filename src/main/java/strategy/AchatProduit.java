package strategy;

import fr.miage.agents.api.model.Produit;

public class AchatProduit {

	public static final Float marge_minimale = new Float(1.05);
	public static final Float marge_maximale = new Float (1.30);
	
	public static Float calculPrix (Produit  p, int quantiteDemandee){
		return p.prixProduit*quantiteDemandee*new Float(marge_minimale);
	}
	public static Float agentNegociation(Produit p, Float prixDemande, int quantiteDemandee){
		return negociation(p, prixDemande, quantiteDemandee);
	}
	public static Float negociation(Produit p, Float prixDemande, int quantiteDemandee){

		//On entame les négociations
		if (prixDemande==null){
			return calculPrix (p, quantiteDemandee);
		}
		//Le prix demandé est acceptable
		else if (prixDemande < p.prixProduit*quantiteDemandee*new Float(marge_maximale)){
			return prixDemande;
		}
		//Le produit est trop cher
		else{
			return null;
		}
 
    }
}
