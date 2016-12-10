package agents;

import fr.miage.agents.api.message.Message;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class Mocker extends Agent 
{

    protected void setup() 
    {
    	// on choisis le truc à tester : client (AgentJ) ou fournisseur (AgentK)
        addBehaviour(new AgentK(this));
        //addBehaviour (new AgentJ(this));
    }
}

