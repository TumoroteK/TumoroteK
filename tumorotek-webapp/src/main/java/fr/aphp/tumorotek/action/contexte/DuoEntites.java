package fr.aphp.tumorotek.action.contexte;

import java.text.DecimalFormat;

public class DuoEntites {

	
	private int idEntiteA;
	private int idEntiteB;
	private String nomEntiteA;
	private String nomEntiteB;
	private String taux;
	private String infoEntiteA;
	private String infoEntiteB;
	
	//pour arrondir le taux
	static DecimalFormat df = new DecimalFormat ( ); 
	
	//constructeur pour etablissement
	public DuoEntites(int idA, int idB, String nomA, String nomB, double t){

		df.setMaximumFractionDigits(2); 
		df.setMinimumFractionDigits(2);
		df.setDecimalSeparatorAlwaysShown ( true ) ; 
		
		idEntiteA=idA;
		idEntiteB=idB;
		
		if(nomA!=null) nomEntiteA=nomA;
		else nomEntiteA="";
		
		if(nomB!=null) nomEntiteB=nomB;
		else nomEntiteB="";
		
		taux = df.format(t);
		
		infoEntiteA = (nomEntiteA.concat(" id : ")).concat(Integer.toString(idEntiteA));
		infoEntiteB = (nomEntiteB.concat(" id : ")).concat(Integer.toString(idEntiteB));
		
		
	}
	
	//constructeur pour Collaborateurs et Services
	public DuoEntites(int idA, int idB, String nomA, String nomB,
			String nomEtaA, String nomEtaB, double t){
		
		df.setMaximumFractionDigits(2); 
		df.setMinimumFractionDigits(2);
		df.setDecimalSeparatorAlwaysShown ( true ) ; 
		
		idEntiteA=idA;
		idEntiteB=idB;
		if(nomA!=null) nomEntiteA=nomA;
		else nomEntiteA="";
		
		if(nomB!=null) nomEntiteB=nomB;
		else nomEntiteB="";
		
		taux = df.format(t);
		
		infoEntiteA = ((nomEntiteA.concat(" id : ")).concat(Integer.toString(idEntiteA)));
		if (nomEtaA != null ) {
			infoEntiteA = infoEntiteA + ", etablissement : " + nomEtaA;
		}
		infoEntiteB = ((nomEntiteB.concat(" id : ")).concat(Integer.toString(idEntiteB)));
		if (nomEtaB != null ) {
			infoEntiteB = infoEntiteB + ", etablissement : " + nomEtaB;
		}
		
	}
	
	public int getIdEntiteA() {
		return idEntiteA;
	}
	public int getIdEntiteB() {
		return idEntiteB;
	}
	public String getTaux() {
		return taux;
	}
	
	public void setIdEntiteA(int idEntiteA) {
		this.idEntiteA = idEntiteA;
	}
	public void setIdEntiteB(int idEntiteB) {
		this.idEntiteB = idEntiteB;
	}
	public void setTaux(String taux) {
		this.taux = taux;
	}
	
	public String getNomEntiteA() {
		return nomEntiteB;
	}

	public void setNomEntiteA(String nomEntiteB) {
		this.nomEntiteB = nomEntiteB;
	}
	
	public String getNomEntiteB() {
		return nomEntiteB;
	}

	public void setNomEntiteB(String nomEntiteB) {
		this.nomEntiteB = nomEntiteB;
	}

	public String getInfoEntiteA() {
		return infoEntiteA;
	}

	public String getInfoEntiteB() {
		return infoEntiteB;
	}

	public void setInfoEntiteA(String infoEntiteA) {
		this.infoEntiteA = infoEntiteA;
	}

	public void setInfoEntiteB(String infoEntiteB) {
		this.infoEntiteB = infoEntiteB;
	}
	
	@Override
	public boolean equals(Object obj) {
			
		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		
		DuoEntites test = (DuoEntites) obj;
		return (this.idEntiteA == test.idEntiteA
				&& this.idEntiteB == test.idEntiteB) 
			|| (this.idEntiteA == test.idEntiteB
					&& this.idEntiteB == test.idEntiteA);
	}
	
}
