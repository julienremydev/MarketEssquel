package agents;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fr.miage.agents.api.message.negociation.InitierAchat;
import fr.miage.agents.api.message.util.PrevenirSolde;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import modele.Product;
import modele.SuperMarche;
import strategy.AchatProduit;
import strategy.PrixVente;
import strategy.DefinirPromotion;

public class AgentGestion extends TickerBehaviour{

	public AgentGestion(Agent a){
		super(a, 1000);
	}

	@Override
	protected void onTick() {
		/*
		 * Gestion des Stocks
		 */
		HashMap<Product, Integer>  produitToBuy = AchatProduit.getWhatToBuy();

		Iterator it = produitToBuy.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			ACLMessage achatProduit = new ACLMessage(ACLMessage.INFORM);
			AID x = new AID ("receiver@169.254.69.248:8521/JADE");
			x.addAddresses("http://DESKTOP-071AKV4:7778/acc");
			achatProduit.addReceiver(x);
			InitierAchat ia = new InitierAchat();
			ia.idProduit = ((Product)pair.getKey()).getIdProduct();
			ia.quantite = (int) pair.getValue();

			try {
				achatProduit.setContentObject(ia);
				this.getAgent().send(achatProduit);
			} catch (IOException e) {
				e.printStackTrace();
			}

			it.remove();
		}


		/*
		 * Gestion des Prix
		 */
		PrixVente.updatePrice();


		/*
		 * Gestion des Soldes
		 */
		HashMap<String, Double> salesOrganize = DefinirPromotion.checkForSales();
		if(!salesOrganize.isEmpty()){
			ACLMessage sales = new ACLMessage(ACLMessage.INFORM);
			AID x = new AID ("receiver@169.254.69.248:8521/JADE");
			x.addAddresses("http://DESKTOP-071AKV4:7778/acc");
			sales.addReceiver(new AID("AgentVente", AID.ISLOCALNAME));

			PrevenirSolde ps = new PrevenirSolde();
			ps.categoriesSoldees = salesOrganize;

			try {
				sales.setContentObject(ps);
				this.getAgent().send(sales);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		
		/*
		 * Calcul Rabais de Noel toutes les dimanches pour la semaine d'après
		 */
		try {
			Date now = new Date();
			
			String stringDebutNoel = "31-10-"+(now.getYear()+1900);
			String stringFinNoel = "31-12-"+(now.getYear()+1900);
			DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			Date dateDebutNoel = format.parse(stringDebutNoel);
			Date dateFinNoel = format.parse(stringFinNoel);
			
			int day = dateDebutNoel.getDay();
			if(now.after(dateDebutNoel) && now.before(dateFinNoel) && now.getDay() == day){
				Calendar calNow = Calendar.getInstance();
				calNow.add(Calendar.DATE, 1);
				DefinirPromotion.calculRabaisNoel(calNow.getTime());
				calNow.add(Calendar.DATE, -1);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		/*
		 * Remise a zero nb jour promo (a chaque debut de nouvelle année)
		 */
		try {
			String string = "01-01-"+(new Date().getYear()+1900);
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			Date dateNouvelAn = format.parse(string);
			
			Calendar now = Calendar.getInstance();
			Calendar nouvelAn = Calendar.getInstance();
			now.setTime(new Date());
			nouvelAn.setTime(dateNouvelAn);
			
			if(now.get(Calendar.DAY_OF_YEAR) == nouvelAn.get(Calendar.DAY_OF_YEAR)){
				SuperMarche.getSuperMarches().get(0).nouvelAn();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		block();
	}
}