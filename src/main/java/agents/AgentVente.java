package agents;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

/**
   This example shows a minimal agent that just prints "Hello World!" 
   and then terminates.
   @author Giovanni Caire - TILAB
 */
public class AgentVente extends Agent {

	protected void setup() {		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID());
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Vente");
		sd.setName("servicevente");
		
		dfd.addServices(sd);
		try {DFService.register(this, dfd);}
		catch (FIPAException e) {System.out.println("servicevente");doDelete();}

		// Make this agent terminate
		
	} 
}

// On ne va pas s'intéresser à comprendre chaque ligne du code mais juste comment compiler et lancer l'agent.