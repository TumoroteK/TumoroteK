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
package fr.aphp.tumorotek.manager.impl.interfacage.scan;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.aphp.tumorotek.dao.interfacage.scan.ScanDeviceDao;
import fr.aphp.tumorotek.dao.interfacage.scan.ScanTerminaleDao;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.exception.interfacage.scan.ScannedTerminaleNotUniqueException;
import fr.aphp.tumorotek.manager.exception.interfacage.scan.ScannedTerminaleOverSizeException;
import fr.aphp.tumorotek.manager.interfacage.scan.ScanTerminaleManager;
import fr.aphp.tumorotek.manager.interfacage.scan.TKScanTerminaleDTO;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.manager.stockage.TerminaleManager;
import fr.aphp.tumorotek.model.TKStockableObject;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.interfacage.scan.ScanDevice;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTerminale;
import fr.aphp.tumorotek.model.interfacage.scan.ScanTube;
import fr.aphp.tumorotek.model.stockage.Conteneur;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.stockage.Enceinte;
import fr.aphp.tumorotek.model.stockage.Terminale;

/**
 *
 * Interface pour le manager du bean de domaine ScanTerminale.
 * Interface créée le 26/04/2016.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.1
 *
 */
public class ScanTerminaleManagerImpl implements ScanTerminaleManager
{

   private final Log log = LogFactory.getLog(ScanTerminaleManager.class);

   private ScanTerminaleDao scanTerminaleDao;
   private ScanDeviceDao scanDeviceDao;
   private EchantillonManager echantillonManager;
   private ProdDeriveManager prodDeriveManager;
   private TerminaleManager terminaleManager;
   private EmplacementManager emplacementManager;

   public void setScanTerminaleDao(final ScanTerminaleDao _s){
      this.scanTerminaleDao = _s;
   }

   public void setScanDeviceDao(final ScanDeviceDao _s){
      this.scanDeviceDao = _s;
   }

   public void setEchantillonManager(final EchantillonManager _e){
      this.echantillonManager = _e;
   }

   public void setProdDeriveManager(final ProdDeriveManager _p){
      this.prodDeriveManager = _p;
   }

   public void setTerminaleManager(final TerminaleManager _t){
      this.terminaleManager = _t;
   }

   public void setEmplacementManager(final EmplacementManager _e){
      this.emplacementManager = _e;
   }

   @Override
   public void createObjectManager(final ScanTerminale sT, ScanDevice sD){
      if(sT != null){
         // test only
         sD = scanDeviceDao.findById(1).orElse(null);
         sT.setScanDevice(sD);
         scanTerminaleDao.save(sT);
         log.debug("ScanTerminale creation: " + sT.getName());
      }
   }

   @Override
   public void removeObjectManager(final ScanTerminale sT){
      if(sT != null){
         scanTerminaleDao.deleteById(sT.getScanTerminaleId());
      }
   }

   @Override
   public List<ScanTerminale> findAllManager(){
      return IterableUtils.toList(scanTerminaleDao.findAll());
   }

   @Override
   public List<ScanTerminale> findByDeviceManager(ScanDevice sD){
      if(sD == null){
         // test only
         sD = scanDeviceDao.findById(1).orElse(null);
      }
      return scanTerminaleDao.findByScanDevice(sD);
   }

   @Override
   public List<String> findTKObjectCodesManager(final ScanTerminale sT){
      return scanTerminaleDao.findTKObjectCodes(sT);
   }

   @Override
   public List<TKStockableObject> findTKStockableObjectsManager(final ScanTerminale sT, final List<Banque> banques){
      final List<TKStockableObject> objs = new ArrayList<>();
      final List<String> codes = scanTerminaleDao.findTKObjectCodes(sT);
      final List<String> notfounds = new ArrayList<>();
      // echantillons
      objs.addAll(
         echantillonManager.findByIdsInListManager(echantillonManager.findByCodeInListManager(codes, banques, notfounds)));
      // derives
      objs.addAll(prodDeriveManager.findByIdsInListManager(prodDeriveManager.findByCodeInListManager(codes, banques, notfounds)));

      return objs;
   }

   @Override
   public Terminale findScannedTerminaleManager(final ScanTerminale sT, final Enceinte enc, final List<Conteneur> conts){

      Terminale found = null;

      if(sT != null){
         final List<Integer> termIds = terminaleManager.findTerminaleIdsFromNomManager(sT.getName(), enc, conts);
         // error si plusieurs boites
         if(termIds.size() > 1){
            final List<String> termsArdls = new ArrayList<>();
            Terminale term;
            String adrl;
            for(final Integer id : termIds){
               term = terminaleManager.findByIdManager(id);
               adrl = emplacementManager.getTerminaleAdrlManager(term);
               termsArdls.add(adrl);
            }
            throw new ScannedTerminaleNotUniqueException(termsArdls, sT.getName());
         }else if(termIds.size() == 1){
            found = terminaleManager.findByIdManager(termIds.get(0));
         }
      }
      return found;
   }

   @Override
   public TKScanTerminaleDTO compareScanAndTerminaleManager(final ScanTerminale sT, final Enceinte enc,
      final List<Conteneur> conts){

      final TKScanTerminaleDTO scanDTO = new TKScanTerminaleDTO();
      scanDTO.setScanTerminale(sT);

      final Terminale found = findScannedTerminaleManager(sT, enc, conts);
      scanDTO.setTerminale(found);

      if(found != null){
         final List<Emplacement> tKEmpls = new ArrayList<>(terminaleManager.getEmplacementsManager(found));

         // compare
         Emplacement corresp;
         for(final ScanTube tube : sT.getScanTubes()){
            corresp = new Emplacement();
            corresp.setTerminale(found);
            corresp.setPosition(tube.getPosition());

            // position sup à taille boite
            if(tube.getPosition() > found.getTerminaleType().getNbPlaces()){
               throw new ScannedTerminaleOverSizeException(found, tube);
            }

            // find emplacement
            if(tKEmpls.contains(corresp)){
               corresp = tKEmpls.get(tKEmpls.indexOf(corresp));
            }

            // Emplacement may be new Instance, thus empty
            // or persisted as empty
            // or persisted as filled

            if(!corresp.getVide()){
               final List<TKStockableObject> objs = emplacementManager.findObjByEmplacementManager(corresp, null);
               if(!tube.isEmpty()){ // 1-emplacement filled && tube filled -> check?
                  if(objs.isEmpty()){
                     // system error if emplacement vide but code not found!
                     throw new RuntimeException("emplacement.vide.objet.incoherent");
                  }else if(!objs.get(0).getCode().equals(tube.getCode())){
                     scanDTO.getEmplacementsMismatch().put(tube, objs.get(0));
                  }
               }else{ // 2-emplacement filled && tube scan empty -> to be freed 
                  scanDTO.getEmplacementsToFree().put(tube, objs.get(0));
               }
            }else{
               if(!tube.isEmpty()){ // 3-emplacement empty && tube scan filled -> to be filled
                  scanDTO.getEmplacementsToFill().put(tube, corresp);
               }
            }
         }
      }
      return scanDTO;
   }
}
