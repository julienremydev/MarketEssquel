package agents;

import org.hibernate.hql.internal.ast.ErrorReporter;

import fr.miage.agents.api.message.Message;
import fr.miage.agents.api.message.recherche.Rechercher;
import fr.miage.agents.api.message.util.AppelMethodeIncorrect;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

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

				switch(message.type){
				case Recherche:
					Rechercher recherche = (Rechercher)msg.getContentObject();
					//recherche.
					break;
				case PrevenirSolde:
					if (msg.getSender().getName().equals("AgentGestion")) {
						Message send = (Message)new AppelMethodeIncorrect();
						ACLMessage error = new ACLMessage(ACLMessage.INFORM);
						this.getAgent().send(error);
					}
					else {
						
					}
					break;

				default:
					Message send = (Message)new AppelMethodeIncorrect();
					ACLMessage error = new ACLMessage(ACLMessage.INFORM);
					this.getAgent().send(error);
				}
			} 
			catch (UnreadableException e) {
				e.printStackTrace();
			}
		}
		block();
	}
}