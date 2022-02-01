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
package fr.aphp.tumorotek.manager.impl.coeur.echantillon;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import fr.aphp.tumorotek.dao.coeur.ObjetStatutDao;
import fr.aphp.tumorotek.dao.qualite.OperationTypeDao;
import fr.aphp.tumorotek.dao.systeme.EntiteDao;
import fr.aphp.tumorotek.dto.EchantillonDTO;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.PrelEchansManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.helper.FileBatch;
import fr.aphp.tumorotek.manager.qualite.ObjetNonConformeManager;
import fr.aphp.tumorotek.manager.stockage.EmplacementManager;
import fr.aphp.tumorotek.model.code.CodeAssigne;
import fr.aphp.tumorotek.model.coeur.ObjetStatut;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.qualite.OperationType;
import fr.aphp.tumorotek.model.stockage.Emplacement;
import fr.aphp.tumorotek.model.systeme.Fichier;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;

/**
 *
 * @author mathieu
 * @version 2.1.1
 */
public class PrelEchansManagerImpl implements PrelEchansManager
{

//   @Autowired
//   private PrelevementManager prelevementManager;
//   @Autowired
//   private EchantillonManager echantillonManager;
//   @Autowired
//   private ObjetStatutDao objetStatutDao;
//   @Autowired
//   private OperationTypeDao operationTypeDao;
//   @Autowired
//   private EmplacementManager emplacementManager;
//   @Autowired
//   private ObjetNonConformeManager objetNonConformeManager;
//   @Autowired
//   private AnnotationValeurManager annotationValeurManager;
//   @Autowired
//   private EntiteDao entiteDao;

   @Override
   public Prelevement createQuickPrelAndEchansManager(final Patient pat, final Maladie mal, final Prelevement prel,
      final List<EchantillonDTO> echanDTOs, final Map<Echantillon, Emplacement> echansEmpl, final Banque banque,
      final Utilisateur utilisateur, final List<FileBatch> batches, final String baseDir){

      final List<File> filesCreated = new ArrayList<>();

      Patient aClone = pat;
      Maladie mClone = mal;
      Prelevement pClone = prel;
//
//      try{
//
//         if(prel != null && prel.getPrelevementId() == null){
//            pClone = prel.clone();
//
//            if(mal != null && mal.getMaladieId() == null){
//               mClone = mal.clone();
//               mClone.setPatient(aClone);
//            }
//
//            if(pat != null && pat.getPatientId() == null){
//               aClone = pat.clone();
//            }
//
//            //enregistrement du prelevement
//            prelevementManager.createObjectManager(pClone, banque, pClone.getNature(), mClone, pClone.getConsentType(),
//               pClone.getPreleveur(), pClone.getServicePreleveur(), pClone.getPrelevementType(), pClone.getConditType(),
//               pClone.getConditMilieu(), pClone.getTransporteur(), pClone.getOperateur(), pClone.getQuantiteUnite(),
//               new ArrayList<>(pClone.getLaboInters()), null, filesCreated, utilisateur, true, baseDir, false);
//         }
//
//         Fichier crAnapath = null;
//         String crDataType = null;
//         String crPath = null;
//
//         final Map<Echantillon, Emplacement> echEmplsClone = new HashMap<>();
//
//         //enregistrement de l'echantillon
//         for(int i = 0; i < echanDTOs.size(); i++){
//            if(echanDTOs.get(i).isNew()){
//               final Echantillon newEchan = echanDTOs.get(i).getEchantillon().clone();
//
//               // clone emplacement map
//               if(echansEmpl != null && echansEmpl.containsKey(newEchan)){
//                  // pour éviter chgt hashcode
//                  // dans Map echansEmpl 
//                  newEchan.setBanque(banque);
//                  echEmplsClone.put(newEchan, echansEmpl.get(echanDTOs.get(i).getEchantillon()));
//               }
//
//               final List<CodeAssigne> codes = new ArrayList<>();
//               for(final CodeAssigne codeA : echanDTOs.get(i).getCodesOrgsToCreateOrEdit()){
//                  codes.add(codeA.clone());
//               }
//               for(final CodeAssigne codeA : echanDTOs.get(i).getCodesLesToCreateOrEdit()){
//                  codes.add(codeA.clone());
//               }
//               //					
//               //					codes.addAll(echanDTOs
//               //							.get(i).getCodesOrgsToCreateOrEdit());
//               //					codes.addAll(echanDTOs
//               //							.get(i).getCodesLesToCreateOrEdit());
//
//               // recupere path + data type si nouveau stream
//               if(newEchan.getCrAnapath() != null && newEchan.getAnapathStream() == null){
//                  newEchan.getCrAnapath().setMimeType(crDataType);
//                  newEchan.getCrAnapath().setPath(crPath);
//                  // crAnapath = newEchan.getCrAnapath().cloneNoId();
//                  // crAnapath.setFichierId(null);
//               }
//               crAnapath = newEchan.getCrAnapath();
//               newEchan.setCrAnapath(null);
//
//               // création de l'objet
//               echantillonManager.saveWithCrAnapathManager(newEchan, banque, pClone, newEchan.getCollaborateur(),
//                  objetStatutDao.findByStatut("NON STOCKE").get(0),
//                  // newEchan.getEmplacement(), 
//                  null, // since 2.0.13.2
//                  newEchan.getEchantillonType(), codes, newEchan.getQuantiteUnite(), newEchan.getEchanQualite(),
//                  newEchan.getModePrepa(), crAnapath, newEchan.getAnapathStream(), filesCreated,
//                  //codeOrgExp, 
//                  //codeLesExp,
//                  echanDTOs.get(i).getValeursToCreateOrUpdate(), utilisateur, true, baseDir, false);
//
//               // non confs
//               if(newEchan.getConformeTraitement() != null && !newEchan.getConformeTraitement()){
//                  // enregistrement de la non conformité 
//                  // après traitement
//                  objetNonConformeManager.createUpdateOrRemoveListObjectManager(newEchan,
//                     echanDTOs.get(i).getNonConformiteTraitements(), "Traitement");
//               }
//               if(newEchan.getConformeCession() != null && !newEchan.getConformeCession()){
//                  // enregistrement de la non conformité 
//                  // à la cession
//                  objetNonConformeManager.createUpdateOrRemoveListObjectManager(newEchan,
//                     echanDTOs.get(i).getNonConformiteCessions(), "Cession");
//               }
//
//               if(newEchan.getAnapathStream() != null){
//                  crDataType = newEchan.getCrAnapath().getMimeType();
//                  crPath = newEchan.getCrAnapath().getPath();
//               }
//            }
//         }
//
//         // emplacements
//         if(!echEmplsClone.isEmpty()){
//            // enregistrement des emplacements	
//            // on parcourt la hashtable du stockage et on extrait
//            // l'emplacement de chaque échantillon
//            final List<Emplacement> emplsFinaux = new ArrayList<>();
//            // Set<Echantillon> echans = (Set<Echantillon>) echansEmpl.keySet();
//            final Set<Entry<Echantillon, Emplacement>> echansEntrySet = echEmplsClone.entrySet();
//            final Iterator<Entry<Echantillon, Emplacement>> itE = echansEntrySet.iterator();
//            Entry<Echantillon, Emplacement> e;
//            while(itE.hasNext()){
//               e = itE.next();
//               final Echantillon echan = e.getKey();
//               final Emplacement empl = e.getValue();
//               empl.setObjetId(echan.getEchantillonId());
//               empl.setEntite(entiteDao.findById(3));
//               emplsFinaux.add(empl);
//            }
//
//            // enregistrement des emplacements
//            emplacementManager.saveMultiEmplacementsManager(emplsFinaux);
//
//            // on va MAJ chaque échantillon : son statut et son
//            // emplacement
//            final Iterator<Echantillon> echansIt = echEmplsClone.keySet().iterator();
//            // it = echans.iterator();
//            final ObjetStatut statut = objetStatutDao.findByStatut("STOCKE").get(0);
//            while(echansIt.hasNext()){
//               final Echantillon echanToUpdate = echansIt.next();
//               final List<OperationType> ops = operationTypeDao.findByNom("Stockage");
//
//               // update de l'objet
//               echantillonManager.saveEchantillonEmplacementManager(echanToUpdate, statut, echEmplsClone.get(echanToUpdate),
//                  utilisateur, ops);
//
//               //	EchantillonDTO deco = new 
//               //	EchantillonDTO(echanToUpdate);
//               // si l'échantillon existait déjà, on MAJ sa fiche
//               // dans la liste des échantillons
//               //	if (!addedEchantillons.contains(deco.getEchantillon())) {		
//               // update de l'échantillon dans la liste
//               //		getObjectTabController().getListe()
//               //			.saveGridListFromOtherPage(echanToUpdate, false);
//               //	}
//            }
//         }
//
//         // Annotations type fichier
//         if(batches != null){
//            // annotation file batches
//            for(final FileBatch batch : batches){
//               annotationValeurManager.createFileBatchForTKObjectsManager(batch.getObjs(), batch.getFile(), batch.getStream(),
//                  batch.getChamp(), banque, utilisateur, baseDir, filesCreated);
//            }
//         }
//
//      }catch(final RuntimeException re){
//
//         for(final File f : filesCreated){
//            f.delete();
//         }
//
//         //			if (isPrelevementProcedure) {	
//         //				if (revertMaladie) {
//         //					getParentObject().getMaladie().setMaladieId(null);
//         //					if (revertPatient) {
//         //						getParentObject().getMaladie()
//         //									.getPatient().setPatientId(null);
//         //					}
//         //				}
//         //				
//         //				getParentObject().setPrelevementId(null);
//         //				// revert Objects
//         //				Iterator<LaboInter> it = 
//         //							getParentObject().getLaboInters().iterator();
//         //				while (it.hasNext()) {
//         //					it.next().setLaboInterId(null);
//         //				}
//         //			}
//         //			Iterator<EchantillonDTO> itE = echanDTOs.iterator();
//         //			
//         //			Echantillon e;
//         //			EchantillonDTO ed;
//         //			ObjetStatut stocke = objetStatutDao
//         //					.findByStatut("STOCKE").get(0);
//         //			ObjetStatut nonstocke = objetStatutDao
//         //					.findByStatut("NON STOCKE").get(0);
//         //			while (itE.hasNext()) {
//         //				ed = itE.next();
//         //				e = ed.getEchantillon();
//         //				if (e.getEchantillonId() != null) {
//         //					e.setEchantillonId(null);
//         //					if (e.getCrAnapath() != null) {
//         //						e.getCrAnapath().setFichierId(null);
//         //					}
//         //					// revert emplacement since 2.0.13.2
//         //					// au stade onGetResultsFromStockage
//         //					// se basant sur echEmpls
//         //					if (echansEmpl != null && echansEmpl.containsKey(e)) {
//         //						e.setObjetStatut(stocke);
//         //						e.setEmplacement(null);
//         //						// clean echanEmpl from emplacement in error
//         //						// doublon ou emplacement occupé 
//         //						if ((re instanceof EmplacementDoublonFoundException 
//         //								&& echansEmpl.get(e).equals(((EmplacementDoublonFoundException) re).getEmplacementMock()))
//         //							|| (re instanceof TKException && re.getMessage().equals("error.emplacement.notEmpty")
//         //									&& ((TKException) re).getTkObj().equals(e))) {
//         //							echansEmpl.remove(e);
//         //							// reset decorateur
//         //							ed.setAdrlTmp(null);
//         //							ed.getEchantillon().setObjetStatut(nonstocke);
//         //						}
//         //					}			
//         //					
//         ////						if (ed.getCodeOrganeToExport() != null) {
//         ////							ed.getCodeOrganeToExport().setEchantillon(e);
//         ////							ed.getCodeOrganeToExport().setEchanExpOrg(e);
//         ////						}
//         ////						if (ed.getCodeLesToExport() != null) {
//         ////							ed.getCodeLesToExport().setEchantillon(e);
//         ////							ed.getCodeLesToExport().setEchanExpLes(e);
//         ////						}
//         //
//         //				} else { // la boucle arrive a l'echantillon planté.
//         //					break;
//         //				}
//         //			}
//         //			
//         ////			updateDecoList(echansEmpl);
//
//         throw re;
//      }
//
//      // envoi informations exterieures
//      //		if (getParentObject() != null) {
//      //			getPrelevementController().handleExtCom((Prelevement) getParentObject(), 
//      //					getObjectTabController());
//      //		}		
      return pClone;
   }
}
