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
package fr.aphp.tumorotek.model.io.export;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * 
 * Objet persistant mappant la table RECHERCHE. Classe créée le 25/02/10.
 * 
 * @author Maxime GOUSSEAU
 * @version 2.0
 * 
 */
@Entity
@Table(name = "RECHERCHE")
@NamedQueries(value = {
		@NamedQuery(name = "Recherche.findByUtilisateur", 
				query = "SELECT r FROM Recherche r WHERE "
				+ "r.createur = ?1"),
		@NamedQuery(name = "Recherche.findByExcludedId", 
				query = "SELECT r FROM Recherche r "
				+ "WHERE r.rechercheId != ?1"),
		@NamedQuery(name = "Recherche.findByAffichage", 
				query = "SELECT r FROM Recherche r "
				+ "WHERE r.affichage = ?1"),
		@NamedQuery(name = "Recherche.findByRequete", 
				query = "SELECT r FROM Recherche r "
			+ "WHERE r.requete = ?1"),
		@NamedQuery(name = "Recherche.findByIntituleUtilisateur", 
				query = "SELECT r FROM Recherche r " 
					+ "WHERE r.intitule like ?1 " 
					+ "AND r.createur = ?2"),
		@NamedQuery(name = "Recherche.findByIntitule", 
				query = "SELECT r FROM Recherche r "
			+ "WHERE r.intitule like ?1"),
		@NamedQuery(name = "Recherche.findByBanqueId", 
			query = "SELECT r FROM Recherche r "
				+ "left join r.banques b "
				+ "WHERE b.banqueId = ?1"),
		@NamedQuery(name = "Recherche.findByBanqueIdinList", 
			query = "SELECT distinct(r) FROM Recherche r "
				+ "left join r.banques b "
				+ "WHERE b.banqueId in (?1)") })
public class Recherche implements TKdataObject, Comparable<Recherche> {

	private Integer rechercheId;
	private String intitule;
	private Utilisateur createur;
	private Affichage affichage;
	private Requete requete;
	private List<Banque> banques = new ArrayList<Banque>();

	public Recherche() {
		super();
	}

	public Recherche(String i, Affichage aff, Requete req, List<Banque> b) {
		super();
		this.intitule = i;
		this.affichage = aff;
		this.requete = req;
		this.banques = b;
	}

	public Recherche(String i, Affichage aff, Requete req, List<Banque> b,
			Utilisateur u) {
		this(i, aff, req, b);
		this.createur = u;
	}

	@Id
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	@Column(name = "RECHERCHE_ID", unique = true, nullable = false)
	public Integer getRechercheId() {
		return rechercheId;
	}

	public void setRechercheId(Integer rId) {
		this.rechercheId = rId;
	}

	@Column(name = "INTITULE", nullable = false)
	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String i) {
		this.intitule = i;
	}

	@OneToOne
	@JoinColumn(name = "CREATEUR_ID")
	public Utilisateur getCreateur() {
		return createur;
	}

	public void setCreateur(Utilisateur cr) {
		this.createur = cr;
	}

	@OneToOne
	@JoinColumn(name = "AFFICHAGE_ID")
	public Affichage getAffichage() {
		return affichage;
	}

	public void setAffichage(Affichage aff) {
		this.affichage = aff;
	}

	@OneToOne
	@JoinColumn(name = "REQUETE_ID")
	public Requete getRequete() {
		return requete;
	}

	public void setRequete(Requete req) {
		this.requete = req;
	}

	@ManyToMany(targetEntity = Banque.class)
	@JoinTable(name = "RECHERCHE_BANQUE", 
			joinColumns = @JoinColumn(name = "RECHERCHE_ID"), 
			inverseJoinColumns = @JoinColumn(name = "BANQUE_ID"))
	@javax.persistence.OrderBy("nom")
	public List<Banque> getBanques() {
		return banques;
	}

	public void setBanques(List<Banque> b) {
		this.banques = b;
	}

	/**
	 * 2 requetes sont considérées comme égales si elles ont le même intitule et
	 * le même createur.
	 * 
	 * @param obj
	 *            est la requete à tester.
	 * @return true si les requetes sont égales.
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}

		Recherche r = (Recherche) obj;

		if (this.intitule != null) {
			if (this.intitule.equals(r.getIntitule())) {
				if (this.affichage != null) {
					if (this.affichage.equals(r.getAffichage())) {
						if (this.createur != null) {
							if (this.createur.equals(r.getCreateur())) {
								if (this.requete != null) {
									return this.requete.equals(r.getRequete());
								} else {
									return (r.getRequete() == null);
								}
							} else {
								return false;
							}
						} else if (r.getCreateur() == null) {
							if (this.requete != null) {
								return this.requete.equals(r.getRequete());
							} else {
								return (r.getRequete() == null);
							}
						} else {
							return false;
						}
					} else {
						return false;
					}
				} else if (r.getAffichage() == null) {
					if (this.createur != null) {
						if (this.createur.equals(r.getCreateur())) {
							if (this.requete != null) {
								return this.requete.equals(r.getRequete());
							} else {
								return (r.getRequete() == null);
							}
						} else {
							return false;
						}
					} else if (r.getCreateur() == null) {
						if (this.requete != null) {
							return this.requete.equals(r.getRequete());
						} else {
							return (r.getRequete() == null);
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else if (r.getIntitule() == null) {
			if (this.affichage != null) {
				if (this.affichage.equals(r.getAffichage())) {
					if (this.createur != null) {
						if (this.createur.equals(r.getCreateur())) {
							if (this.requete != null) {
								return this.requete.equals(r.getRequete());
							} else {
								return (r.getRequete() == null);
							}
						} else {
							return false;
						}
					} else if (r.getCreateur() == null) {
						if (this.requete != null) {
							return this.requete.equals(r.getRequete());
						} else {
							return (r.getRequete() == null);
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else if (r.getAffichage() == null) {
				if (this.createur != null) {
					if (this.createur.equals(r.getCreateur())) {
						if (this.requete != null) {
							return this.requete.equals(r.getRequete());
						} else {
							return (r.getRequete() == null);
						}
					} else {
						return false;
					}
				} else if (r.getCreateur() == null) {
					if (this.requete != null) {
						return this.requete.equals(r.getRequete());
					} else {
						return (r.getRequete() == null);
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Le hashcode est calculé sur les attributs intitule et createur.
	 * 
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {

		int hash = 7;
		int hashIntitule = 0;
		int hashCreateur = 0;
		int hashAffichage = 0;
		int hashRequete = 0;

		if (this.intitule != null) {
			hashIntitule = this.intitule.hashCode();
		}
		if (this.createur != null) {
			hashCreateur = this.createur.hashCode();
		}
		if (this.affichage != null) {
			hashAffichage = this.affichage.hashCode();
		}
		if (this.requete != null) {
			hashRequete = this.requete.hashCode();
		}

		hash = 31 * hash + hashIntitule;
		hash = 31 * hash + hashCreateur;
		hash = 31 * hash + hashAffichage;
		hash = 31 * hash + hashRequete;

		return hash;

	}

	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString() {
		if (this.intitule != null) {
			return this.intitule;
		} else {
			return "{Empty Recherche}";
		}
	}

	public Recherche clone() {
		Recherche clone = new Recherche();
		clone.setRechercheId(getRechercheId());
		clone.setCreateur(this.createur);
		clone.setIntitule(this.intitule);
		clone.setRequete(this.requete);
		clone.setAffichage(this.affichage);
		clone.setBanques(this.banques);
		return clone;
	}

	@Override
	public int compareTo(Recherche rec) {
		return this.getIntitule().compareToIgnoreCase(rec.getIntitule());
	}

	@Override
	public Integer listableObjectId() {
		return getRechercheId();
	}
}