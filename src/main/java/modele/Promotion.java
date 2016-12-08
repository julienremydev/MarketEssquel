package modele;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;


public class Promotion {
	private int id;
	private Date dateDebut;
	private Date dateFin;
	private Categorie promosCategorie;
	private double promo;
	
	public Promotion(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public Categorie getPromosCategorie() {
		return promosCategorie;
	}

	public void setPromosCategorie(Categorie promosCategorie) {
		this.promosCategorie = promosCategorie;
	}

	public double getPromo() {
		return promo;
	}

	public void setPromo(double promo) {
		this.promo = promo;
	}


}
