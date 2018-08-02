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
package fr.aphp.tumorotek.manager.validation.coeur.cession;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe transporteur des codes relevés en erreur lors de la demande de
 * création/validation large cession depuis fichier Excel.
 * Les incohérences suivantes sont relevées:
 *  - codes non trouvés
 *  - les codes correspodant à un objet dont le statut de stockage est incompatible avec la cession
 *  - les codes à un objet dont le statut de stockage est incompatible avec la de depart de la cession
 *  - les codes à un objet pour lequel un évènement de stockage est incompatible avec la de depart de la cession
 * Date: 15/11/2016
 *
 * @author Mathieu BARTHELEMY
 * @version 2.1
 *
 */
public class LargeCessionHelper
{

   Integer entiteId;
   List<Integer> idsFound = new ArrayList<>();
   List<String> codesNotFound = new ArrayList<>();
   List<String> objsNonStockes = new ArrayList<>();
   List<String> dateStockIncompatible = new ArrayList<>();
   List<String> evtsStockIncompatible = new ArrayList<>();

   public LargeCessionHelper(final Integer _i){
      entiteId = _i;
   }

   public boolean containsErrs(){
      return !codesNotFound.isEmpty() || !objsNonStockes.isEmpty() || !dateStockIncompatible.isEmpty()
         || !evtsStockIncompatible.isEmpty();
   }

   public Integer getEntiteId(){
      return entiteId;
   }

   public void setEntiteId(final Integer _i){
      this.entiteId = _i;
   }

   public List<Integer> getIdsFound(){
      return idsFound;
   }

   public void setIdsFound(final List<Integer> _i){
      this.idsFound = _i;
   }

   public List<String> getCodesNotFound(){
      return codesNotFound;
   }

   public void setCodesNotFound(final List<String> _c){
      this.codesNotFound = _c;
   }

   public List<String> getObjsNonStockes(){
      return objsNonStockes;
   }

   public void setObjsNonStockes(final List<String> _o){
      this.objsNonStockes = _o;
   }

   public List<String> getDateStockIncompatible(){
      return dateStockIncompatible;
   }

   public void setDateStockIncompatible(final List<String> _d){
      this.dateStockIncompatible = _d;
   }

   public List<String> getEvtsStockIncompatible(){
      return evtsStockIncompatible;
   }

   public void setEvtsStockIncompatible(final List<String> _e){
      this.evtsStockIncompatible = _e;
   }

}
