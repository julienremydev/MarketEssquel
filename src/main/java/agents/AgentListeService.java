package agents;
import java.util.Iterator;

import jade.core.Agent;
import jade.core.Service;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

/**
   This example shows a minimal agent that just prints "Hello World!" 
   and then terminates.
   @author Giovanni Caire - TILAB
 */
public class AgentListeService extends Agent {

	protected void setup() {		
		DFAgentDescription dfd = new DFAgentDescription();
		try {
			System.out.println("launched");
			DFAgentDescription[] result = DFService.search(this,dfd);
			System.out.println(result.length);
			for (int i=0; i<result.length; i++) {
				System.out.println("boucle1");
				Iterator iter = result[i].getAllServices();
				while (iter.hasNext()) {
					System.out.println("boucle2");
					ServiceDescription sd = (ServiceDescription)iter.next();
					System.out.println(sd.getName() + " - "+sd.getType());

				}
			}
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//doDelete();	
	} 
}

// On ne va pas s'intéresser à comprendre chaque ligne du code mais juste comment compiler et lancer l'agent.