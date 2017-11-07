/** 
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * 
 * Ce logiciel est un programme informatique servant à la gestion de 
 * l'activité de biobanques. 
 *
 * Ce logiciel est régi par la licence CeCILL soumise au droit français
 * et respectant les principes de diffusion des logiciels libres. Vous 
 * pouvez utiliser, modifier et/ou redistribuer ce programme sous les 
 * conditions de la licence CeCILL telle que diffusée par le CEA, le 
 * CNRS et l'INRIA sur le site "http://www.cecill.info". 
 * En contrepartie de l'accessibilité au code source et des droits de   
 * copie, de modification et de redistribution accordés par cette 
 * licence, il n'est offert aux utilisateurs qu'une garantie limitée. 
 * Pour les mêmes raisons, seule une responsabilité restreinte pèse sur 
 * l'auteur du programme, le titulaire des droits patrimoniaux et les 
 * concédants successifs.
 *
 * A cet égard  l'attention de l'utilisateur est attirée sur les 
 * risques associés au chargement,  à l'utilisation,  à la modification 
 * et/ou au  développement et à la reproduction du logiciel par 
 * l'utilisateur étant donné sa spécificité de logiciel libre, qui peut 
 * le rendre complexe à manipuler et qui le réserve donc à des 	
 * développeurs et des professionnels  avertis possédant  des 
 * connaissances  informatiques approfondies.  Les utilisateurs sont 
 * donc invités à charger  et  tester  l'adéquation  du logiciel à leurs
 * besoins dans des conditions permettant d'assurer la sécurité de leurs
 * systèmes et ou de leurs données et, plus généralement, à l'utiliser 
 * et l'exploiter dans les mêmes conditions de sécurité. 
 *	
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous 
 * avez pris connaissance de la licence CeCILL, et que vous en avez 
 * accepté les termes. 
 **/
package fr.aphp.tumorotek.model.imprimante;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Plateforme;


/**
 * 
 * Objet persistant mappant la table IMPRIMANTE.
 * Classe créée le 17/09/09.
 * 
 * @author Pierre Ventadour
 * @version 2.0.11
 * @since 2.0.11 adresse IP et resolution pour RAW printing
 * 
 */
@Entity
@Table(name = "IMPRIMANTE")
@NamedQueries(value = {@NamedQuery(name = "Imprimante.findByNom",
			query = "SELECT i FROM Imprimante i WHERE i.nom = ?1"),
		@NamedQuery(name = "Imprimante.findByAbscisse",
			query = "SELECT i FROM Imprimante i WHERE i.abscisse = ?1"),
		@NamedQuery(name = "Imprimante.findByOrdonnee",
			query = "SELECT i FROM Imprimante i WHERE i.ordonnee = ?1"),
		@NamedQuery(name = "Imprimante.findByLargeur",
			query = "SELECT i FROM Imprimante i WHERE i.largeur = ?1"),
		@NamedQuery(name = "Imprimante.findByLongueur",
			query = "SELECT i FROM Imprimante i WHERE i.longueur = ?1"),
		@NamedQuery(name = "Imprimante.findByOrientation",
			query = "SELECT i FROM Imprimante i WHERE i.orientation = ?1"),
		@NamedQuery(name = "Imprimante.findByPlateforme", 
			query = "SELECT i FROM Imprimante i " 
				+ "WHERE i.plateforme = ?1 " 
				+ "ORDER BY i.nom"),
		@NamedQuery(name = "Imprimante.findByPlateformeSelectNom", 
			query = "SELECT i.nom FROM Imprimante i " 
				+ "WHERE i.plateforme = ?1 " 
				+ "ORDER BY i.nom"),
		@NamedQuery(name = "Imprimante.findByNomAndPlateforme", 
			query = "SELECT i FROM Imprimante i " 
				+ "WHERE i.nom = ?1 AND i.plateforme = ?2"),
		@NamedQuery(name = "Imprimante.findByExcludedId", 
			query = "SELECT i FROM Imprimante i " 
				+ "WHERE i.imprimanteId != ?1") })
public class Imprimante implements TKdataObject, Serializable {
	
	private Integer imprimanteId;
	private String nom;
	private Integer abscisse;
	private Integer ordonnee;
	private Integer largeur;
	private Integer longueur;
	private Integer orientation;
	private Integer mbioPrinter;
	private ImprimanteApi imprimanteApi;
	private Plateforme plateforme;
	
	private String adresseIp;
	private Integer resolution;
	private Integer port;
	
	private Set<AffectationImprimante> affectationImprimantes = 
		new HashSet<AffectationImprimante>();
	
	private static final long serialVersionUID = 1L;

	public Imprimante() {
		super();
	}

	@Id
	@Column(name = "IMPRIMANTE_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getImprimanteId() {
		return this.imprimanteId;
	}

	public void setImprimanteId(Integer id) {
		this.imprimanteId = id;
	}

	@Column(name = "NOM", nullable = false, length = 50)
	public String getNom() {
		return this.nom;
	}

	public void setNom(String n) {
		this.nom = n;
	}

	@Column(name = "ABSCISSE", nullable = false)
	public Integer getAbscisse() {
		return this.abscisse;
	}

	public void setAbscisse(Integer abs) {
		this.abscisse = abs;
	}

	@Column(name = "ORDONNEE", nullable = false)
	public Integer getOrdonnee() {
		return this.ordonnee;
	}

	public void setOrdonnee(Integer ord) {
		this.ordonnee = ord;
	}

	@Column(name = "LARGEUR", nullable = false)
	public Integer getLargeur() {
		return this.largeur;
	}

	public void setLargeur(Integer larg) {
		this.largeur = larg;
	}

	@Column(name = "LONGUEUR", nullable = false)
	public Integer getLongueur() {
		return this.longueur;
	}

	public void setLongueur(Integer longue) {
		this.longueur = longue;
	}

	@Column(name = "ORIENTATION", nullable = false)
	public Integer getOrientation() {
		return this.orientation;
	}

	public void setOrientation(Integer orient) {
		this.orientation = orient;
	}

	@Column(name = "MBIO_PRINTER", nullable = true)
	public Integer getMbioPrinter() {
		return mbioPrinter;
	}

	public void setMbioPrinter(Integer m) {
		this.mbioPrinter = m;
	}

	@ManyToOne()
	@JoinColumn(name = "IMPRIMANTE_API_ID", nullable = false)
	public ImprimanteApi getImprimanteApi() {
		return imprimanteApi;
	}

	public void setImprimanteApi(ImprimanteApi iApi) {
		this.imprimanteApi = iApi;
	}

	@ManyToOne()
	@JoinColumn(name = "PLATEFORME_ID", nullable = false)
	public Plateforme getPlateforme() {
		return plateforme;
	}

	public void setPlateforme(Plateforme pf) {
		this.plateforme = pf;
	}
	
	@OneToMany(mappedBy = "pk.imprimante", cascade = { CascadeType.REMOVE })
	public Set<AffectationImprimante> getAffectationImprimantes() {
		return affectationImprimantes;
	}

	public void setAffectationImprimantes(
			Set<AffectationImprimante> aImprimantes) {
		this.affectationImprimantes = aImprimantes;
	}
	
	@Column(name = "ADRESSEIP", nullable = true, length = 20)
	public String getAdresseIp() {
		return this.adresseIp;
	}

	public void setAdresseIp(String n) {
		this.adresseIp = n;
	}
	
	@Column(name = "RESOLUTION", nullable = true, length = 5)
	public Integer getResolution() {
		return this.resolution;
	}

	public void setResolution(Integer i) {
		this.resolution = i;
	}
	
	@Column(name = "PORT", nullable = true, length = 5)
	public Integer getPort() {
		return this.port;
	}

	public void setPort(Integer i) {
		this.port = i;
	}

	/**
	 * 2 imprimantes sont considérées comme égales si tous leurs attributs
	 * sont égaux.
	 * @param obj est l'imprimante à tester.
	 * @return true si les imprimantes sont égales.
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		Imprimante test = (Imprimante) obj;
		return ((this.nom == test.nom || (this.nom != null 
				&& this.nom.equals(test.nom))) 
		&& (this.plateforme == test.plateforme 
				|| (this.plateforme != null 
				&& this.plateforme.equals(test.plateforme)))
		);
	}

	/**
	 * Le hashcode est calculé sur tous les attributs.
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashNom = 0;
		int hashPf = 0;
		
		if (this.nom != null) {
			hashNom = this.nom.hashCode();
		}
		if (this.plateforme != null) {
			hashPf = this.plateforme.hashCode();
		}
		
		hash = 7 * hash + hashNom;
		hash = 31 * hash + hashPf;
		
		return hash;
	}
	
	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString() {
		if (this.nom != null) {
			return "{" + this.nom + ", " 
				+ plateforme.getNom() + "(Plateforme)}";
		} else {
			return "{Empty Imprimante}";
		}
	}
	
	public Imprimante clone() {
		Imprimante clone = new Imprimante();
		
		clone.setImprimanteId(this.imprimanteId);
		clone.setNom(this.nom);
		clone.setAbscisse(this.abscisse);
		clone.setOrdonnee(this.ordonnee);
		clone.setLargeur(this.largeur);
		clone.setLongueur(this.longueur);
		clone.setOrientation(this.orientation);
		clone.setMbioPrinter(this.getMbioPrinter());
		clone.setImprimanteApi(this.imprimanteApi);
		clone.setPlateforme(this.plateforme);
		clone.setAffectationImprimantes(this.affectationImprimantes);
		clone.setResolution(this.resolution);
		clone.setAdresseIp(this.adresseIp);
		clone.setPort(this.port);
		return clone;
	}

	@Override
	public Integer listableObjectId() {
		return getImprimanteId();
	}
}
