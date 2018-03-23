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
package fr.aphp.tumorotek.manager.impl.io;

import java.util.Iterator;
import java.util.List;

import fr.aphp.tumorotek.manager.code.CodeAssigneManager;
import fr.aphp.tumorotek.manager.coeur.echantillon.EchantillonManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.io.ExtractValueFromChampManager;
import fr.aphp.tumorotek.manager.io.export.ChampManager;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.io.export.Champ;

public class ExtractValueFromChampManagerImpl implements ExtractValueFromChampManager
{

   private ChampManager champManager;
   private EchantillonManager echantillonManager;
   private ProdDeriveManager prodDeriveManager;
   private CodeAssigneManager codeAssigneManager;
   private PrelevementManager prelevementManager;

   public void setChampManager(final ChampManager cManager){
      this.champManager = cManager;
   }

   public void setEchantillonManager(final EchantillonManager eManager){
      this.echantillonManager = eManager;
   }

   public void setProdDeriveManager(final ProdDeriveManager pManager){
      this.prodDeriveManager = pManager;
   }

   public void setCodeAssigneManager(final CodeAssigneManager cManager){
      this.codeAssigneManager = cManager;
   }

   public void setPrelevementManager(final PrelevementManager pManager){
      this.prelevementManager = pManager;
   }

   @Override
   public String extractValueForChampManager(final Echantillon echantillon, final Champ champ){
      if(echantillon != null && champ != null){
         String value = null;
         if(champ.getChampEntite() != null){
            if(champ.getChampEntite().getEntite().getNom().equals("Patient")){
               // on récupère le patient
               final Prelevement prlvt = echantillonManager.getPrelevementManager(echantillon);
               Patient patient = null;
               if(prlvt != null && prlvt.getMaladie() != null){
                  patient = prlvt.getMaladie().getPatient();
               }
               value = (String) champManager.getValueForObjectManager(champ, patient, true);
            }else if(champ.getChampEntite().getEntite().getNom().equals("Maladie")){
               // on récupère la maladie
               final Prelevement prlvt = echantillonManager.getPrelevementManager(echantillon);
               Maladie maladie = null;
               if(prlvt != null){
                  maladie = prlvt.getMaladie();
               }
               value = (String) champManager.getValueForObjectManager(champ, maladie, true);
            }else if(champ.getChampEntite().getEntite().getNom().equals("Prelevement")){
               // on récupère le prelevement
               final Prelevement prlvt = echantillonManager.getPrelevementManager(echantillon);
               if(champ.getChampEntite().getNom().equals("Risques")){
                  // extraction des risques
                  final Iterator<Risque> risksIt = prelevementManager.getRisquesManager(prlvt).iterator();
                  final StringBuffer sb = new StringBuffer();
                  while(risksIt.hasNext()){
                     sb.append(risksIt.next().getNom());
                     if(risksIt.hasNext()){
                        sb.append(", ");
                     }
                  }
                  value = sb.toString();
               }else{
                  value = (String) champManager.getValueForObjectManager(champ, prlvt, true);
               }
            }else if(champ.getChampEntite().getEntite().getNom().equals("Echantillon")){
               // si c'est l'emplacement à extraire
               if(champ.getChampEntite().getNom().equals("EmplacementId")){
                  value = echantillonManager.getEmplacementAdrlManager(echantillon);
               }else if(champ.getChampEntite().getNom().equals("CodeOrganes")){
                  // si ce sont les codes organes à extraire
                  final List<String> codes = codeAssigneManager
                     .formatCodesAsStringsManager(codeAssigneManager.findCodesOrganeByEchantillonManager(echantillon));
                  final StringBuffer sb = new StringBuffer();
                  for(int k = 0; k < codes.size(); k++){
                     sb.append(codes.get(k));
                     if(k + 1 < codes.size()){
                        sb.append(", ");
                     }
                  }
                  value = sb.toString();
               }else if(champ.getChampEntite().getNom().equals("CodeMorphos")){
                  // si ce sont les codes morphos à extraire
                  final List<String> codes = codeAssigneManager
                     .formatCodesAsStringsManager(codeAssigneManager.findCodesMorphoByEchantillonManager(echantillon));
                  final StringBuffer sb = new StringBuffer();
                  for(int k = 0; k < codes.size(); k++){
                     sb.append(codes.get(k));
                     if(k + 1 < codes.size()){
                        sb.append(", ");
                     }
                  }
                  value = sb.toString();
               }else{
                  value = (String) champManager.getValueForObjectManager(champ, echantillon, true);
               }
            }
         }
         return value;
      }else{
         return null;
      }
   }

   @Override
   public String extractValueForChampManager(final ProdDerive prodDerive, final Champ champ){
      if(prodDerive != null && champ != null){
         String value = null;
         if(champ.getChampEntite() != null){
            if(champ.getChampEntite().getEntite().getNom().equals("Patient")){
               // on récupère le patient
               final Patient patient = prodDeriveManager.getPatientParentManager(prodDerive);
               value = (String) champManager.getValueForObjectManager(champ, patient, true);
            }else if(champ.getChampEntite().getEntite().getNom().equals("Maladie")){
               // on récupère la maladie
               final Prelevement prlvt = prodDeriveManager.getPrelevementParent(prodDerive);
               Maladie maladie = null;
               if(prlvt != null){
                  maladie = prlvt.getMaladie();
               }
               value = (String) champManager.getValueForObjectManager(champ, maladie, true);
            }else if(champ.getChampEntite().getEntite().getNom().equals("Prelevement")){
               // on récupère le prelevement
               final Prelevement prlvt = prodDeriveManager.getPrelevementParent(prodDerive);
               if(champ.getChampEntite().getNom().equals("Risques")){
                  // extraction des risques
                  final Iterator<Risque> risksIt = prelevementManager.getRisquesManager(prlvt).iterator();
                  final StringBuffer sb = new StringBuffer();
                  while(risksIt.hasNext()){
                     sb.append(risksIt.next().getNom());
                     if(risksIt.hasNext()){
                        sb.append(", ");
                     }
                  }
                  value = sb.toString();
               }else{
                  value = (String) champManager.getValueForObjectManager(champ, prlvt, true);
               }
            }else if(champ.getChampEntite().getEntite().getNom().equals("Echantillon")){
               // on récupère l'échantillon
               final Object parent = prodDeriveManager.findParent(prodDerive);
               if(parent.getClass().getSimpleName().equals("Echantillon")){
                  value = extractValueForChampManager((Echantillon) parent, champ);
               }
            }else if(champ.getChampEntite().getEntite().getNom().equals("ProdDerive")){
               // si c'est l'emplacement à extraire
               if(champ.getChampEntite().getNom().equals("EmplacementId")){
                  value = prodDeriveManager.getEmplacementAdrlManager(prodDerive);
               }else{
                  value = (String) champManager.getValueForObjectManager(champ, prodDerive, true);
               }
            }
         }
         return value;
      }else{
         return null;
      }
   }
}
