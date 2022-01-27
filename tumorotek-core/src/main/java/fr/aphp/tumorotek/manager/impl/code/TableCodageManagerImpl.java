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
package fr.aphp.tumorotek.manager.impl.code;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.IterableUtils;

import fr.aphp.tumorotek.dao.code.TableCodageDao;
import fr.aphp.tumorotek.manager.code.AdicapManager;
import fr.aphp.tumorotek.manager.code.CimMasterManager;
import fr.aphp.tumorotek.manager.code.CimoMorphoManager;
import fr.aphp.tumorotek.manager.code.CodeUtilisateurManager;
import fr.aphp.tumorotek.manager.code.TableCodageManager;
import fr.aphp.tumorotek.model.code.Adicap;
import fr.aphp.tumorotek.model.code.CimMaster;
import fr.aphp.tumorotek.model.code.CimoMorpho;
import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.code.CodeUtilisateur;
import fr.aphp.tumorotek.model.code.TableCodage;
import fr.aphp.tumorotek.model.contexte.Banque;

/**
 *
 * Implémentation du manager du bean de domaine TableCodage.
 * Date: 19/05/2010.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class TableCodageManagerImpl // implements TableCodageManager
{

   private TableCodageDao tableCodageDao;
   private AdicapManager adicapManager;
   private CimMasterManager cimMasterManager;
   private CimoMorphoManager cimoMorphoManager;
   private CodeUtilisateurManager codeUtilisateurManager;

   public void setTableCodageDao(final TableCodageDao tDao){
      this.tableCodageDao = tDao;
   }

   public void setAdicapManager(final AdicapManager aManager){
      this.adicapManager = aManager;
   }

   public void setCimMasterManager(final CimMasterManager cManager){
      this.cimMasterManager = cManager;
   }

   public void setCimoMorphoManager(final CimoMorphoManager cManager){
      this.cimoMorphoManager = cManager;
   }

   public void setCodeUtilisateurManager(final CodeUtilisateurManager cuManager){
      this.codeUtilisateurManager = cuManager;
   }

   @Override
   public List<TableCodage> findAllObjectsManager(){
      return IterableUtils.toList(tableCodageDao.findAll());
   }

   @Override
   public List<TableCodage> findByNomManager(final String nom){
      return tableCodageDao.findByNom(nom);
   }

   //	
   //	@Override
   //	public CodeCommon findCodeByTableCodageAndId(Integer codeId,
   //														TableCodage table) {
   //		
   //		List<CodeCommon> results  = new ArrayList<CodeCommon>();
   //		
   //		if (table != null) {
   //			log.debug("Recherche code correspondant au couple TableCodage : " 
   //					+ table.toString() + " - CodeId : " + codeId);
   //			
   //			String nomTable = table.getNom();
   //			String nomAttribut = null;
   //			if (table.getNom().equals("ADICAP")) {
   //				nomTable = "Adicap";
   //				nomAttribut = "adicapId";
   //			} else if (table.getNom().equals("CIM_MASTER")) {
   //				nomTable = "CimMaster";
   //				nomAttribut = "sid";
   //			} else if (table.getNom().equals("CIMO_MORPHO")) {
   //				nomTable = "CimoMorpho";
   //				nomAttribut = "cimoMorphoId";
   //			} else if (table.getNom().equals("UTILISATEUR")) {
   //				results.add(codeUtilisateurManager.findByIdManager(codeId));
   //			} 
   //			
   //			if (nomAttribut != null) {
   //				StringBuffer sb = new StringBuffer();
   //				sb.append("SELECT c FROM ");
   //				sb.append(nomTable);
   //				sb.append(" c WHERE c.");
   //				sb.append(nomAttribut);
   //				sb.append(" = ");
   //				sb.append(codeId);
   //				
   //				EntityManager em = entityManagerFactoryCodes
   //												.createEntityManager();
   //				Query query = em.createQuery(sb.toString());
   //				results.addAll((List<CodeCommon>) query.getResultList());
   //			}
   //		}
   //	 
   //		if (results.size() > 0) {
   //			return results.get(0);
   //		} else {
   //			return null;
   //		}
   //	}
   //
   //	@Override
   //	public TableCodage getTableCodageFromCodeCommon(CodeCommon code) {
   //		TableCodage table = null;
   //		if (code instanceof Adicap) {
   //			table = findByNomManager("ADICAP").get(0);
   //		} else if (code instanceof CimMaster) {
   //			table = findByNomManager("CIM_MASTER").get(0);
   //		} else if (code instanceof CimoMorpho) {
   //			table = findByNomManager("CIMO_MORPHO").get(0);
   //		} else if (code instanceof CodeUtilisateur) {
   //			table = findByNomManager("UTILISATEUR").get(0);
   //		}
   //		return table;
   //	}

   //	@Override
   //	public Set<Banque> getBanquesManager(TableCodage tab) {
   //		Set<Banque> banks = new HashSet<Banque>();
   //		if (tab != null) {
   //			TableCodage table = tableCodageDao.save(tab);
   //			banks = table.getBanques();
   //			banks.isEmpty(); // operation empechant LazyInitialisationException
   //		}
   //		return banks;
   //	}

   @Override
   public List<CodeCommon> transcodeManager(final CodeCommon code, final List<TableCodage> tables, final List<Banque> banks){
      final List<CodeCommon> resTrans = new ArrayList<>();
      if(code != null && tables != null){

         final boolean containsAdicap = tables.contains(tableCodageDao.findByNom("ADICAP").get(0));
         final boolean containsCim = tables.contains(tableCodageDao.findByNom("CIM_MASTER").get(0));
         final boolean containsCimo = tables.contains(tableCodageDao.findByNom("CIMO_MORPHO").get(0));
         final boolean containsUser = tables.contains(tableCodageDao.findByNom("UTILISATEUR").get(0));

         if(code instanceof Adicap){
            if(containsCim){
               resTrans.addAll(adicapManager.getCimMastersManager((Adicap) code));
            }
            if(containsCimo){
               resTrans.addAll(adicapManager.getCimoMorphosManager((Adicap) code));
            }
            if(containsUser){
               resTrans.addAll(codeUtilisateurManager.findByTranscodageManager(code, banks));
            }
         }else if(code instanceof CimMaster){
            if(containsAdicap){
               resTrans.addAll(cimMasterManager.getAdicapsManager((CimMaster) code));
            }
            if(containsUser){
               resTrans.addAll(codeUtilisateurManager.findByTranscodageManager(code, banks));
            }
         }else if(code instanceof CimoMorpho){
            if(containsAdicap){
               resTrans.addAll(cimoMorphoManager.getAdicapsManager((CimoMorpho) code));
            }
            if(containsUser){
               resTrans.addAll(codeUtilisateurManager.findByTranscodageManager(code, banks));
            }
         }else if(code instanceof CodeUtilisateur){
            resTrans.addAll(codeUtilisateurManager.getTranscodesManager((CodeUtilisateur) code, tables));
         }
      }
      return resTrans;
   }

   
   @Override
   public List<CodeCommon> findCodesAndTranscodesFromStringManager(final String codeorLib, final List<TableCodage> tables,
      final List<Banque> banks, final boolean exactMatch){

      final Set<CodeCommon> codes = new LinkedHashSet<>();

      final List<CodeCommon> res = new ArrayList<>();
      final List<CodeCommon> trans = new ArrayList<>();
      if(tables != null){
         for(int i = 0; i < tables.size(); i++){

            res.clear();
            trans.clear();

            if("ADICAP".equals(tables.get(i).getNom())){
               res.addAll(adicapManager.findByCodeLikeManager(codeorLib, exactMatch));
               res.addAll(adicapManager.findByLibelleLikeManager(codeorLib, exactMatch));
            }else if("CIM_MASTER".equals(tables.get(i).getNom())){
               res.addAll(cimMasterManager.findByCodeLikeManager(codeorLib, exactMatch));
               res.addAll(cimMasterManager.findByLibelleLikeManager(codeorLib, exactMatch));
            }else if("CIMO_MORPHO".equals(tables.get(i).getNom())){
               res.addAll(cimoMorphoManager.findByCodeLikeManager(codeorLib, exactMatch));
               res.addAll(cimoMorphoManager.findByLibelleLikeManager(codeorLib, exactMatch));
            }else if("UTILISATEUR".equals(tables.get(i).getNom())){
               res.addAll(codeUtilisateurManager.findByCodeLikeManager(codeorLib, exactMatch, banks));
               res.addAll(codeUtilisateurManager.findByLibelleLikeManager(codeorLib, exactMatch, banks));
            }

            // cherche transcodes dans autre tables
            final Iterator<CodeCommon> resIt = res.iterator();
            CodeCommon next;
            while(resIt.hasNext()){
               next = resIt.next();
               trans.addAll(transcodeManager(next, tables, banks));
            }

            codes.addAll(res);
            codes.addAll(trans);
         }
      }

      return new ArrayList<>(codes);
   }

   /**
    * Transforme une liste de CodeCommon en la liste de codes équivalente.
    * @param codes
    * @return liste de codes String
    */
   @Override
   public List<String> getListCodesFromCodeCommon(final List<CodeCommon> codes){
      final List<String> codesStr = new ArrayList<>();
      if(codes != null){
         for(int i = 0; i < codes.size(); i++){
            if(codes.get(i).getCode() != null){
               codesStr.add(codes.get(i).getCode());
            }
         }
      }
      return codesStr;
   }

   /**
    * Transforme une liste de CodeCommon en la liste de libelles équivalente.
    * @param codes
    * @return liste de libelles String
    */
   @Override
   public List<String> getListLibellesFromCodeCommon(final List<CodeCommon> codes){
      final List<String> libellesStr = new ArrayList<>();
      if(codes != null){
         for(int i = 0; i < codes.size(); i++){
            if(codes.get(i).getLibelle() != null){
               libellesStr.add(codes.get(i).getLibelle());
            }
         }
      }
      return libellesStr;
   }

}
