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
package fr.aphp.tumorotek.manager.impl.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.aphp.tumorotek.dao.coeur.cession.CederObjetDao;
import fr.aphp.tumorotek.dao.coeur.prodderive.ProdDeriveDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dto.EchantillonDTO;
import fr.aphp.tumorotek.manager.code.CodeAssigneManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.dto.EchantillonDTOManager;
import fr.aphp.tumorotek.manager.qualite.OperationManager;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;

public class EchantillonDTOManagerImpl implements EchantillonDTOManager
{

   private ProdDeriveDao prodDeriveDao;
   private CederObjetDao cederObjetDao;
   private EntiteDao entiteDao;
   private CodeAssigneManager codeAssigneManager;
   private OperationManager operationManager;
   private EchantillonManager echantillonManager;
   private ConteneurManager conteneurManager;

   public void setProdDeriveDao(final ProdDeriveDao _p){
      this.prodDeriveDao = _p;
   }

   public void setCederObjetDao(final CederObjetDao _c){
      this.cederObjetDao = _c;
   }

   public void setEntiteDao(final EntiteDao _e){
      this.entiteDao = _e;
   }

   public void setCodeAssigneManager(final CodeAssigneManager _c){
      this.codeAssigneManager = _c;
   }

   public void setOperationManager(final OperationManager _o){
      this.operationManager = _o;
   }

   public void setEchantillonManager(final EchantillonManager _e){
      this.echantillonManager = _e;
   }

   public void setConteneurManager(final ConteneurManager _c){
      this.conteneurManager = _c;
   }

   @Override
   public EchantillonDTO initEchantillonDecoratorManager(final Echantillon e){
      if(e != null){
         final EchantillonDTO dto = new EchantillonDTO(e);

         if(e.getEchantillonId() != null){

            dto.setNbDerives(prodDeriveDao.findCountByParent(e.getEchantillonId(), 
            		entiteDao.findById(3).orElse(null)).get(0).intValue());
            dto.setNbCessions(cederObjetDao.findCountObjCession(e.getEchantillonId(), 
            		entiteDao.findById(3).orElse(null)).get(0).intValue());
            dto.setDateCreation(operationManager.findDateCreationManager(e));
            dto.setEmplacementAdrl(echantillonManager.getEmplacementAdrlManager(e));
            dto.setTempStock(conteneurManager.findTempForEmplacementManager(e.getEmplacement()));

            dto.setCodesOrgsToCreateOrEdit(codeAssigneManager.findCodesOrganeByEchantillonManager(e));

            dto.setCodesLesToCreateOrEdit(codeAssigneManager.findCodesMorphoByEchantillonManager(e));

         }

         return dto;
      }
      return null;
   }

   @Override
   public List<EchantillonDTO> decorateListeManager(final List<Echantillon> echans){
      final List<EchantillonDTO> liste = new ArrayList<>();
      final Iterator<Echantillon> it = echans.iterator();
      while(it.hasNext()){
         liste.add(initEchantillonDecoratorManager(it.next()));
      }
      return liste;
   }

   @Override
   public List<Echantillon> extractListeManager(final List<EchantillonDTO> decos){
      final List<Echantillon> liste = new ArrayList<>();
      final Iterator<EchantillonDTO> it = decos.iterator();
      while(it.hasNext()){
         liste.add(it.next().getEchantillon());
      }
      return liste;
   }
}
