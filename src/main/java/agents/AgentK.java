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
				response.addReceiver(new AID("receiver", AID.ISLOCALNAME));
				switch(message.type){
				case InitierAchat:
					InitierAchat ia = (InitierAchat)message;
					System.out.println("mocker est rentré dans initier Achat");
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
				case NegocierPrix :
					NegocierPrix np = (NegocierPrix) message;
					InitierAchat ia2 = (InitierAchat) messages.get(np.session).get(0);
					ResultatInitiationAchat ria2 = (ResultatInitiationAchat) messages.get(np.session).get(1);
					System.out.println("tapez 1 : pour fauve oto");
					int isOk2= sc.nextInt();
					boolean success2 = false;
					if (isOk2==1) {
						success2 = true;
					}
					ResultatNegociation rn = new ResultatNegociation();
					rn.estAccepte=success2;
					rn.session=np.session;
					rn.idProduit=ia2.idProduit;
					System.out.println("Entrez prix negocie : ");
					rn.prixNegocie=sc.nextFloat();
					rn.quantiteDisponible=ria2.quantiteDisponible;
					response.setContentObject(rn);
					break;
				case FinaliserAchat:

					System.out.println("mocker est rentré dans finaliser Achat");
					FinaliserAchat fa = (FinaliserAchat) message;

					System.out.println("Entrez boolean success (1 = true, 0 = false: ");
					
					InitierAchat ia3 = (InitierAchat) messages.get(fa.session).get(0);
					ResultatInitiationAchat ria3 = (ResultatInitiationAchat) messages.get(fa.session).get(1);

					
					ResultatFinalisationAchat rfa = new ResultatFinalisationAchat();
					rfa.session=fa.session;
					rfa.idProduit=ia3.idProduit;
					rfa.prixFinal=ria3.prixFixe;
					rfa.quantiteProduit=ria3.quantiteDisponible;
					response.setContentObject(rfa);
					break;
				case AnnulerAchat:
					AnnulerAchat aa= (AnnulerAchat) message;
					ResultatAnnulationAchat raa = new ResultatAnnulationAchat();
					raa.session=aa.session;
					response.setContentObject(raa);
					break;
				default: 
					System.out.println("Problème");
				}
				System.out.println(response.getContentObject());
				this.getAgent().send(response);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}