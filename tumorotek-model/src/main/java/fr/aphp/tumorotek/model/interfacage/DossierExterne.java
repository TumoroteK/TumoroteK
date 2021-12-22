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
package fr.aphp.tumorotek.model.interfacage;

import java.util.Calendar;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * Objet persistant mappant la table DOSSIER_EXTERNE, qui représente 
 * la consommation d'une transmission depuis un SGL.
 * 
 *
 * @author Pierre Ventadour
 * @author Mathieu BARTHELEMY
 * 
 * @version 2.3
 *
 */
@Entity
@Table(name = "DOSSIER_EXTERNE")
//@NamedQueries(value = {
//	@NamedQuery(name = "DossierExterne.findByEmetteur",
//			query = "SELECT d FROM DossierExterne d " + "WHERE d.emetteur = ?1 " + "ORDER BY d.dateOperation"),
//	@NamedQuery(name = "DossierExterne.findByEmetteurAndIdentification",
//	query = "SELECT d FROM DossierExterne d " + "WHERE d.emetteur = ?1 " + "AND d.identificationDossier like ?2 "
//			+ "ORDER BY d.dateOperation"),
//	@NamedQuery(name = "DossierExterne" + ".findByEmetteurInListAndIdentification",
//	query = "SELECT d FROM DossierExterne d " + "WHERE d.emetteur in (?1) " + "AND d.identificationDossier like ?2 "
//			+ "ORDER BY d.dateOperation"),
//	@NamedQuery(name = "DossierExterne" + ".findByEmetteurInListSelectIdentification",
//	query = "SELECT d.identificationDossier " + "FROM DossierExterne d " + "WHERE d.emetteur in (?1)"),
//	@NamedQuery(name = "DossierExterne.findByIdentification",
//	query = "SELECT d FROM DossierExterne d " + "WHERE d.identificationDossier like ?1"),
//	@NamedQuery(name = "DossierExterne.findCountAll", query = "SELECT count(d) FROM DossierExterne d"),
//	@NamedQuery(name = "DossierExterne.findFirst", query = "SELECT d FROM DossierExterne d "
//			+ "where d.dateOperation = (select min(dateOperation) " + "from DossierExterne)"),
//	@NamedQuery(name = "DossierExterne.findByEmetteurAndEntite", query = "SELECT distinct d FROM DossierExterne d "
//			+ "WHERE d.emetteur = ?1 AND d.entiteId = ?2 ORDER BY d.identificationDossier"),
//	@NamedQuery(name = "DossierExterne.findByEmetteurAndEntiteNull", query = "SELECT distinct d FROM DossierExterne d "
//			+ "WHERE d.emetteur = ?1 AND d.entiteId is null ORDER BY d.identificationDossier"),
//	@NamedQuery(name = "DossierExterne.findChildrenByEmetteurValeur", query = "SELECT distinct d FROM ValeurExterne v "
//			+ "JOIN v.blocExterne.dossierExterne d WHERE d.emetteur = ?1 AND v.champEntiteId = ?2 "
//			+ "AND v.valeur like ?3 AND d.entiteId is not null ORDER BY d.identificationDossier")
//})
public class DossierExterne implements java.io.Serializable
{

	private static final long serialVersionUID = 1798455685809786945L;

	private Integer dossierExterneId;
	private String identificationDossier;
	private Calendar dateOperation;
	private String operation;
	private Emetteur emetteur;

	// @since 2.2.3-genno un dossier peut être d'un niveau inférieur
	// par défaut entite = null = PRELEVEMENT
	// mais peut être DERIVE
	private Integer entiteId;

	private Set<BlocExterne> blocExternes = new HashSet<>();

	public DossierExterne(){
		super();
	}

	@Id
	@Column(name = "DOSSIER_EXTERNE_ID", unique = true, nullable = false)
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	public Integer getDossierExterneId(){
		return dossierExterneId;
	}

	public void setDossierExterneId(final Integer d){
		this.dossierExterneId = d;
	}

	@Column(name = "IDENTIFICATION_DOSSIER", nullable = false, length = 100)
	public String getIdentificationDossier(){
		return identificationDossier;
	}

	public void setIdentificationDossier(final String i){
		this.identificationDossier = i;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_OPERATION", nullable = true)
	public Calendar getDateOperation(){
		if(dateOperation != null){
			final Calendar cal = Calendar.getInstance();
			cal.setTime(dateOperation.getTime());
			return cal;
		}
		return null;
	}

	public void setDateOperation(final Calendar cal){
		if(cal != null){
			this.dateOperation = Calendar.getInstance();
			this.dateOperation.setTime(cal.getTime());
		}else{
			this.dateOperation = null;
		}
	}

	@Column(name = "OPERATION", nullable = true, length = 50)
	public String getOperation(){
		return operation;
	}

	public void setOperation(final String o){
		this.operation = o;
	}

	@ManyToOne
	@JoinColumn(name = "EMETTEUR_ID", nullable = false)
	public Emetteur getEmetteur(){
		return emetteur;
	}

	public void setEmetteur(final Emetteur e){
		this.emetteur = e;
	}

	@Column(name = "ENTITE_ID", nullable = true)
	public Integer getEntiteId(){
		return entiteId;
	}

	public void setEntiteId(final Integer e){
		this.entiteId = e;
	}

	@OneToMany(mappedBy = "dossierExterne", cascade = CascadeType.REMOVE)
	public Set<BlocExterne> getBlocExternes(){
		return blocExternes;
	}

	public void setBlocExternes(final Set<BlocExterne> b){
		this.blocExternes = b;
	}

	@Override
	public boolean equals(final Object obj){

		if(this == obj){
			return true;
		}
		if((obj == null) || obj.getClass() != this.getClass()){
			return false;
		}
		final DossierExterne test = (DossierExterne) obj;
		return ((this.identificationDossier == test.identificationDossier
				|| (this.identificationDossier != null && this.identificationDossier.equals(test.identificationDossier)))
				&& (this.emetteur == test.emetteur || (this.emetteur != null && this.emetteur.equals(test.emetteur))));
	}

	@Override
	public int hashCode(){

		int hash = 7;
		int hashId = 0;
		int hashEm = 0;

		if(this.identificationDossier != null){
			hashId = this.identificationDossier.hashCode();
		}
		if(this.emetteur != null){
			hashEm = this.emetteur.hashCode();
		}

		hash = 7 * hash + hashId;
		hash = 7 * hash + hashEm;

		return hash;
	}

	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString(){
		if(this.identificationDossier != null){
			return "{" + this.identificationDossier + ", " + emetteur.getIdentification() + "(Emetteur)}";
		}
		return "{Empty DossierExterne}";
	}

}
