package agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import fr.miage.agents.api.message.Message;
import fr.miage.agents.api.message.auth.DemanderSession;
import fr.miage.agents.api.message.auth.ResultatDemandeSession;
import fr.miage.agents.api.message.negociation.InitierAchat;
import fr.miage.agents.api.message.negociation.ResultatInitiationAchat;
import fr.miage.agents.api.message.recherche.ResultatRecherche;
import fr.miage.agents.api.model.Produit;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class AgentAchat extends CyclicBehaviour{

	public AgentAchat(Agent a){
		super(a);
	}
	HashMap<UUID,ArrayList<Message>> transaction = new HashMap<UUID,ArrayList<Message>>();
	HashMap<String,ArrayList<Object>> produitTemp = new HashMap<String,ArrayList<Object>>();
	public void action() 
	{
		ACLMessage msg= this.getAgent().receive();
		if (msg!=null){
			try {
				Message message = (Message)msg.getContentObject();

				switch(message.type){
				case InitierAchat:
					String notreUuid =  UUID.randomUUID().toString();
					//reception du message de l'agent gestion
					InitierAchat infoAgentGestion = (InitierAchat)msg.getContentObject();
					//on stocke la quantité car l'API n'a pas été faite avec nous donc c'est pas prévu
					
					ACLMessage demandeSession = new ACLMessage(ACLMessage.INFORM);
					demandeSession.addReceiver(new AID("DuNomDeLeurAgent", AID.ISLOCALNAME));
					//on créer ensuite une recherche pour l'envoyer au fournisseur avec les infos fournies par l'agent gesiton
					DemanderSession demande = new DemanderSession();
					demande.ping = notreUuid;
					demandeSession.setContentObject(demande);
					this.getAgent().send(demandeSession);

					break;
				case ResultatDemandeSession:
					ResultatDemandeSession pong = (ResultatDemandeSession)msg.getContentObject();
					
					
					ACLMessage messInitAchat = new ACLMessage(ACLMessage.INFORM);
					messInitAchat.addReceiver(new AID("DuNomDeLeurAgent", AID.ISLOCALNAME));
					
					InitierAchat initAchat = new InitierAchat();
					initAchat.idProduit=(int) produitTemp.get(pong.session).get(0);
					initAchat.quantite=(int) produitTemp.get(pong.session).get(1);
					initAchat.session=pong.session;
					
					messInitAchat.setContentObject(initAchat);
					ArrayList<Message> listMessages = new ArrayList<Message>();
					listMessages.add(initAchat);
					transaction.put(pong.session, listMessages);
					
					this.getAgent().send(messInitAchat);
					
					break;
				
				case ResultatInitiationAchat:
					ResultatInitiationAchat resultAch = (ResultatInitiationAchat)msg.getContentObject();
					
					
					break;
				case ResultatRecherche:
					ResultatRecherche resultatRecherche = (ResultatRecherche) msg.getContentObject();
					transaction.put(resultatRecherche.Session, msg);
					if(resultatRecherche.produitList.get(0) != null){
						Produit produit = (Produit)resultatRecherche.produitList.get(0);
						
						ACLMessage initAchat = new ACLMessage(ACLMessage.INFORM);
						initAchat.addReceiver(new AID("DuNomDeLeurAgent", AID.ISLOCALNAME));
						//on créer ensuite une recherche pour l'envoyer au fournisseur avec les infos fournies par l'agent gesiton
						InitierAchat initAch = new InitierAchat();
						initAch.idProduit = (int) produit.idProduit;
						Double d = produitTemp.get(produit.nomProduit);
						initAch.quantite = d.intValue();
						
						initAchat.setContentObject(initAch);
						this.getAgent().send(initAchat);
					}
					else
					{
						//TODO produit pas dispo
					}
					
					break;

				}
			} catch (UnreadableException | IOException e) {
				e.printStackTrace();
			}
		}
		block();
	}
}