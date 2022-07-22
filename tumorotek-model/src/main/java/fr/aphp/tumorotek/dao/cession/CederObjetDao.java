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
package fr.aphp.tumorotek.dao.cession;

import java.util.List;

import fr.aphp.tumorotek.dao.GenericDaoJpa;
import fr.aphp.tumorotek.model.cession.CederObjet;
import fr.aphp.tumorotek.model.cession.CederObjetPK;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.cession.ECederObjetStatut;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 *
 * Interface pour le DAO du bean de domaine CederObjet.
 * Interface créée le 26/01/10.
 *
 * @author Pierre Ventadour
 * @version 2.1.1
 *
 */
public interface CederObjetDao extends GenericDaoJpa<CederObjet, CederObjetPK>
{

   /**
    * Recherche les CederObjets sauf celui dont la clé primaire est
    * passé en paramètre.
    * @param pk CederObjetPK.
    * @return une liste de CederObjets.
    */
   List<CederObjet> findByExcludedPK(CederObjetPK pk);

   /**
    * Recherche les CederObjets dont l'entité est égale au paramètre.
    * @param entite Entite des CederObjets recherchés.
    * @return une liste de CederObjets.
    */
   List<CederObjet> findByEntite(Entite entite);

   /**
    * Recherche touss les CederObjet ppur un couple de valeurs entité
    * et objetId.
    * @param entite Entite des CederObjets recherchés.
    * @param objetId Identifiant de l'obejt.
    * @return Liste ordonnée de CederObjets.
    */
   List<CederObjet> findByEntiteObjet(Entite entite, Integer objetId);

   /**
    * Recherche touss les CederObjet ppur un couple de valeurs entité
    * et objetId.
    * @param entite Entite des CederObjets recherchés.
    * @param objetId Identifiant de l'obejt.
    * @param statut statut de l'oobjet cédé
    * @return Liste ordonnée de CederObjets.
    */
   List<CederObjet> findByEntiteObjetStatut(Entite entite, Integer objetId, ECederObjetStatut statut);

   /**
    * Recherche les CederObjets par objetId.
    * @param objetId Integer.
    * @return Liste des CederObjets.
    */
   List<CederObjet> findByObjetId(Integer objetId);

   /**
    * Recherche les CederObjets dont la cession et l'entité sont
    * passées en paramètres.
    * @param cession Cession des CederObjets recherchés.
    * @param entite Entite des CederObjets recherchés.
    * @return une liste de CederObjet.
    */
   List<CederObjet> findByCessionEntite(Cession cession, Entite entite);

   /**
    * Compte les echantillons ou dérivés impliqués dans la session.
    * @param cess
    * @param entite
    * @return le compte
    * @since 2.0.10
    */
   List<Long> findObjectsCessedCount(Cession cess, Entite entite);

   /**
    * Renvoies les codes des échantillons cédés pour une cession.
    * @param Cession cess
    * @return List<String> codes
    * @since 2.0.10
    */
   List<String> findCodesEchantillonByCession(Cession cess);

   /**
    * Renvoies les codes des dérivés cédés pour une cession.
    * @param Cession cess
    * @return List<String> codes
    * @since 2.0.10
    */
   List<String> findCodesDeriveByCession(Cession cess);

   /**
    * Compte les cession auxquelles a participé l'objet passé
    * passée en paramètre sous la forme id + entite
    * @param parent id Integer
    * @param Entite
    * @return long
    * @since 2.1.1
    */
   List<Long> findCountObjCession(Integer pId, Entite entite);
}
