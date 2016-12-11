package agents;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fr.miage.agents.api.message.negociation.InitierAchat;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import modele.Product;
import strategy.AchatProduit;
import strategy.PrixVente;

public class AgentGestion extends TickerBehaviour{

	public AgentGestion(Agent a){
		super(a,30000);
	}

	public void onTick() {
		/*
		 * Gestion des Stocks
		 */
		HashMap<Product, Integer>  produitToBuy = AchatProduit.getWhatToBuy();
		//HashMap<Product, Integer>  produitToBuy = new HashMap<Product,Integer>();
		
		//produitToBuy.put(Product.getProduct(1), 10);
		if(produitToBuy!=null){
			Iterator it = produitToBuy.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				ACLMessage achatProduit = new ACLMessage(ACLMessage.INFORM);
				AID x = new AID ("receiver@169.254.69.248:8521/JADE");
				x.addAddresses("http://DESKTOP-071AKV4:7778/acc");
				achatProduit.addReceiver(x);
				InitierAchat ia = new InitierAchat();
				ia.idProduit = ((Product)pair.getKey()).getIdProduct();
				ia.quantite = (int) pair.getValue();

				try {
					achatProduit.setContentObject(ia);
					this.getAgent().send(achatProduit);
				} catch (IOException e) {
					e.printStackTrace();
				}

				it.remove();
			}
		}

		//PrixVente.updatePrice();
		

		block();
	}
}