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

import fr.aphp.tumorotek.dto.ParametreDTO;
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
import org.zkoss.zk.ui.Sessions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class fournissant les methodes récupérant les variables de session.
 * Date: 26/07/2010.
 *
 * @author Mathieu BARTHELEMY
 * @version 2.2.0
 * @since 2.0
 */
public final class SessionUtils {

   private SessionUtils(){
   }

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
      for(Banque bank : banks){
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
      }
      return contexte;
   }
   
   /**
    * @version 2.3.0-gatsbi
    * @return Contexte gatsbi
    */
   public static List<Contexte> getGatsbiContextes() {
	   List<Contexte> contextes = new ArrayList<Contexte>();
	   if (null != Sessions.getCurrent().getAttribute("Banque") 
			   && ((Banque) Sessions.getCurrent().getAttribute("Banque")).getEtude() != null) {
		   contextes.addAll(((Banque) Sessions.getCurrent().getAttribute("Banque"))
	        		 .getEtude().getContextes());
	   }
	  return contextes;
   }
   
   /**
	* Rnvoie le contexte à appliquer pour une entité/onglet.
    * @version 2.3.0-gatsbi
    * @return Contexte gatsbi
    */
   @SuppressWarnings("unchecked")
   public static Contexte getCurrentGatsbiContexteForEntiteId(Integer eId) {
	   Contexte gatsbiContexte = null;
	   
	   // trouve le contexte depuis l'étude GATSBI, venant de la banque sélectionnée, 
	   // ou la première banque de la liste si 'Toutes collections' sélectionné
	   if (Sessions.getCurrent().getAttribute("Banque") != null
			   && ((Banque) Sessions.getCurrent().getAttribute("Banque")).getEtude() != null) {
		   gatsbiContexte = ((Banque) Sessions.getCurrent().getAttribute("Banque"))
	        	.getEtude().getContexteForEntite(eId);
	   } else if (Sessions.getCurrent().getAttribute("ToutesCollections") != null 
			   && ((List<Banque>) Sessions.getCurrent()
					.getAttribute("ToutesCollections"))
			      .stream().map(c -> c.getEtude()).distinct().count() == 1) {
	      
	      if (((List<Banque>) Sessions.getCurrent().getAttribute("ToutesCollections"))
            .get(0).getEtude() != null) {
               gatsbiContexte = ((List<Banque>) Sessions.getCurrent().getAttribute("ToutesCollections"))
                  .get(0).getEtude().getContexteForEntite(eId);
            }
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
    * Renvoie true si l'affichage Toutes collections contiens un mélange de 
    * collections contextualisées par Gatsbi, avec d'autres collections non contextualisé.
    * Permet d'appliquer des désactivations sélectives des boutons de modification.
    * @return boolean
    */
   @SuppressWarnings("unchecked")
   public static boolean areToutesCollectionContainsOneGatsbi(){  
      return Sessions.getCurrent().getAttribute("ToutesCollections") != null 
         && ((List<Banque>) Sessions.getCurrent()
            .getAttribute("ToutesCollections")).stream().anyMatch(b -> b.getEtude() != null);  
   }

   /**
    * Stocker les paramètres de la plateforme dans la session.
    *
    * @param parametres List<ParametreDTO> paramètres de la plateforme à stocker
    * @param sessionScp la map de session où les paramètres seront stockés
    */
   public static void setPlatformParameters(List<ParametreDTO> parametres, final Map<String, Object> sessionScp) {
      sessionScp.put("Parametres", parametres);
   }

   /**
    * Stocker les paramètres de la plateforme dans la session.
    *
    * @param parametres List<ParametreDTO> paramètres de la plateforme à stocker
    */
   public static void setPlatformParameters(List<ParametreDTO> parametres) {
      Sessions.getCurrent().setAttribute("Parametres", parametres);
     }

   /**
    * Récupère les paramètres de la plateforme depuis la session.
    *
    * @return List<ParametreDTO> L'ensemble des paramètres de la plateforme.
    */
   public static List<ParametreDTO> getPlatformParameters(final Map<String, Object> sessionScp) {
      if(sessionScp.get("Parametres") != null){
         return (List<ParametreDTO>) sessionScp.get("Parametres");
      }
      return new ArrayList<>();
   }

   /**
    * Récupère les paramètres de la plateforme depuis la session.
    *
    * @return List<ParametreDTO> L'ensemble des paramètres de la plateforme.
    */
   public static List<ParametreDTO> getPlatformParameters() {
      // Récupère l'objet des paramètres depuis la session
      return (List<ParametreDTO>) Sessions.getCurrent().getAttribute("Parametres");

   }

   /**
    * Récupère un ParametreDTO en utilisant le code spécifié.
    *
    * @param codeToFind Le code à utiliser pour récupérer le ParametreDTO.
    * @return Le ParametreDTO correspondant au code spécifié, ou null s'il n'est pas trouvé.
    */
   public static ParametreDTO getParametreByCode(String codeToFind, Map<String, Object> sessionScp) {
      // Récupère l'ensemble des ParametreDTO à partir de la session de la plateforme
      List<ParametreDTO> parametreDTOList = getPlatformParameters(sessionScp);

      // Parcours l'ensemble des ParametreDTO pour trouver celui avec le code spécifié
      for (ParametreDTO parametreDTO : parametreDTOList) {
         // Vérifie si le code du ParametreDTO actuel correspond au code recherché
         if (parametreDTO.getCode().equals(codeToFind)) {
            // Retourne le ParametreDTO trouvé
            return parametreDTO;
         }
      }

      // Aucun ParametreDTO correspondant n'a été trouvé, retourne null
      return null;
   }


}
