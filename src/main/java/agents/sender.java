package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class sender extends Agent {

	  protected void setup() {

	  	ACLMessage message = new ACLMessage(ACLMessage.INFORM);
	  	message.addReceiver(new AID("receiver", AID.ISLOCALNAME));
	  	message.setContent("bonjour…");
	  	send(message);
	  	
	  	
	  	doDelete();
	  }
	  
	  
	}
