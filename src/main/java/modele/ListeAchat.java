package modele;

import java.util.ArrayList;

public class ListeAchat {
	
	private int id;
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
		for (int i = 0 ; i< 4 ; i++)
		{
			for (int j =0; j < achats.size() ; j++)
			{
				if (achats.get(j).equals(produitsPromo.get(i).produit1))
				{
					if (achats.contains(produitsPromo.get(i).produit2)) {
						Prix prix = null; // get en BDD des prix des deux produits
								prix.setPrix((float) (prix.getPrix()*0.7));
					}
					
				}
			}
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

}
