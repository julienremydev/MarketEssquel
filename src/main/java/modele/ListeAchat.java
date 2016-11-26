package modele;

import java.util.ArrayList;

public class ListeAchat {
	
	public ArrayList<Achat> achats;
	
	public final static ArrayList<EnsembleProduit> produitsPromo = new ArrayList<EnsembleProduit>();
	
	static {
		produitsPromo.add(new EnsembleProduit("Courgette","Tomate"));
		produitsPromo.add(new EnsembleProduit("Eponge","Serpillière"));
		produitsPromo.add(new EnsembleProduit("Four microonde","Grille pain"));
		produitsPromo.add(new EnsembleProduit("Montre connectée","Smartphone"));
		produitsPromo.add(new EnsembleProduit("Bière","Mirabelle"));
	}

	public ArrayList<Achat> getAchats() {
		return achats;
	}

	public void setAchats(ArrayList<Achat> achats) {
		this.achats = achats;
	}
	
	public void addAchat(Achat achat) {
		achats.add(achat);
	}
	
	public EnsembleProduit getPromotion() {
		//if achats.contains(produitsPromo.get(0).produit1)
		return null;
	}

}
