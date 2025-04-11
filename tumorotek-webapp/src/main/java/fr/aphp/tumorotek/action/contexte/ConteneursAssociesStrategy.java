/**
 * Copyright ou © ou Copr. SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.action.contexte;

import java.util.List;

import fr.aphp.tumorotek.action.exception.ForbiddenI18nException;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.Conteneur;

/**
 * interface du pattern "Strategy" permettant de définir les règles à appliquer pour la gestion des conteneurs
 * selon les cas d'utilisation 
 * @author chuet
 *
 */
public interface ConteneursAssociesStrategy
{
   /**
    * indique si un conteneur passé en paramètre peut être supprimé dans un cas d'utilisation lié à la plateforme passée en paramètre
    * @param plateforme du cas d'utilisation
    * @param conteneur à traiter
    * @return true si supprimable, false sinon
    */
   boolean returnIfConteneurIsSupprimable(Plateforme plateforme, Conteneur conteneur);
   
   /**
    * renvoie la liste de tous les conteneurs pouvant être associés qu'ils le soient déjà ou non
    * @param plateforme du cas d'utilisation
    * @return une liste de conteneurs
    */
   List<Conteneur> retrieveListAllConteneurAssociable(Plateforme plateforme);
   
   /**
    * Renvoie la liste des conteneurs à associer à l'entité en cours de traitement (table CONTENEUR_XXX)
    * Généralement, tous les conteneurs affichés à l'écran sont à prendre en compte (cas de la fiche banque). La méthode extrait alors juste les conteneurs des conteneurDecorator
    * Mais dans certains cas, certains conteneurs affichés sont "intrinsèques" donc ils ne sont pas à retourner dans ce cas (cf cas de la fiche plateforme)
    * @param listConteneurDecoratorAffiche
    * @return la liste des conteneurs 
    */
   List<Conteneur> retrieveListConteneurForAssociation(List<ConteneurDecorator> listConteneurDecoratorAffiche);
   
   /**
    * méthode à utiliser pour faire un traitement avant de supprimer un conteneur
    * @param conteneurDecorator conteneurDecorator du conteneur à supprimer
    * @throws ForbiddenI18nException exception à renvoyer pour bloquer la suppression
    */
   void doBeforeDelete(ConteneurDecorator conteneurDecorator) throws ForbiddenI18nException;
      
}
