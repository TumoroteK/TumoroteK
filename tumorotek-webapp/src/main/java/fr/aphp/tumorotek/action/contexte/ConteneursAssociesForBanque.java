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
import java.util.stream.Collectors;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Messagebox;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.exception.ForbiddenI18nException;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.stockage.Conteneur;

/**
 * classe "Strategy" correspondant aux règles à appliquer pour la gestion des conteneurs
 * sur la Fiche Banque (Fiche Collection)  
 * @author chuet
 *
 */
public class ConteneursAssociesForBanque implements ConteneursAssociesStrategy
{
  
   /**
    * Dans le cas de la fiche banque, tous les conteneurs peuvent êtres détachés de la banque
    * @param plateforme, correspond à la plateforme de la banque en cours de traitement
    * @param conteneur
    * @return true systématiquement
    */
   public boolean returnIfConteneurIsSupprimable(Plateforme plateforme, Conteneur conteneur) {
      return true;
   }
   
   /**
    * renvoie les conteneurs pouvant être ajoutés. Ils correspondent à :
    *    - tous les conteneurs de la plateforme passée en paramètre
    *    - ainsi que tous les conteneurs mis à disposition de la plateforme passée en paramètre et acceptés par celle-ci (rattachement fait au niveau de la plateforme) 
    *    - à l'exception de ceux déjà associés à la plateforme passée en paramètre
    * @param plateforme, correspond à la plateforme de la banque en cours de traitement
    * @param listConteneurDejaAssocie conteneur à exclure du résultat
    * @return une liste de conteneurs
    */
   @Override
   public List<Conteneur> retrieveListAllConteneurAssociable(Plateforme plateforme) {
      List<Conteneur> listConteneurAjoutable = new ArrayList<Conteneur>();
      listConteneurAjoutable.addAll(ManagerLocator.getConteneurManager().findByPlateformeOrigWithOrderManager(plateforme));
      listConteneurAjoutable.addAll(ManagerLocator.getConteneurManager().findByPartageManager(plateforme, true));
      
      return listConteneurAjoutable;
   }
   
   /**
    * tous les conteneurs des conteneurDecorator passés en paramètre sont à retourner pour alimenter la table CONTENEUR_BANQUE
    */
   @Override
   public List<Conteneur> retrieveListConteneurForAssociation(List<ConteneurDecorator> listConteneurDecoratorAffiche) {
      if(listConteneurDecoratorAffiche != null) {
         return listConteneurDecoratorAffiche.stream().map(conteneurDecorator -> conteneurDecorator.getConteneur()).collect(Collectors.toList());
      }
      
      return new ArrayList<Conteneur>();
   }

   /**
    * Affichage systématique d'un warning avant de faire la suppression
    */
   @Override
   public void doBeforeDelete(ConteneurDecorator conteneurDecorator) throws ForbiddenI18nException {
      Messagebox.show(Labels.getLabel("banque.conteneur.remove.warning"), Labels.getLabel("general.warning"), Messagebox.OK,
         Messagebox.EXCLAMATION);
   }
}
