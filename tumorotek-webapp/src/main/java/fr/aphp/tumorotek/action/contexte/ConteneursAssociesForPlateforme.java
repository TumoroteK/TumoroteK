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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.exception.ForbiddenI18nException;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.Conteneur;

/**
 * classe "Strategy" correspondant aux règles à appliquer pour la gestion des conteneurs
 * sur la Fiche Plateforme  
 * @author chuet
 *
 */
public class ConteneursAssociesForPlateforme implements ConteneursAssociesStrategy
{

   /**
    * Dans le cas de la fiche plateforme, seuls les conteneurs n'appartenant pas à la plateforme sont supprimables
    * @param plateforme, plateforme en cours de traitement
    * @param conteneur, conteneur pour lequel il faut déterminer si il est supprimable pour un traitement sur la plateforme passé en paramètre
    * @return true si supprimable, false sinon
    */
   public boolean returnIfConteneurIsSupprimable(Plateforme plateforme, Conteneur conteneur) {
      return !plateforme.equals(conteneur.getPlateformeOrig());
   }
   
   /**
    * retourne tous les conteneurs partagés avec la plateforme qu'ils soient déjà associés à la plateforme ou non
    */
   @Override
   public List<Conteneur> retrieveListAllConteneurAssociable(Plateforme plateforme) {
      //si la plateforme est en cours de création, il n'y a pas de conteneur qui ont pu lui être partagé par une autre plateforme
      if(plateforme == null || plateforme.getPlateformeId() == null) {
         return new ArrayList<Conteneur>();
      }

      return ManagerLocator.getConteneurManager().findAllPartagesManager(plateforme);
   }

   /**
    * Seuls les conteneurs à stocker dans CONTENEUR_PLATEFORME sont à retourner. Il s'agit des conteneurs partagés 
    * c'est-à-dire ceux dont le decorator est "supprimable"
    */
   @Override
   public List<Conteneur> retrieveListConteneurForAssociation(List<ConteneurDecorator> listConteneurDecoratorAffiche) {
      if(listConteneurDecoratorAffiche != null) {
         return listConteneurDecoratorAffiche.stream()
            .filter(conteneurDecorator -> conteneurDecorator.isSupprimable())
            .map(conteneurDecorator -> conteneurDecorator.getConteneur()).collect(Collectors.toList());
      }
      
      return new ArrayList<Conteneur>();
   }
   
   /**
    * regarde si le conteneur associé au conteneurDecorator passé en paramètre est rattaché à d'autres collections
    * de la plateforme du conteneurDecorator (plateforme sur laquelle l'utilisateur tente de supprimer le conteneur forcément partagé)
    * si oui une exception est lancée pour bloquer la suppression
    * sinon, ne fait rien
    */
   @Override
   public void doBeforeDelete(ConteneurDecorator conteneurDecorator) throws ForbiddenI18nException {
      
      Set<Banque> listBanqueDeLaPlateforme = ManagerLocator.getPlateformeManager().getBanquesManager(conteneurDecorator.getPlateforme());
      List<Conteneur> listConteneurUtiliseSurLaPlateforme = ManagerLocator.getConteneurManager().findByBanquesWithOrderManager(new ArrayList<>(listBanqueDeLaPlateforme));
      if(listConteneurUtiliseSurLaPlateforme.contains(conteneurDecorator.getConteneur())){
         throw new ForbiddenI18nException("plateforme.conteneur.remove.error");
      }
   }
}
