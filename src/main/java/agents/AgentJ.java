package agents;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

import fr.miage.agents.api.message.Message;
import fr.miage.agents.api.message.negociation.InitierAchat;
import fr.miage.agents.api.message.relationclientsupermarche.Achat;
import fr.miage.agents.api.message.relationclientsupermarche.ResultatAchat;
import fr.miage.agents.api.model.Produit;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgentJ extends CyclicBehaviour{
	private static final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
	public AgentJ(Agent a){
		super(a);
	}
	HashMap<UUID,ACLMessage> transaction = new HashMap<UUID,ACLMessage>();
	HashMap<UUID,InitierAchat> produitEtQuantite = new HashMap<UUID,InitierAchat>();
	Scanner sc = new Scanner(System.in);
	public void action() 
	{
		int again=1;
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
		while(again==1) {
		System.out.println("Entrez produit : ");
		int idProduit = sc.nextInt();
		System.out.println("Entrez quantite: ");
		int quantite = sc.nextInt();
		map.put(idProduit, quantite);
		System.out.println("Encore un produit ? (1: oui)");
		again=sc.nextInt();
		}
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.addReceiver(new AID("receiver", AID.ISLOCALNAME));
		Achat achat = new Achat();
		achat.listeCourses=map;
		try {
			message.setContentObject(achat);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.getAgent().send(message);
		System.out.println("message Achat est envoyé");
		block();
		ACLMessage msg= this.getAgent().blockingReceive(mt);
		if (msg!=null){
			try {
				Message msg2 = (Message)msg.getContentObject();

				switch(msg2.type){
				case ResultatAchatClient:
					
					ResultatAchat res = new ResultatAchat();
					for (Produit prod : res.courses.keySet())
					{
						System.out.println(" Prix "+prod.prixProduit+" quantitté : "+res.courses.get(prod));
					}
					break;
					
				}
			}
			catch(Exception e)
			{
				
			}
		}
	}
}