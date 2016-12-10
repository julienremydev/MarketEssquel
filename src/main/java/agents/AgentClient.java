package agents;

import java.io.IOException;

import fr.miage.agents.api.message.Message;
import fr.miage.agents.api.message.recherche.Rechercher;
import fr.miage.agents.api.message.relationclientsupermarche.Achat;
import fr.miage.agents.api.message.relationclientsupermarche.ResultatAchat;
import fr.miage.agents.api.message.util.AppelMethodeIncorrect;
import fr.miage.agents.api.message.util.PrevenirSolde;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import strategy.AppelBDD;

public class AgentClient extends CyclicBehaviour{

	public AgentClient(Agent a){
		super(a);
	}

	public void action() 
	{
		ACLMessage msg= this.getAgent().receive();
		if (msg!=null){
			try {
				Message message = (Message)msg.getContentObject();
				ACLMessage response= new ACLMessage(ACLMessage.INFORM);
				response.addReceiver(new AID("mocker", AID.ISLOCALNAME));
				switch(message.type){
				case AchatClient:
					Achat achat = (Achat)msg.getContentObject();
					ResultatAchat resultatAchat = AppelBDD.listeCourses(achat);
					response.setContentObject(resultatAchat);
					break;
				case Recherche:
					Rechercher recherche = (Rechercher)msg.getContentObject();
					response.setContentObject(recherche);
					break;
				case PrevenirSolde:
					if (msg.getSender().getName().equals("AgentGestion")) {
						response.setContentObject(new PrevenirSolde());
					}
					break;
				default:
					response.setContentObject(new AppelMethodeIncorrect());
				}
				this.getAgent().send(response);
			} 
			catch (UnreadableException | IOException e) {
				e.printStackTrace();
			}
		}
		block();
	}
}