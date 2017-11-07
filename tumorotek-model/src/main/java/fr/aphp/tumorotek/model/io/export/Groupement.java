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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * Objet persistant mappant la table GROUPEMENT. Classe créée le 23/10/09.
 * 
 * @author Maxime GOUSSEAU
 * @version 2.0
 * 
 */
@Entity
@Table(name = "GROUPEMENT")
@NamedQueries(value = { @NamedQuery(name = "Groupement.findEnfants",
		query = "SELECT g FROM Groupement g WHERE parent = ?1") })
public class Groupement {

	private Integer groupementId;
	private Critere critere1;
	private Critere critere2;
	private String operateur;
	private Groupement parent;

	public Groupement() {
		super();
	}

	public Groupement(Critere cr1, Critere cr2, String op, Groupement par) {
		this.critere1 = cr1;
		this.critere2 = cr2;
		this.operateur = op;
		this.parent = par;
	}

	@Id
	@GeneratedValue(generator = "autoincrement")
	@GenericGenerator(name = "autoincrement", strategy = "increment")
	@Column(name = "GROUPEMENT_ID", unique = true, nullable = false)
	public Integer getGroupementId() {
		return groupementId;
	}

	public void setGroupementId(Integer grId) {
		this.groupementId = grId;
	}

	@OneToOne
	@JoinColumn(name = "CRITERE1_ID")
	public Critere getCritere1() {
		return critere1;
	}

	public void setCritere1(Critere cr1) {
		this.critere1 = cr1;
	}

	@OneToOne
	@JoinColumn(name = "CRITERE2_ID")
	public Critere getCritere2() {
		return critere2;
	}

	public void setCritere2(Critere cr2) {
		this.critere2 = cr2;
	}

	@Column(name = "OPERATEUR")
	public String getOperateur() {
		return operateur;
	}

	public void setOperateur(String op) {
		this.operateur = op;
	}

	@OneToOne
	@JoinColumn(name = "PARENT_ID")
	public Groupement getParent() {
		return parent;
	}

	public void setParent(Groupement par) {
		this.parent = par;
	}

	/**
	 * 2 groupements sont considérés comme égaux si ils ont le même opérateur,
	 * le même critere 1, le même critere 2 et le même parent.
	 * 
	 * @param obj
	 *            est le groupement à tester.
	 * @return true si les groupements sont égaux.
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		Groupement test = (Groupement) obj;
		if (this.operateur == null) {
			if (test.operateur == null) {
				if (this.critere1 == null) {
					if (test.critere1 == null) {
						if (this.critere2 == null) {
							if (test.critere2 == null) {
								if (this.parent == null) {
									return (test.parent == null);
								} else {
									return (this.parent.equals(test.parent));
								}
							} else {
								return false;
							}
						} else if (this.critere2.equals(test.critere2)) {
							if (this.parent == null) {
								return (test.parent == null);
							} else {
								return (this.parent.equals(test.parent));
							}
						} else {
							return false;
						}
					} else {
						return false;
					}
				} else if (this.critere1.equals(test.critere1)) {
					if (this.critere2 == null) {
						if (test.critere2 == null) {
							if (this.parent == null) {
								return (test.parent == null);
							} else {
								return (this.parent.equals(test.parent));
							}
						} else {
							return false;
						}
					} else if (this.critere2.equals(test.critere2)) {
						if (this.parent == null) {
							return (test.parent == null);
						} else {
							return (this.parent.equals(test.parent));
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
		} else if (this.operateur.equals(test.operateur)) {
			if (this.critere1 == null) {
				if (test.critere1 == null) {
					if (this.critere2 == null) {
						if (test.critere2 == null) {
							if (this.parent == null) {
								return (test.parent == null);
							} else {
								return (this.parent.equals(test.parent));
							}
						} else {
							return false;
						}
					} else if (this.critere2.equals(test.critere2)) {
						if (this.parent == null) {
							return (test.parent == null);
						} else {
							return (this.parent.equals(test.parent));
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else if (this.critere1.equals(test.critere1)) {
				if (this.critere2 == null) {
					if (test.critere2 == null) {
						if (this.parent == null) {
							return (test.parent == null);
						} else {
							return (this.parent.equals(test.parent));
						}
					} else {
						return false;
					}
				} else if (this.critere2.equals(test.critere2)) {
					if (this.parent == null) {
						return (test.parent == null);
					} else {
						return (this.parent.equals(test.parent));
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
	 * Le hashcode est calculé sur les attributs opérateur, critere1, critere2
	 * et parent.
	 * 
	 * @return la valeur du hashcode.
	 */
	@Override
	public int hashCode() {

		int hash = 7;
		int hashOperateur = 0;
		int hashCritere1 = 0;
		int hashCritere2 = 0;
		int hashParent = 0;

		if (this.operateur != null) {
			hashOperateur = this.operateur.hashCode();
		}
		if (this.critere1 != null) {
			hashCritere1 = this.critere1.hashCode();
		}
		if (this.critere2 != null) {
			hashCritere2 = this.critere2.hashCode();
		}
		if (this.parent != null) {
			hashParent = this.parent.hashCode();
		}

		hash = 31 * hash + hashOperateur;
		hash = 31 * hash + hashCritere1;
		hash = 31 * hash + hashCritere2;
		hash = 31 * hash + hashParent;

		return hash;

	}

	/**
	 * Méthode surchargeant le toString() de l'objet.
	 */
	@Override
	public String toString() {
		if (this.groupementId != null) {
			return "{" + this.groupementId + "}";
		} else {
			return "{Empty Groupement}";
		}
	}
}
