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
package fr.aphp.tumorotek.manager.interfacage.scan;

import java.util.List;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.interfacage.scan.ScanDevice;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;

/**
 * 
 * Interface pour le manager du bean de domaine ScanTerminale.
 * Interface créée le 26/04/2016.
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.1
 *
 */
public interface ScanTerminaleManager {
	
	/**
	 * Persiste le scan d'une boite pour un scanner passé en paramètre.
	 * Le scan d'une boite contient une liste de ScanTubes qui représentent les 
	 * objets contenus à chaque position de la boite (ou leur absence si ScanTube.code = null)
	 * @param sT scanTerminale scan de la boite
	 * @param sD scanDEvice scanner
	 */
	void createObjectManager(ScanTerminale sT, ScanDevice sD);
	
	/**
	 * Supprime les informations relatives à un scan de boites 
	 * @param sT scanTerminale scan de la boite
	 */
	void removeObjectManager(ScanTerminale sT);
	
	/**
	 * Renvoie toutes les instances ScanTerminale
	 * @return List<ScanTerminale>
	 */
	List<ScanTerminale> findAllManager();
	
	/**
	 * Renvoie tous les scans de boites pour un scanner passé en 
	 * paramètre dans l'ordre inverse de leur création
	 * @param scanDevice scanner
	 * @return List<ScanTerminale>
	 */
	List<ScanTerminale> findByDeviceManager(ScanDevice sD);
	
	/**
	 * Renvoie tous les codes échantillons/dérivés extraits par le 
	 * scanner sous la forme de ScanTubes
	 * @param sT scan boite
	 * @return List<String> codes
	 */
	List<String> findTKObjectCodesManager(ScanTerminale sT);
	
	/**
	 * Renvoie les objets stockables TK contenu dans la boite 
	 * à partir du scan.
	 * Renvoie une erreur de validation si scan incoherent avec stockage dans TK 
	 * cad tubes ne sont pas dans 1 seule boite de TK.
	 * @param sT scan boite
	 * @param list banques accessibles
	 * @return List<TKStockableObject> objects
	 */
	List<TKStockableObject> findTKStockableObjectsManager(ScanTerminale sT, List<Banque> banques);
	
	/**
	 * Compare le scan et la terminale trouvée dans le système de stockage, 
	 * en se basant sur les positions :
	 *  - trouve les emplacements remplis dans la boite, alors qu'ils ne le sont pas dans le 
	 * système de stockage virtuel de TK (fill)
	 * Si aucun emplacement n'est défini, un nouvel objet Emplacement est créé à la position.
	 *  - trouve les emplacements vides dans la boite alors qu'ils sont remplis 
	 *  dans le système de stockage TK (free)
	 *  - trouve les emplacements incohérents cad contenant un objet dont le code ne correspond 
	 *  pas à celui du code du tube scanné à sa position 
	 * Renvoie une erreur si le code de la boite scannée correspond à plusieurs boites/alias 
	 * dans TK trouvées à partir d'une enceinte parente ou d'un conteneur racine. Si enceinte parente 
	 * et/ou conteneur racine sont nulls, il n'y a plus de restriction de la recherche dans 
	 * l'arborescence.
	 * @param sT scan boite
	 * @param enc Enceinte parente
	 * @param liste Conteneurs racine accessibles
	 * @boolean si true fill mode, recherche les emplacements à remplir, ceux à vider sinon
	 * @return TKScanTerminaleDTO Objet de transfert l'ensembled des informations
	 */
	TKScanTerminaleDTO compareScanAndTerminaleManager(ScanTerminale sT, Enceinte enc, 
														List<Conteneur> conts);

	/**
	 * Recherche la boite depuis son scan dans le système de stockage 
	 * à partir du conteneur racine, et/ou d'une enceinte parente.
	 * Renvoie une ScannedTerminaleNotUniqueException si plusieurs boites 
	 * partagent le nom/alias scanné.
	 * @param sT scanTerminale
	 * @param enc Enceinte parente
	 * @param liste Conteneurs racine accessibles
	 * @return Terminale trouvée
	 */
	Terminale findScannedTerminaleManager(ScanTerminale sT, Enceinte enc, List<Conteneur> conts);
}
