package fr.aphp.tumorotek.webapp.general;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Row;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.model.coeur.patient.Patient;
import fr.aphp.tumorotek.model.contexte.Banque;
import fr.aphp.tumorotek.model.utilisateur.Utilisateur;
import fr.aphp.tumorotek.param.TkParam;
import fr.aphp.tumorotek.param.TumorotekProperties;

@SuppressWarnings("deprecation")
public class ConnexionCrf extends GenericForwardComposer<Component>
{

   private static final long serialVersionUID = -6157769915889694408L;

   private final Log log = LogFactory.getLog(ConnexionCrf.class);

   private Row rowWait;
   private Row rowError;
   private Row rowInactive;

   // variables POST
   private String login = null;
   private String pass = null;
   private String banque = null;
   private String nip = null;
   private String nom = null;
   private String nomNaissance = null;
   private String prenom = null;
   private String sexe = null;
   private String dateNaissance = null;

   private Banque selectedBanque = null;
   private Utilisateur user = null;

   @Override
   public void doAfterCompose(final Component comp) throws Exception{
      super.doAfterCompose(comp);

      // on vérifie que la connexion est bien active
      if(connexionActive()){
         rowInactive.setVisible(false);
         extractParameters();

         if(selectedBanque != null && logUser()){
            rowWait.setVisible(true);
            rowError.setVisible(false);

            user = ConnexionUtils.getLoggedUtilisateur();

            redirect();

         }else{
            if(selectedBanque != null){
               log.info("La tentative de connection " + login + " a échoué " + "car les paramètres de connection sont invalides");
            }else{
               log.info("Aucune banque trouvée ayant l'identifiant " + "'" + banque + "'");
            }
            rowWait.setVisible(false);
            rowError.setVisible(true);
         }
      }else{
         rowInactive.setVisible(true);
         rowWait.setVisible(false);
         rowError.setVisible(false);
      }
   }

   /**
    * envoie true si la connexion depuis un crf est active.
    * @return
    */
   public boolean connexionActive(){
      boolean active = false;

      // on récupère le bundle de paramétrage de l'application
      ResourceBundle res = null;
      if(ManagerLocator.getResourceBundleTumo().doesResourceBundleExists(TumorotekProperties.TUMO_PROPERTIES_FILENAME)){
         res = ManagerLocator.getResourceBundleTumo().getResourceBundle(TumorotekProperties.TUMO_PROPERTIES_FILENAME);
      // on récupère la propriété définissant les interfaçages
         String connexion = null;
         if(res.containsKey(TkParam.CONNEXION_CRF.getKey())){
            connexion = res.getString(TkParam.CONNEXION_CRF.getKey());
         }
         if(connexion != null && connexion.equals("true")){
            // extraction des interfaçages
            active = true;
         }
      }
      return active;
   }

   /**
    * Extrait les paramètres passés dans l'url.
    */
   public void extractParameters(){
      // extraction du login
      String[] tmp = (Executions.getCurrent().getParameterMap().get("loginCrf"));
      if(tmp != null && tmp.length > 0){
         login = tmp[0];
      }else{
         login = null;
      }

      // extraction du mdp
      tmp = (Executions.getCurrent().getParameterMap().get("passCrf"));
      if(tmp != null && tmp.length > 0){
         pass = tmp[0];
      }else{
         pass = null;
      }

      // extraction de la banque
      tmp = (Executions.getCurrent().getParameterMap().get("banque"));
      if(tmp != null && tmp.length > 0){
         banque = tmp[0];
         try{
            final Integer id = Integer.parseInt(banque);
            selectedBanque = ManagerLocator.getBanqueManager().findByIdManager(id);
         }catch(final Exception e){
            selectedBanque = null;
         }
      }else{
         banque = null;
         selectedBanque = null;
      }

      // extraction du nip
      tmp = (Executions.getCurrent().getParameterMap().get("nip"));
      if(tmp != null && tmp.length > 0){
         nip = tmp[0];
      }else{
         nip = null;
      }

      // extraction du nom
      tmp = (Executions.getCurrent().getParameterMap().get("nom"));
      if(tmp != null && tmp.length > 0){
         nom = tmp[0];
      }else{
         nom = null;
      }

      // extraction du nomNaissance
      tmp = (Executions.getCurrent().getParameterMap().get("nomNaissance"));
      if(tmp != null && tmp.length > 0){
         nomNaissance = tmp[0];
      }else{
         nomNaissance = null;
      }

      // extraction du prenom
      tmp = (Executions.getCurrent().getParameterMap().get("prenom"));
      if(tmp != null && tmp.length > 0){
         prenom = tmp[0];
      }else{
         prenom = null;
      }

      // extraction du sexe
      tmp = (Executions.getCurrent().getParameterMap().get("sexe"));
      if(tmp != null && tmp.length > 0){
         sexe = tmp[0];
      }else{
         sexe = null;
      }

      // extraction de la date de naissance
      tmp = (Executions.getCurrent().getParameterMap().get("dateNaissance"));
      if(tmp != null && tmp.length > 0){
         dateNaissance = tmp[0];
      }else{
         dateNaissance = null;
      }
   }

   /**
    * Log un utilisateur et renvoie true si la connection s'est
    * bien passée.
    * @param login
    * @param pass
    * @return
    */
   public boolean logUser(){
      boolean ok = false;

      if(login != null && pass != null){

         // on transforme le mdp en MD5
         // final PasswordEncoder encoder = new Md5PasswordEncoder();
         // final String pwd = encoder.encodePassword(pass, null);
    	String pwd = pass;

         if(ManagerLocator.getUtilisateurManager().findByLoginPasswordAndArchiveManager(login, pwd, false).size() > 0){
            ok = true;

            // création d'un utilisateur pour SpringSecurity
            final User u = new User(login, pwd, true, true, true, true, getAuthorities(false));

            // Authentification de cet utilisateur
            final Authentication auth = new UsernamePasswordAuthenticationToken(u, null, getAuthorities(false));

            // on met l'utilisateur dans SpringSecurity
            SecurityContextHolder.getContext().setAuthentication(auth);
         }else{
            ok = false;
         }
      }
      return ok;
   }

private Collection<? extends GrantedAuthority> getAuthorities(boolean b) {
	return null;
}

//   private Collection<? extends GrantedAuthority> getAuthorities(final boolean isAdmin){
//      final List<GrantedAuthority> authList = new ArrayList<>(2);
//      authList.add(new GrantedAuthorityImpl("ROLE_USER"));
//      if(isAdmin){
//         authList.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
//      }
//      return authList;
//   }

   /**
    * Valide la page et redirige vers TumoroteK.
    */
   public void redirect(){
      if(selectedBanque != null){
         sessionScope.put("User", user);
         sessionScope.put("Plateforme", selectedBanque.getPlateforme());
         sessionScope.put("Banque", selectedBanque);
         final List<Banque> bks = new ArrayList<>();
         bks.add(selectedBanque);
         ConnexionUtils.setSessionCatalogues(bks, sessionScope);
         ConnexionUtils.generateDroitsForSelectedBanque(selectedBanque, 
        		 selectedBanque.getPlateforme(), user, sessionScope);
         sessionScope.remove("ToutesCollections");

         // gestion des interfaçages
         ConnexionUtils.initInterfacages(selectedBanque.getPlateforme(), sessionScope);

         // on passe le patient dans la session
         sessionScope.put("patient", createPatientFromParameters());

         Executions.sendRedirect("/zuls/main/main.zul");
      }
   }

   public void onClientInfo$winConnectionCrf(final ClientInfoEvent event){
      sessionScope.put("screenWidth", event.getDesktopWidth());
      sessionScope.put("screenHeight", event.getDesktopHeight());
   }

   /**
    * Crée et initialise un objet Patient à partir des paramètres
    * passés dans l'url.
    * @return
    */
   public Patient createPatientFromParameters(){
      final Patient pat = new Patient();

      pat.setNip(nip);
      pat.setNom(nom);
      pat.setNomNaissance(nomNaissance);
      pat.setPrenom(prenom);
      pat.setSexe(sexe);

      Date date = null;
      if(dateNaissance != null){
         final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
         try{
            date = sdf.parse(dateNaissance);
         }catch(final ParseException e){
         }
      }
      pat.setDateNaissance(date);

      return pat;
   }

   public String getLogin(){
      return login;
   }

   public void setLogin(final String l){
      this.login = l;
   }

   public String getPass(){
      return pass;
   }

   public void setPass(final String p){
      this.pass = p;
   }

   public String getBanque(){
      return banque;
   }

   public void setBanque(final String b){
      this.banque = b;
   }

   public Banque getSelectedBanque(){
      return selectedBanque;
   }

   public void setSelectedBanque(final Banque s){
      this.selectedBanque = s;
   }

   public Utilisateur getUser(){
      return user;
   }

   public void setUser(final Utilisateur u){
      this.user = u;
   }

   public String getNip(){
      return nip;
   }

   public void setNip(final String n){
      this.nip = n;
   }

   public String getNom(){
      return nom;
   }

   public void setNom(final String n){
      this.nom = n;
   }

   public String getNomNaissance(){
      return nomNaissance;
   }

   public void setNomNaissance(final String n){
      this.nomNaissance = n;
   }

   public String getPrenom(){
      return prenom;
   }

   public void setPrenom(final String p){
      this.prenom = p;
   }

   public String getSexe(){
      return sexe;
   }

   public void setSexe(final String s){
      this.sexe = s;
   }

   public String getDateNaissance(){
      return dateNaissance;
   }

   public void setDateNaissance(final String d){
      this.dateNaissance = d;
   }

}
