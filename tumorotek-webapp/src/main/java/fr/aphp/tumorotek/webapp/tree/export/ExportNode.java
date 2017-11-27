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
package fr.aphp.tumorotek.webapp.tree.export;

import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.export.Critere;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Classe abstraite représentant un noeud d'un arbre RequeteModel.
 * Classe créée le 09/02/10.
 * @since 2.0.10 gère les Calendar (data type datetime)
 * 
 * @author GOUSSEAU Maxime.
 * @version 2.0.10
 *
 */
public abstract class ExportNode {
	
	private Log log = LogFactory.getLog(ExportNode.class);
	
	/**
	 * Parent
	 */
	GroupementNode parent = null;
	private String critereAlphanumValue;
	private Date critereDateValue;
	private Calendar critereCalendarValue;
	private Boolean critereBooleanValue;
	private Float critereNumValue;

	public GroupementNode getParent() {
		return parent;
	}

	public void setParent(GroupementNode parent) {
		this.parent = parent;
	}

	public String getCritereAlphanumValue() {
		return critereAlphanumValue;
	}

	public void setCritereAlphanumValue(String c) {
		this.critereAlphanumValue = c;
	}

	public Date getCritereDateValue() {
		return critereDateValue;
	}

	public void setCritereDateValue(Date c) {
		this.critereDateValue = c;
	}

	// hack car calendarbox ne récupère la value par binding...?

	public Date getCritereCalendarValue() {
		if (critereCalendarValue != null) {
			return critereCalendarValue.getTime();
		}
		return null;
	}

	public void setCritereCalendarValue(Date c) {
		if (c != null) {
			Calendar cal  = Calendar.getInstance();
			cal.setTime(c);
			this.critereCalendarValue = cal;
		} else {
			this.critereCalendarValue = null;
		}
	}

	public Boolean getCritereBooleanValue() {
		return critereBooleanValue;
	}

	public void setCritereBooleanValue(Boolean c) {
		this.critereBooleanValue = c;
	}

	public Float getCritereNumValue() {
		return critereNumValue;
	}

	public void setCritereNumValue(Float c) {
		this.critereNumValue = c;
	}

	public abstract boolean isLeaf();
	
	/*public String getCritereToString() {
		String retour = null;
		if (this instanceof GroupementNode) {
			return ((GroupementNode) this).toString();
		} else if (this instanceof CritereNode) {
			Critere crit = ((CritereNode) this).getCritere();
			String op = "";
			if (crit.getOperateur().equals("is null")) {
				op = Labels.getLabel("critere.is.null");
			} else {
				op = crit.getOperateur();
			}
			if (crit.getChamp() != null 
					&& crit.getOperateur() != null
					&& crit.getValeur() != null) {
			return new ChampDecorator(crit.getChamp()).getLabelLong() + " "
					+ op + " " + crit.getValeur();
			} else {
				return new CombinaisonDecorator(crit.getCombinaison())
						.getLabel()
						+ " " + op + " " + crit.getValeur();
			}
			return ((CritereNode)this).toString();
		}
		return retour;
	}*/
	
	public List<ExportNode> getExportNodeList() {
		List<ExportNode> liste = new ArrayList<ExportNode>();
		if (this instanceof GroupementNode) {
			if (((GroupementNode)this).getChild(1) != null) {
				liste.addAll(((ExportNode) ((GroupementNode) this).getChild(1))
						.getExportNodeList());
			}
			if (((GroupementNode) this).getOperateur() != null) {
				liste.add(this);
				if (((GroupementNode) this).getChild(2) != null) {
					liste.addAll(((ExportNode) ((GroupementNode) this)
							.getChild(2)).getExportNodeList());
				}
			}
		} else if (this instanceof CritereNode) {
			liste.add(this);
		}
		return liste;
	}
	
	/**
	 * @return Nombre d'ascendants du noeud. 
	 */
	public int getParentCount() {
		int cpt = 0;
		if (getParent() != null) {
			cpt = 1 + getParent().getParentCount();
		}
		return cpt;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean retour = false;
		if (obj instanceof GroupementNode) {
			retour = ((GroupementNode)obj).equals(this);
		} else if (obj instanceof CritereNode) {
			retour = ((CritereNode)obj).equals(this);
			
		}
		return retour;
	}
	
	@Override
	public String toString() {
		String retour = null;
		if (this instanceof GroupementNode) {
			return ((GroupementNode)this).toString();
		} else if (this instanceof CritereNode) {
			return ((CritereNode)this).toString();
		}
		return retour;
	}

	public abstract String getWidth();
	
	public abstract String getSclass();
	
	
	/**
	 * Suppression d'un noeud
	 */
	public static List<Critere> deleteNode(ExportNode e, GroupementNode racine) {
		List<Critere> crit = null;
		if (e != null) {
			crit = new ArrayList<Critere>();
			/** On recherche le GroupementNode du noeud */
			GroupementNode parent = null;
			boolean isRacine = false;
			if (e.equals(racine)) {
				parent = racine;
				isRacine = true;
				/** On supprime le noeud */
				if (racine.getNode1() != null) {
					crit.addAll(racine.removeNode(racine.getNode1()));
				}
				if (racine.getNode2() != null) {
					crit.addAll(racine.removeNode(racine.getNode2()));
				}
			} else {
				parent = racine.getGroupementNode(e);
				/** On supprime le noeud */
				crit.addAll(parent.removeNode(e));
			}
			
			
			
			/** On regarde si le noeud a un frere */
			ExportNode frere = null;
			if (parent.getNode1() != null && !parent.getNode1().equals(e)) {
				frere = parent.getNode1();
			} else if (parent.getNode2() != null && !parent.getNode2().equals(e)) {
				frere = parent.getNode2();
			}
			/** On regarde si le noeud est le noeud racine */
			if (isRacine) {
				racine.setNode1(null);
				racine.setNode2(null);
				racine.setOperateur(null);
				racine = null;
			} else if(parent.equals(racine)) {
				if (frere == null) {
					racine = null;
				} else {
					if (parent.getNode1() != null) {
						/** Si le noeud 1 est un GroupementNode, on remplace son
						 *  parent par lui même
						 */
						if (frere instanceof GroupementNode) {
							parent.insertToNode1(((GroupementNode) frere).getNode1());
							parent.setOperateur(((GroupementNode) frere).getOperateur());
							parent.insertToNode2(((GroupementNode) frere).getNode2());
						} else if (frere instanceof CritereNode) {
							parent.setOperateur(null);
							parent.setNode2(null);
						}
					} else if (parent.getNode2() != null) {
						/** Si le noeud 2 est un CritereNode, on le met sur le
						 * noeud 1
						 */
						if (frere instanceof CritereNode) {
							parent.insertToNode1(frere);						
							parent.setNode2(null);
							parent.setOperateur(null);
						} else if (frere instanceof GroupementNode) {
							/** Si le noeud 2 est un GroupementNode, on remplace son
							 *  parent par lui même
							 */
							parent.insertToNode1(((GroupementNode) frere).getNode1());
							parent.setOperateur(((GroupementNode) frere).getOperateur());
							parent.insertToNode2(((GroupementNode) frere).getNode2());
						}
					} else {
						parent.setOperateur(null);
					}
				}
			} else {
				/** Si le noeud n'est pas le noeud racine, on remonte son frere */
				if (frere != null) {
					if (parent.getParent() != null) {
						GroupementNode grandParent = parent.getParent();
						if (grandParent.getNode1().equals(parent)) {
							grandParent.insertToNode1(frere);
							parent = null;
						} else if (grandParent.getNode2().equals(parent)) {
							grandParent.insertToNode2(frere);
							parent = null;
						}
					}
				}
			}
			
		}
		e = null;
		return crit;
	}
	
	/**
	 * Renvoie True si la valeur du critère est alphanumerique : on
	 * va afficher une Textbox.
	 * @return
	 */
	public boolean getVisibleTextbox() {
		if (this instanceof CritereNode) {
			CritereNode cn = (CritereNode) this;
			if (cn.getCritere().getOperateur().equals("is null")) {
				return false;
			} else {
				if (cn.getCritere().getChamp().getChampEntite() != null) {
					if (cn.getCritere().getChamp().getChampEntite()
							.getDataType().getType().equals("alphanum")
							|| cn.getCritere().getChamp().getChampEntite()
								.getDataType().getType().equals("texte")
							|| cn.getCritere().getChamp().getChampEntite()
								.getDataType().getType().equals("thesaurus")
							|| cn.getCritere().getChamp().getChampEntite()
								.getDataType().getType().equals("hyperlien")
							|| cn.getCritere().getChamp().getChampEntite()
							.getDataType().getType().equals("thesaurusM")) {
						return true;
					} else {
						return false;
					}
				} else {
					if (cn.getCritere().getChamp().getChampAnnotation()
							.getDataType().getType().equals("alphanum")
							|| cn.getCritere().getChamp().getChampAnnotation()
							.getDataType().getType().equals("texte")
							|| cn.getCritere().getChamp().getChampAnnotation()
							.getDataType().getType().equals("thesaurus")
							|| cn.getCritere().getChamp().getChampAnnotation()
							.getDataType().getType().equals("hyperlien")
							|| cn.getCritere().getChamp().getChampAnnotation()
							.getDataType().getType().equals("thesaurusM")
							|| cn.getCritere().getChamp().getChampAnnotation()
							.getDataType().getType().equals("num")) {
						return true;
					} else {
						return false;
					}
				}
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Renvoie True si la valeur du critère est une date : on
	 * va afficher une Datebox.
	 * @return
	 */
	public boolean getVisibleDatebox() {
		if (this instanceof CritereNode) {
			CritereNode cn = (CritereNode) this;
			if (cn.getCritere().getOperateur().equals("is null")) {
				return false;
			} else {
				if (cn.getCritere().getChamp().getChampEntite() != null) {
					if (cn.getCritere().getChamp().getChampEntite()
							.getDataType().getType().equals("date")) {
						return true;
					} else {
						return false;
					}
				} else {
					if (cn.getCritere().getChamp().getChampAnnotation()
							.getDataType().getType().equals("date")) {
						return true;
					} else {
						return false;
					}
				}
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Renvoie True si la valeur du critère est une date : on
	 * va afficher une Datebox.
	 * @return
	 */
	public boolean getVisibleCalendarbox() {
		if (this instanceof CritereNode) {
			CritereNode cn = (CritereNode) this;
			if (cn.getCritere().getOperateur().equals("is null")) {
				return false;
			} else {
				if (cn.getCritere().getChamp().getChampEntite() != null) {
					if (cn.getCritere().getChamp().getChampEntite()
							.getDataType().getType().equals("datetime")) {
						return true;
					} else {
						return false;
					}
				} else {
					if (cn.getCritere().getChamp().getChampAnnotation()
							.getDataType().getType().equals("datetime")) {
						return true;
					} else {
						return false;
					}
				}
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Renvoie True si la valeur du critère est boolean : on
	 * va afficher une Checkbox.
	 * @return
	 */
	public boolean getVisibleCheckbox() {
		if (this instanceof CritereNode) {
			CritereNode cn = (CritereNode) this;
			if (cn.getCritere().getOperateur().equals("is null")) {
				return false;
			} else {
				if (cn.getCritere().getChamp().getChampEntite() != null) {
					if (cn.getCritere().getChamp().getChampEntite()
							.getDataType().getType().equals("boolean")) {
						return true;
					} else {
						return false;
					}
				} else {
					if (cn.getCritere().getChamp().getChampAnnotation()
							.getDataType().getType().equals("boolean")) {
						return true;
					} else {
						return false;
					}
				}
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Renvoie True si la valeur du critère est numerique : on
	 * va afficher une Decimalbox.
	 * @return
	 */
	public boolean getVisibleDecimalbox() {
		if (this instanceof CritereNode) {
			CritereNode cn = (CritereNode) this;
			if (cn.getCritere().getOperateur().equals("is null")) {
				return false;
			} else {
				if (cn.getCritere().getChamp().getChampEntite() != null) {
					if (cn.getCritere().getChamp().getChampEntite()
							.getDataType().getType().equals("num")) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Renvoie True si l'opérateur du critère est is null.
	 * @return
	 */
	public boolean getIsNullOperateur() {
		if (this instanceof CritereNode) {
			CritereNode cn = (CritereNode) this;
			if (cn.getCritere().getOperateur().equals("is null")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Renvoie la valeur saisie.
	 * @return
	 */
	public Object getCritereValue() {
		if (this instanceof CritereNode) {
			CritereNode cn = (CritereNode) this;
			if (cn.getCritere().getChamp().getChampEntite() != null) {
				if (cn.getCritere().getChamp().getChampEntite()
						.getDataType().getType().equals("alphanum")
						|| cn.getCritere().getChamp().getChampEntite()
							.getDataType().getType().equals("texte")
						|| cn.getCritere().getChamp().getChampEntite()
							.getDataType().getType().equals("thesaurus")
						|| cn.getCritere().getChamp().getChampEntite()
							.getDataType().getType().equals("hyperlien")
						|| cn.getCritere().getChamp().getChampEntite()
						.getDataType().getType().equals("thesaurusM")) {
					if (critereAlphanumValue != null 
							&& critereAlphanumValue.equals("")) {
						critereAlphanumValue = null;
					}
					return critereAlphanumValue;
				} else if (cn.getCritere().getChamp().getChampEntite()
						.getDataType().getType().equals("num")) {
					if (cn.getCritere().getChamp().getChampEntite()
							.getNom().equals("ConditNbr")) {
						return critereNumValue.intValue();
					} if (cn.getCritere().getChamp().getChampEntite()
							.getNom().equals("AgeAuPrelevement")) {
						return new Integer (critereNumValue.intValue());
					} else {
						return critereNumValue;
					}
				} else if (cn.getCritere().getChamp().getChampEntite()
						.getDataType().getType().equals("datetime")) {
					return critereCalendarValue;
				} else if (cn.getCritere().getChamp().getChampEntite()
						.getDataType().getType().equals("date")) {
					if (isCalendar(cn.getCritere().getChamp()
							.getChampEntite())) {
						Calendar cal = Calendar.getInstance();
						if (critereDateValue != null) {
							cal.setTime(critereDateValue);
						} else {
							cal = null;
						}
						return cal;
					} else {
						return critereDateValue;
					}
				} else if (cn.getCritere().getChamp().getChampEntite()
						.getDataType().getType().equals("boolean")) {
					return critereBooleanValue;
				} else {
					return null;
				}
			} else {
				if (cn.getCritere().getChamp().getChampAnnotation()
						.getDataType().getType().equals("alphanum")
						|| cn.getCritere().getChamp().getChampAnnotation()
							.getDataType().getType().equals("texte")
						|| cn.getCritere().getChamp().getChampAnnotation()
							.getDataType().getType().equals("thesaurus")
						|| cn.getCritere().getChamp().getChampAnnotation()
							.getDataType().getType().equals("hyperlien")
						|| cn.getCritere().getChamp().getChampAnnotation()
						.getDataType().getType().equals("thesaurusM")
						|| cn.getCritere().getChamp().getChampAnnotation()
						.getDataType().getType().equals("num")) {
					return critereAlphanumValue;
				} else if (cn.getCritere().getChamp().getChampAnnotation()
						.getDataType().getType().matches("date.*")) {
				   
				 //FIXME (FIXED) incident TK-13: cn.getCritere().getChamp().getChampEntite() Forcément null puisque le premier test ligne 521 : cn.getCritere().getChamp().getChampEntite() != null
//					if (isCalendar(cn.getCritere().getChamp().getChampEntite())) {
//						Calendar cal = Calendar.getInstance();
//						if (critereDateValue != null) {
//							cal.setTime(critereDateValue);
//						} else {
//							cal = null;
//						}
//						return cal;
//					} else {
//						return critereDateValue;
//					}
				   
				   //[TK-13 FIX]: on récupère directement les valeurs envoyées, pas besoin de passer par isCalendar()
               if (null != critereDateValue || null != critereCalendarValue) {
                  Calendar cal = Calendar.getInstance();
                  if (critereDateValue != null) {
                     cal.setTime(critereDateValue);
                  } else {
                     cal = critereCalendarValue;
                  }
                  return cal;
               } else {
                  return critereDateValue;
               }
             //[/TKB-2 FIX]
               
				} else if (cn.getCritere().getChamp().getChampAnnotation()
						.getDataType().getType().equals("boolean")) {
					return critereBooleanValue;
				} else {
					return null;
				}
			}
		} else {
			return null;
		}
	}
	
	public boolean isCalendar(ChampEntite ce) {
		boolean isCalendar = false;
		
		Object obj = null;
		if (ce.getEntite().getNom().equals("Patient")) {
			obj = new Patient();
		} else if (ce.getEntite().getNom().equals("Maladie")) {
			obj = new Maladie();
		} else if (ce.getEntite().getNom().equals("Prelevement")) {
			obj = new Prelevement();
		} else if (ce.getEntite().getNom().equals("Echantillon")) {
			obj = new Echantillon();
		} else if (ce.getEntite().getNom().equals("ProdDerive")) {
			obj = new ProdDerive();
		} else if (ce.getEntite().getNom().equals("Cession")) {
			obj = new Cession();
		}
		
		String nomChamp = ce.getNom()
			.replaceFirst(".",
					(ce.getNom().charAt(0) + "")
					.toLowerCase());
		try {
			String type = PropertyUtils
				.getPropertyDescriptor(obj, nomChamp)
				.getPropertyType().getSimpleName();
			
			if (type != null && type.equals("Calendar")) {
				isCalendar = true;
			}
		} catch (IllegalAccessException e) {
			log.error(e);
		} catch (InvocationTargetException e) {
			log.error(e);
		} catch (NoSuchMethodException e) {
			log.error(e);
		}
		
		return isCalendar;
	}

	/*public boolean isCalendar(ChampAnnotation champAnnotation) {
		boolean isCalendar = false;

		Object obj = null;
		if (champAnnotation.getEntite().getNom().equals("Patient")) {
			obj = new Patient();
		} else if (champAnnotation.getEntite().getNom().equals("Maladie")) {
			obj = new Maladie();
		} else if (champAnnotation.getEntite().getNom().equals("Prelevement")) {
			obj = new Prelevement();
		} else if (champAnnotation.getEntite().getNom().equals("Echantillon")) {
			obj = new Echantillon();
		} else if (champAnnotation.getEntite().getNom().equals("ProdDerive")) {
			obj = new ProdDerive();
		} else if (champAnnotation.getEntite().getNom().equals("Cession")) {
			obj = new Cession();
		}

		String nomChamp = champAnnotation.getNom()
			.replaceFirst(".",
				(champAnnotation.getNom().charAt(0) + "")
					.toLowerCase());
		try {
			String type = PropertyUtils
				.getPropertyDescriptor(obj, nomChamp)
				.getPropertyType().getSimpleName();

			if (type != null && type.equals("Calendar")) {
				isCalendar = true;
			}
		} catch (IllegalAccessException e) {
			log.error(e);
		} catch (InvocationTargetException e) {
			log.error(e);
		} catch (NoSuchMethodException e) {
			log.error(e);
		}

		return isCalendar;
	}*/
	
}
