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
package fr.aphp.tumorotek.manager.stats;

import java.util.List;
import java.util.Set;

import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stats.Indicateur;
import fr.aphp.tumorotek.model.stats.SModele;
import fr.aphp.tumorotek.model.stats.SModeleIndicateur;
import fr.aphp.tumorotek.model.stats.Subdivision;

/**
 *
 * @author jhusson
 * @version 2.1
 *
 */
public interface SModeleManager
{

   /**
    * Recherche un IndicateurModele dont l'identifiant est passé en paramètre.
    * @param modeleId Identifiant du Modele que l'on recherche.
    * @return Un IndicateurModele.
    */
   SModele findByIdManager(Integer modeleId);

   /**
    * Recherche tous les IndicateurModele présents dans la base, ordonnés
    * par nom.
    * @return Liste des IndicateurModele.
    */
   List<SModele> findAllObjectsManager();

   /**
    * Recherche tous les IndicateurModele présents d'une Plateforme, ordonnés
    * par nom.
    * @param plateforme Plateforme.
    * @return Liste de Modeles.
    */
   List<SModele> findByPlateformeManager(Plateforme plateforme);

   /**
    * Recherche toutes les collections associées 
    * à ce modele
    * 
    * @param im
    * @return Liste des banques
    */
   Set<Banque> getBanquesManager(SModele im);

   /**
    * Recherche les doublons de l IndicateurModele passé en paramètre.
    * @param modele Un IndicateurModele pour lequel on cherche des doublons.
    * @return True s'il existe des doublons.
    */
   Boolean findDoublonManager(SModele modele);

   /**
    * Test si un Modele est lié à des utilisateurs.
    * @param modele IndicateurModele que l'on souhaite tester.
    * @return Vrai si le Modele est utilisé.
    */
   Boolean isUsedObjectManager(SModele modele);

   /**
    * Persist une instance de Modele dans la base de données.
    * @param modele Nouvelle instance de l'objet à créer.
    * @param plateforme Plateforme du modèle.
    * @param liste des indicateurs associes
    * @param liste des banques sur lesquels appliquer les indicateurs
    */
   void createObjectManager(SModele modele, Plateforme plateforme, List<Indicateur> indicateurs, List<Banque> banques);

   /**
    * Sauvegarde les modifications apportées à un objet persistant.
    * @param modele Objet à mettre à jour dans la base.
    * @param plateforme Plateforme du modèle.
    * @param liste des indicateurs associes
    * @param liste des banques sur lesquels appliquer les indicateurs
    * @return SModele modifié
    * @version 2.1
    */
   SModele updateObjectManager(SModele modele, Plateforme plateforme, List<Indicateur> indicateurs, List<Banque> banques);

   /**
    * Supprime un IndicateurModele de la base de données.
    * @param modele IndicateurModele à supprimer de la base de données.
    */
   void removeObjectManager(SModele modele);

   /**
    * Getter method pour eviter LazyInitialize exception
    * @param sM SModele
    * @return List SModeleIndicateurs
    */
   Set<SModeleIndicateur> getSModeleIndicateursManager(SModele sM);

   /**
    * Retourne la liste d'indicateurs associes à un SModele
    * @param sM SModele
    * @return liste Indicateurs
    */
   List<Indicateur> getIndicateursManager(SModele sM);

   /**
    * Retourne la subdivision associée au sModele.
    * La subdivision est obtenue à partir des indicateurs qui composent 
    * le sModele. Si plusieurs subdivisons sont trouvées, throws une 
    * RuntimeException pour lever cette incoherence.
    * @param sM
    * @return la Subdivision attribuée au modèle, ou null si comptes totaux
    */
   Subdivision getSubdivisionManager(SModele sM);

}
