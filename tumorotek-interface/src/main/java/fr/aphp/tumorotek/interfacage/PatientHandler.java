/**
 * Copyright ou © ou Copr. Ministère de la santé, FRANCE (01/01/2011)
 * dsi-projet.tk@aphp.fr
 * <p>
 * Ce logiciel est un programme informatique servant à la gestion de
 * l'activité de biobanques.
 * <p>
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
 * <p>
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
 * <p>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous
 * avez pris connaissance de la licence CeCILL, et que vous en avez
 * accepté les termes.
 **/
package fr.aphp.tumorotek.interfacage;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.aphp.tumorotek.interfacage.jaxb.SipMessage;
import fr.aphp.tumorotek.interfacage.jaxb.inclusion.StudySubjectType;
import fr.aphp.tumorotek.manager.coeur.patient.PatientManager;
import fr.aphp.tumorotek.manager.interfacage.PatientSipManager;
import fr.aphp.tumorotek.manager.utilisateur.UtilisateurManager;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.interfacage.PatientSip;
import fr.aphp.tumorotek.model.interfacage.PatientSipSejour;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.parser.PipeParser;

/**
 * CLasse de manipulation des messages venant des SIPs routés par
 * Camel.
 * Date: 08/11/2011
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 *
 */
public class PatientHandler
{

   // private Log log = LogFactory.getLog(PatientHandler.class);

   private Integer maxSipTableSize = 1000000;

   private PatientSipManager patientSipManager;
   private PatientManager patientManager;
   private UtilisateurManager utilisateurManager;

   private final PipeParser pipe = new PipeParser();

   public PatientHandler(){}

   public void setPatientSipManager(final PatientSipManager pM){
      this.patientSipManager = pM;
   }

   public void setPatientManager(final PatientManager pManager){
      this.patientManager = pManager;
   }

   public void setUtilisateurManager(final UtilisateurManager uManager){
      this.utilisateurManager = uManager;
   }

   public Integer getMaxSipTableSize(){
      return maxSipTableSize;
   }

   public void setMaxSipTableSize(final Integer m){
      this.maxSipTableSize = m;
   }

   /**
    * Cree un patient dans la base temporaire.
    * @param body SipMessage
    */
   public void handleCreation(final SipMessage body){
      patientSipManager.createOrUpdatePatientInTempTableManager(body.toPatientSip(), getMaxSipTableSize(), false);
   }

   /**
    * Cree un patient dans la base temporaire à partir d'un message HL7.
    * @param HAPI AbstractMessage
    * @throws IOException
    * @throws HL7Exception
    */
   public void handleCreation(final AbstractMessage body) throws IOException, HL7Exception{
      patientSipManager.createOrUpdatePatientInTempTableManager(patientSipManager.parseHl7MessagePID(pipe.encode(body)),
         getMaxSipTableSize(), false);
   }

   /**
    * Traite un message de modification.
    * @param body SipMessage
    */
   public void handleModification(final SipMessage body){
      final PatientSip sip = body.toPatientSip();
      doModification(sip);
   }

   /**
    * Traite un message de modification HL7.
    * @param body HAPI AbstractMessage
    * @throws IOException
    * @throws HL7Exception
    */
   public void handleModification(final AbstractMessage body) throws IOException, HL7Exception{

      final PatientSip sip = patientSipManager.parseHl7MessagePID(pipe.encode(body));
      doModification(sip);
   }

   /** Cherche le patient par son NIP dans la base TK.
    * Si le patient existe dans la base TK -> lance une modification
    * (synchronization).
    * Sinon, vérifie si le patient est enregistré dans la table temporaire
    * Si oui -> lance un update
    * Si non -> creation.
    */
   private void doModification(final PatientSip pSip){
      final Patient inBase = patientSipManager.doSynchronizePatientManager(pSip);
      if(inBase != null){ // synchro
         patientSipManager.updatePatientSystem(pSip, inBase);
      }else{
         patientSipManager.createOrUpdatePatientInTempTableManager(pSip, getMaxSipTableSize(), false);
      }
   }

   /**
    * Traite un message de fusion.
    * @param body SipMessage
    */
   public void handleFusion(final SipMessage body){
      final PatientSip sipA = body.toPatientActif();
      final PatientSip sipP = body.toPatientPassif();

      doFusion(sipA, sipP);
   }

   /**
    * Traite un message de fusion HL7.
    * @param body HAPI AbstractMessage
    * @throws IOException
    * @throws HL7Exception
    */
   public void handleFusion(final AbstractMessage body) throws IOException, HL7Exception{
      final PatientSip sipA = patientSipManager.parseHl7MessagePID(pipe.encode(body));
      final PatientSip sipP = patientSipManager.parseHl7MessageMRG(pipe.encode(body));

      doFusion(sipA, sipP);
   }

   /**
    * Executes la fusion en prenant en compte les différentes associations
    * possibles:<br>
    *  - fusion système TK<br>
    *  - MAJ Patient TK<br>
    *  - Suppression Patient temp passif.<br>
    * @param patient à conserver
    * @param patient à supprimer par fusion
    */
   public void doFusion(final PatientSip sipA, final PatientSip sipP){
      if(sipA != null && sipP != null){
         final Patient inBaseA = patientSipManager.doSynchronizePatientManager(sipA);
         final Patient inBaseP = patientSipManager.doSynchronizePatientManager(sipP);

         Date dateDecesP = null;
         if(inBaseP == null){ //passif dans temporaire ou inexistant
            // suppression du passif dans la table temporaire
            // et récupération numéro de séjours avant suppression
            final List<PatientSip> passif = patientSipManager.findByNipLikeManager(sipP.getNip(), true);
            if(!passif.isEmpty()){
               for(final PatientSipSejour sej : passif.get(0).getSejours()){
                  final PatientSipSejour newSej = new PatientSipSejour();
                  newSej.setNumero(sej.getNumero());
                  newSej.setDateSejour(sej.getDateSejour());
                  newSej.setPatientSip(sipA);
                  sipA.getSejours().add(newSej);
               }
               patientSipManager.removeObjectManager(passif.get(0));

               // Date deces
               dateDecesP = passif.get(0).getDateDeces();
            }

            if(inBaseA == null){ //actif dans temporaire ou inexistant
               sipA.setDateDecesP(dateDecesP);
               patientSipManager.createOrUpdatePatientInTempTableManager(sipA, getMaxSipTableSize(), true);
            }
         }else{ //passif dans TK
            if(inBaseA != null){ //actif dans TK
               // -> fusion TK car les deux patients sont dans TK
               patientSipManager.fusionPatientSystemManager(inBaseA, inBaseP);
            }else{ //actif dans temporaire ou inexistant
               // -> MAJ du passif de TK avec l'actif
               patientSipManager.updatePatientSystem(sipA, inBaseP);
               if(!patientSipManager.findByNipLikeManager(sipA.getNip(), true).isEmpty()){
                  patientSipManager.removeObjectManager(patientSipManager.findByNipLikeManager(sipA.getNip(), true).get(0));
               }
            }
         }
      }
   }

   /*
    * Cree un patient dans la base tumo2 à partir du web service
    * d'inclusion basé sur les spécifications d'Openclinica.
    * L'inclusion MELBASE implique la création des maladies par défaut
    *  - MELANOME INITIAL
    *  - INCLUSION
    *  - VISITE 3 MOIS.
    *  - VISITE 6 MOIS.
    *  - VISITE 12 MOIS.
    *  - VISITE 18 MOIS.
    *  - VISITE 24 MOIS.
    *  Associations:
    *   label-> NIP
    *   secondaryLabel -> nom;prenom
    *   gender -> Sexe
    *   yearOfBirth -> date naissance incomplète
    *
    * @param StudySubjectType subject
    */
   public void handleInclusionMelbase(final StudySubjectType subject) throws ParseException{
      if(subject != null){
         final Patient pat = new Patient();
         // pat.setNip(subject.getLabel());
         pat.setNom(subject.getLabel());
         pat.setPrenom(subject.getSecondaryLabel());
         pat.setSexe("Ind");
         if(subject.getSubject().getGender() != null){
            pat.setSexe(subject.getSubject().getGender().value().toUpperCase());
         }
         final Date dateNais =
            new SimpleDateFormat("dd-MM-yyyy").parse("01-01-" + String.valueOf(subject.getSubject().getYearOfBirth()));
         pat.setDateNaissance(dateNais);
         pat.setPatientEtat("V");

         final List<Maladie> maladies = new ArrayList<>();
         final Maladie initiale = new Maladie();
         initiale.setLibelle("MELANOME INITIAL");
         initiale.setPatient(pat);
         initiale.setDateDebut(dateNais);
         maladies.add(initiale);
         final Maladie inclusion = new Maladie();
         inclusion.setLibelle("INCLUSION");
         inclusion.setDateDebut(subject.getEnrollmentDate().toGregorianCalendar().getTime());
         inclusion.setPatient(pat);
         maladies.add(inclusion);
         final Maladie visite3m = new Maladie();
         visite3m.setLibelle("VISITE 3 MOIS");
         visite3m.setPatient(pat);
         final Calendar c3m = subject.getEnrollmentDate().toGregorianCalendar();
         c3m.add(Calendar.MONTH, 3);
         visite3m.setDateDebut(c3m.getTime());
         maladies.add(visite3m);
         final Maladie visite6m = new Maladie();
         visite6m.setLibelle("VISITE 6 MOIS");
         visite6m.setPatient(pat);
         final Calendar c6m = subject.getEnrollmentDate().toGregorianCalendar();
         c6m.add(Calendar.MONTH, 6);
         visite6m.setDateDebut(c6m.getTime());
         maladies.add(visite6m);
         final Maladie visite12m = new Maladie();
         visite12m.setLibelle("VISITE 12 MOIS");
         visite12m.setPatient(pat);
         final Calendar c12m = subject.getEnrollmentDate().toGregorianCalendar();
         c12m.add(Calendar.MONTH, 12);
         visite12m.setDateDebut(c12m.getTime());
         maladies.add(visite12m);
         final Maladie visite18m = new Maladie();
         visite18m.setLibelle("VISITE 18 MOIS");
         visite18m.setPatient(pat);
         final Calendar c18m = subject.getEnrollmentDate().toGregorianCalendar();
         c18m.add(Calendar.MONTH, 18);
         visite18m.setDateDebut(c18m.getTime());
         maladies.add(visite18m);
         final Maladie visite24m = new Maladie();
         visite24m.setLibelle("VISITE 24 MOIS");
         visite24m.setPatient(pat);
         final Calendar c24m = subject.getEnrollmentDate().toGregorianCalendar();
         c24m.add(Calendar.MONTH, 24);
         visite24m.setDateDebut(c24m.getTime());
         maladies.add(visite24m);

         patientManager.createOrUpdateObjectManager(pat, maladies, null, null, null, null, null, null,
            utilisateurManager.findByLoginManager("inclusion").get(0), "creation", null, false);
      }

   }
}
