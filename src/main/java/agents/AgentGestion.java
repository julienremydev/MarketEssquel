package agents;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.miage.agents.api.message.Message;
import fr.miage.agents.api.message.TypeMessage;
import fr.miage.agents.api.message.negociation.InitierAchat;
import fr.miage.agents.api.message.recherche.Rechercher;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import modele.Produit;
import strategy.AchatProduit;

public class AgentGestion extends TickerBehaviour{
	
	public AgentGestion(Agent a){
		super(a, 1000);
	}

	@Override
	protected void onTick() {
		
		HashMap<Produit, Integer>  produitToBuy = AchatProduit.getWhatToBuy();
		
		Iterator it = produitToBuy.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        ACLMessage achatProduit = new ACLMessage(ACLMessage.INFORM);
			achatProduit.addReceiver(new AID("AgentAchat", AID.ISLOCALNAME));
			
			Rechercher rech = new Rechercher();
			rech.reference = ((Produit)pair.getKey()).getReference();
			rech.prixMax = (Double)pair.getValue();
			
			achatProduit.setContentObject(rech);
			this.getAgent().send(achatProduit);
			
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
		
		
		block();
	}
}
