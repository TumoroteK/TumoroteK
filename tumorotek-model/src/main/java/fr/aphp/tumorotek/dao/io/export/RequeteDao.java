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
package fr.aphp.tumorotek.dao.io.export;

import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.io.export.Requete;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 * 
 * Interface pour le DAO du bean de domaine Requete.
 * Interface créée le 23/10/09.
 * 
 * @author Maxime GOUSSEAU
 * @version 2.0
 */
public interface RequeteDao extends GenericDaoJpa<Requete, Integer> {
	
	/**
	 * Recherche les requêtes d'un utilisateur.
	 * @param utilisateur : utilisateur dont on veut connaître les requêtes.
	 * @return la liste des requêtes de l'utilisateur
	 */
    List<Requete> findByUtilisateur(Utilisateur utilisateur);
    
    /**
	 * Recherche les requêtes d'une Banque.
	 * @param banque Banque dont on veut connaître les requêtes.
	 * @return la liste des requêtes de la Banque.
	 */
    List<Requete> findByBanque(Banque banque);
    
    /**
	 * Recherche les Requetes pour plusieurs Banques.
	 * @param banques Liste de Banques dont on veut connaître les Requetes.
	 * @return la liste des Requetes des Banques.
	 */
    List<Requete> findByBanqueInList(List<Banque> banques);

    /**
	 * Recherche toutes les Requetes, sauf celle dont l'id est passé 
	 * en paramètre.
	 * @param requeteId Identifiant de la Requete que l'on souhaite
	 * exclure de la liste retournée.
	 * @return une liste de Requetes.
	 */
	List<Requete> findByExcludedId(Integer requeteId);

	/**
	 * Recherche les requêtes d'un intitulé.
	 * @param intitulé : intitulé dont on veut connaître les requêtes.
	 * @return la liste des requêtes de l'intitulé
	 */
	List<Requete> findByIntitule(String intitule);
	
	/**
	 * Recherche les Requetes d'un utilisateur par intitulé.
	 * @param intitulé : intitulé dont on veut connaître les Requetes.
	 * @param utilisateur : Utilisateur ayant créé les Requetes.
	 * @return la liste des Requetes de l'intitulé
	 */
    List<Requete> findByIntituleUtilisateur(
    		String intitule, Utilisateur utilisateur);

}
