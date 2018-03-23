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
package fr.aphp.tumorotek.webapp.general;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.Sessions;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.manager.CrudManager;
import fr.aphp.tumorotek.manager.TKThesaurusManager;
import fr.aphp.tumorotek.manager.context.CategorieManager;
import fr.aphp.tumorotek.manager.context.SpecialiteManager;
import fr.aphp.tumorotek.manager.impl.interfacage.ResultatInjection;
import fr.aphp.tumorotek.model.coeur.annotation.Catalogue;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.contexte.EContexte;
import fr.aphp.tumorotek.model.contexte.Plateforme;
import fr.aphp.tumorotek.model.interfacage.Emetteur;
import fr.aphp.tumorotek.model.interfacage.Recepteur;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.utils.Utils;

/**
 * Utility class fournissant les methodes récupérant les variables de session.
 * Date: 26/07/2010.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.0
 */
public final class SessionUtils
{

   private SessionUtils(){}

   private static Log log = LogFactory.getLog(SessionUtils.class);

   private static String baseDir = null;
   private static String databaseURL = null;
   private static String driverClass = null;
   private static String databasePathClass = null;
   private static String usernameDB = null;
   private static String passwordDB = null;

   private final static String MYSQL_DB = "mysql";
   private final static String ORACLE_DB = "oracle";
   private final static String seroContextName = "Serotek";
   private final static String banqueOrganeContextName = "CONT3";

   /**
    * Renvoie la liste de banque selectionnées par l'utilisateur et passée en
    * variable de session.
    * 
    * @return liste de Banque
    */
   
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
    * @param liste
    *            de banques
    * @return boolean true/false
    */
   public static boolean isAnyDefMaladieInBanques(final List<Banque> banks){
      final Iterator<Banque> it = banks.iterator();
      while(it.hasNext()){
         if(it.next().getDefMaladies()){
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
      if(baseDir == null){
         try{
            // get a handle on the JNDI root context
            final Context ctx = new InitialContext();

            // and access the environment variable for this web component
            baseDir = (String) ctx.lookup("java:comp/env/tk/tkFileSystem");
         }catch(final NamingException ex){
            log.error(ex);
         }
      }
      return baseDir;
   }

   /**
    * Accède via JNDI au path spécifié dans le ficher de conf du server.
    * 
    * @return database url path
    */
   public static String getDatabaseURL(){
      if(databaseURL == null){
         try{
            databaseURL = Utils.getDatabaseURL();
         }catch(final NamingException ex){
            log.error(ex);
         }
      }
      return databaseURL;
   }

   /**
    * Accède via JNDI au path spécifié dans le ficher de conf du server.
    * 
    * @return database url path
    */
   public static String getDriverClass(){
      if(driverClass == null){
         try{
            driverClass = Utils.getDriverClass();
         }catch(final NamingException ex){
            log.error(ex);
         }
      }
      return driverClass;
   }

   /**
    * 
    * @return db class to export
    */
   public static String getDatabasePathClass(){
      if(getDatabaseURL().contains(MYSQL_DB)){
         databasePathClass = "fr.aphp.tumorotek.webapp.general.export.Export_MySQL";
      }else if(getDatabaseURL().contains(ORACLE_DB)){
         databasePathClass = "fr.aphp.tumorotek.webapp.general.export.Export_Oracle";
      }
      return databasePathClass;
   }

   public static Boolean isOracleDBMS(){
      return getDatabaseURL().contains(ORACLE_DB);
   }

   /**
    * 
    * @return usernameDB
    */
   public static String getUsernameDB(){
      if(usernameDB == null){
         try{
            usernameDB = Utils.getUsernameDB();
         }catch(final NamingException ex){
            log.error(ex);
         }
      }
      return usernameDB;
   }

   /**
    * 
    * @return passwordDB
    */
   public static String getPasswordDB(){
      if(passwordDB == null){
         try{
            passwordDB = Utils.getPasswordDB();
         }catch(final NamingException ex){
            log.error(ex);
         }
      }
      return passwordDB;
   }

   public static Boolean isINCaCollection(final Map<?, ?> sessionScp){
      return sessionScp.containsKey("catalogues") && ((Map<String, Catalogue>) sessionScp.get("catalogues")).containsKey("INCa");
   }

   /**
    * Renvoie la liste d'emetteurs définis comme interfacages.
    * 
    * @return liste d'Emetteurs
    */
   
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
    * @throws NamingException
    *             Exception.
    */
   public static String getDbms() throws NamingException{

      final Context ctx = new InitialContext();
      String jdbcDialect = (String) ctx.lookup("java:comp/env/jdbc/dialect");

      if(jdbcDialect.contains("Oracle")){
         jdbcDialect = "oracle";
      }else{
         jdbcDialect = "mysql";
      }

      return jdbcDialect;
   }

   /**
    * Indique si la collection courante est de contexte Serotheque. Toutes
    * collections -> false car l'interface affiche les pages definies pour le
    * contexte par defaut de TK (anapath).
    * 
    * @param sessionScp
    *            session map
    * @return true si collection serotheque, false sinon.
    */
   public static boolean isSeroContexte(final Map<?, ?> sessionScp){
      return ((getSelectedBanques(sessionScp).size() == 1)
         && (getSelectedBanques(sessionScp).get(0).getContexte().getNom().equals(seroContextName)));
   }

   /**
    * @deprecated : Remplacer par getCurrentContexte()
    * @param sessionScp
    * @return
    */
   @Deprecated
   public static boolean isBanqueOrganeContexte(final Map<?, ?> sessionScp){
      return ((getSelectedBanques(sessionScp).size() == 1)
         && (getSelectedBanques(sessionScp).get(0).getContexte().getNom().equals(banqueOrganeContextName)));
   }

   public static Plateforme getCurrentPlateforme(){
      return (Plateforme) Sessions.getCurrent().getAttribute("Plateforme");
   }

   public static Enum<EContexte> getCurrentContexte(){
      EContexte contexte = EContexte.DEFAUT;
      if(null != Sessions.getCurrent().getAttribute("Banque")){
         contexte = EContexte.valueOf(((Banque) Sessions.getCurrent().getAttribute("Banque")).getContexte().getNom());
      }
      return contexte;
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
   private static CrudManager thesManager = null;

   public static List<? extends Object> getThesaurusListeValeurs(final String typeThesaurus){
      List<? extends Object> listValeurs = new ArrayList<>();

      if(typeThesaurus.equals("Nature")){
         thesManager = ManagerLocator.getNatureManager();
      }else if(typeThesaurus.equals("PrelevementType")){
         thesManager = ManagerLocator.getPrelevementTypeManager();
      }else if(typeThesaurus.equals("EchantillonType")){
         thesManager = ManagerLocator.getEchantillonTypeManager();
      }else if(typeThesaurus.equals("EchanQualite")){
         thesManager = ManagerLocator.getEchanQualiteManager();
      }else if(typeThesaurus.equals("ProdType")){
         thesManager = ManagerLocator.getProdTypeManager();
      }else if(typeThesaurus.equals("ProdQualite")){
         thesManager = ManagerLocator.getProdQualiteManager();
      }else if(typeThesaurus.equals("ConditType")){
         thesManager = ManagerLocator.getConditTypeManager();
      }else if(typeThesaurus.equals("ConditMilieu")){
         thesManager = ManagerLocator.getConditMilieuManager();
      }else if(typeThesaurus.equals("ConsentType")){
         thesManager = ManagerLocator.getConsentTypeManager();
      }else if(typeThesaurus.equals("Risque")){
         thesManager = ManagerLocator.getRisqueManager();
      }else if(typeThesaurus.equals("ModePrepa")){
         thesManager = ManagerLocator.getModePrepaManager();
      }else if(typeThesaurus.equals("ModePrepaDerive")){
         thesManager = ManagerLocator.getModePrepaDeriveManager();
      }else if(typeThesaurus.equals("CessionExamen")){
         thesManager = ManagerLocator.getCessionExamenManager();
      }else if(typeThesaurus.equals("DestructionMotif")){
         thesManager = ManagerLocator.getDestructionMotifManager();
      }else if(typeThesaurus.equals("ProtocoleType")){
         thesManager = ManagerLocator.getProtocoleTypeManager();
      }else if(typeThesaurus.equals("Specialite")){
         thesManager = ManagerLocator.getSpecialiteManager();
      }else if(typeThesaurus.equals("Categorie")){
         thesManager = ManagerLocator.getCategorieManager();
      }else if(typeThesaurus.equals("ConteneurType")){
         thesManager = ManagerLocator.getConteneurTypeManager();
      }else if(typeThesaurus.equals("EnceinteType")){
         thesManager = ManagerLocator.getEnceinteTypeManager();
      }else if(typeThesaurus.equals("Protocole")){
         thesManager = ManagerLocator.getProtocoleManager();
      }

      // si c'est un thes de non conformité
      if(thesManager == null){
         if(typeThesaurus.equals("NonConformiteArrivee")){
            listValeurs = ManagerLocator.getNonConformiteManager()
               .findByPlateformeEntiteAndTypeStringManager(getCurrentPlateforme(), "Arrivee", null);
         }else if(typeThesaurus.equals("NonConformiteTraitementEchan")){
            listValeurs = ManagerLocator.getNonConformiteManager()
               .findByPlateformeEntiteAndTypeStringManager(getCurrentPlateforme(), "Traitement", null);
         }else if(typeThesaurus.equals("NonConformiteCessionEchan")){
            listValeurs = ManagerLocator.getNonConformiteManager()
               .findByPlateformeEntiteAndTypeStringManager(getCurrentPlateforme(), "Cession", null);
         }else if(typeThesaurus.equals("NonConformiteTraitementDerive")){
            listValeurs = ManagerLocator.getNonConformiteManager()
               .findByPlateformeEntiteAndTypeStringManager(getCurrentPlateforme(), "TraitementDerive", null);
         }else if(typeThesaurus.equals("NonConformiteCession")){
            listValeurs = ManagerLocator.getNonConformiteManager()
               .findByPlateformeEntiteAndTypeStringManager(getCurrentPlateforme(), "Cession", null);
         }
      }else if(thesManager instanceof TKThesaurusManager){
         listValeurs = ((TKThesaurusManager) thesManager).findByOrderManager(getCurrentPlateforme());
      }else{
         if(typeThesaurus.equals("Specialite")){
            listValeurs = ((SpecialiteManager) thesManager).findAllObjectsManager();
         }else if(typeThesaurus.equals("Categorie")){
            listValeurs = ((CategorieManager) thesManager).findAllObjectsManager();
         }
      }

      return listValeurs;

   }

}
