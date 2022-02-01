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
package fr.aphp.tumorotek.manager.imprimante;

import java.util.Hashtable;
import java.util.List;

import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.imprimante.ChampLigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.LigneEtiquette;
import fr.aphp.tumorotek.model.imprimante.Modele;
import fr.aphp.tumorotek.model.imprimante.ModeleType;

/**
 *
 * Interface pour le manager du bean de domaine Modele.
 * Interface créée le 21/03/2011.
 *
 * @author Pierre Ventadour
 * @version 2.0
 *
 */
public interface ModeleManager
{

   /**
    * Recherche un Modele dont l'identifiant est passé en paramètre.
    * @param modeleId Identifiant du Modele que l'on recherche.
    * @return Un Modele.
    */
   Modele findByIdManager(Integer modeleId);

   /**
    * Recherche tous les Modeles présents dans la base, ordonnés
    * par nom.
    * @return Liste de Modeles.
    */
   List<Modele> findAllObjectsManager();

   /**
    * Recherche tous les Modeles présents d'une Plateforme, ordonnés
    * par nom.
    * @param plateforme Plateforme.
    * @return Liste de Modeles.
    */
   List<Modele> findByPlateformeManager(Plateforme plateforme);

   /**
    * Recherche tous les Modele présents d'une Plateforme, en
    * fonction de leur nom.
    * @param nom Nom du Modele.
    * @param plateforme Plateforme.
    * @return Liste de Modeles.
    */
   List<Modele> findByNomAndPlateformeManager(String nom, Plateforme plateforme);

   /**
    * Recherche les doublons du Modele passé en paramètre.
    * @param modele Un Modele pour lequel on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(Modele modele);

   /**
    * Test si un Modele est lié à des utilisateurs.
    * @param modele Modele que l'on souhaite tester.
    * @return Vrai si le Modele est utilisé.
    */
   Boolean isUsedObjectManager(Modele modele);

   /**
    * Persist une instance de Modele dans la base de données.
    * @param modele Nouvelle instance de l'objet à créer.
    * @param plateforme Plateforme du modèle.
    * @param modeleType ModeleType du modèle.
    * @param ligneEtiquettes LigneEtiquettes.
    * @param champLigneEtiquettes hashtable contenant les champs
    * pour chaque ligne.
    */
   void createObjectManager(Modele modele, Plateforme plateforme, ModeleType modeleType, List<LigneEtiquette> ligneEtiquettes,
      Hashtable<LigneEtiquette, List<ChampLigneEtiquette>> champLigneEtiquettes);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param modele Objet à mettre à jour dans la base.
    * @param plateforme Plateforme du modèle.
    * @param modeleType ModeleType du modèle.
    */
   void updateObjectManager(Modele modele, Plateforme plateforme, ModeleType modeleType,
      List<LigneEtiquette> ligneEtiquettesToCreate, List<LigneEtiquette> ligneEtiquettesToremove,
      Hashtable<LigneEtiquette, List<ChampLigneEtiquette>> champLigneEtiquettesToCreate,
      Hashtable<LigneEtiquette, List<ChampLigneEtiquette>> champLigneEtiquettesToRemove);

   /**
    * Supprime un Modele de la base de données.
    * @param modele Modele à supprimer de la base de données.
    */
   void removeObjectManager(Modele modele);

}
