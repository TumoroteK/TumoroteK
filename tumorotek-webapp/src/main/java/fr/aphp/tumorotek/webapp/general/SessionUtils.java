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
package fr.aphp.tumorotek.webapp.general;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Sessions;

import fr.aphp.tumorotek.manager.impl.interfacage.ResultatInjection;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.contexte.gatsbi.Contexte;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.interfacage.Recepteur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.param.TkParam;
import fr.aphp.tumorotek.utils.Utils;

/**
 * Utility class fournissant les methodes récupérant les variables de session.
 * Date: 26/07/2010.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.0
 * @since 2.0
 */
public final class SessionUtils
{

   private SessionUtils(){}

   private static String databasePathClass = null;

   private final static String MYSQL_DB = "mysql";

   private final static String ORACLE_DB = "oracle";

   /**
    * Renvoie la liste de banque selectionnées par l'utilisateur et passée en
    * variable de session.
    *
    * @return liste de Banque
    */
   @SuppressWarnings("unchecked")
   public static List<Banque> getSelectedBanques(final Map<?, ?> sessionScp){
      final List<Banque> banks = new ArrayList<>();
      if(sessionScp.containsKey("Banque")){
         banks.add((Banque) sessionScp.get("Banque"));
      }else if(sessionScp.containsKey("ToutesCollections")){
         banks.addAll((List<Banque>) sessionScp.get("ToutesCollections"));
      }
      return banks;
   }

   public static Banque getCurrentBanque(final Map<?, ?> sessionScp){
      final List<Banque> banks = getSelectedBanques(sessionScp);
      return !banks.isEmpty() ? banks.get(0) : null;
   }

   public static Utilisateur getLoggedUser(final Map<?, ?> sessionScp){
      return (Utilisateur) sessionScp.get("User");
   }

   public static Plateforme getPlateforme(final Map<?, ?> sessionScp){
      return (Plateforme) sessionScp.get("Plateforme");
   }

   /**
    * Indique si l'utilisateur est connecté en tant qu'admin de plateforme.
    *
    * @param sessionScp
    * @return true si oui.
    */
   public static Boolean isAdminPF(final Map<String, Object> sessionScp){
      if(sessionScp.get("AdminPF") != null){
         return (Boolean) sessionScp.get("AdminPF");
      }
      return false;
   }

   /**
    * Indique si parmi les banques selectionnées par l'utilisateur au moins une
    * définit des maladies.
    *
    * @param banks liste de banques
    * @return boolean true/false
    */
   public static boolean isAnyDefMaladieInBanques(final List<Banque> banks){
      for(final Banque bank : banks){
         if(bank.getDefMaladies()){
            return true;
         }
      }
      return false;
   }

   /**
    * Accède via JNDI au path spécifié en tant que base directory dans le
    * ficher de conf du server.
    *
    * @return base Directory path
    */
   public static String getSystemBaseDir(){
      return TkParam.FILESYSTEM.getValue();
   }

   /**
    *
    * @return db class to export
    */
   public static String getDatabasePathClass(){
      final String databaseUrl = Utils.getDatabaseURL();
      if(databaseUrl.contains(MYSQL_DB)){
         databasePathClass = "fr.aphp.tumorotek.webapp.general.export.Export_MySQL";
      }else if(databaseUrl.contains(ORACLE_DB)){
         databasePathClass = "fr.aphp.tumorotek.webapp.general.export.Export_Oracle";
      }
      return databasePathClass;
   }

   public static Boolean isOracleDBMS(){
      return Utils.getDatabaseURL().contains(ORACLE_DB);
   }

   /**
    * Renvoie la liste d'emetteurs définis comme interfacages.
    *
    * @return liste d'Emetteurs
    */
   @SuppressWarnings("unchecked")
   public static List<Emetteur> getEmetteursInterfacages(final Map<?, ?> sessionScp){
      final List<Emetteur> emetteurs = new ArrayList<>();
      if(sessionScp.containsKey("Emetteurs")){
         emetteurs.addAll((List<Emetteur>) sessionScp.get("Emetteurs"));
      }
      return emetteurs;
   }

   /**
    * Renvoie la liste de recepteurs définis comme interfacages.
    *
    * @return liste de Recepteurs
    */
   @SuppressWarnings("unchecked")
   public static List<Recepteur> getRecepteursInterfacages(final Map<?, ?> sessionScp){
      final List<Recepteur> recepteurs = new ArrayList<>();
      if(sessionScp.containsKey("Recepteurs")){
         recepteurs.addAll((List<Recepteur>) sessionScp.get("Recepteurs"));
      }
      return recepteurs;
   }

   /**
    * Retourne le type de base de données utilisé (mysql ou oracle).
    *
    * @return mysql ou oracle.
    */
   public static String getDbms(){

      String jdbcDialect = TkParam.TK_DATABASE_DIALECT.getValue();

      if(jdbcDialect.contains("Oracle")){
         jdbcDialect = "oracle";
      }else{
         jdbcDialect = "mysql";
      }

      return jdbcDialect;
   }

   public static Plateforme getCurrentPlateforme(){
      return (Plateforme) Sessions.getCurrent().getAttribute("Plateforme");
   }

   public static EContexte getCurrentContexte(){
      EContexte contexte = EContexte.DEFAUT;
      if(null != Sessions.getCurrent().getAttribute("Banque")){
         contexte = EContexte.valueOf(((Banque) Sessions.getCurrent().getAttribute("Banque")).getContexte().getNom());
      }else if(null != Sessions.getCurrent().getAttribute("ToutesCollections")){
         contexte = EContexte
            .valueOf(((List<Banque>) Sessions.getCurrent().getAttribute("ToutesCollections")).get(0).getContexte().getNom());
      }
      return contexte;
   }

   /**
    * @version 2.3.0-gatsbi
    * @return Contexte gatsbi
    */
   public static List<Contexte> getGatsbiContextes(){
      final List<Contexte> contextes = new ArrayList<>();
      if(null != Sessions.getCurrent().getAttribute("Banque")
         && ((Banque) Sessions.getCurrent().getAttribute("Banque")).getEtude() != null){
         contextes.addAll(((Banque) Sessions.getCurrent().getAttribute("Banque")).getEtude().getContextes());
      }
      return contextes;
   }

   /**
   * Rnvoie le contexte à appliquer pour une entité/onglet.
    * @version 2.3.0-gatsbi
    * @return Contexte gatsbi
    */
   @SuppressWarnings("unchecked")
   public static Contexte getCurrentGatsbiContexteForEntiteId(final Integer eId){
      Contexte gatsbiContexte = null;

      // trouve le contexte depuis l'étude GATSBI, venant de la banque sélectionnée,
      // ou la première banque de la liste si 'Toutes collections' sélectionné
      if(Sessions.getCurrent().getAttribute("Banque") != null
         && ((Banque) Sessions.getCurrent().getAttribute("Banque")).getEtude() != null){
         gatsbiContexte = ((Banque) Sessions.getCurrent().getAttribute("Banque")).getEtude().getContexteForEntite(eId);
      }else if(Sessions.getCurrent().getAttribute("ToutesCollections") != null
         && ((List<Banque>) Sessions.getCurrent().getAttribute("ToutesCollections")).get(0).getEtude() != null){
         gatsbiContexte =
            ((List<Banque>) Sessions.getCurrent().getAttribute("ToutesCollections")).get(0).getEtude().getContexteForEntite(eId);
      }

      return gatsbiContexte;
   }

   /**
    * Dossier externe en cours d'utilisation
    * @param sessionScp
    * @return ResultatInjection contenant reference vers DossierExterne
    */
   public static ResultatInjection getDossierExterneInjection(final Map<String, Object> sessionScp){
      if(sessionScp.get("dossierExt") != null){
         return (ResultatInjection) sessionScp.get("dossierExt");
      }
      return null;
   }

   public static void setDossierExterneInjection(final Map<String, Object> sessionScp, final ResultatInjection res){
      sessionScp.remove("dossierExt");
      if(res != null){
         sessionScp.put("dossierExt", res);
      }
   }

   /**
    * Initialise la liste des objets du thésaurus à afficher.
    *
    * @param reset
    *            si manager change
    */
   //   private static CrudManager<?> thesManager = null;

   /*public static List<?> getThesaurusListeValeurs(final String typeThesaurus){
      List<?> listValeurs = new ArrayList<>();

      switch(typeThesaurus){
         case "Nature":
            thesManager = ManagerLocator.getNatureManager();
            break;
         case "PrelevementType":
            thesManager = ManagerLocator.getPrelevementTypeManager();
            break;
         case "EchantillonType":
            thesManager = ManagerLocator.getEchantillonTypeManager();
            break;
         case "EchanQualite":
            thesManager = ManagerLocator.getEchanQualiteManager();
            break;
         case "ProdType":
            thesManager = ManagerLocator.getProdTypeManager();
            break;
         case "ProdQualite":
            thesManager = ManagerLocator.getProdQualiteManager();
            break;
         case "ConditType":
            thesManager = ManagerLocator.getConditTypeManager();
            break;
         case "ConditMilieu":
            thesManager = ManagerLocator.getConditMilieuManager();
            break;
         case "ConsentType":
            thesManager = ManagerLocator.getConsentTypeManager();
            break;
         case "Risque":
            thesManager = ManagerLocator.getRisqueManager();
            break;
         case "ModePrepa":
            thesManager = ManagerLocator.getModePrepaManager();
            break;
         case "ModePrepaDerive":
            thesManager = ManagerLocator.getModePrepaDeriveManager();
            break;
         case "CessionExamen":
            thesManager = ManagerLocator.getCessionExamenManager();
            break;
         case "DestructionMotif":
            thesManager = ManagerLocator.getDestructionMotifManager();
            break;
         case "ProtocoleType":
            thesManager = ManagerLocator.getProtocoleTypeManager();
            break;
         case "Specialite":
            thesManager = ManagerLocator.getSpecialiteManager();
            break;
         case "Categorie":
            thesManager = ManagerLocator.getCategorieManager();
            break;
         case "ConteneurType":
            thesManager = ManagerLocator.getConteneurTypeManager();
            break;
         case "EnceinteType":
            thesManager = ManagerLocator.getEnceinteTypeManager();
            break;
         case "Protocole":
            thesManager = ManagerLocator.getProtocoleManager();
            break;
      }

      // si c'est un thes de non conformité
      if(thesManager == null){
         switch(typeThesaurus){
            case "NonConformiteArrivee":
               listValeurs = ManagerLocator.getNonConformiteManager()
                  .findByPlateformeEntiteAndTypeStringManager(getCurrentPlateforme(), "Arrivee", null);
               break;
            case "NonConformiteTraitementEchan":
               listValeurs = ManagerLocator.getNonConformiteManager()
                  .findByPlateformeEntiteAndTypeStringManager(getCurrentPlateforme(), "Traitement", null);
               break;
            case "NonConformiteCessionEchan":
               listValeurs = ManagerLocator.getNonConformiteManager()
                  .findByPlateformeEntiteAndTypeStringManager(getCurrentPlateforme(), "Cession", null);
               break;
            case "NonConformiteTraitementDerive":
               listValeurs = ManagerLocator.getNonConformiteManager()
                  .findByPlateformeEntiteAndTypeStringManager(getCurrentPlateforme(), "TraitementDerive", null);
               break;
            case "NonConformiteCession":
               listValeurs = ManagerLocator.getNonConformiteManager()
                  .findByPlateformeEntiteAndTypeStringManager(getCurrentPlateforme(), "Cession", null);
               break;
         }
      }else if(thesManager instanceof PfDependantTKThesaurusManager){
         listValeurs = ((PfDependantTKThesaurusManager<?>) thesManager).findByOrderManager(getCurrentPlateforme());
      }else{
         if(typeThesaurus.equals("Specialite")){
            listValeurs = ((SpecialiteManager) thesManager).findAllObjectsManager();
         }else if(typeThesaurus.equals("Categorie")){
            listValeurs = ((CategorieManager) thesManager).findAllObjectsManager();
         }
      }
      return listValeurs;
   }*/
}
