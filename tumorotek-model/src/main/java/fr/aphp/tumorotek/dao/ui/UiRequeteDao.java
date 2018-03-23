/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (14/07/2014)
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
package fr.aphp.tumorotek.dao.ui;

import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.systeme.Entite;
import fr.aphp.tumorotek.model.ui.UiRequete;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * nterface pour le DAO du bean de domaine UI_REQUETE.
 * Classe créée le 16/07/14.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.11
 *
 */
public interface UiRequeteDao extends GenericDaoJpa<UiRequete, Integer>
{

   /**
    * Recherche les requetes enregistrées par un utilisateur pour un 
    * onglet (entite)
    * @param u Utilisateur
    * @param e Entite
    * @return liste de UiRequetes
    */
   public List<UiRequete> findByUtilisateurAndEntite(Utilisateur u, Entite e);

   /**
    * Recherche les requetes enregistrées par un utilisateur pour un 
    * onglet (entite) et un nom spécifiés.
    * Methode utilisée dans la recherche de doublons.
    * @param u Utilisateur
    * @param e Entite
    * @param String nom 
    * @return liste de UiRequetes
    */
   public List<UiRequete> findByNomUtilisateurAndEntite(Utilisateur u, Entite e, String nom);
}
