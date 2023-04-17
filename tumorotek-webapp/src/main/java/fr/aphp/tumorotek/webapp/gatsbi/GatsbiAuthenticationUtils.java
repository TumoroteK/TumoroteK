/**
 * Copyright ou © ou Copr. Assistance Publique des Hôpitaux de 
 * PARIS et SESAN
 * projet-tk@sesan.fr
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
package fr.aphp.tumorotek.webapp.gatsbi;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Execution;

import com.auth0.jwt.exceptions.JWTCreationException;

import fr.aphp.tumorotek.action.ManagerLocator;
import fr.aphp.tumorotek.param.TkParam;
import fr.aphp.tumorotek.utils.JWTGenerator;
import fr.aphp.tumorotek.utils.TokenGenerator;

public class GatsbiAuthenticationUtils
{
   public static final String GATSBI_COOKIE_NAME = "auth-gatsbi";

   public static final String UNDEFINED_JWT = "";

   public static final int DUREE_VIE__JWT = 60 * 60 * 1000;//1h en millisecondes

   public static final int DUREE_VIE__COOKIE = 60 * 60 * 24;//24h en secondes

   public static final String ROLE__ADMIN_PLATEFORME = "AdminPF";

   public static final String ROLE__ADMIN_COLLECTION = "AdminCol";

   public static final String ROLE__SUPER_ADMIN = "Super";
   
   public static final String KEY__CLAIM_ROLE = "role";

   private final Logger log = LoggerFactory.getLogger(GatsbiAuthenticationUtils.class);

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
            mapPrivateClaim.put(KEY__CLAIM_ROLE, role);
         }
         int dureeVieJwt = DUREE_VIE__JWT;//valeur par défaut qui peut être surchargée par la conf
         if(TkParam.GATSBI_JWT_EXPIRATION.getValue() != null) {
            dureeVieJwt = Integer.parseInt(TkParam.GATSBI_JWT_EXPIRATION.getValue());
         }
         return ManagerLocator.getManager(JWTGenerator.class).generate("Gatsbi", login, dureeVieJwt, tokenCSRF, mapPrivateClaim);
      }catch(JWTCreationException e){
         log.error("An error occurred: {}", e.toString()); 
         //le cookie sera invalide donc l'authentication échouera
         return UNDEFINED_JWT;
      }
   }

   private Cookie buildCookie(String JWT){
      Cookie cookie = new Cookie(GATSBI_COOKIE_NAME, JWT);
      cookie.setMaxAge(DUREE_VIE__COOKIE);
      cookie.setPath("/");

      return cookie;
   }

   private String buildUrl(String tokenCSRF, int plateformeId){
      return MessageFormat.format(TkParam.GATSBI_APPLI_URL_PATH.getValue(),
         new Object[] {tokenCSRF, Integer.toString(plateformeId)});
   }
}