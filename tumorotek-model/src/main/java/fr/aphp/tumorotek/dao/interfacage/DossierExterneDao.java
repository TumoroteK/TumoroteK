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
package fr.aphp.tumorotek.dao.interfacage;

import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.interfacage.DossierExterne;
import fr.aphp.tumorotek.model.interfacage.Emetteur;

/**
 *
 * Interface pour le DAO du bean de domaine DOSSIER_EXTERNE.
 * Interface créée le 04/10/11.
 *
 * @author Pierre VENTADOUR
 * @version 2.1
 *
 */
public interface DossierExterneDao extends GenericDaoJpa<DossierExterne, Integer>
{

   /**
    * Recherche les Dossiers externes d'un emetteur ordonnés par date.
    * @param emetteur Emetteur.
    * @return Une liste de dossiers.
    */
   List<DossierExterne> findByEmetteur(Emetteur emetteur);

   /**
    * Recherche les dossiers d'un émetteur par identification.
    * @param emetteur Emetteur des dossiers recherchés.
    * @param identification Identification des emetteurs recherchés.
    * @return Une liste de dossiers.
    */
   List<DossierExterne> findByEmetteurAndIdentification(Emetteur emetteur, String identification);

   /**
    * Recherche les dossiers par identification.
    * @param identification Identification des emetteurs recherchés.
    * @return Une liste de dossiers.
    */
   List<DossierExterne> findByIdentification(String identification);

   /**
    * Recherche les dossiers d'émetteurs par identification.
    * @param emetteurs Liste des Emetteurs des dossiers recherchés.
    * @param identification Identification des emetteurs recherchés.
    * @return Une liste de dossiers.
    */
   List<DossierExterne> findByEmetteurInListAndIdentification(List<Emetteur> emetteurs, String identification);

   List<String> findByEmetteurInListSelectIdentification(List<Emetteur> emetteurs);

   /**
    * Compte le nombre d'entrees dans la table.
    * @return une liste avec un seul élément = compte.
    * @since 2.1
    */
   List<Long> findCountAll();

   /**
    * Renvoie le premier objet enregistré.
    * @return firstDossierExterne
    * @since 2.1
    */
   List<DossierExterne> findFirst();

   /**
    * Renvoie les dossiers externes partageant une valeur externe.
    * Utilisé par GENNO pour regrouper les dérivés enfants avec le dossier parent.
    * @param emetteur
    * @param champ entite id
    * @param valeur
    * @return liste de dossiers
    * @since 2.2.3-genno
    */
   List<DossierExterne> findChildrenByEmetteurValeur(Emetteur _e, Integer _c, String valeur);

   /**
    * Renvoie les dossiers externes corrspondant à une entité.
    * @param emetteur
    * @param entite id
    * @return liste de dossiers
    * @since 2.2.3-genno
    */
   List<DossierExterne> findByEmetteurAndEntite(Emetteur _e, Integer _i);

   /**
    * Renvoie les dossiers externes corrspondant à une entité nulle.
    * @param emetteur
    * @return liste de dossiers
    * @since 2.2.3-genno
    */
   List<DossierExterne> findByEmetteurAndEntiteNull(Emetteur _e);
}
