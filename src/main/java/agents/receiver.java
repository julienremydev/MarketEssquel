package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class receiver extends Agent {

	  protected void setup() {

	  	ACLMessage message =null ;
	  	
	  	while (message == null){
	  		message = receive();
	  	}
	  	
	  	System.out.println(message.getContent().toString());
	  	
	  	doDelete();
	  }

}
