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
package fr.aphp.tumorotek.webapp.tree.code;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Treeitem;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.code.Adicap;
import fr.aphp.tumorotek.model.code.AdicapGroupe;
import fr.aphp.tumorotek.model.code.CimMaster;
import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.code.CodeDossier;
import fr.aphp.tumorotek.model.code.CodeSelect;
import fr.aphp.tumorotek.model.code.CodeUtilisateur;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.webapp.tree.TumoTreeNode;

/**
 * Classe représentant un noeud Code d'un arbre contenant les
 * codes medicaux. Ce noeud peut être un code ou un dossier.
 * Classe créée le 25/05/10.
 * 
 * @author Mathieu BARTHELEMY.
 * @version 2.0.
 *
 */
public class CodeNode extends TumoTreeNode {
	
	private TableCodage codification;
	private CodeCommon code = null;
	private CodeDossier dossier = null;
	private CodeSelect codeSelect = null;
	private AdicapGroupe adicapGroupe = null;
	private Boolean isTopo = null;
	private Treeitem parentItem;
	
	private boolean isRoot = false;

	private List< ? extends CodeCommon> childrenCodes = 
												new ArrayList<CodeCommon>(); 
	private List<AdicapGroupe> childrenGroupes = 
											new ArrayList<AdicapGroupe>(); 
	private List<CodeDossier> childrenDossiers = 
										new ArrayList<CodeDossier>();
	
	public CodeNode() {
	}
	
	/**
	 * Constructeur d'un noeud code.
	 * @param c code 
	 * @param t codification associée
	 * @param b isTopo
	 */
	public CodeNode(CodeCommon c, TableCodage t, Boolean b) {
		this.code = c;
		this.codification = t;
		this.isTopo = b;
	}
	
	public CodeNode(CodeDossier d, TableCodage t) {
		this.dossier = d;
		this.codification = t;
	}
	
	/**
	 * Constructeur d'un noeud de type Groupe Adicap.
	 * Assigne le membre isTopo qui sera propagé aux code 
	 * Adicap fils.
	 * @param groupe
	 * @param t Codification Adicap
	 */
	public CodeNode(AdicapGroupe groupe, TableCodage t) {
		this.adicapGroupe = groupe;
		this.codification = t;
		if (groupe.getNom().equals("D3") || groupe.getNom().equals("D8")) {
			isTopo = true;
		} else if (groupe.getNom().equals("D6")) {
			isTopo = false;
		}
	}
	
	public void setCodification(TableCodage t) {
		this.codification = t;
	}
	
	public TableCodage getCodification() {
		return this.codification;
	}

	public CodeCommon getCode() {
		return code;
	}

	public void setCode(CodeCommon c) {
		this.code = c;
	}

	public void setDossier(CodeDossier dos) {
		this.dossier = dos;
	}

	public void setCodeSelect(CodeSelect cSelect) {
		this.codeSelect = cSelect;
	}

	public List< ? extends CodeCommon> getChildrenCodes() {
		return childrenCodes;
	}

	public CodeDossier getDossier() {
		return dossier;
	}
	
	public CodeSelect getCodeSelect() {
		return codeSelect;
	}

	public AdicapGroupe getAdicapGroupe() {
		return adicapGroupe;
	}
	
	public Treeitem getParentItem() {
		return this.parentItem;
	}

	public void setParentItem(Treeitem it) {
		this.parentItem = it;
	}
	
	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean ir) {
		this.isRoot = ir;
	}

	/**
	 * renvoie le label du node en fonction de sa composistion.
	 * @return String label
	 */
	public String getLabel() {
		if (code != null) {
			if (code.getLibelle() != null) {
				return code.getCode() + " - " + code.getLibelle();
			} else {
				return code.getCode();
			}
		} else if (adicapGroupe != null) {
			return adicapGroupe.getNom();
		} else if (dossier != null) {
			return dossier.getNom();
		}
		return null;
	}

	@Override
	public void readChildren() {
		children = new ArrayList<TumoTreeNode>();
		
		if (adicapGroupe != null) {
			childrenGroupes = ManagerLocator.getAdicapManager()
							.getAdicapGroupesManager(adicapGroupe);
			childrenCodes = ManagerLocator.getAdicapManager()
					.findByAdicapGroupeManager(adicapGroupe);			
		} else if (code != null) {	
			if ("ADICAP".equals(codification.getNom())) {
				childrenCodes = ManagerLocator.getAdicapManager()
						.findByAdicapParentManager((Adicap) code, isTopo);
			} else if ("CIM_MASTER".equals(codification.getNom())) {
				childrenCodes = ManagerLocator.getCimMasterManager()
								.findByCimParentManager((CimMaster) code);
			} else if ("UTILISATEUR".equals(codification.getNom())) {
				childrenCodes = ManagerLocator.getCodeUtilisateurManager()
						.findByCodeParentManager((CodeUtilisateur) code);
			}
		} else if (dossier != null) {
			childrenDossiers = ManagerLocator.getCodeDossierManager()
								.findByCodeDossierParentManager(dossier);
			if ("UTILISATEUR".equals(codification.getNom())) {
				childrenCodes = ManagerLocator.getCodeUtilisateurManager()
										.findByCodeDossierManager(dossier);
			} else if ("FAVORIS".equals(codification.getNom())) {
				childrenCodes = ManagerLocator.getCodeSelectManager()
							.findCodesFromSelectByDossierManager(dossier);
			}
		}
		
		// renvoie les codes en premier
		for (int i = 0; i < childrenCodes.size(); i++) {
			CodeNode node = new CodeNode(childrenCodes.get(i), 
													codification, isTopo);
			children.add(node);
		}
		for (int i = 0; i < childrenGroupes.size(); i++) {
			CodeNode node = new CodeNode(childrenGroupes.get(i), codification);
			children.add(node);
		}
		for (int i = 0; i < childrenDossiers.size(); i++) {
			CodeNode node = new CodeNode(childrenDossiers.get(i), codification);
			children.add(node);
		}
		
		
	}
	
	@Override
	public boolean isLeaf() {
		if (dossier != null || adicapGroupe != null) {
			return false;
		}
		return getChildCount() == 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		
		if ((obj == null) || obj.getClass() != this.getClass()) {
			return false;
		}
		
		CodeNode node = (CodeNode) obj;
		if (this.dossier != null) {
			return this.dossier.equals(node.getDossier());
		} else if (this.adicapGroupe != null) {
			return this.adicapGroupe.equals(node.getAdicapGroupe());
		} else if (this.code != null) {
			return this.code.equals(node.getCode());
		}
		return false;
		
	}

	@Override
	public int hashCode() {
		
		int hash = 7;
		int hashCodeMed = 0;
		int hashCodeDossier = 0;
		
		if (this.code != null) {
			hashCodeMed = this.code.hashCode();
		}
		
		if (this.dossier != null) {
			hashCodeDossier = this.dossier.hashCode();
		}
		
		hash = 7 * hash + hashCodeMed;
		hash = 7 * hash + hashCodeDossier;

		return hash;
	}
	
	/**
	 * Indique si le node est groupe de code non selectionnable
	 * donc dossier ou adicap groupe.
	 * @return boolean true si dossier
	 */
	public boolean isDossierOrGroupe() {
		return this.dossier != null || this.adicapGroupe != null;
	}
}