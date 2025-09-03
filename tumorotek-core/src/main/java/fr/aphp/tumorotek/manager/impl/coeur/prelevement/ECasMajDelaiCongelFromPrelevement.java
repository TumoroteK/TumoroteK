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
package fr.aphp.tumorotek.manager.impl.coeur.prelevement;

import fr.aphp.tumorotek.manager.impl.coeur.echantillon.ModaleMajDelaiCongelationConstants;
import fr.aphp.tumorotek.utils.Utils;

/**
 * cette enum définit les différents cas possibles suite à la modification par l'utilisateur de la date de prélèvement sur un prélèvement existant.
 * cela permet de simplifier la compréhension du code en le lieu à un cas fonctionnel précis
 * 
 * 1er cas : si une date avec heure existait et que la nouvelle valeur est non null et contient bien les heures (nécessaires pour le calcul du délai théorique) :
 *           - si il existe des échantillons avec un délai saisi manuellement et une date de stockage renseignée avec l'heure, on demande à l'utilisateur si il faut modifier la valeur pour mettre le nouveau délai théorique.
 *           - à noter que les échantillons avec le précédent délai théorique et ceux sans délai avec une date de stockage avec l'heure seront mis à jour automatiquement
 * 2e cas  : si la date n'existait pas (ou sans les heures) et qu'elle est ajoutée (modifiée) avec une valeur non null et avec les heures :
 *           - si il existe des échantillons avec un délai saisi manuellement (et une date de stockage avec l'heure), on demande à l'utilisateur si il faut modifier la valeur pour mettre le nouveau délai théorique.
 *           - à noter : 
 *                 - que les échantillons avec un délai inexistant (et une date de stockage avec l'heure) seront mis à jour automatiquement 
 *                 - qu'il ne peut pas y avoir d'échantillon avec un délai théorique
 * 3e cas  : si une date avec heure existait et qu'elle a été supprimée ou remplacée par une date sans heure :
 *           - si il existait des échantillons avec un délai théorique, on demande à l'utilisateur si il faut supprimer ces délais. Si il dit non le délai théorique deviendra un délai "saisi"
 *           - à noter qu'on ne fera rien pour les échantillons avec délai saisi manuellement
 * A noter que le 4e cas "la date n'existait pas (ou sans les heures) et qu'elle est ajoutée (modifiée) sans les heures" n'est pas à gérer
 * car aucun délai théorique n'est calculable (ni avant modif ni après modif) donc on ne touche pas à la saisie existante. 
 * 
 * @since 2.3.1.x (demande TK-427)
 * @author chuet
 *
 */
public enum ECasMajDelaiCongelFromPrelevement
{
   
   CAS1__DATES_AVANT_APRES_VALIDES(ModaleMajDelaiCongelationConstants.KEY_PREFIX_MSG__PRELEVEMENT__CONFIRMATION__ECRASER_AVEC_THEORIQUE),
   CAS2__DATE_AVANT_NON_VALIDE__DATE_APRES_VALIDE(ModaleMajDelaiCongelationConstants.KEY_PREFIX_MSG__PRELEVEMENT__CONFIRMATION__ECRASER_AVEC_THEORIQUE),
   CAS3__DATE_AVANT_VALIDE__DATE_APRES_NON_VALIDE(ModaleMajDelaiCongelationConstants.KEY_PREFIX_MSG__PRELEVEMENT__CONFIRMATION__SUPPRIMER_THEORIQUE);

   //préfixe de la clé du message internationalisé à afficher dans le cas où une confirmation est demandée à l'utilisateur
   //il sera complété par ".singulier" ou ".pluriel" selon le nombre d'échantillons concernés 
   private String keyPrefixI18nMessageForModifDatePrelevement;

   private ECasMajDelaiCongelFromPrelevement(String keyI18nMessageForModifDatePrelevement) {
      this.keyPrefixI18nMessageForModifDatePrelevement=keyI18nMessageForModifDatePrelevement;
   }

   /**
    * retourne la clé du message internationalisé à afficher en fonction du nombre d'échantillons concernés (ajout du suffixe ".singulier" ou ".pluriel"
    * @param nbEchantillon
    * @return la clé
    */
   public String retrieveKeyI18nMessageForModifDatePrelevement(int nbEchantillon){
      return Utils.manageLibelleAuSingulierOuPluriel(keyPrefixI18nMessageForModifDatePrelevement, nbEchantillon);
   }
}
