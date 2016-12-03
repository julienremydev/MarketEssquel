package agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import fr.miage.agents.api.message.Message;

public class AgentAchat extends CyclicBehaviour{
	Agent agent;
	
	public AgentAchat(Agent a){
		agent = a;
	}
	
	public void action() 
    {
       ACLMessage msg= agent.receive();
       if (msg!=null){
    	   try {
			Message message = (Message)msg.getContentObject();
			
			switch(message.type){
				case InitierAchat:
				
					break;
					
			}
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
       }
       block();
    }
}