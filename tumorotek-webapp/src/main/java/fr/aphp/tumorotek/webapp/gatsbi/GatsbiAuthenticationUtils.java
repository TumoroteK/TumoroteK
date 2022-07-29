package fr.aphp.tumorotek.webapp.gatsbi;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.Execution;

import com.auth0.jwt.exceptions.JWTCreationException;

import fr.aphp.tumorotek.param.TkParam;
import fr.aphp.tumorotek.utils.JWTGenerator;
import fr.aphp.tumorotek.utils.TokenGenerator;

public class GatsbiAuthenticationUtils
{
   public static final String GATSBI_COOKIE_NAME = "auth-gatsbi";

   public static final String UNDEFINED_JWT = "";

   public static final int DUREE_VIE_JWT = 60 * 60 * 1000;//1h en millisecondes

   public static final int DUREE_VIE_COOKIE = 60 * 60 * 24;//24h en secondes

   public static final String ROLE_ADMIN_PLATEFORME = "AdminPF";

   public static final String ROLE_ADMIN_COLLECTION = "AdminCol";

   public static final String ROLE_SUPER_ADMIN = "Super";
   
   public static final String KEY_CLAIM_ROLE = "role";

   private final Log log = LogFactory.getLog(GatsbiAuthenticationUtils.class);

   private Execution execution;

   public GatsbiAuthenticationUtils(Execution execution){
      this.execution = execution;
   }

   /**
    * Pour sécuriser la connexion à Gatsbi : génération d'un JWT qui contient un tokenCSRF 
    * qui sera également passé en paramètre de l'appel de l'url d'authentification sur Gatsbi
    * @param login
    * @param plateformeId
    */
   public void connectToGatsbi(String login, String role, int plateformeId){
      String tokenCSRF = TokenGenerator.generateToken();
      ((HttpServletResponse) execution.getNativeResponse()).addCookie(buildCookie(buildJWT(login, role, tokenCSRF)));
      execution.sendRedirect(buildUrl(tokenCSRF, plateformeId), "_blank");
   }

   private String buildJWT(String login, String role, String tokenCSRF){
      try{
         Map<String, String> mapPrivateClaim = new HashMap<>();
         if(role != null){
            mapPrivateClaim.put(KEY_CLAIM_ROLE, role);
         }
         return JWTGenerator.generate("Gatsbi", login, DUREE_VIE_JWT, tokenCSRF, mapPrivateClaim);
      }catch(JWTCreationException e){
         log.error(e);
         //le cookie sera invalide donc l'authentication échouera
         return UNDEFINED_JWT;
      }
   }

   private Cookie buildCookie(String JWT){
      Cookie cookie = new Cookie(GATSBI_COOKIE_NAME, JWT);
      cookie.setMaxAge(DUREE_VIE_COOKIE);
      cookie.setPath("/");

      return cookie;
   }

   private String buildUrl(String tokenCSRF, int plateformeId){
      return MessageFormat.format(TkParam.GATSBI_APPLI_URL_PATH.getValue(),
         new Object[] {tokenCSRF, Integer.toString(plateformeId)});
   }
}
