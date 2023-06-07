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
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.aphp.tumorotek.dao.code.CodeUtilisateurDao;
import fr.aphp.tumorotek.dao.code.TableCodageDao;
import fr.aphp.tumorotek.manager.code.CodeCommonManager;
import fr.aphp.tumorotek.manager.code.CommonUtilsManager;
import fr.aphp.tumorotek.model.code.Adicap;
import fr.aphp.tumorotek.model.code.CimMaster;
import fr.aphp.tumorotek.model.code.CimoMorpho;
import fr.aphp.tumorotek.model.code.CodeCommon;
import fr.aphp.tumorotek.model.code.CodeUtilisateur;
import fr.aphp.tumorotek.model.code.TableCodage;

public class CommonUtilsManagerImpl implements CommonUtilsManager
{

   private final Logger log = LoggerFactory.getLogger(CodeCommonManager.class);

   private EntityManagerFactory entityManagerFactoryCodes;

   private CodeUtilisateurDao codeUtilisateurDao;

   private TableCodageDao tableCodageDao;

   public void setEntityManagerFactoryCodes(final EntityManagerFactory eCodes){
      this.entityManagerFactoryCodes = eCodes;
   }

   public void setCodeUtilisateurDao(final CodeUtilisateurDao cDao){
      this.codeUtilisateurDao = cDao;
   }

   public void setTableCodageDao(final TableCodageDao tDao){
      this.tableCodageDao = tDao;
   }

   @Override
   public CodeCommon findCodeByTableCodageAndIdManager(final Integer codeId, final TableCodage table){

      final List<CodeCommon> results = new ArrayList<>();

      if(table != null){
         log.debug("Recherche code correspondant au couple TableCodage : {} - CodeId : {}", table, codeId);
         String nomTable = table.getNom();
         String nomAttribut = null;
         if(table.getNom().equals("ADICAP")){
            nomTable = "Adicap";
            nomAttribut = "adicapId";
         }else if(table.getNom().equals("CIM_MASTER")){
            nomTable = "CimMaster";
            nomAttribut = "sid";
         }else if(table.getNom().equals("CIMO_MORPHO")){
            nomTable = "CimoMorpho";
            nomAttribut = "cimoMorphoId";
         }else if(table.getNom().equals("UTILISATEUR")){
            results.add(codeUtilisateurDao.findById(codeId));
         }

         if(nomAttribut != null){
            final StringBuffer sb = new StringBuffer();
            sb.append("SELECT c FROM ");
            sb.append(nomTable);
            sb.append(" c WHERE c.");
            sb.append(nomAttribut);
            sb.append(" = ");
            sb.append(codeId);

            final EntityManager em = entityManagerFactoryCodes.createEntityManager();
            final TypedQuery<CodeCommon> query = em.createQuery(sb.toString(), CodeCommon.class);
            results.addAll(query.getResultList());
         }
      }

      if(results.size() > 0){
         return results.get(0);
      }
      return null;
   }

   @Override
   public TableCodage getTableCodageFromCodeCommonManager(final CodeCommon code){
      TableCodage table = null;
      if(code instanceof Adicap){
         table = tableCodageDao.findByNom("ADICAP").get(0);
      }else if(code instanceof CimMaster){
         table = tableCodageDao.findByNom("CIM_MASTER").get(0);
      }else if(code instanceof CimoMorpho){
         table = tableCodageDao.findByNom("CIMO_MORPHO").get(0);
      }else if(code instanceof CodeUtilisateur){
         table = tableCodageDao.findByNom("UTILISATEUR").get(0);
      }
      return table;
   }

}
