package fr.aphp.tumorotek.model.cession;

/**
 * Statut de l'objet cédé Créée le 22/05/2018
 * 
 * @author Answald Bournique
 * @since 2.2.0
 * @version 2.2.0
 */
public enum ECederObjetStatut {
	TRAITEMENT("TRAITEMENT"), TRAITE("TRAITE"), ANNULE("ANNULE");

	private String statut;

	private ECederObjetStatut(String statut) {
		this.statut = statut;
	}

	public String getStatut() {
		return statut;
	}

	@Override
	public String toString() {
		return statut;
	}
}
