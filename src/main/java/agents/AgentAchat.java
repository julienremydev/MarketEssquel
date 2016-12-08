package agents;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import fr.miage.agents.api.message.Message;
import fr.miage.agents.api.message.negociation.FinaliserAchat;
import fr.miage.agents.api.message.negociation.InitierAchat;
import fr.miage.agents.api.message.negociation.ResultatFinalisationAchat;
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
	HashMap<UUID,ACLMessage> transaction = new HashMap<UUID,ACLMessage>();
	HashMap<UUID,InitierAchat> produitEtQuantite = new HashMap<UUID,InitierAchat>();
	public void action() 
	{
		block();
		ACLMessage msg= this.getAgent().receive();
		if (msg!=null){
			try {
				Message message = (Message)msg.getContentObject();

				switch(message.type){
				case InitierAchat:
					UUID notreUuid =  UUID.randomUUID();
					//reception du message de l'agent gestion
					InitierAchat infoAgentGestion = (InitierAchat)msg.getContentObject();
					//creation d'un message pour agents
					ACLMessage initiationDeLachat = new ACLMessage(ACLMessage.INFORM);
					initiationDeLachat.addReceiver(new AID("DuNomDeLeurAgent", AID.ISLOCALNAME));
					//met les attrivuts
					InitierAchat contenuInitAchat = new InitierAchat();
					contenuInitAchat.session = notreUuid;
					contenuInitAchat.idProduit = infoAgentGestion.idProduit;
					contenuInitAchat.quantite = infoAgentGestion.quantite;
					//envoi des informations pour initier un achat avec le fournisseur
					initiationDeLachat.setContentObject(contenuInitAchat);
					
					transaction.put(notreUuid, initiationDeLachat);
					produitEtQuantite.put(notreUuid, infoAgentGestion);
					this.getAgent().send(initiationDeLachat);
					break;
				case ResultatInitiationAchat:
					ResultatInitiationAchat resultAch = (ResultatInitiationAchat)msg.getContentObject();
					UUID sessionCourante = resultAch.session;
					InitierAchat infoAgentGestionResult = (InitierAchat)produitEtQuantite.get(sessionCourante);
					
					if (resultAch.quantiteDisponible != 0)
					{
						if(resultAch.quantiteDisponible == infoAgentGestionResult.quantite )
						{
							if (resultAch.prixFixe == 15)
							{
								ACLMessage finalisationAchat = new ACLMessage(ACLMessage.INFORM);
								finalisationAchat.addReceiver(new AID("DuNomDeLeurAgent", AID.ISLOCALNAME));
								
								FinaliserAchat contenuFinalAchat = new FinaliserAchat();
								contenuFinalAchat.session=sessionCourante;
								
								finalisationAchat.setContentObject(contenuFinalAchat);
								transaction.put(sessionCourante, finalisationAchat);
								this.getAgent().send(finalisationAchat);
							}
						}
						else{
							// initier nouvel Achat
						}
					}
						
					
					break;
				case ResultatFinalisationAchat:
					ResultatFinalisationAchat resultatFinalAchat = (ResultatFinalisationAchat)msg.getContentObject();
					UUID sessionCouranteFinalAchat = resultatFinalAchat.session;
					long idprod = resultatFinalAchat.idProduit;
					int quantitDemande = resultatFinalAchat.quantiteProduit;
					float prixAchat = resultatFinalAchat.prixFinal;
					// Do some shit to add BDD
					System.out.println("achat de : " + idprod + " au nombre de : "+quantitDemande+" au prix de : "+prixAchat);
					
					
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
						//Double d = produitTemp.get(produit.nomProduit);
						//initAch.quantite = d.intValue();
						
						initAchat.setContentObject(initAch);
						this.getAgent().send(initAchat);
					}
					else
					{
						//TODO produit pas dispo
					}
					
					break;
				case Recherche:
					System.out.println("on est dedans mireille");
					break;

				}
			} catch (UnreadableException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}