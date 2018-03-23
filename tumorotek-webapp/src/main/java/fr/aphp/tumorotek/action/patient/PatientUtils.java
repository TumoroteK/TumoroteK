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
package fr.aphp.tumorotek.action.patient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.util.resource.Labels;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.decorator.ObjectTypesFormatters;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.serotk.MaladieSero;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.webapp.general.SessionUtils;

/**
 * Utility class fournissant les methodes récupérant et formattant les valeurs
 * de Patient pour un affichage particulier dans l'interface.
 * Date: 26/07/2010.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public final class PatientUtils
{

   private PatientUtils(){}

   public static final LabelCodeItem SEXE_EMPTY = new LabelCodeItem("", null);
   public static final LabelCodeItem SEXE_M = new LabelCodeItem(Labels.getLabel("patient.sexe.homme"), "M");
   public static final LabelCodeItem SEXE_F = new LabelCodeItem(Labels.getLabel("patient.sexe.femme"), "F");
   public static final LabelCodeItem SEXE_IND = new LabelCodeItem(Labels.getLabel("patient.sexe.ind"), "Ind");

   private static List<LabelCodeItem> sexes = new ArrayList<>();
   static{
      sexes.add(SEXE_EMPTY);
      sexes.add(SEXE_M);
      sexes.add(SEXE_F);
      sexes.add(SEXE_IND);
   }

   public static final LabelCodeItem ETAT_V = new LabelCodeItem(Labels.getLabel("patient.etat.vivant"), "V");
   public static final LabelCodeItem ETAT_VF = new LabelCodeItem(Labels.getLabel("patient.etat.vivant.f"), "V");
   public static final LabelCodeItem ETAT_D = new LabelCodeItem(Labels.getLabel("patient.etat.decede"), "D");
   public static final LabelCodeItem ETAT_DF = new LabelCodeItem(Labels.getLabel("patient.etat.decede.f"), "D");
   public static final LabelCodeItem ETAT_I = new LabelCodeItem(Labels.getLabel("patient.etat.inconnu"), "Inconnu");
   private static List<LabelCodeItem> etats = new ArrayList<>();
   static{
      etats.add(ETAT_V);
      etats.add(ETAT_D);
      etats.add(ETAT_I);
   }
   private static List<LabelCodeItem> etatsF = new ArrayList<>();
   static{
      etatsF.add(ETAT_VF);
      etatsF.add(ETAT_DF);
      etatsF.add(ETAT_I);
   }

   public static List<LabelCodeItem> getSexes(){
      return sexes;
   }

   public static List<LabelCodeItem> getEtats(){
      return etats;
   }

   public static List<LabelCodeItem> getEtatsF(){
      return etatsF;
   }

   /**
    * Trouve le bon LabelCodeItem en fonction de sa valeur passée en paramètre.
    * @param value
    * @param isMale si renvoie des etats au masculin
    * @return LabelCodeItem correspondant à la value.
    */
   public static LabelCodeItem getLabelCodeItemFromValue(final String value, final boolean isMale){
      if("M".equals(value)){
         return SEXE_M;
      }else if("F".equals(value)){
         return SEXE_F;
      }else if("Ind".equals(value)){
         return SEXE_IND;
      }else{
         if(isMale){
            if("V".equals(value)){
               return ETAT_V;
            }else if("D".equals(value)){
               return ETAT_D;
            }else if("Inconnu".equals(value)){
               return ETAT_I;
            }
         }
      }
      return null;
   }

   /**
    * A partir des variables de session banques selectionnées et utilisateur,
    * étend la liste aux banques consultables qui seront prises en compte dans 
    * l'affichage des comptes de prélèvements.
    * @param Map session 
    * @return liste banque
    */
   public static List<Banque> getBanquesConsultForPrelevement(final Map<?, ?> session){
      final Set<Banque> banks = new HashSet<>(SessionUtils.getSelectedBanques(session));
      if(banks.size() == 1){
         // la liste de banques consultables
         banks
            .addAll(ManagerLocator.getBanqueManager().findByEntiteConsultByUtilisateurManager(SessionUtils.getLoggedUser(session),
               ManagerLocator.getEntiteManager().findByNomManager("Prelevement").get(0), SessionUtils.getPlateforme(session)));
         // etend la liste existante à la liste de banques autoriseCross
         banks.addAll(ManagerLocator.getBanqueManager().findByAutoriseCrossPatientManager(true));
      }
      return new ArrayList<>(banks);
   }

   /**
    * Calcule le nombre de prélèvements à afficher pour un 
    * patient spécifié. Ce calcul prend en compte les banques consultables par 
    * l'utilisateur (droits et autoriseCrossPatient) passées en paramètres.
    * @param patient
    * @param liste banks consultables 
    * @return int le compte de prélèvements
    */
   public static int getNbPrelsForPatientAndUser(final Patient patient, final List<Banque> banks){
      int nbPrelevements = 0;

      final Iterator<Banque> it = banks.iterator();

      while(it.hasNext()){
         nbPrelevements =
            (int) (nbPrelevements + ManagerLocator.getPatientManager().getCountPrelevementsByBanqueManager(patient, it.next()));
      }

      return nbPrelevements;
   }

   /**
    * Transforme la valeur 'sexe' récupérée de la base de données
    * en un String affichable par l'interface.
    * @param pat
    * @return String sexe internationalise
    */
   public static String setSexeFromDBValue(final Patient pat){
      if(pat.getSexe() != null){
         if(pat.getSexe().equals("M")){
            return Labels.getLabel("patient.sexe.homme");
         }else if(pat.getSexe().equals("F")){
            return Labels.getLabel("patient.sexe.femme");
         }else{
            return Labels.getLabel("patient.sexe.ind");
         }
      }else{
         return null;
      }
   }

   /**
    * Transforme la valeur 'état' récupérée de la base de données
    * en un String affichable par l'interface.
    * @param pat
    * @return String état internationalisé et sexé
    */
   public static String setEtatFromDBValue(final Patient pat){
      if(pat.getPatientEtat() != null){
         if(pat.getPatientEtat().equals("V")){
            if(!"F".equals(pat.getSexe())){
               return Labels.getLabel("patient.etat.vivant");
            }else{
               return Labels.getLabel("patient.etat.vivant.f");
            }
         }else if(pat.getPatientEtat().equals("D")){
            if(!"F".equals(pat.getSexe())){
               return Labels.getLabel("patient.etat.decede");
            }else{
               return Labels.getLabel("patient.etat.decede.f");
            }
         }else{
            return Labels.getLabel("patient.etat.inconnu");
         }
      }
      return null;
   }

   /**
    * Renvoie la date état ou de décès en fonction du patient 
    * passé en paramètre.
    */
   public static String getDateDecesOrEtat(final Patient patient){
      if(!"D".equals(patient.getPatientEtat())){
         return ObjectTypesFormatters.dateRenderer2(patient.getDateEtat());
      }else{
         return ObjectTypesFormatters.dateRenderer2(patient.getDateDeces());
      }
   }

   /*************************************************************************/
   /*************************** SEROTK	**************************************/
   /*************************************************************************/

   /**
    * Transforme la valeur 'diagnostic' Maladise seroTK 
    * récupérée de la base de données
    * en un String affichable par l'interface.
    * @param pat
    * @return String diagnostic maladie internationalise
    */
   public static String setDiagSeroFromDBValue(final Maladie mal){
      if(mal != null && mal.getDelegate() != null && ((MaladieSero) mal.getDelegate()).getDiagnostic() != null){
         final MaladieSero mS = (MaladieSero) mal.getDelegate();
         if(mS.getDiagnostic().equals("C")){
            return Labels.getLabel("maladie.diagnostic.certain");
         }else if(mS.getDiagnostic().equals("P")){
            return Labels.getLabel("maladie.diagnostic.probable");
         }else if(mS.getDiagnostic().equals("S")){
            return Labels.getLabel("maladie.diagnostic.suspecte");
         }
      }
      return null;
   }

   public static final LabelCodeItem DIAG_EMPTY = new LabelCodeItem("", null);
   public static final LabelCodeItem DIAG_C = new LabelCodeItem(Labels.getLabel("maladie.diagnostic.certain"), "C");
   public static final LabelCodeItem DIAG_P = new LabelCodeItem(Labels.getLabel("maladie.diagnostic.probable"), "P");
   public static final LabelCodeItem DIAG_S = new LabelCodeItem(Labels.getLabel("maladie.diagnostic.suspecte"), "S");

   private static List<LabelCodeItem> diagsSero = new ArrayList<>();
   static{
      diagsSero.add(DIAG_EMPTY);
      diagsSero.add(DIAG_C);
      diagsSero.add(DIAG_P);
      diagsSero.add(DIAG_S);
   }

   public static List<LabelCodeItem> getDiagsSero(){
      return diagsSero;
   }
}
