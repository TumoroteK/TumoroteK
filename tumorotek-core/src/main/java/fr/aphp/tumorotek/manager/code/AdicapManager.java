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
package fr.aphp.tumorotek.manager.code;

import java.util.List;
import java.util.Set;

import fr.aphp.tumorotek.model.code.Adicap;
import fr.aphp.tumorotek.model.code.AdicapGroupe;
import fr.aphp.tumorotek.model.code.CimMaster;
import fr.aphp.tumorotek.model.code.CimoMorpho;

/**
 * 
 * Interface pour le Manager du bean de domaine Adicap.
 * Interface créée le 19/05/10.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public interface AdicapManager extends CodeCommonManager {
	
	/**
	 * Recherche les codes Adicap dont le groupe est passé en paramètre.
	 * @param groupe Adicap pour lequel on recherche des codes Adicap.
	 * @return une liste de codes Adicap.
	 */
	List<Adicap> findByAdicapGroupeManager(AdicapGroupe groupe);
	
	/**
	 * Recherche les codes Adicap dont la morpho est passée en paramètre.
	 * @param isMorpho Vrai ou faux.
	 * @return une liste de codes Adicap.
	 */
	List<Adicap> findByMorphoManager(Boolean isMorpho);
	
	/**
	 * Recherche les enfants du code Adicap passé en paramètre.
	 * @param code Adicap topo pour lequel on recherche les enfants.
	 * @param si recherche parent en mode topographie (dico8)
	 * @return une liste de codes Adicap.
	 */
	List<Adicap> findByAdicapParentManager(Adicap parent, Boolean isTopo);
	
	/**
	 * Recherche les codes Cimo morpho issus du transcodage du
	 * code Adicap passé en paramètre.
	 * @param code adicap qui sera transcodé
	 * @return Liste de code CimoMorpho
	 */
	Set<CimoMorpho> getCimoMorphosManager(Adicap adicap);
	
	/**
	 * Recherche les codes Cim topo issus du transcodage du
	 * code Adicap passé en paramètre.
	 * @param code adicap qui sera transcodé
	 * @return Liste de code CimMaster
	 */
	Set<CimMaster> getCimMastersManager(Adicap adicap);
	
	/**
	 * Cree la liste de dossiers correspondants à la liste 
	 * de dictionnaires de l'ADICAP.
	 * @return liste de AdicapGroupe
	 */ 
	List<AdicapGroupe> findDictionnairesManager();
	
	/**
	 * Renvoie les groupes enfants d'un groupe de codes Adicap.
	 * @param groupe parent
	 * @return liste de AdicapGroupe
	 */
	List<AdicapGroupe> getAdicapGroupesManager(AdicapGroupe groupe);
	
	/**
	 * Renvoie les codes ADICAP contenu dans l'arborescence dont la 
	 * racine est le code ou le groupe passé en paramètre. Les codes sont 
	 * filtrés par le critère stringOrLibelle ('%' supprime ce filtre).
	 * @param code ADICAP
	 * @param groupe ADICAP
	 * @param stringOrLibelle
	 * @return liste codes 'enfants'.
	 */
	List<Adicap> findChildrenCodesManager(Adicap code, AdicapGroupe groupe, 
			String codeOrLibelle);
	
	/**
	 * Recherche les codes ADICAP dans un dictionnaire par son code 
	 * ou son libellé LIKE.
	 * @param grp Dico ADICAP
	 * @param codeOrLibelle
	 * @param true si recherche exact match
	 * @return liste de codes Adicap
	 */
	List<Adicap> findByDicoAndCodeOrLibelleManager(AdicapGroupe grp, 
								String codeOrLibelle, boolean exactMatch);
}
