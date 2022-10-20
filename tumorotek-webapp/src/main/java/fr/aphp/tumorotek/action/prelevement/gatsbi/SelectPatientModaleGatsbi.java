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
package fr.aphp.tumorotek.action.prelevement.gatsbi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.ListitemRenderer;
import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.action.patient.gatsbi.GatsbiControllerPatient;
import fr.aphp.tumorotek.action.prelevement.SelectPatientModale;
import fr.aphp.tumorotek.decorator.gatsbi.PatientItemRendererGatsbi;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;

/**
 * 
 * @author Mathieu BARTHELEMY
 * @version 2.3.0-gatsbi
 */
public class SelectPatientModaleGatsbi extends SelectPatientModale
{

   private PatientItemRendererGatsbi patientRendererGatsbi = new PatientItemRendererGatsbi(false);

   private Contexte contexte; 
   private Banque banque;
   
   @AfterCompose
   public void afterCompose(@ContextParam(ContextType.VIEW) final Component view){
      super.afterCompose(view);
      
      Map<Integer, String> widths = new HashMap<Integer, String>();
      widths.put(3, getNomHeaderWidth()); // nom taille fusion
      widths.put(3, getPrenomHeaderWidth()); // prenom  taille fusion
      widths.put(6, "50px"); // sexe listheader 50px
      
      GatsbiControllerPatient.drawListheadersForPatients(contexte, patientsBox, isFusionPatients, widths);  
   }

   @Init
   public void init(@ExecutionArgParam("path") final String pathToPage, @ExecutionArgParam("methode") final String methode,
      @ExecutionArgParam("isFusion") final Boolean isFusion, @ExecutionArgParam("critere") final String critere,
      @ExecutionArgParam("patAExclure") final Patient patAExclure, 
      @ExecutionArgParam("contexte") Contexte _c, @ExecutionArgParam("banque") Banque _b){
      this.path = pathToPage;
      this.returnMethode = methode;
      this.isFusionPatients = isFusion;
      this.critereValue = critere;
      this.patientAExclure = patAExclure;
      
      this.contexte = _c;
      this.banque = _b; 
      
      patientRendererGatsbi.setContexte(contexte);
      patientRendererGatsbi.setBanque(banque);

      searchForPatients();
   }
   
   /**
    * Ajoute une recherche identifiant à la recherche patient existante.
    */
   @Override
   protected LinkedHashSet<Patient> searchPatientInTK(){
      final LinkedHashSet<Patient> res = super.searchPatientInTK();
      
      if(critereValue != null && !critereValue.equals("")){
         // clean empty identifiant TODO ajouter tous les patients ?
         res.removeIf(p -> p.getIdentifiant(banque).getIdentifiant() == null);
         
         res.addAll(ManagerLocator.getPatientManager()
            .findByIdentifiantLikeManager(critereValue, false, Arrays.asList(banque))); 
      }
      
      return res;
   }
   
   @Override
   public ListitemRenderer<Patient> getPatientRenderer(){
      return patientRendererGatsbi;
   }

//   /**
//    * @since 2.0.9 Recherche sur le numéro de séjour
//    * @since 2.3.0 recherche sur l'identifiant
//    * @version 2.3.0
//    */
//   public void searchForPatients(){
//      // recherche des patients dans la base TK
//      final LinkedHashSet<Patient> res = new LinkedHashSet<>();
//      if(critereValue != null && !critereValue.equals("")){
//         res.addAll(ManagerLocator.getPatientManager().findByNipLikeManager(critereValue, false));
//         res.addAll(ManagerLocator.getPatientManager().findByNomLikeManager(critereValue, false));
//         res.addAll(ManagerLocator.getPatientManager().findByNdaLikeManager(critereValue, false));
//         if (getIsGatsbiContexte()) {
//            // clean empty identifiant
//            res.removeIf(p -> p.getIdentifiant(banque).getIdentifiant() == null);
//            
//            res.addAll(ManagerLocator.getPatientManager()
//               .findByIdentifiantLikeManager(critereValue, false, Arrays.asList(banque)));            
//         }
//      }
//      this.patients = new ArrayList<>(res);
//
//      // si nous sommes dans le cas d'une recherche de patients pour une
//      // fusion, on ne va pas les chercher dans le serveur d'identités
//      // patient
//      // @since 2.3.0-gatsbi TODO recherche PatientSIp si contexte Gatsbi ?!
//      if(!isFusionPatients && !getIsGatsbiContexte()){
//         //recherche dans le serveur identite patient si il est actif
//         if(SipFactory.isDirectSip()){
//            log.info("->Debut traitement recherche de patients " + "dans serveur d'identités");
//
//            // plusieurs fichiers de connexion au Sip peuvent être
//            // définis ils doivent etre de la forme pt1_serveur_Identites,
//            // pt2_serveur_Identites... ou serveur_Identites
//            boolean found = true;
//            int idx = 1;
//            // on itère sur les noms possibles tant qu'on trouve
//            // un fichier correspondant
//            while(found){
//               final StringBuffer nomFichier = new StringBuffer();
//               nomFichier.append("pt");
//               nomFichier.append(idx);
//               nomFichier.append("_serveur_Identites.properties");
//
//               // on vérifie que le fichier existe
//               found = ManagerLocator.getResourceBundleSip().doesResourceBundleExists(nomFichier.toString());
//
//               // s'il existe
//               if(found){
//                  // recherche
//                  searchPatientsInSip(nomFichier.toString());
//               }else{
//                  // sinon on utilise le fichier par défaut
//                  // "serveur_Identites"
//                  if(ManagerLocator.getResourceBundleSip().doesResourceBundleExists("serveur_Identites.properties")){
//                     searchPatientsInSip("serveur_Identites.properties");
//                  }
//               }
//               ++idx;
//            }
//
//         }else if(SipFactory.isMessagesSip()){
//            log.debug("Recherche de patients par interfacage");
//
//            final Set<PatientSip> listPatientSip = new HashSet<>();
//            listPatientSip.addAll(ManagerLocator.getPatientSipManager().findByNipLikeManager(critereValue, true));
//            listPatientSip.addAll(ManagerLocator.getPatientSipManager().findByNomLikeManager(critereValue, true));
//            listPatientSip.addAll(ManagerLocator.getPatientSipManager().findByNumeroSejourManager(critereValue, true));
//
//            final List<Patient> listPatientTmp = new ArrayList<>();
//            final Iterator<PatientSip> it = listPatientSip.iterator();
//            while(it.hasNext()){
//               listPatientTmp.add(it.next().toPatient());
//            }
//
//            fusionerNouvelleListeAvecPrincipale(listPatientTmp);
//            log.info("->Fin recherche de patients interfacage");
//         }
//         patients.addAll(patientsSip);
//
//      }else{
//      if (isFusionPatients) {
//         // si un patient doit être exclu, on le retire de la liste
//         if(patientAExclure != null){
//            int idx = -1;
//            for(int i = 0; i < patients.size(); i++){
//               if(patients.get(i).getPatientId() != null && patientAExclure.getPatientId() != null
//                  && patients.get(i).getPatientId().equals(patientAExclure.getPatientId())){
//                  idx = i;
//               }
//            }
//
//            if(idx > -1){
//               patients.remove(idx);
//            }
//         }
//      }
//      // on va trier les patients extraits en fct de leur nom
//      Collections.sort(patients, new PatientNomComparator(true));
//   }
}