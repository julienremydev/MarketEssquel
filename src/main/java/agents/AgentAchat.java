package agents;

import java.io.IOException;
import java.util.HashMap;
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
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import strategy.AchatProduit;
import strategy.AppelBDD;
/** 
 * Agent s'occupant des �changes avec l'agent du groupe gournisseur.
 * @author arthu
 *
 */
public class AgentAchat extends CyclicBehaviour{
	private static final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
	private int compteurResultatInitierAchat = 0;
	private int compteurResultatFinaliserAchat = 0;
	public AgentAchat(Agent a){
		super(a);
	}
	HashMap<UUID,ACLMessage> transaction = new HashMap<UUID,ACLMessage>();
	HashMap<UUID,InitierAchat> produitEtQuantite = new HashMap<UUID,InitierAchat>();
	public void action() 
	{

		ACLMessage msg= this.getAgent().blockingReceive(mt);

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
					initiationDeLachat.addReceiver(new AID("mocker", AID.ISLOCALNAME));
					//met les attrivuts
					InitierAchat contenuInitAchat = new InitierAchat();
					contenuInitAchat.session = notreUuid;
					contenuInitAchat.idProduit = infoAgentGestion.idProduit;
					contenuInitAchat.quantite = infoAgentGestion.quantite;
					//envoi des informations pour initier un achat avec le fournisseur
					initiationDeLachat.setContentObject(contenuInitAchat);

					transaction.put(notreUuid, initiationDeLachat);
					produitEtQuantite.put(notreUuid, contenuInitAchat);
					this.getAgent().send(initiationDeLachat);

					break;
				case ResultatInitiationAchat:
					compteurResultatInitierAchat++;
					System.out.println("Compteur resultat initier Achat : "+ compteurResultatInitierAchat);
					ResultatInitiationAchat resultAch = (ResultatInitiationAchat)msg.getContentObject();
					UUID sessionCourante = resultAch.session;
					InitierAchat infoAgentGestionResult = (InitierAchat)produitEtQuantite.get(sessionCourante);

					boolean prixOk = AppelBDD.isOkForBuying(resultAch.prixFixe, resultAch.quantiteDisponible, infoAgentGestionResult.idProduit);

					if (resultAch.quantiteDisponible != 0)
					{
						if(resultAch.quantiteDisponible == infoAgentGestionResult.quantite )
						{
							if (prixOk)// envoi finalisation achat
							{
								System.out.println(prixOk);
								ACLMessage finalisationAchat = new ACLMessage(ACLMessage.INFORM);
								finalisationAchat.addReceiver(new AID("mocker", AID.ISLOCALNAME));

								FinaliserAchat contenuFinalAchat = new FinaliserAchat();
								contenuFinalAchat.session=sessionCourante;

								finalisationAchat.setContentObject(contenuFinalAchat);
								this.getAgent().send(finalisationAchat);
							}
							else{ // ren�gociation du prix
								ACLMessage negociationPrix = new ACLMessage(ACLMessage.INFORM);
								negociationPrix.addReceiver(new AID("mocker", AID.ISLOCALNAME));

								NegocierPrix negoPrice = new NegocierPrix();
								negoPrice.idProduit = (int) infoAgentGestionResult.idProduit;
								negoPrice.session = sessionCourante;
								negoPrice.prixDemande = (float) (resultAch.prixFixe * 0.9) ; // ici 
								negoPrice.quantiteDemande = resultAch.quantiteDisponible;

								negociationPrix.setContentObject(negoPrice);
								this.getAgent().send(negociationPrix);
							}
						}
						else{ 
							//on modifie juste la quantit� de notre initerAchat pr�c�dent et on renvoie
							infoAgentGestionResult.quantite = resultAch.quantiteDisponible;
							ACLMessage nouvelleInitiationDeLachat = new ACLMessage(ACLMessage.INFORM);
							nouvelleInitiationDeLachat.addReceiver(new AID("mocker", AID.ISLOCALNAME));
							infoAgentGestionResult.quantite = resultAch.quantiteDisponible;
							nouvelleInitiationDeLachat.setContentObject(infoAgentGestionResult);
							this.getAgent().send(nouvelleInitiationDeLachat);
						}
					}
					else
					{
						ACLMessage annulerLachat = new ACLMessage(ACLMessage.INFORM);
						annulerLachat.addReceiver(new AID("mocker", AID.ISLOCALNAME));

						AnnulerAchat newAnnulation = new AnnulerAchat();
						newAnnulation.session = sessionCourante;

						annulerLachat.setContentObject(newAnnulation);
						this.getAgent().send(annulerLachat);
					}
					break;
				case ResultatFinalisationAchat:
					compteurResultatFinaliserAchat++;
					System.out.println("Compteur resultat finaliser achat " +compteurResultatFinaliserAchat );
					ResultatFinalisationAchat resultatFinalAchat = (ResultatFinalisationAchat)msg.getContentObject();
					UUID sessionCouranteFinalAchat = resultatFinalAchat.session;
					long idprod = resultatFinalAchat.idProduit;
					int quantitDemande = resultatFinalAchat.quantiteProduit;
					float prixAchat = resultatFinalAchat.prixFinal;
					AchatProduit.achatFournisseur(idprod,quantitDemande,prixAchat);

					System.out.println("achat de : " + idprod + " au nombre de : "+quantitDemande+" au prix de : "+prixAchat+" est tarmin�");				
					break;
				case ResultatNegociation:
					ResultatNegociation resultatNegociation = (ResultatNegociation)msg.getContentObject();
					UUID sessionCouranteResultNego = resultatNegociation.session;
					InitierAchat infoAgentGestionNego = (InitierAchat)produitEtQuantite.get(sessionCouranteResultNego);
					boolean prixNegoOk = resultatNegociation.estAccepte; // m�thode � julien
					if(prixNegoOk)// on finalise l'achat car le prix renvoyer lors de la n�gociation nous conviens
					{
						ACLMessage finalisationAchat = new ACLMessage(ACLMessage.INFORM);
						finalisationAchat.addReceiver(new AID("mocker", AID.ISLOCALNAME));

						FinaliserAchat contenuFinalAchat = new FinaliserAchat();
						contenuFinalAchat.session=sessionCouranteResultNego;

						finalisationAchat.setContentObject(contenuFinalAchat);
						transaction.put(sessionCouranteResultNego, finalisationAchat);
						this.getAgent().send(finalisationAchat);
					}
					else // le prix renvoyer lors apr�s la n�gociation ne nous conviens toujours pas, on annule l'achat
					{
						ACLMessage annulerLachat = new ACLMessage(ACLMessage.INFORM);
						annulerLachat.addReceiver(new AID("mocker", AID.ISLOCALNAME));

						AnnulerAchat newAnnulation = new AnnulerAchat();
						newAnnulation.session = sessionCouranteResultNego;

						annulerLachat.setContentObject(newAnnulation);
						this.getAgent().send(annulerLachat);
					}
					break;
				case ResultatAnnulationAchat: // On enleve l'uuid correspondant � la transaction de nos hashmap
					ResultatAnnulationAchat resultatAnnulationAchat = (ResultatAnnulationAchat)msg.getContentObject();
					UUID keyRemoval = resultatAnnulationAchat.session;
					transaction.remove(keyRemoval);
					produitEtQuantite.remove(keyRemoval);
					break;
				}
			} catch (UnreadableException | IOException e) {
				e.printStackTrace();
			}
		}
		else{
			block();
		}
	}
}