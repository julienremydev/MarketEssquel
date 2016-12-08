package modele;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;


public class Promotion {
	private int id;
	private Date dateDebut;
	private Date dateFin;
	private HashMap<String, Integer> promosCategorie;
	
	public Promotion(){
		promosCategorie = new HashMap<>();
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

	public HashMap<String, Integer> getPromosCategorie() {
		return promosCategorie;
	}

	public void setPromosCategorie(HashMap<String, Integer> promosCategorie) {
		this.promosCategorie = promosCategorie;
	}
}
