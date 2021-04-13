package fr.aphp.tumorotek.manager.impl.io.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import fr.aphp.tumorotek.dto.EchantillonDTO;
import fr.aphp.tumorotek.manager.code.CodeAssigneManager;
import fr.aphp.tumorotek.manager.coeur.annotation.AnnotationValeurManager;
import fr.aphp.tumorotek.manager.coeur.prelevement.PrelevementManager;
import fr.aphp.tumorotek.manager.coeur.prodderive.ProdDeriveManager;
import fr.aphp.tumorotek.manager.dto.EchantillonDTOManager;
import fr.aphp.tumorotek.manager.io.export.ChampManager;
import fr.aphp.tumorotek.manager.io.utils.CorrespondanceManager;
import fr.aphp.tumorotek.manager.qualite.ConformiteTypeManager;
import fr.aphp.tumorotek.manager.qualite.ObjetNonConformeManager;
import fr.aphp.tumorotek.manager.stockage.ConteneurManager;
import fr.aphp.tumorotek.model.TKAnnotableObject;
import fr.aphp.tumorotek.model.TKdataObject;
import fr.aphp.tumorotek.model.cession.Cession;
import fr.aphp.tumorotek.model.coeur.annotation.AnnotationValeur;
import fr.aphp.tumorotek.model.coeur.annotation.ChampAnnotation;
import fr.aphp.tumorotek.model.coeur.echantillon.Echantillon;
import fr.aphp.tumorotek.model.coeur.patient.Maladie;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.coeur.patient.serotk.MaladieSero;
import fr.aphp.tumorotek.model.coeur.prelevement.Prelevement;
import fr.aphp.tumorotek.model.coeur.prelevement.Risque;
import fr.aphp.tumorotek.model.coeur.prelevement.delegate.PrelevementSero;
import fr.aphp.tumorotek.model.coeur.prodderive.ProdDerive;
import fr.aphp.tumorotek.model.contexte.Protocole;
import fr.aphp.tumorotek.model.impression.CleImpression;
import fr.aphp.tumorotek.model.io.export.Affichage;
import fr.aphp.tumorotek.model.io.export.Champ;
import fr.aphp.tumorotek.model.io.export.ChampDelegue;
import fr.aphp.tumorotek.model.io.export.ChampEntite;
import fr.aphp.tumorotek.model.io.export.Resultat;
import fr.aphp.tumorotek.model.qualite.ObjetNonConforme;
import fr.aphp.tumorotek.model.systeme.Entite;

/**
 * Classe utilitaire manager regroupant les methodes optimisées
 * de récupération des objets et
 * d'affichage du module de Recherches complexes
 * Date: 11/03/2013
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.0
 * @since 2.0.10
 * @update 13/03/2018
 *
 */
public class RechercheUtilsManager
{

   private static CorrespondanceManager correspondanceManager;
   private static ChampManager champManager;
   private static AnnotationValeurManager annotationValeurManager;
   private static PrelevementManager prelevementManager;
   private static ObjetNonConformeManager objetNonConformeManager;
   private static ConformiteTypeManager conformiteTypeManager;
   private static CodeAssigneManager codeAssigneManager;
   private static ProdDeriveManager prodDeriveManager;
   private static ConteneurManager conteneurManager;
   private static EchantillonDTOManager echantillonDTOManager;

   public static void setCorrespondanceManager(CorrespondanceManager correspondanceManager){
      RechercheUtilsManager.correspondanceManager = correspondanceManager;
   }

   public static void setChampManager(ChampManager champManager){
      RechercheUtilsManager.champManager = champManager;
   }

   public static void setAnnotationValeurManager(AnnotationValeurManager annotationValeurManager){
      RechercheUtilsManager.annotationValeurManager = annotationValeurManager;
   }

   public static void setPrelevementManager(PrelevementManager prelevementManager){
      RechercheUtilsManager.prelevementManager = prelevementManager;
   }

   public static void setObjetNonConformeManager(ObjetNonConformeManager objetNonConformeManager){
      RechercheUtilsManager.objetNonConformeManager = objetNonConformeManager;
   }

   public static void setConformiteTypeManager(ConformiteTypeManager conformiteTypeManager){
      RechercheUtilsManager.conformiteTypeManager = conformiteTypeManager;
   }

   public static void setCodeAssigneManager(CodeAssigneManager codeAssigneManager){
      RechercheUtilsManager.codeAssigneManager = codeAssigneManager;
   }

   public static void setProdDeriveManager(ProdDeriveManager prodDeriveManager){
      RechercheUtilsManager.prodDeriveManager = prodDeriveManager;
   }

   public static void setEchantillonDTOManager(EchantillonDTOManager echantillonDTOManager){
      RechercheUtilsManager.echantillonDTOManager = echantillonDTOManager;
   }
   
   public static void setConteneurManager(ConteneurManager conteneurManager){
      RechercheUtilsManager.conteneurManager = conteneurManager;
   }

   /**
    * Recherche un objet de classe spécifique dans une liste d'objets
    * @param listeObjets liste d'objets à parcourir
    * @param tkClass la classe d'objet recherchée
    * @return Un objet de la classe recherchée dans la liste
    */
   @SuppressWarnings("unchecked")
   private static <T> T getObjectFromList(final List<Object> listeObjets, final Class<T> tkClass){
      final Iterator<Object> itObj = listeObjets.iterator();
      while(itObj.hasNext()){
         final Object temp = itObj.next();
         if(tkClass.isInstance(temp)){
            return (T) temp;
         }
      }
      return null;
   }

   /**
    * Retourne la liste des objets liés à un objet passé en paramètre en fonction d'une entité recherchée
    * @param objetInitial objet initial
    * @param entite l'entité recherchée
    * @param reservedEntite entité reservée ???
    * @return liste des objets liés à un objet initial
    */
   public static List<Object> getListeObjetsCorrespondants(final Object objetInitial, final Entite entite,
      final String reservedEntite){
      final List<Object> liste = new ArrayList<>();
      Object recup = null;
      // hack pour Etalissement Preleveur du Prelevement
      if(!entite.getNom().equals("Service")){
         // ne relance pas la recherche si entite deja recuperee
         if(objetInitial.getClass().getSimpleName().equals(entite.getNom())){
            recup = objetInitial;
         }else{
            final List<Object> lObj = new ArrayList<>();
            lObj.add(objetInitial);
            final List<Object> recups = correspondanceManager.recupereEntitesViaDAutres(lObj, entite.getNom());
            if(recups != null && recups.size() >= 1){
               recup = recups.get(0);
            }
         }
         if(reservedEntite == null || entite.getNom().equals(reservedEntite)){
            liste.add(recup);
         }
      }else{
         liste.add(null);
      }
      return liste;
   }

   /**
    * Retourne la liste des objets liés à un objet passé en paramètre en fonction d'un champ recherché
    * @param objetInitial objet initial
    * @param champ le champ recherché
    * @param reservedEntite entité reservée ???
    * @return liste des objets liés à un objet initial
    */
   public static List<Object> getListeObjetsCorrespondants(final Object objetInitial, final Champ champ,
      final String reservedEntite){
      final List<Object> liste = new ArrayList<>();
      if(champ.getChampEntite() != null){
         liste.addAll(getListeObjetsCorrespondants(objetInitial, champ.getChampEntite(), champ.getChampParent(), reservedEntite));
      }else if(champ.getChampDelegue() != null){
         liste.addAll( getListeObjetsCorrespondants(objetInitial, champ.getChampDelegue(), champ.getChampParent(), reservedEntite));
      }else if(champ.getChampAnnotation() != null){
         liste.addAll(getListeObjetsCorrespondants(objetInitial, champ.getChampAnnotation(), reservedEntite));
      }

      return liste;
   }

   /**
    * Retourne la liste des objets liés à un objet passé en paramètre en fonction d'un champ recherché
    * @param objetInitial objet initial
    * @param champEntite le champ recherché
    * @param reservedEntite entité reservée ???
    * @return liste des objets liés à un objet initial
    */
   public static List<Object> getListeObjetsCorrespondants(final Object objetInitial, final ChampEntite champEntite,
      final Champ champParent, final String reservedEntite){
      Entite entite = null;
      final List<Object> liste = new ArrayList<>();
      if(champEntite != null){
         entite = champEntite.getEntite();
         // On récupère l'entité parente s'il y en a !
         Champ parent = champParent;
         while(parent != null){
            entite = champParent.getChampEntite().getEntite();
            parent = parent.getChampParent();
         }

         if(entite != null){
            liste.addAll(getListeObjetsCorrespondants(objetInitial, entite, reservedEntite));
         }
      }

      return liste;
   }

   /**
    * Retourne la liste des objets liés à un objet passé en paramètre en fonction d'un champ recherché
    * @param objetInitial objet initial
    * @param champDelegue le champ recherché
    * @param reservedEntite entité reservée ???
    * @return liste des objets liés à un objet initial
    */
   public static List<Object> getListeObjetsCorrespondants(final Object objetInitial, final ChampDelegue champDelegue,

      final Champ champParent, final String reservedEntite){
      Entite entite = null;
      final List<Object> liste = new ArrayList<>();
      if(champDelegue != null){
         entite = champDelegue.getEntite();
         // On récupère l'entité parente s'il y en a !
         Champ parent = champParent;
         while(parent != null){
            entite = champParent.getChampEntite().getEntite();
            parent = parent.getChampParent();
         }

         if(entite != null){
            liste.addAll(getListeObjetsCorrespondants(objetInitial, entite, reservedEntite));
         }
      }

      return liste;

   }

   /**
    * Retourne la liste des objets liés à un objet passé en paramètre en fonction d'un champ recherché
    * @param objetInitial objet initial
    * @param champAnnotation le champ recherché
    * @param reservedEntite entité reservée ???
    * @return liste des objets liés à un objet initial
    */
   public static List<Object> getListeObjetsCorrespondants(final Object objetInitial, final ChampAnnotation champAnnotation,
      final String reservedEntite){
      Entite entite = null;
      final List<Object> liste = new ArrayList<>();
      if(champAnnotation != null){
         entite = champAnnotation.getTableAnnotation().getEntite();
         if(entite != null){
            liste.addAll(getListeObjetsCorrespondants(objetInitial, entite, reservedEntite));
         }
      }

      return liste;
   }

   /**
    * Retourne la liste des objets liés à un objet passé en paramètre en fonction de clés d'impressions recherchées
    * @param objetInitial objet initial
    * @param cles liste des clefs d'impression
    * @param reservedEntite entité reservée ???
    * @return liste des objets liés à un objet initial
    */
   public static List<Object> getListeObjetsCorrespondants(final Object objetInitial, final List<CleImpression> cles,
      final String reservedEntite){
      final List<Object> liste = new ArrayList<>();
      /* On itère la liste des résultats de l'affichage. */
      final Iterator<CleImpression> itRes = cles.iterator();

      while(itRes.hasNext()){
         final CleImpression res = itRes.next();
         /* On récupère l'entité depuis le champ du Resultat. */
         if(res != null){
            if(res.getChamp() != null){
               liste.addAll(getListeObjetsCorrespondants(objetInitial, res.getChamp(), reservedEntite));
            }
         }
      }
      return liste;
   }

   /**
    * Retourne la liste des objets liés à un objet passé en paramètre
    * @param objetInitial objet initial
    * @param affichage affichage de la recherche
    * @param reservedEntite entité reservée ???
    * @return liste des objets liés à un objet initial
    */
   public static List<Object> getListeObjetsCorrespondants(Object objetInitial, Affichage affichage, String reservedEntite){
      List<Object> liste = new ArrayList<>();
      /* On itère la liste des résultats de l'affichage. */
      Iterator<Resultat> itRes = affichage.getResultats().iterator();
      Entite entite = null;
      //TODO Vraie utilité du prévious ?
      Entite previous = null;
      Object recup = null;
      while(itRes.hasNext()){
         Resultat res = itRes.next();
         /* On récupère l'entité depuis le champ du Resultat. */
         if(res != null){
            if(res.getChamp() != null){
               if(res.getChamp().getChampEntite() != null){
                  entite = res.getChamp().getChampEntite().getEntite();
                  // On récupère l'entité parente s'il y en a !
                  Champ parent = res.getChamp().getChampParent();
                  while(parent != null){
                     entite = parent.getChampEntite().getEntite();
                     parent = parent.getChampParent();
                  }
               }else if(res.getChamp().getChampAnnotation() != null){
                  entite = res.getChamp().getChampAnnotation().getTableAnnotation().getEntite();
               }else if(res.getChamp().getChampDelegue() != null) {
                  entite = res.getChamp().getChampDelegue().getEntite();
               }else{
                  liste.add(null);
               }
            }else{
               liste.add(null);
            }
         }else{
            liste.add(null);
         }
         if(entite != null){
            // hack pour Etalissement Preleveur du Prelevement
            if(!entite.getNom().equals("Service")){
               // ne relance pas la recherche si entite deja recuperee
               if(!entite.equals(previous)){
                  if(objetInitial.getClass().getSimpleName().equals(entite.getNom())){
                     recup = objetInitial;
                  }else{
                     List<Object> lObj = new ArrayList<>();
                     lObj.add(objetInitial);
                     List<Object> recups = correspondanceManager.recupereEntitesViaDAutres(lObj, entite.getNom());
                     if(recups != null && recups.size() >= 1){
                        recup = recups.get(0);
                     }
                  }
                  if(reservedEntite == null || entite.getNom().equals(reservedEntite)){
                     liste.add(recup);
                  }
               }else{
                  if(reservedEntite == null || entite.getNom().equals(reservedEntite)){
                     liste.add(recup);
                  }
               }

               previous = entite;
            }else{
               liste.add(null);
            }
         }
      }
      return liste;
   }

   /**
    * Renvoie la valeur du champ pour l'objet.
    * @param champ Champ à extraire.
    * @param tkObject Objet.
    * @param boolean prettyFormat String
    * @return Valeur du champ.
    */
   private static Object getChampValueForObject(final Champ champ, final TKdataObject tkObject, final Boolean prettyFormat){
      return champManager.getValueForObjectManager(champ, tkObject, prettyFormat);
   }



   /**
    * Retourne la valeur d'un champAnnotation de l'objet correspondant
    * @param obj objet contenant l'annotation
    * @param ca champAnnotation
    * @return valeur du champAnnotation de l'objet correspondant
    */
   private static Object getChampAnnotationValeur(final TKAnnotableObject obj, final ChampAnnotation ca, boolean prettyFormat){
      final List<AnnotationValeur> avs = annotationValeurManager.findByChampAndObjetManager(ca, obj);
      Object res = null;
      if(!avs.isEmpty()){
         if(prettyFormat){
            final StringBuffer sb = new StringBuffer();
            for(int j = 0; j < avs.size(); j++){
               sb.append(avs.get(j).formateAnnotationValeur());
               if(j + 1 < avs.size()){
                  sb.append(";");
               }
            }
            res = sb.toString();
         }else{
            if(1 == avs.size()){
               res = avs.get(0).getValeur();
            }else{
               List<Object> lo = new ArrayList<>();
               for(AnnotationValeur annoVal : avs){
                  lo.add(annoVal.getValeur());
               }
               res = lo;
            }
         }
      }
      return res;
   }

   /**
    * Retourne la valeur d'un champ recherché dans une liste d'objets
    * @param champ le champ a rechercher
    * @param listeObjets la liste d'objets dans laquelle recherche le champ
    * @return la valeur du champ
    */
   public static Object getChampValueFromObjectList(final Champ champ, final List<Object> listeObjets){
      Entite entite = null;
      Champ parent = null;
      Object value = null;

      if(champ.getChampEntite() != null){
         entite = champ.getChampEntite().getEntite();
         // On cherche l'entite parente
         Champ temp = champ.getChampParent();
         while(temp != null){
            parent = temp;
            entite = parent.getChampEntite().getEntite();
            temp = parent.getChampParent();
         }
      }
      else if(champ.getChampDelegue() != null) {
         entite = champ.getChampDelegue().getEntite();
      }
      else if(champ.getChampAnnotation() != null){
         entite = champ.getChampAnnotation().getTableAnnotation().getEntite();
      }

      if(null != entite){
         value = getChampValueFromEntite(entite, champ, parent, listeObjets);
      }

      return value;
   }

   /**
    * Retourne la valeur d'un champ recherché dans une liste d'objets
    * @param champAnnotation le champ a rechercher
    * @param listeObjets la liste d'objets dans laquelle recherche le champ
    * @return la valeur du champ
    */
   public static Object getChampValueFromObjectList(final ChampAnnotation champAnnotation, final List<Object> listeObjets){
      Entite entite = null;
      Champ parent = null;
      Object value = null;
      if(champAnnotation != null){
         entite = champAnnotation.getTableAnnotation().getEntite();
         if(null != entite){
            value = getChampValueFromEntite(entite, champAnnotation, parent, listeObjets);
         }
      }

      return value;
   }

   /**
    * Retourne la valeur d'un champ pour une entité Patient recherchée dans une liste d'objets
    * @param chp champ à récupérer
    * @param listeObjets liste des objets dans laquelle recherchée l'entité Patient et le champ
    * @return la valeur du champ pour l'entité patient
    */
   public static Object getChampValueFromPatient(final Champ chp, final List<Object> listeObjets){
      final Patient recup = getObjectFromList(listeObjets, Patient.class);
      if(null != recup){
         return getChampValueFromPatient(recup, chp);
      }

      return null;
   }

   /**
    * Retourne la valeur d'un champ pour une entité Patient recherchée dans une liste d'objets
    * @param chp champ à récupérer
    * @param listeObjets liste des objets dans laquelle recherchée l'entité Patient et le champ
    * @return la valeur du champ pour l'entité patient
    */
   public static Object getChampValueFromPatient(final ChampAnnotation chp, final List<Object> listeObjets, final Boolean prettyFormat){
      final Patient recup = getObjectFromList(listeObjets, Patient.class);
      if(null != recup){
         return getChampValueFromPatient(recup, chp, prettyFormat);
      }

      return null;
   }

   /**
    * Retourne la valeur d'un champ d'un objet Patient
    * @param pat objet patient
    * @param chp champ à récupérer
    * @return la valeur du champ pour l'entité patient
    */
   public static Object getChampValueFromPatient(final Patient pat, final Champ chp){
      if(pat != null && chp != null){
         if(chp.getChampEntite() != null || chp.getChampDelegue() != null){
            return getChampValueForObject(chp, pat, false);
         }else if(chp.getChampAnnotation() != null){
            return getChampValueFromPatient(pat, chp.getChampAnnotation(), true);
         }
      }
      return null;
   }

   /**
    * Retourne la valeur d'un champ d'un objet Patient
    * @param pat objet patient
    * @param chp champ à récupérer
    * @return la valeur du champ pour l'entité patient
    */
   public static Object getChampValueFromPatient(final Patient pat, final ChampAnnotation chp, final Boolean prettyFormat){
      if(pat != null && chp != null){
         return getChampAnnotationValeur(pat, chp, prettyFormat);
      }
      return null;
   }

   /**
    * Retourne la valeur d'un champ maladie
    * @param maladie objet maladie
    * @param chp champ recherché
    * @param parent champ parent
    * @return valeur du champ maladie
    */
   public static Object getChampValueFromMaladie(final Maladie maladie, final Champ chp, final Champ parent){
      
      if(maladie != null && chp != null){

         //Cas particulier du champ délégué diagnostic (contexte SéroTK)
         if(chp.getChampDelegue() != null){

            if("Diagnostic".equals(chp.getChampDelegue().getNom()) && maladie.getDelegate() != null){
               final MaladieSero prelSero = ((MaladieSero) maladie.getDelegate());
               if(prelSero.getDiagnostic() != null){
                  return prelSero.getDiagnostic().getNom();
               }
            }

         }
         
         //Cas général
         if(chp.getChampEntite() != null){
            if(parent == null){
               return getChampValueForObject(chp, maladie, false);
            }
            // Traitement des sousChamps de patient
            if(parent.getChampEntite().getNom().equals("PatientId")){
               return getChampValueForObject(parent, maladie, false);
            }
         }
      }
      
      return null;
      
   }

   /**
    * Retourne la valeur d'un champ maladie à partir d'une liste d'objets
    * @param chp champ recherché
    * @param parent champ parent
    * @param listeObjets liste d'objets où rechercher l'objet maladie
    * @return valeur du champ maladie
    */
   public static Object getChampValueFromMaladie(final Champ chp, final Champ parent, final List<Object> listeObjets){
      final Maladie recup = getObjectFromList(listeObjets, Maladie.class);
      if(null != recup){
         return getChampValueFromMaladie(recup, chp, parent);
      }

      return null;
   }

   /**
    * Retourne la valeur d'un champ prelevement à partir d'un champ
    * @param prel prelevement
    * @param chp champ recherché
    * @param parent champ parent
    * @return valeur du champ maladie
    */
   public static Object getChampValueFromPrelevement(final Prelevement prel, final Champ chp, final Champ parent){
      if(prel != null && chp != null){
         if(chp.getChampDelegue() != null) {

            if("Protocoles".equals(chp.getChampDelegue().getNom()) && prel.getDelegate() != null) {
               PrelevementSero prelSero = ((PrelevementSero)prel.getDelegate());
               if(prelSero.getProtocoles() != null) {
                  return prelSero.getProtocoles().stream().map(Protocole::getNom).sorted().collect(Collectors.joining(","));
               }
            }

            return getChampValueForObject(chp, prel, false);
         }
         else if(chp.getChampEntite() != null){
            
            if("Risques".equals(chp.getChampEntite().getNom())) {
               return prel.getRisques().stream().map(Risque::getNom).collect(Collectors.joining(","));
            }
            
            if(parent == null){
               if(chp.getChampEntite().getNom().equals("EtablissementId")){
                  if(prel.getServicePreleveur() != null){
                     return prel.getServicePreleveur().getEtablissement().getNom();
                  }else if(prel.getPreleveur() != null && prel.getPreleveur().getEtablissement() != null){
                     return prel.getPreleveur().getEtablissement().getNom();
                  }
               }

               return getChampValueForObject(chp, prel, false);

            }
            if(parent.getChampEntite().getNom().equals("MaladieId")){
               return getChampValueFromMaladie(prel.getMaladie(), parent, null);
            }else if(parent.getChampEntite().getNom().equals("Risques")){
               final Iterator<Risque> risksIt = prelevementManager.getRisquesManager(prel).iterator();
               final StringBuffer sb = new StringBuffer();
               while(risksIt.hasNext()){
                  sb.append(risksIt.next().getNom());
                  if(risksIt.hasNext()){
                     sb.append(";");
                  }
               }
               return sb.toString();
            }else if(parent.getChampEntite().getNom().matches("ConformeArrivee.Raison")){

               return formatNonConformites(prel, parent.getChampEntite());
            }else{
               return getChampValueForObject(parent, prel, false);
            }
         }else if(chp.getChampAnnotation() != null){
            return getChampValueFromPrelevement(prel, chp.getChampAnnotation(), parent, true);
         }
      }
      return null;
   }

   /**
    * Retourne la valeur d'un champ prelevement à partir d'un champ
    * @param prel prelevement
    * @param chp champ recherché
    * @param parent champ parent
    * @return valeur du champ maladie
    */
   public static Object getChampValueFromPrelevement(final Prelevement prel, final ChampAnnotation chp, final Champ parent, final Boolean prettyFormat){
      if(prel != null && chp != null){
         return getChampAnnotationValeur(prel, chp, prettyFormat);
      }
      return null;
   }

   /**
    * Retourne la valeur d'un champ prelevement à partir d'une liste d'objets
    * @param chp champ recherché
    * @param parent champ parent
    * @param listeObjets liste d'objets où rechercher l'objet maladie
    * @return valeur du champ maladie
    */
   public static Object getChampValueFromPrelevement(final Champ chp, final Champ parent, final List<Object> listeObjets){
      final Prelevement recup = getObjectFromList(listeObjets, Prelevement.class);
      if(null != recup){
         return getChampValueFromPrelevement(recup, chp, parent);
      }
      return null;
   }

   /**
    * Retourne la valeur d'un champ prelevement à partir d'une liste d'objets
    * @param chp champ recherché
    * @param parent champ parent
    * @param listeObjets liste d'objets où rechercher l'objet maladie
    * @return valeur du champ maladie
    */
   public static Object getChampValueFromPrelevement(final ChampAnnotation chp, final Champ parent,
      final List<Object> listeObjets, final Boolean prettyFormat){
      final Prelevement recup = getObjectFromList(listeObjets, Prelevement.class);
      if(null != recup){
         return getChampValueFromPrelevement(recup, chp, parent, prettyFormat);
      }
      return null;
   }

   /**
    * ??
    * @param obj
    * @param champEntite
    * @return
    */
   private static String formatNonConformites(final Object obj, final ChampEntite champEntite){
      String cNom = "";
      final Pattern p = Pattern.compile("Conforme(.*)\\.Raison");
      final Matcher m = p.matcher(champEntite.getNom());
      final boolean b = m.matches();
      if(b && m.groupCount() > 0){
         cNom = m.group(1);
      }
      final Iterator<ObjetNonConforme> ncsIt = objetNonConformeManager
         .findByObjetAndTypeManager(obj, conformiteTypeManager.findByEntiteAndTypeManager(cNom, champEntite.getEntite()).get(0))
         .iterator();
      final StringBuffer sb = new StringBuffer();
      while(ncsIt.hasNext()){
         sb.append(ncsIt.next().getNonConformite().getNom());
         if(ncsIt.hasNext()){
            sb.append(";");
         }
      }
      return sb.toString();
   }

   /**
    * Retourne la valeur d'un champ echantillon à partir d'un champ
    * @param echanDeco echantillon
    * @param chp champ recherché
    * @param parent champ parent
    * @return valeur du champ echantillon
    */
   public static Object getChampValueFromEchantillon(final EchantillonDTO echanDeco, final Champ chp, final Champ parent){
      if(echanDeco.getEchantillon() != null && chp != null){
         if(chp.getChampEntite() != null){
            if(parent == null){
               if(chp.getChampEntite().getNom().equals("EmplacementId")){
                  return echanDeco.getEmplacementAdrl();
                  // since 2.0.13 temp stock
               }else if(chp.getChampEntite().getNom().equals("TempStock")){
                  return echanDeco.getTempStock();
               }else if(chp.getChampEntite().getNom().equals("CodeOrganes")){
                  final List<String> codes = codeAssigneManager.formatCodesAsStringsManager(
                     codeAssigneManager.findCodesOrganeByEchantillonManager(echanDeco.getEchantillon()));
                  final StringBuffer sb = new StringBuffer();
                  for(int k = 0; k < codes.size(); k++){
                     sb.append(codes.get(k));
                     if(k + 1 < codes.size()){
                        sb.append(", ");
                     }
                  }
                  return sb.toString();
               }else if(chp.getChampEntite().getNom().equals("CodeMorphos")){
                  final List<String> codes = codeAssigneManager.formatCodesAsStringsManager(
                     codeAssigneManager.findCodesMorphoByEchantillonManager(echanDeco.getEchantillon()));
                  final StringBuffer sb = new StringBuffer();
                  for(int k = 0; k < codes.size(); k++){
                     sb.append(codes.get(k));
                     if(k + 1 < codes.size()){
                        sb.append(", ");
                     }
                  }
                  return sb.toString();
               }else{
                  return getChampValueForObject(chp, echanDeco.getEchantillon(), false);
               }
            }
            if(parent.getChampEntite().getNom().equals("PrelevementId") && echanDeco.getEchantillon().getPrelevement() != null){
               return getChampValueFromPrelevement(echanDeco.getEchantillon().getPrelevement(), parent, null);
            }else if(parent.getChampEntite().getNom().matches("Conforme.*Raison")){
               return formatNonConformites(echanDeco.getEchantillon(), parent.getChampEntite());
            }else{
               return getChampValueForObject(parent, echanDeco.getEchantillon(), false);
            }
         }else if(chp.getChampAnnotation() != null){
            return getChampValueFromEchantillon(echanDeco, chp.getChampAnnotation(), parent, true);
         }
      }
      return null;
   }

   /**
    * Retourne la valeur d'un champ echantillon à partir d'un champ
    * @param echanDeco echantillon
    * @param chp champ recherché
    * @param parent champ parent
    * @return valeur du champ echantillon
    */
   public static Object getChampValueFromEchantillon(final EchantillonDTO echanDeco, final ChampAnnotation chp,
      final Champ parent, final Boolean prettyFormat){
      if(echanDeco.getEchantillon() != null && chp != null){
         return getChampAnnotationValeur(echanDeco.getEchantillon(), chp, prettyFormat);
      }
      return null;
   }

   /**
    * Retourne la valeur d'un champ echantillon à partir d'une liste d'objets
    * @param chp champ recherché
    * @param parent champ parent
    * @param listeObjets liste d'objets où rechercher l'objet echantillon
    * @return valeur du champ echantillon
    */
   public static Object getChampValueFromEchantillon(final Champ chp, final Champ parent, final List<Object> listeObjets){
      final Echantillon recup = getObjectFromList(listeObjets, Echantillon.class);
      if(null != recup){
         final EchantillonDTO echDeco = echantillonDTOManager.initEchantillonDecoratorManager(recup);
         return getChampValueFromEchantillon(echDeco, chp, parent);
      }
      return null;
   }

   /**
    * Retourne la valeur d'un champ echantillon à partir d'une liste d'objets
    * @param chp champ recherché
    * @param parent champ parent
    * @param listeObjets liste d'objets où rechercher l'objet echantillon
    * @return valeur du champ echantillon
    */
   public static Object getChampValueFromEchantillon(final ChampAnnotation chp, final Champ parent,
      final List<Object> listeObjets, final Boolean prettyFormat){
      final Echantillon recup = getObjectFromList(listeObjets, Echantillon.class);
      if(null != recup){
         final EchantillonDTO echDeco = echantillonDTOManager.initEchantillonDecoratorManager(recup);
         return getChampValueFromEchantillon(echDeco, chp, parent, prettyFormat);
      }
      return null;
   }

   /**
    * Retourne la valeur d'un champ produit dérivé à partir d'un champ
    * @param prodDerive produit dérivé
    * @param chp champ recherché
    * @param parent champ parent
    * @return valeur du champ produit dérivé
    */
   public static Object getChampValueFromDerive(final ProdDerive prodDerive, final Champ chp, final Champ parent){
      if(prodDerive != null && chp != null){
         if(chp.getChampEntite() != null){
            if(parent == null){
               if(chp.getChampEntite().getNom().equals("EmplacementId")){
                  return prodDeriveManager.getEmplacementAdrlManager(prodDerive);
                  //                  return prodDeco.getEmplacementAdrl();
                  // since 2.0.13 temp stock
               }else if(chp.getChampEntite().getNom().equals("TempStock")){
                  final Float temp = conteneurManager.findTempForEmplacementManager(prodDerive.getEmplacement());
                  if(temp != null){
                     return temp.toString();
                  }
                  //                  return prodDeco.getTempStock();
               }else{
                  return getChampValueForObject(chp, prodDerive, false);
               }
            }
            if(null != parent && parent.getChampEntite().getNom().matches("Conforme.*Raison")){
               return formatNonConformites(prodDerive, parent.getChampEntite());
            }

            return getChampValueForObject(parent, prodDerive, false);

         }else if(chp.getChampAnnotation() != null){
            return getChampValueFromDerive(prodDerive, chp.getChampAnnotation(), parent, true);
         }
      }
      return null;
   }

   /**
    * Retourne la valeur d'un champ produit dérivé à partir d'un champ
    * @param prodDerive produit dérivé
    * @param chp champ recherché
    * @param parent champ parent
    * @return valeur du champ produit dérivé
    */
   public static Object getChampValueFromDerive(final ProdDerive prodDerive, final ChampAnnotation chp, final Champ parent, final Boolean prettyFormat){
      if(prodDerive != null && chp != null){
         return getChampAnnotationValeur(prodDerive, chp, prettyFormat);
      }
      return null;
   }

   /**
    * Retourne la valeur d'un champ produit dérivé à partir d'une liste d'objets
    * @param chp champ recherché
    * @param parent champ parent
    * @param listeObjets liste d'objets où rechercher l'objet echantillon
    * @return valeur du champ produit dérivé
    */
   public static Object getChampValueFromDerive(final Champ chp, final Champ parent, final List<Object> listeObjets){
      final ProdDerive recup = getObjectFromList(listeObjets, ProdDerive.class);
      if(null != recup){
         return getChampValueFromDerive(recup, chp, parent);
      }

      return null;
   }

   /**
    * Retourne la valeur d'un champ produit dérivé à partir d'une liste d'objets
    * @param chp champ recherché
    * @param parent champ parent
    * @param listeObjets liste d'objets où rechercher l'objet echantillon
    * @return valeur du champ produit dérivé
    */
   public static Object getChampValueFromDerive(final ChampAnnotation chp, final Champ parent, final List<Object> listeObjets, final Boolean prettyFormat){
      final ProdDerive recup = getObjectFromList(listeObjets, ProdDerive.class);
      if(null != recup){
         return getChampValueFromDerive(recup, chp, parent, prettyFormat);
      }

      return null;
   }

   public static Object getChampValueFromCession(final Cession cession, final Champ chp, final Champ parent){
      if(cession != null && chp != null){
         if(chp.getChampEntite() != null){
            if(parent == null){
               return getChampValueForObject(chp, cession, false);

            }
            return getChampValueForObject(parent, cession, false);

         }else if(chp.getChampAnnotation() != null){
            return getChampValueFromCession(cession, chp.getChampAnnotation(), true);
         }
      }
      return null;
   }

   public static Object getChampValueFromCession(final Cession cession, final ChampAnnotation chp, final Boolean prettyFormat){
      if(cession != null && chp != null){
         return getChampAnnotationValeur(cession, chp, prettyFormat);
      }
      return null;
   }

   public static Object getChampValueFromCession(final Champ chp, final Champ parent, final List<Object> listeObjets){
      final Cession recup = getObjectFromList(listeObjets, Cession.class);
      if(null != recup){
         return getChampValueFromCession(recup, chp, parent);
      }

      return null;
   }

   public static Object getChampValueFromCession(final ChampAnnotation chp, final List<Object> listeObjets, final Boolean prettyFormat){
      final Cession recup = getObjectFromList(listeObjets, Cession.class);
      if(null != recup){
         return getChampValueFromCession(recup, chp, prettyFormat);
      }

      return null;
   }

   /**
    * Retourne la valeur d'un champ d'une entité recherchée dans une liste d'objet
    * @param entite objet entite
    * @param chp champ à récupérer
    * @param parent champ parent du champ a récupérer
    * @param listeObjets liste d'objets à partire de laquelle récuperer l'entité puis la valeur du champ
    * @return la valeur du champ pour l'entité patient
    */
   public static Object getChampValueFromEntite(final Entite entite, final Champ chp, final Champ parent,
      final List<Object> listeObjets){
      Object value = null;
      switch(entite.getNom()){
         case "Patient":
            value = getChampValueFromPatient(chp, listeObjets);
            break;
         case "Maladie":
            value = getChampValueFromMaladie(chp, parent, listeObjets);
            break;
         case "Prelevement":
         case "Service":
            value = getChampValueFromPrelevement(chp, parent, listeObjets);
            break;
         case "Echantillon":
            value = getChampValueFromEchantillon(chp, parent, listeObjets);
            break;
         case "ProdDerive":
            value = getChampValueFromDerive(chp, parent, listeObjets);
            break;
         case "Cession":
            value = getChampValueFromCession(chp, parent, listeObjets);
            break;
         default:
            break;
      }

      return value;
   }

   /**
    * Retourne la valeur d'un champ d'une entité recherchée dans une liste d'objet
    * @param entite objet entite
    * @param chp champ à récupérer
    * @param parent champ parent du champ a récupérer
    * @param listeObjets liste d'objets à partire de laquelle récuperer l'entité puis la valeur du champ
    * @return la valeur du champ pour l'entité patient
    */
   public static Object getChampValueFromEntite(final Entite entite, final ChampAnnotation chp, final Champ parent,
      final List<Object> listeObjets){
      Object value = null;
      switch(entite.getNom()){
         case "Patient":
            value = getChampValueFromPatient(chp, listeObjets, false);
            break;
         case "Prelevement":
         case "Service":
            value = getChampValueFromPrelevement(chp, parent, listeObjets, false);
            break;
         case "Echantillon":
            value = getChampValueFromEchantillon(chp, parent, listeObjets, false);
            break;
         case "ProdDerive":
            value = getChampValueFromDerive(chp, parent, listeObjets, false);
            break;
         case "Cession":
            value = getChampValueFromCession(chp, listeObjets, false);
            break;
         default:
            break;
      }

      return value;
   }

}
