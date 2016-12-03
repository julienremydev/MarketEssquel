package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Receiver extends Agent 
{
    protected void setup() 
    {
        addBehaviour(new ReceptionMessage(this));
    }
}
