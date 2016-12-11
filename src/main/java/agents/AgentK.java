package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

import fr.miage.agents.api.message.Message;
import fr.miage.agents.api.message.negociation.AnnulerAchat;
import fr.miage.agents.api.message.negociation.FinaliserAchat;
import fr.miage.agents.api.message.negociation.InitierAchat;
import fr.miage.agents.api.message.negociation.NegocierPrix;
import fr.miage.agents.api.message.negociation.ResultatAnnulationAchat;
import fr.miage.agents.api.message.negociation.ResultatFinalisationAchat;
import fr.miage.agents.api.message.negociation.ResultatInitiationAchat;
import fr.miage.agents.api.message.negociation.ResultatNegociation;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
/**
 * Class de test pour l'agent Achat qui s'occupera de l'achat auprès des fournisseurs
 * @author arthu
 *
 */
public class AgentK extends CyclicBehaviour{
	private static final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
	public AgentK(Agent a){
		super(a);
	}
	private int compteur = 0;
	HashMap<UUID,ArrayList<Message>> messages = new HashMap<UUID,ArrayList<Message>>();
	Scanner sc = new Scanner(System.in);
	public void action() 
	{
		
		ACLMessage msg= this.getAgent().blockingReceive(mt);
		if (msg!=null){
			try {
				Message message = (Message)msg.getContentObject();
				switch(message.type){
				case InitierAchat:
					
					ACLMessage response= new ACLMessage(ACLMessage.INFORM);
					response.addReceiver(new AID("receiver", AID.ISLOCALNAME));
					
					InitierAchat ia = (InitierAchat)message;
					System.out.println("mocker est rentré dans initier Achat");
					messages.put(ia.session, new ArrayList<Message>());
					messages.get(ia.session).add(ia);
					float prix = 2;
					int quantite = ia.quantite;
					int isOk= 1;
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
					this.getAgent().send(response);
					break;
				case NegocierPrix :
					ACLMessage response2= new ACLMessage(ACLMessage.INFORM);
					response2.addReceiver(new AID("receiver", AID.ISLOCALNAME));
					NegocierPrix np = (NegocierPrix) message;
					InitierAchat ia2 = (InitierAchat) messages.get(np.session).get(0);
					ResultatInitiationAchat ria2 = (ResultatInitiationAchat) messages.get(np.session).get(1);
					//System.out.println("tapez 1 : pour fauve oto");
					int isOk2= 1;
					boolean success2 = false;
					if (isOk2==1) {
						success2 = true;
					}
					ResultatNegociation rn = new ResultatNegociation();
					rn.estAccepte=success2;
					rn.session=np.session;
					rn.idProduit=ia2.idProduit;
					//System.out.println("Entrez prix negocie : ");
					rn.prixNegocie=2;
					rn.quantiteDisponible=ria2.quantiteDisponible;
					response2.setContentObject(rn);
					this.getAgent().send(response2);
					break;
				case FinaliserAchat:
					compteur ++;
					System.out.println("Compteur finaliserAchat agent K : "+ compteur);
					ACLMessage response1= new ACLMessage(ACLMessage.INFORM);
					response1.addReceiver(new AID("receiver", AID.ISLOCALNAME));
					FinaliserAchat fa = (FinaliserAchat) message;
					
					InitierAchat ia3 = (InitierAchat) messages.get(fa.session).get(0);
					ResultatInitiationAchat ria3 = (ResultatInitiationAchat) messages.get(ia3.session).get(1);
					
					ResultatFinalisationAchat rfa = new ResultatFinalisationAchat();
					rfa.session=fa.session;
					rfa.idProduit=ia3.idProduit;
					rfa.prixFinal=ria3.prixFixe;
					rfa.quantiteProduit=ria3.quantiteDisponible;
					response1.setContentObject(rfa);
					this.getAgent().send(response1);
					break;
				case AnnulerAchat:
					ACLMessage response3= new ACLMessage(ACLMessage.INFORM);
					response3.addReceiver(new AID("receiver", AID.ISLOCALNAME));
					AnnulerAchat aa= (AnnulerAchat) message;
					ResultatAnnulationAchat raa = new ResultatAnnulationAchat();
					raa.session=aa.session;
					response3.setContentObject(raa);
					this.getAgent().send(response3);
					break;
				default: 
					System.out.println("Problème");
				}
				
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			block();
		}
	}
}