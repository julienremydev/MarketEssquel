package agents;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.miage.agents.api.message.Message;
import fr.miage.agents.api.message.TypeMessage;
import fr.miage.agents.api.message.negociation.InitierAchat;
import fr.miage.agents.api.message.recherche.Rechercher;
import fr.miage.agents.api.message.util.PrevenirSolde;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import modele.Product;
import strategy.AchatProduit;
import strategy.PrixVente;
import strategy.Promotion;

public class AgentGestion extends TickerBehaviour{

	public AgentGestion(Agent a){
		super(a, 1000);
	}

	@Override
	protected void onTick() {
		/*
		 * Gestion des Stocks
		 */
		HashMap<Product, Integer>  produitToBuy = AchatProduit.getWhatToBuy();

		Iterator it = produitToBuy.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			ACLMessage achatProduit = new ACLMessage(ACLMessage.INFORM);
			achatProduit.addReceiver(new AID("AgentAchat", AID.ISLOCALNAME));

			InitierAchat ia = new InitierAchat();
			ia.idProduit = ((Product)pair.getKey()).getIdProduct();
			ia.quantite = (int) pair.getValue();

			achatProduit.setContentObject(ia);
			this.getAgent().send(achatProduit);

			it.remove();
		}


		/*
		 * Gestion des Prix
		 */
		PrixVente.updatePrice();


		/*
		 * Gestion des Soldes
		 */
		HashMap<String, Double> salesOrganize = Promotion.checkForSales();
		if(salesOrganize != null){
			ACLMessage sales = new ACLMessage(ACLMessage.INFORM);
			sales.addReceiver(new AID("AgentVente", AID.ISLOCALNAME));

			PrevenirSolde ps = new PrevenirSolde();
			ps.categoriesSoldees = salesOrganize;

			sales.setContentObject(ps);
			this.getAgent().send(sales);
		}

		block();
	}
}

//if(!sales.isEmpty()){
//	
//}