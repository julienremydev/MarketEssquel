package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

import fr.miage.agents.api.message.Message;
import fr.miage.agents.api.message.negociation.FinaliserAchat;
import fr.miage.agents.api.message.negociation.InitierAchat;
import fr.miage.agents.api.message.negociation.ResultatFinalisationAchat;
import fr.miage.agents.api.message.negociation.ResultatInitiationAchat;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgentK extends CyclicBehaviour{
	private static final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
	public AgentK(Agent a){
		super(a);
	}
	HashMap<UUID,ArrayList<Message>> messages = new HashMap<UUID,ArrayList<Message>>();
	Scanner sc = new Scanner(System.in);
	public void action() 
	{
		block();
		ACLMessage msg= this.getAgent().blockingReceive(mt);
		if (msg!=null){
			try {
				Message message = (Message)msg.getContentObject();
				ACLMessage response= new ACLMessage(ACLMessage.INFORM);
				switch(message.type){
				case InitierAchat:
					InitierAchat ia = (InitierAchat)message;
					
					messages.put(ia.session, new ArrayList<Message>());
					messages.get(ia.session).add(ia);
					System.out.println("Entrez prix : ");
					float prix = sc.nextFloat();
					System.out.println("Entrez quantite: ");
					int quantite = sc.nextInt();
					System.out.println("Entrez boolean success (1 = true, 0 = false: ");
					int isOk= sc.nextInt();
					boolean success = false;
					if (isOk==1) {
						success = true;
					}
					ResultatInitiationAchat ria = new ResultatInitiationAchat();
					ria.prixFixe=prix;
					ria.quantiteDisponible=quantite;
					ria.success=success;
					ria.session=ia.session;
					messages.get(ia.session).add(ria);
					response.setContentObject(ria);
					break;
				case FinaliserAchat:
					FinaliserAchat fa = (FinaliserAchat) message;
					InitierAchat ia2 = (InitierAchat) messages.get(fa.session).get(0);
					ResultatInitiationAchat ria2 = (ResultatInitiationAchat) messages.get(fa.session).get(0);
					ResultatFinalisationAchat rfa = new ResultatFinalisationAchat();
					rfa.session=fa.session;
					rfa.idProduit=ia2.idProduit;
					rfa.prixFinal=ria2.prixFixe;
					rfa.quantiteProduit=ria2.quantiteDisponible;
					response.setContentObject(rfa);
					break;
				default: 
					System.out.println("Problème");
				}
				this.getAgent().send(response);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}