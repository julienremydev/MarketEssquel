package agents;

import fr.miage.agents.api.message.Message;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class Sender extends Agent 
{

    protected void setup() 
    {
        addBehaviour(new AgentGestion(this));
    }
}

