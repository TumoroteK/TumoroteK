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
package fr.aphp.tumorotek.action.thesaurus;

import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import fr.aphp.tumorotek.model.cession.CessionExamen;
import fr.aphp.tumorotek.model.cession.DestructionMotif;
import fr.aphp.tumorotek.model.cession.ProtocoleType;
import fr.aphp.tumorotek.model.coeur.echantillon.EchanQualite;
import fr.aphp.tumorotek.model.coeur.echantillon.EchantillonType;
import fr.aphp.tumorotek.model.coeur.echantillon.ModePrepa;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditMilieu;
import fr.aphp.tumorotek.model.coeur.prelevement.ConditType;
import fr.aphp.tumorotek.model.coeur.prelevement.ConsentType;
import fr.aphp.tumorotek.model.coeur.prelevement.Nature;
import fr.aphp.tumorotek.model.coeur.prelevement.PrelevementType;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.coeur.prodderive.ModePrepaDerive;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdQualite;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdType;
import fr.aphp.tumorotek.model.contexte.Categorie;
import fr.aphp.tumorotek.model.contexte.Specialite;
import fr.aphp.tumorotek.model.qualite.NonConformite;
import fr.aphp.tumorotek.model.stockage.ConteneurType;
import fr.aphp.tumorotek.model.stockage.EnceinteType;

/**
 * Renderer permettant d'afficher les valeurs d'un thésaurus.
 * @author Pierre Ventadour.
 * Classe créée le 11/10/2010.
 *
 */
public class ThesaurusRowRenderer implements RowRenderer {
	
	private boolean isAdmin = false;

	@Override
	public void render(Row row, Object data, int index) throws Exception {
		
		// valeur row
		Label valLabel = new Label(getValeur(data));
		valLabel.setParent(row);
		
		if (!isAdmin) {
			Label vide1 = new Label("");
			vide1.setParent(row);
			Label vide2 = new Label("");
			vide2.setParent(row);
		} else {
			// edit-validate row
			Image editImg = new Image();
			editImg.setStyle("cursor:pointer");
			editImg.setAlign("center");
			editImg.setSrc("/images/icones/edit.png");
			editImg.setHeight("12px");
			editImg.addForward("onClick", editImg.getParent(), 
									"onClickUpdateItem", data);
			editImg.setParent(row);
			
			// revert-delete row
			Image delImg = new Image();
			delImg.setStyle("cursor:pointer; text-align:center");
			delImg.setSrc("/images/icones/small_delete.png");
			delImg.addForward("onClick", delImg.getParent(), 
									"onClickDeleteItem", data);
			delImg.setParent(row);
		}
		
	}
	
	/**
	 * Renvoie le nom à afficher en fonction du thésaurus.
	 * @param data Valeur du thésaurus.
	 * @return Valeur à afficher.
	 */
	public String getValeur(Object data) {
		String thesaurus = data.getClass().getSimpleName();
		// valeur
		String valeur = "";
		if (thesaurus.equals("Nature")) {
			valeur = ((Nature) data).getNature();
		} else if (thesaurus.equals("PrelevementType")) {
			valeur = ((PrelevementType) data).getType();
		} else if (thesaurus.equals("EchantillonType")) {
			valeur = ((EchantillonType) data).getType();
		} else if (thesaurus.equals("EchanQualite")) {
			valeur = ((EchanQualite) data).getEchanQualite();
		} else if (thesaurus.equals("ProdType")) {
			valeur = ((ProdType) data).getType();
		} else if (thesaurus.equals("ProdQualite")) {
			valeur = ((ProdQualite) data).getProdQualite();
		} else if (thesaurus.equals("ConditType")) {
			valeur = ((ConditType) data).getType();
		} else if (thesaurus.equals("ConditMilieu")) {
			valeur = ((ConditMilieu) data).getMilieu();
		} else if (thesaurus.equals("ConsentType")) {
			valeur = ((ConsentType) data).getType();
		} else if (thesaurus.equals("Risque")) {
			valeur = ((Risque) data).getNom();
		} else if (thesaurus.equals("ModePrepa")) {
			valeur = ((ModePrepa) data).getNom();
		} else if (thesaurus.equals("ModePrepaDerive")) {
			valeur = ((ModePrepaDerive) data).getNom();
		} else if (thesaurus.equals("CessionExamen")) {
			valeur = ((CessionExamen) data).getExamen();
		} else if (thesaurus.equals("DestructionMotif")) {
			valeur = ((DestructionMotif) data).getMotif();
		} else if (thesaurus.equals("ProtocoleType")) {
			valeur = ((ProtocoleType) data).getType();
		} else if (thesaurus.equals("Specialite")) {
			valeur = ((Specialite) data).getNom();
		} else if (thesaurus.equals("Categorie")) {
			valeur = ((Categorie) data).getNom();
		} else if (thesaurus.equals("ConteneurType")) {
			valeur = ((ConteneurType) data).getType();
		} else if (thesaurus.equals("EnceinteType")) {
			valeur = ((EnceinteType) data).getType();
		} else if (thesaurus.contains("NonConformite")) {
			valeur = ((NonConformite) data).getNom();
		}
		
		return valeur;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isA) {
		this.isAdmin = isA;
	}

}
