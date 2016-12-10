package agents;

import fr.miage.agents.api.message.Message;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class Mocker extends Agent 
{

    protected void setup() 
    {
    	DFAgentDescription dfd = new DFAgentDescription();
    	dfd.setName(getAID());
    	try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        addBehaviour(new AgentK(this));
        //addBehaviour (new AgentJ(this));
    }
}

