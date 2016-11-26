package agents;
import java.io.IOException;

import fr.miage.agents.api.message.demande.Acheter;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.introspection.ACLMessage;

/**
   This example shows a minimal agent that just prints "Hello World!" 
   and then terminates.
   @author Giovanni Caire - TILAB
 */
public class AgentAchat extends Agent {

	protected void setup() {		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID());
		ACLMessage b = new ACLMessage();
		jade.lang.acl.ACLMessage a = new jade.lang.acl.ACLMessage();
		Acheter achat = null;
		try {
			a.setContentObject(achat);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//a.setContentObject();
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Achat");
		sd.setName("serviceachat");
		
		dfd.addServices(sd);
		try {DFService.register(this, dfd);}
		catch (FIPAException e) {System.out.println("serviceachat");doDelete();}

		// Make this agent terminate
		
	} 
}

// On ne va pas s'intéresser à comprendre chaque ligne du code mais juste comment compiler et lancer l'agent.