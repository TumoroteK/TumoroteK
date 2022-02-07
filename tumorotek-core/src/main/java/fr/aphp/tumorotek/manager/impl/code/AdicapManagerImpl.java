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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.dao.code.AdicapDao;
import fr.aphp.tumorotek.dao.code.AdicapGroupeDao;
import fr.aphp.tumorotek.manager.code.AdicapManager;
import fr.aphp.tumorotek.model.code.Adicap;
import fr.aphp.tumorotek.model.code.AdicapGroupe;
import fr.aphp.tumorotek.model.code.CimMaster;
import fr.aphp.tumorotek.model.code.CimoMorpho;

/**
 *
 * Implémentation du manager du bean de domaine Adicap.
 * Date: 19/05/2010.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class AdicapManagerImpl implements AdicapManager
{

   private final Log log = LogFactory.getLog(AdicapManager.class);

   private AdicapDao adicapDao;
   private AdicapGroupeDao adicapGroupeDao;

   public void setAdicapDao(final AdicapDao aDao){
      this.adicapDao = aDao;
   }

   public void setAdicapGroupeDao(final AdicapGroupeDao agDao){
      this.adicapGroupeDao = agDao;
   }

   @Override
   public List<Adicap> findAllObjectsManager(){
      return IterableUtils.toList(adicapDao.findAll());
   }

   @Override
   public List<Adicap> findByCodeLikeManager(String code, final boolean exactMatch){
      if(!exactMatch){
         code = "%" + code + "%";
      }
      log.debug("Recherche Adicap par code: " + code + " exactMatch " + String.valueOf(exactMatch));
      return adicapDao.findByCodeLike(code);
   }

   @Override
   public List<Adicap> findByAdicapGroupeManager(AdicapGroupe groupe){

      // si dictionnaire = 8 ou 6, equivalent 3
      if(groupe != null && (groupe.getNom().equals("D8") || groupe.getNom().equals("D6"))){
         groupe = adicapGroupeDao.findById(3).orElse(null);
      }

      return adicapDao.findByAdicapGroupeNullParent(groupe);
   }

   @Override
   public List<Adicap> findByLibelleLikeManager(String libelle, final boolean exactMatch){
      if(!exactMatch){
         libelle = "%" + libelle + "%";
      }
      log.info("Recherche Adicap par libelle: " + libelle + " exactMatch " + String.valueOf(exactMatch));
      return adicapDao.findByLibelleLike(libelle);
   }

   @Override
   public List<Adicap> findByMorphoManager(final Boolean isMorpho){
      return adicapDao.findByMorpho(isMorpho);
   }

   @Override
   public List<Adicap> findByAdicapParentManager(final Adicap parent, final Boolean isTopo){

      final List<Adicap> ads = adicapDao.findByAdicapParentAndCodeOrLibelle(parent, "%");

      if(isTopo != null){
         // affiche les codes topo epurés des codes lesionnels
         if(isTopo){
            for(int i = 0; i < ads.size(); i++){
               if(ads.get(i).getAdicapGroupe().getNom().equals("D6")){
                  ads.remove(ads.get(i));
                  i--;
               }
            }
         }else{
            // affiche les codes lesionnels dans l'aroborescence des 
            // codes topos.
            for(int i = 0; i < ads.size(); i++){
               if(ads.get(i).getAdicapGroupe().getNom().equals("D8") || (ads.get(i).getAdicapGroupe().getNom().equals("D3")
                  && findByAdicapParentManager(ads.get(i), false).isEmpty())){
                  ads.remove(ads.get(i));
                  i--;
               }
            }
         }
      }

      return ads;
   }

   @Override
   public Set<CimMaster> getCimMastersManager(final Adicap adicap){
      Set<CimMaster> cims = new HashSet<>();
      final Adicap aM = adicapDao.save(adicap);
      cims = aM.getCimMasters();
      cims.size(); // operation empechant LazyInitialisationException
      return cims;
   }

   @Override
   public Set<CimoMorpho> getCimoMorphosManager(final Adicap adicap){
      Set<CimoMorpho> cims = new HashSet<>();
      final Adicap aM = adicapDao.save(adicap);
      cims = aM.getCimoMorphos();
      cims.isEmpty(); // operation empechant LazyInitialisationException
      return cims;
   }

   @Override
   public List<AdicapGroupe> findDictionnairesManager(){
      return adicapGroupeDao.findDictionnaires();
   }

   @Override
   public List<AdicapGroupe> getAdicapGroupesManager(final AdicapGroupe groupe){
      final List<AdicapGroupe> groupes = new ArrayList<>();
      final AdicapGroupe aG = adicapGroupeDao.save(groupe);
      groupes.addAll(aG.getAdicapGroupes());
      groupes.isEmpty(); // operation empechant LazyInitialisationException
      return groupes;
   }

   @Override
   public List<Adicap> findChildrenCodesManager(final Adicap code, final AdicapGroupe groupe, final String codeOrLibelle){
      final List<Adicap> codes = new ArrayList<>();
      if(code != null){
         codes.add(code);
         findRecursiveChildren(code, codes, codeOrLibelle);
      }else if(groupe != null){
         final List<AdicapGroupe> grps = new ArrayList<>();
         grps.add(groupe);
         findRecursiveGroup(groupe, grps);
         List<Adicap> grpCodes;
         for(int i = 0; i < grps.size(); i++){
            grpCodes = adicapDao.findByAdicapGroupeAndCodeOrLibelle(grps.get(i), codeOrLibelle);
            for(int j = 0; j < grpCodes.size(); j++){
               codes.add(grpCodes.get(j));
               findRecursiveChildren(grpCodes.get(j), codes, codeOrLibelle);
            }

         }
      }
      return codes;
   }

   private void findRecursiveChildren(final Adicap parent, final List<Adicap> codes, final String codeOrLibelle){
      final List<Adicap> children = adicapDao.findByAdicapParentAndCodeOrLibelle(parent, codeOrLibelle);
      codes.addAll(children);
      for(int i = 0; i < children.size(); i++){
         findRecursiveChildren(children.get(i), codes, codeOrLibelle);
      }
   }

   private void findRecursiveGroup(final AdicapGroupe groupe, final List<AdicapGroupe> grps){
      final List<AdicapGroupe> groups = getAdicapGroupesManager(groupe);
      grps.addAll(groups);
      for(int i = 0; i < groups.size(); i++){
         findRecursiveGroup(groups.get(i), grps);
      }
   }

   @Override
   public List<Adicap> findByDicoAndCodeOrLibelleManager(final AdicapGroupe grp, String codeOrLibelle, final boolean exactMatch){
      // Set<Adicap> res = new HashSet<Adicap>();
      if(!exactMatch){
         // codeOrLibelle = ".*" + codeOrLibelle + ".*";
         codeOrLibelle = "%" + codeOrLibelle + "%";
      }
      //Iterator<Adicap> aIt = 
      return findChildrenCodesManager(null, grp, codeOrLibelle);
      // .iterator();
      //		Adicap next;
      //		while (aIt.hasNext()) {
      //			next = aIt.next();
      //			if (next.getCode().matches(codeOrLibelle)
      //				|| (next.getLibelle() != null 
      //							&& next.getLibelle().matches(codeOrLibelle))) {
      //				res.add(next);		
      //			}
      //		}
      //		return new ArrayList<Adicap>(res);
   }
}
