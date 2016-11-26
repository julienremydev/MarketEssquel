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
public class MessageParamAgent extends Agent {

	protected void setup() {
		Object[] args = getArguments();
		if (args != null) {
			if (args.length>0 && args[0] instanceof String) {
				String service = (String) args[0];
				System.out.println("Hello, My name is "+getLocalName() + "and I provide "+service);
			}
		}
		// Make this agent terminate
		doDelete();
	} 
}

// On ne va pas s'intéresser à comprendre chaque ligne du code mais juste comment compiler et lancer l'agent.