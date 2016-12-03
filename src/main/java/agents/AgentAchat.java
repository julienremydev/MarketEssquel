package agents;

import fr.miage.agents.api.message.Message;
import fr.miage.agents.api.message.recherche.Rechercher;
import fr.miage.agents.api.message.recherche.ResultatRecherche;
import fr.miage.agents.api.model.Produit;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class AgentAchat extends CyclicBehaviour{
	
	public AgentAchat(Agent a){
		super(a);
	}
	
	public void action() 
    {
       ACLMessage msg= this.getAgent().receive();
       if (msg!=null){
    	   try {
			Message message = (Message)msg.getContentObject();
			
			switch(message.type){
			case Recherche:
				Rechercher recherche = (Rechercher)msg.getContentObject();
				break;
				case ResultatInitiationAchat:
				
				break;
				case ResultatRecherche:
					ResultatRecherche resultatRecherche = (ResultatRecherche) msg.getContentObject();
					Produit produit = (Produit)resultatRecherche.produitList.get(0);
				break;
					
			}
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
       }
       block();
    }
}