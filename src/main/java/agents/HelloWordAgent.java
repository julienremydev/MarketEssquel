package agents;
import jade.core.Agent;

/**
   This example shows a minimal agent that just prints "Hello World!" 
   and then terminates.
   @author Giovanni Caire - TILAB
 */
public class HelloWordAgent extends Agent {

  protected void setup() {
  	System.out.println("Hello World! My name is "+getLocalName());
   	// Make this agent terminate
  	doDelete();
  }
  
  
}

// On ne va pas s'intéresser à comprendre chaque ligne du code mais juste comment compiler et lancer l'agent.