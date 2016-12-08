package modele;

public class Categorie {

	private int idCategorie;
	private String nomCategorie;
	
	public Categorie( int idCategorie, String nomCategorie ){
		this.setIdCategorie(idCategorie);
		this.setNomCategorie(nomCategorie);
	}

	public int getIdCategorie() {
		return idCategorie;
	}

	public void setIdCategorie(int idCategorie) {
		this.idCategorie = idCategorie;
	}

	public String getNomCategorie() {
		return nomCategorie;
	}

	public void setNomCategorie(String nomCategorie) {
		this.nomCategorie = nomCategorie;
	}
	
	
}
