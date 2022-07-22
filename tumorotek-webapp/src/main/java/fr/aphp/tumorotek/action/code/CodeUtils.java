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
package fr.aphp.tumorotek.action.code;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.code.AdicapGroupe;
import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 * Utility class contenant les méthodes appelées dans la recherche des
 * codes à partir des classifications
 * Date: 14/05/2013
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0.10
 *
 */
public class CodeUtils
{

   /**
    * Lance la requete de codes sur la codification choisie, avec un critère
    * portant sur code et libelle et exactMatch ou non.
    * Peuple la liste membre 'res'.
    * @param codeOrLibelle valeur à rechercher
    * @param isOrgane
    * @param isMorpho
    * @param res List<CodeCommon>
    * @param table
    * @param exactMact
    * @param banques accessibles (ne concerne que les recherches portant sur
    * la classification UTILISATEUR)
    */
   public static void findCodes(final String codeOrLibelle, final boolean isOrgane, final boolean isMorpho,
      final List<CodeCommon> res, final TableCodage table, final boolean exactMatch, final List<Banque> banks){
      res.clear();
      if("ADICAP".equals(table.getNom())){
         if(isOrgane || isMorpho){
            final List<AdicapGroupe> grps = new ArrayList<>();
            if(isOrgane){
               grps.add(ManagerLocator.getAdicapManager().findDictionnairesManager().get(2));
            }else if(isMorpho){
               grps.add(ManagerLocator.getAdicapManager().findDictionnairesManager().get(3));
               grps.add(ManagerLocator.getAdicapManager().findDictionnairesManager().get(4));
               grps.add(ManagerLocator.getAdicapManager().findDictionnairesManager().get(5));
               grps.add(ManagerLocator.getAdicapManager().findDictionnairesManager().get(6));
            }
            final Iterator<AdicapGroupe> grpsIt = grps.iterator();
            while(grpsIt.hasNext()){
               res.addAll(
                  ManagerLocator.getAdicapManager().findByDicoAndCodeOrLibelleManager(grpsIt.next(), codeOrLibelle, exactMatch));
            }

         }else{
            res.addAll(ManagerLocator.getAdicapManager().findByCodeLikeManager(codeOrLibelle, exactMatch));
            res.addAll(ManagerLocator.getAdicapManager().findByLibelleLikeManager(codeOrLibelle, exactMatch));
         }
      }else if("CIM_MASTER".equals(table.getNom())){
         res.addAll(ManagerLocator.getCimMasterManager().findByCodeLikeManager(codeOrLibelle, exactMatch));
         res.addAll(ManagerLocator.getCimMasterManager().findByLibelleLikeManager(codeOrLibelle, exactMatch));
      }else if("CIMO_MORPHO".equals(table.getNom())){
         res.addAll(ManagerLocator.getCimoMorphoManager().findByCodeLikeManager(codeOrLibelle, exactMatch));
         res.addAll(ManagerLocator.getCimoMorphoManager().findByLibelleLikeManager(codeOrLibelle, exactMatch));
      }else if("UTILISATEUR".equals(table.getNom())){
         res.addAll(ManagerLocator.getCodeUtilisateurManager().findByCodeLikeManager(codeOrLibelle, exactMatch, banks));
         res.addAll(ManagerLocator.getCodeUtilisateurManager().findByLibelleLikeManager(codeOrLibelle, exactMatch, banks));
      }else if("FAVORIS".equals(table.getNom())){
         res.addAll(
            ManagerLocator.getCodeSelectManager().findByCodeOrLibelleLikeManager(codeOrLibelle, exactMatch, null, banks.get(0)));
      }
   }

   public static void findCodesInAllTables(final String codeOrLibelle, final boolean isOrgane, final boolean isMorpho,
      final Set<CodeCommon> allCodes, final boolean exactMatch, final List<Banque> banks){

      allCodes.clear();

      final List<TableCodage> tables = ManagerLocator.getTableCodageManager().findAllObjectsManager();

      final List<CodeCommon> res = new ArrayList<>();

      for(final TableCodage tb : tables){
         if(!tb.getNom().equals("FAVORIS")){
            findCodes(codeOrLibelle, isOrgane, isMorpho, res, tb, exactMatch, banks);
            allCodes.addAll(res);
         }
      }
   }
}
